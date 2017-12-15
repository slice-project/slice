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

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.handlers.event.Publishes;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.etri.slice.commons.car.event.SeatPosture;
import org.etri.slice.commons.car.service.SeatControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class SeatController implements SeatControl {

	private static Logger s_logger = LoggerFactory.getLogger(SeatController.class);	
	
	@Publishes(name="pub:seat_posture", topics="seat_posture", dataKey="seat.posture")
	private Publisher m_publisher;	
	private SeatPosture m_posture = new SeatPosture(30, 30, 30);
	
	@Override
	public double getHeight() {
		return m_posture.getHeight();
	}

	@Override
	public void setHeight(double height) {
		m_posture.setHeight(height);
		s_logger.info("SET : " + m_posture);
		publish();		
	}

	@Override
	public double getPosition() {
		return m_posture.getPosition();
	}

	@Override
	public void setPosition(double position) {
		m_posture.setPosition(position);
		s_logger.info("SET : " + m_posture);
		publish();		
	}

	@Override
	public double getTilt() {
		return m_posture.getTilt();
	}

	@Override
	public void setTilt(double tilt) {
		m_posture.setTilt(tilt);
		s_logger.info("SET : " + m_posture);
		publish();		
	}	

	@Override
	public SeatPosture getPosture() {
		return m_posture;
	}

	@Override
	public void setPosture(SeatPosture posture) {
		m_posture = posture;
		s_logger.info("SET : " + m_posture);
		publish();		
	}
	
	private void publish() {
		SeatPosture.SeatPostureBuilder builder = SeatPosture.builder();
		SeatPosture posture = builder.height(m_posture.getHeight())
									.position(m_posture.getPosition())
									.tilt(m_posture.getTilt()).build();
		m_publisher.sendData(posture);				
		s_logger.info("PUB: " + posture);			
	}
	
}
