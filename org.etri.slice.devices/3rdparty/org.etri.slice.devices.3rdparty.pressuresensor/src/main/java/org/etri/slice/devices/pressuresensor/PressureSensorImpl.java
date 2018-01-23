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
package org.etri.slice.devices.pressuresensor;


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
import org.etri.slice.commons.Sensor;
import org.etri.slice.commons.car.event.Pressure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

@Component
@Instantiate
public class PressureSensorImpl implements Sensor, Runnable {
	
	private static Logger s_logger = LoggerFactory.getLogger(PressureSensorImpl.class);	
	
	@Property(name="interval", value="1000")
	private long m_interval;
	
	private Lock m_lock = new ReentrantLock();
	@GuardedBy("m_lock") private Condition m_stopCondition = m_lock.newCondition();
	@GuardedBy("m_lock") private volatile boolean m_stopRequested = false;

	@Publishes(name="PressureSensorImpl", topics=Pressure.topic, dataKey=Pressure.dataKey)
	private Publisher m_publisher;	
	
	private GpioController m_gpio;	
	private GpioPinDigitalInput m_PGP;	
	private PRESS_MODE press_mode = PRESS_MODE.NONE;
	
	private enum PRESS_MODE{
		UP,
		DOWN,
		NONE
	}
	
	public PressureSensorImpl() {

	}
	
	@Validate
	public void start() {
		m_gpio = GpioFactory.getInstance();		
		m_PGP = m_gpio.provisionDigitalInputPin(RaspiPin.GPIO_13, PinPullResistance.PULL_UP); //Press, Gpio, Pin, GPIO_09
		
		new Thread(this).start();
		s_logger.info("PressSensor started.");
	}
	
	@Invalidate
	public void stop() {
		m_lock.lock();
		m_stopRequested = true;
		m_stopCondition.signal();
		m_gpio.shutdown();
		s_logger.info("PressSensor stoppted");		
	}	
	
	@Override
	public void run() {
		m_lock.lock();
		try {
			while ( !m_stopRequested ) {
				if ( m_PGP.isLow() ) {
					if( press_mode == PRESS_MODE.NONE || press_mode == PRESS_MODE.DOWN ) {
						press_mode = PRESS_MODE.UP;
						Pressure detected = Pressure.builder().value(0).build();
						s_logger.info("PUB: " + detected);
					}
				}
				else {
					if( press_mode == PRESS_MODE.NONE || press_mode == PRESS_MODE.UP ) {
						press_mode = PRESS_MODE.DOWN;
						Pressure detected = Pressure.builder().value(50).build();
						s_logger.info("PUB: " + detected);
					}
				}
				m_stopCondition.await(m_interval, TimeUnit.MILLISECONDS);
			}
        }
		catch ( Exception e ) {
	        	System.out.println("gpio shutdown!!");
        }
		finally {
			m_lock.unlock();
			m_gpio.shutdown();
		}
	}
}
