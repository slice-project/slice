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

import org.apache.edgent.execution.services.ControlService;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.etri.slice.api.device.Device;
import org.etri.slice.api.inference.WorkingMemory;
import org.etri.slice.commons.car.BodyPartLength;
import org.etri.slice.commons.car.UserInfo;
import org.etri.slice.commons.car.event.SeatPosture;
import org.etri.slice.commons.car.service.SeatControl;

@Component
@Instantiate
public class SeatControlAdaptor implements SeatControl {

	@Requires
	private SeatControl m_control;
	
	@Requires
	protected WorkingMemory m_wm;
	
	@Requires
	protected Device m_device;
	
	
	public SeatControlAdaptor() {
		m_wm.addServiceAdaptor("seatControl", this);
		
		ControlService control = m_device.getService(ControlService.class);
		control.registerControl("SeatControlAdaptor", "seatControl", null, SeatControl.class, this);	
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
	public void control(double height, double position, double tilt) {
		m_control.control(height, position, tilt);
	}

	@Override
	public void adjustTo(BodyPartLength bodyLength) {
		m_control.adjustTo(bodyLength);
	}

	@Override
	public void adjustTo(UserInfo info) {
		m_control.adjustTo(info);
	}
}
