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

import java.awt.EventQueue;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.handlers.event.Subscriber;
import org.etri.slice.commons.car.context.SeatPosture;
import org.etri.slice.commons.car.service.SeatControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
@Instantiate
public class SeatPostureSensorEmulator implements Runnable {
	
	private static Logger s_logger = LoggerFactory.getLogger(SeatPostureSensorEmulator.class);	

	@Requires
	private SeatControl m_posture;
	private SeatPostureSensorGUI m_window;
	
	@Validate
	public void start() {
		EventQueue.invokeLater(this);
		s_logger.info("STARTED: SeatPostureSensorEmulator");
	}
	
	@Invalidate
	public void stop() {
		s_logger.info("STOPPED: SeatPostureSensorEmulator");
		
	}
	
	@Subscriber(name="SeatPostureAdaptor", topics=SeatPosture.topic, 
			dataKey=SeatPosture.dataKey, dataType=SeatPosture.dataType)
	public void receive(SeatPosture posture) {
		m_window.setCurrentSeatPosture(posture);
	}	

	@Override
	public void run() {
		try {
			m_window = new SeatPostureSensorGUI(m_posture);
			m_window.m_frame.setVisible(true);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}