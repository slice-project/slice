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

import java.io.IOException;

import org.apache.edgent.execution.services.ControlService;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.etri.slice.api.device.Device;
import org.etri.slice.api.inference.WorkingMemory;
import org.etri.slice.api.learning.Action;
import org.etri.slice.api.learning.Action.ActionBuilder;
import org.etri.slice.api.learning.ActionLogger;
import org.etri.slice.commons.car.BodyPartLength;
import org.etri.slice.commons.car.event.SeatPosture;
import org.etri.slice.commons.car.service.SeatControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Instantiate
public class SeatControlCommander implements SeatControl {
	private static Logger s_logger = LoggerFactory.getLogger(SeatControlCommander.class);
	
	@Requires
	private SeatControl m_control;
	
	@Requires
	private ActionLogger m_logger;
	
	@Requires
	private Device m_device;
	
	@Requires
	private WorkingMemory m_wm;
		
	public SeatControlCommander() {
		m_wm.addServiceAdaptor(SeatControl.id, this);
		ControlService control = m_device.getService(ControlService.class);
		control.registerControl(this.getClass().getSimpleName(), SeatControl.id, null, SeatControl.class, this);		
	}

	@Override
	public SeatPosture getPosture() {
		return m_control.getPosture();
	}

	@Override
	public void setPosture(SeatPosture posture) {
		m_control.setPosture(posture);
	}

	@Override
	public double getHeight() {
		return m_control.getPosture().getHeight();
	}

	@Override
	public void setHeight(double height) {
		m_control.setHeight(height);
		
		ActionBuilder builder = Action.builder();
		builder.setRelation("carseat_height");
		builder.addContext(BodyPartLength.Field.head);
		builder.setAction(SeatControl.setHeight, height);
		logAction(builder.build());
	}

	@Override
	public double getPosition() {
		return m_control.getPosture().getPosition();
	}

	@Override
	public void setPosition(double position) {
		m_control.setPosition(position);
		
		ActionBuilder builder = Action.builder();
		builder.setRelation("carseat_position");
		builder.addContext(BodyPartLength.Field.head);
		builder.setAction(SeatControl.setPosition, position);
		logAction(builder.build());
	}

	@Override
	public double getTilt() {
		return m_control.getPosture().getTilt();
	}

	@Override
	public void setTilt(double tilt) {
		m_control.setTilt(tilt);	
		
		ActionBuilder builder = Action.builder();
		builder.setRelation("carseat_tilt");
		builder.addContext(BodyPartLength.Field.head);
		builder.setAction(SeatControl.setTilt, tilt);
		logAction(builder.build());
	}
	
	private void logAction(Action action) {
		try {
			m_logger.log(action);
		} 
		catch ( Exception e ) {
			s_logger.error("ERR : " + e.getMessage());
		}			
	}
}
