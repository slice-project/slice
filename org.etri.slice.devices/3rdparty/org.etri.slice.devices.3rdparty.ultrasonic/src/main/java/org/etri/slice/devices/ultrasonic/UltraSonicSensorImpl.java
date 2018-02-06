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

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioInterrupt;
import com.pi4j.wiringpi.GpioInterruptEvent;
import com.pi4j.wiringpi.GpioInterruptListener;
import com.pi4j.wiringpi.GpioUtil;

@Component
@Instantiate
public class UltraSonicSensorImpl implements Runnable {
	
	private static Logger s_logger = LoggerFactory.getLogger(UltraSonicSensorImpl.class);	
	private static final int GREEN_LED = 12; //BCM 10
	
	@Property(name="interval", value="500")
	private long m_interval;
	
	private Lock m_lock = new ReentrantLock();
	@GuardedBy("m_lock") private Condition m_stopCondition = m_lock.newCondition();
	@GuardedBy("m_lock") private volatile boolean m_stopRequested = false;	
	
	@Publishes(name="UltraSonicSensor", topics=ObjectInfo.topic, dataKey=ObjectInfo.dataKey)
	private Publisher m_publisher;
	
	@Validate
	public void start() {	
        if (Gpio.wiringPiSetup() == -1) {            
        		s_logger.error("FAILED: GPIO SETUP FAILED");
            return;
        }
		
        GpioUtil.export(GREEN_LED, GpioUtil.DIRECTION_OUT);
        Gpio.pinMode(GREEN_LED, Gpio.OUTPUT);        
        
		GpioUtil.export(0, GpioUtil.DIRECTION_OUT);
		Gpio.pinMode(0, Gpio.OUTPUT);
		
		GpioUtil.export(2, GpioUtil.DIRECTION_IN);
		GpioUtil.setEdgeDetection(2, GpioUtil.EDGE_RISING);
		Gpio.pinMode(2, Gpio.INPUT);
        Gpio.pullUpDnControl(2, Gpio.PUD_UP);
        
        GpioInterrupt.enablePinStateChangeCallback(2);       

        // create and add GPIO listener
        GpioInterrupt.addListener(new GpioInterruptListener() {        	
        		long pulseStart, pulseEnd;
        		double distance;
            @Override
            public void pinStateChange(GpioInterruptEvent event) {
           		if( event.getPin() == 2 ) { 
           			if( event.getState() == true ) {                	   
           				pulseStart = System.nanoTime();
           			}
           			else if( event.getState() == false ) {
           				pulseEnd = System.nanoTime();
           				distance = (float)(pulseEnd - pulseStart) / 1000f / 1000f;
           				if ( distance < 1 || distance > 4 ) return;
           				
	   					ObjectInfo objInfo = ObjectInfo.builder().objectId("obj").distance(distance).build();
	   					m_publisher.sendData(objInfo);			
	   					s_logger.info("PUB: " + objInfo);
                   }
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
		s_logger.info("UltraSonicSensor stoppted");		
	}

	@Override
	public void run() {
		m_lock.lock();
		try {
			while ( !m_stopRequested ) {				
				Gpio.digitalWrite(0, 0);
				m_stopCondition.await(10,  TimeUnit.MILLISECONDS);
				
				Gpio.digitalWrite(0, 1);
				m_stopCondition.await(100,  TimeUnit.MILLISECONDS);
			}
		}
		catch ( Exception e ) {
	        s_logger.error("gpio shutdown!!");
		}
		finally {
			m_lock.unlock();
		}
	}
	
	private void turnOnLED() {
		Gpio.digitalWrite(GREEN_LED, 1);
	}
	
	private void turnOffLED() {
		Gpio.digitalWrite(GREEN_LED, 0);
	}
}
