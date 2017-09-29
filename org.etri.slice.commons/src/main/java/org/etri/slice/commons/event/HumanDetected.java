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
package org.etri.slice.commons.event;

import com.google.common.collect.Range;

public class HumanDetected {

	private static Range<Float> s_SMALL = Range.open(120f, 160f);
	private static Range<Float> s_MEDIUM = Range.open(160f, 180f);
	private static Range<Float> s_LARGE = Range.open(180f, 190f);

	public static enum BodySize {
		SMALL,
		MEDIUM,
		LARGE
	}
	
	private BodySize m_size;
	
	public HumanDetected(BodySize size) {
		m_size = size;
	}
}
