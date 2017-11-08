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

import org.apache.felix.ipojo.annotations.Requires;
import org.etri.slice.api.inference.WorkingMemory;
import org.etri.slice.commons.car.BodyPartLength;
import org.etri.slice.commons.car.SeatPosture;
import org.etri.slice.commons.car.UserInfo;
import org.etri.slice.commons.car.service.SeatControl;

public class SeatControlAdaptor implements SeatControl {

	@Requires
	private SeatControl m_control;
	
	@Requires
	protected WorkingMemory m_wm;
	
	public SeatControlAdaptor() {
		m_wm.addServiceAdaptor(SeatControl.class.getName(), this);
	}
	
	@Override
	public SeatPosture getPosture() {
		return m_control.getPosture();
	}

	@Override
	public void setPosture(double height, double position, double tilt) {
		m_control.setPosture(height, position, tilt);
	}

	@Override
	public void setPosture(BodyPartLength bodyLength) {
		m_control.setPosture(bodyLength);
	}

	@Override
	public void setPosture(UserInfo info) {
//		m_control.setPosture(info);
		System.out.println("SetPosture w.r.t =>" + info);
	}

}
