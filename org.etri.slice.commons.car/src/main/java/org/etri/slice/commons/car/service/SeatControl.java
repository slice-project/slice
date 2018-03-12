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
package org.etri.slice.commons.car.service;

import javax.management.MXBean;

import org.etri.slice.commons.car.context.SeatPosture;

@MXBean 
public interface SeatControl {
	
	static final String id = "seatControl";
	static final String setHeight = "seatControl.setHeight";
	static final String setPosition = "seatControl.setPosition";
	static final String setTilt = "seatControl.setTilt";

	double getHeight();
	
	void setHeight(double height);
	
	double getPosition();
	
	void setPosition(double position);
	
	double getTilt();
	
	void setTilt(double tilt);
	
	SeatPosture getPosture();
	
	void setPosture(SeatPosture posture);
}
