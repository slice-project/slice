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
package org.etri.slice.commons;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public abstract class SliceEvent implements Serializable {
	private static final long serialVersionUID = -1942122667322976607L;
	
	@JsonIgnore
	private ObjectMapper m_jasonMapper = new ObjectMapper();
	private static ObjectMapper s_jasonMapper = new ObjectMapper();
	
	static {
		s_jasonMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
		s_jasonMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);		
	}
	
	public SliceEvent() {
		m_jasonMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
		m_jasonMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);		
	}
	
	public String toJSON() throws SliceException {
		try {
			return m_jasonMapper.writeValueAsString(this);
		}
		catch ( Throwable e ) {
			throw new SliceException(e);
		}
	}
	
	public static <T> T fromJSON(Class<?> cls, String msg) throws SliceException {
		try {
			return s_jasonMapper.reader(cls).readValue(msg);
		}
		catch ( Throwable e ) {
			throw new SliceException(e);
		}
	}
	
	public static <T> T fromJSON(String className, String msg) throws SliceException {
		try {
			return s_jasonMapper.reader(Class.forName(className)).readValue(msg);
		}
		catch ( Throwable e ) {
			throw new SliceException(e);
		}
	}	
}
