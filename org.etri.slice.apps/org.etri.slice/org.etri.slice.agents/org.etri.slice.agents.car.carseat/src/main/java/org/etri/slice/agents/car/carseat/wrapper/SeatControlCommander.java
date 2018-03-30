/**
 * Copyright (c) 2017-2017 SLICE project team (yhsuh@etri.re.kr)
 * http://slice.etri.re.kr
 *
 * This file is part of The ROOT project of SLICE components and applications
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
 * along with The ROOT project of SLICE components and applications; see the file COPYING.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.etri.slice.agents.car.carseat.wrapper;

import org.apache.edgent.execution.services.ControlService;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.etri.slice.api.agent.Agent;
import org.etri.slice.api.learning.Action;
import org.etri.slice.api.learning.Action.ActionBuilder;
import org.etri.slice.api.learning.ActionLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;		

import org.etri.slice.commons.car.context.BodyPartLength;
import org.etri.slice.commons.car.context.SeatPosture;
import org.etri.slice.commons.car.service.SeatControl;

@Component
@Instantiate
public class SeatControlCommander implements SeatControl {
	private static Logger s_logger = LoggerFactory.getLogger(SeatControlCommander.class);
	
	@Requires
	private SeatControl m_proxy;
	
	@Requires
	private ActionLogger m_logger;
	
	@Requires
	private Agent m_agent;
	
	public SeatControlCommander() {
		ControlService control = m_agent.getService(ControlService.class);
		control.registerControl(this.getClass().getSimpleName(), SeatControl.id, null, SeatControl.class, this);
	}
	
	@Override
	public double getHeight() {
		return m_proxy.getHeight();
	}
	
	@Override		        
	public void setHeight(double height) {
		m_proxy.setHeight(height);
		
		ActionBuilder builder = Action.builder();
		builder.setRelation("carseat_height");
		builder.addContext(BodyPartLength.Field.height);
		builder.setAction(SeatControl.setHeight, height);
		logAction(builder.build());		
	}
	
	@Override
	public double getPosition() {
		return m_proxy.getPosition();
	}
	
	@Override		        
	public void setPosition(double position) {
		m_proxy.setPosition(position);
		
		ActionBuilder builder = Action.builder();
		builder.setRelation("carseat_position");
		builder.addContext(BodyPartLength.Field.height);
		builder.setAction(SeatControl.setPosition, position);
		logAction(builder.build());		
	}
	
	@Override
	public double getTilt() {
		return m_proxy.getTilt();
	}
	
	@Override		        
	public void setTilt(double tilt) {
		m_proxy.setTilt(tilt);
		
		ActionBuilder builder = Action.builder();
		builder.setRelation("carseat_tilt");
		builder.addContext(BodyPartLength.Field.height);
		builder.setAction(SeatControl.setTilt, tilt);
		logAction(builder.build());		
	}
	
	@Override
	public SeatPosture getPosture() {
		return m_proxy.getPosture();
	}
	
	@Override		        
	public void setPosture(SeatPosture posture) {
		m_proxy.setPosture(posture);
		
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
