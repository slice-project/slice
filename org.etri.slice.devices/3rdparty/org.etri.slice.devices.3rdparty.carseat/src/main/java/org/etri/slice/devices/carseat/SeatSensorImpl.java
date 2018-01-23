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
package org.etri.slice.devices.carseat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.felix.ipojo.handlers.event.Publishes;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.etri.slice.commons.Sensor;
import org.etri.slice.commons.car.event.SeatPosture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class SeatSensorImpl implements Sensor{
	
	private static Logger s_logger = LoggerFactory.getLogger(SeatSensorImpl.class);	
	private static final String fileName = "/workspace/save_position.txt";
	
	@Publishes(name="pub:seat_posture", topics=SeatPosture.topic, dataKey=SeatPosture.dataKey)
	private Publisher m_publisher;
	private GpioController m_gpio;	
	private GpioPinDigitalInput m_SCCGP;
	
	@Override
	public void start() {
		m_gpio = GpioFactory.getInstance();		
		m_SCCGP = m_gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, PinPullResistance.PULL_UP); //Static Control Check Gpio Pin, GPIO_25
		
		//Press Confirm button
		m_SCCGP.setShutdownOptions(true);
		m_SCCGP.addListener(new GpioPinListenerDigital() {
			
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				s_logger.info("----> GPIO PIN STATE CHANGE : " + event.getPin() + " = " + event.getState());
				try {
					Thread.sleep(5);
					if( m_SCCGP.getState() == PinState.LOW ) {
						s_logger.info("Control Confirm OK!!");
						SeatPosture seatPosture = loadPosition();
						m_publisher.sendData(seatPosture);
						s_logger.info("PUB: " + seatPosture);	
					}
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		s_logger.info("STARTED: " + "SeatSensor");
	}

	@Override
	public void stop() {
		m_gpio.shutdown();
		s_logger.info("STOPPED: " + "SeatSensor");
	}
	
	private SeatPosture loadPosition() {
		SeatPosture seatPosture = new SeatPosture(0,0,0);
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str;
			if((str = in.readLine()) !=null) {
				seatPosture.setHeight(Double.parseDouble(str));
			}
			if((str = in.readLine()) !=null) {
				seatPosture.setPosition(Double.parseDouble(str));
			}
			if((str = in.readLine()) !=null) {
				seatPosture.setTilt(Double.parseDouble(str));
			}
			in.close();
			s_logger.info("Seat Position Load : "+ seatPosture.getHeight() + ", "+seatPosture.getPosition()+", "+seatPosture.getTilt());
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return seatPosture;
	}
}
