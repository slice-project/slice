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
package org.etri.slice.commons.car.context;

import org.etri.slice.commons.SliceContext;
import org.kie.api.definition.type.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Role(Role.Type.EVENT)
@SliceContext
public class BodyPartLength {
	
	public static class Field {
		public static final String head = "BodyPartLength.head";
		public static final String torso = "BodyPartLength.torso";
		public static final String arms = "BodyPartLength.arms";
		public static final String legs = "BodyPartLength.legs";
		public static final String height = "BodyPartLength.height";
	}
	
	public static final String dataType = "org.etri.slice.commons.car.BodyPartLength";
	public static final String topic = "body_part_length";
	public static final String dataKey = "dataKey:" + dataType;
		
	private double head;
	private double torso;
	private double arms;
	private double legs;
	private double height;
}
