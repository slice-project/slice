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
import org.etri.slice.commons.car.BodyPartLength;
import org.etri.slice.commons.car.UserInfo;
import org.etri.slice.commons.car.event.SeatPosture;
import org.etri.slice.commons.car.service.SeatControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class SeatController implements SeatControl {

	private static Logger s_logger = LoggerFactory.getLogger(SeatController.class);	
	
	private SeatPosture m_posture = new SeatPosture(30, 30, 30);
	
	@Override
	public SeatPosture getPosture() {
		return m_posture;
	}

	@Override
	public synchronized void setPosture(BodyPartLength bodyLength) {
		m_posture.setHeight(30);
		m_posture.setPosition(50);
		m_posture.setTilt(12);
		
		s_logger.info("SET : " + m_posture);
	}

	@Override
	public synchronized void setPosture(UserInfo userInfo) {
		m_posture.setHeight(50);
		m_posture.setPosition(20);
		m_posture.setTilt(30);

		s_logger.info("SET : " + m_posture);
	}

	@Override
	public synchronized void setPosture(double height, double position, double tilt) {
		m_posture.setHeight(height);
		m_posture.setPosition(position);
		m_posture.setTilt(tilt);
		
		s_logger.info("SET : " + m_posture);		
	}

}
