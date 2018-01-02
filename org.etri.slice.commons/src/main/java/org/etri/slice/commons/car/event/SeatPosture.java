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
package org.etri.slice.commons.car.event;

import java.beans.ConstructorProperties;

import org.etri.slice.commons.SliceContext;
import org.kie.api.definition.type.Role;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor

@Role(Role.Type.EVENT)
@SliceContext
public class SeatPosture {
	public static final String dataType = "org.etri.slice.commons.car.event.SeatPosture";
	public static final String topic = "seat_posture";
	public static final String dataKey = "dataKey:" + dataType;	
	
	private double height;
	private double position;
	private double tilt;
	
	@ConstructorProperties({"height", "position", "tilt"}) 
	public SeatPosture(double height, double position, double tilt) {
		this.height = height;
		this.position = position;
		this.tilt = tilt;
	}
}
