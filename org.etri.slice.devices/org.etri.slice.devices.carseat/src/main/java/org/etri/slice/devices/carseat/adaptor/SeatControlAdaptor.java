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
package org.etri.slice.devices.carseat.adaptor;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.etri.slice.api.inference.WorkingMemory;
import org.etri.slice.commons.car.event.SeatPosture;
import org.etri.slice.commons.car.service.SeatControl;

@Component
@Instantiate
public class SeatControlAdaptor implements SeatControl {

	@Requires
	private SeatControl m_control;
	
	@Requires
	private WorkingMemory m_wm;
	
	public SeatControlAdaptor() {
		m_wm.addServiceAdaptor(SeatControl.id, this);
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
		return m_control.getHeight();
	}

	@Override
	public void setHeight(double height) {
		m_control.setHeight(height);
	}

	@Override
	public double getPosition() {
		return m_control.getPosition();
	}

	@Override
	public void setPosition(double position) {
		m_control.setPosition(position);
	}

	@Override
	public double getTilt() {
		return m_control.getTilt();
	}

	@Override
	public void setTilt(double tilt) {		
		m_control.setTilt(tilt);
	}
	

}
