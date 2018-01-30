/**
 *
 * Copyright (c) 2017-2017 SLICE project team (yhsuh@etri.re.kr)
 * http://slice.etri.re.kr
 *
 * This file is part of The SLICE components
 *
 * This Program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This Program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with The SLICE components; see the file COPYING.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.etri.slice.devices.ultrasonic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.handlers.event.Publishes;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.apache.http.annotation.GuardedBy;
import org.etri.slice.commons.car.ObjectInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

@Component
@Instantiate
public class UltraSonicSensorImpl implements Runnable {
	
	private static Logger s_logger = LoggerFactory.getLogger(UltraSonicSensorImpl.class);	
	
	@Property(name="interval", value="500")
	private long m_interval;
	
	private Lock m_lock = new ReentrantLock();
	@GuardedBy("m_lock") private Condition m_stopCondition = m_lock.newCondition();
	@GuardedBy("m_lock") private volatile boolean m_stopRequested = false;	
	
	@Publishes(name="UltraSonicSensor", topics=ObjectInfo.topic, dataKey=ObjectInfo.dataKey)
	private Publisher m_publisher;
	
	GpioController gpio;
	
	GpioPinDigitalOutput LED_R;
	GpioPinDigitalOutput LED_G;
	GpioPinDigitalOutput POINTER;
	//A0,B0,C0,PWM0
	GpioPinDigitalOutput TRIGER;
	GpioPinDigitalInput ECHO;
	
	@Validate
	public void start() {
		gpio = GpioFactory.getInstance();
		
		LED_R = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, PinState.HIGH); //Red Led GPIO_9
		LED_G = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, PinState.HIGH); //Green Led GPIO_10
		POINTER = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, PinState.HIGH); //Pointer GPIO_14
		
		TRIGER = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW); //Gpio, Pin, GPIO_17
		
		ECHO = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN); //Gpio, Pin, GPIO_27
		ECHO.addListener(new GpioPinListenerDigital() {
			long pulseStart, pulseEnd, dist;
			
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent arg0) {
				if(ECHO.getState() == PinState.HIGH ) {
					pulseStart = System.currentTimeMillis();
					s_logger.info("DETECTED: high");
				}
				
				if(ECHO.getState() == PinState.LOW ) {
					pulseEnd = System.currentTimeMillis();
					s_logger.info("DETECTED: low");;
					
					dist = pulseEnd - pulseStart;
					
					ObjectInfo distance = ObjectInfo.builder().objectId("obj").distance(dist).build();
					m_publisher.sendData(distance);			
					s_logger.info("PUB: " + distance);
				}
			}
		});		
		
		new Thread(this).start();
		s_logger.info("UltraSonicSensor started.");
	}
	
	@Invalidate
	public void stop() {
		m_lock.lock();
		m_stopRequested = true;
		m_stopCondition.signal();
		m_lock.unlock();
		gpio.shutdown();
		s_logger.info("UltraSonicSensor stoppted");		
	}

	@Override
	public void run() {
		m_lock.lock();
		try {
			while ( !m_stopRequested ) {
				TRIGER.low();
				try {
					m_stopCondition.await(10,  TimeUnit.MICROSECONDS);
				} catch (InterruptedException e) {
				}
				
				TRIGER.high();
				try {
					m_stopCondition.await(100,  TimeUnit.MICROSECONDS);
				} catch (InterruptedException e) {
				}
			}
		}
		catch ( Exception e ) {
	        s_logger.error("gpio shutdown!!");
		}
		finally {
			m_lock.unlock();
		}
	}
}
