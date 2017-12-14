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
package org.etri.slice.api.rule;

import java.util.ArrayList;
import java.util.List;

public class Declare {

	private String m_type;
	private List<String> m_fields;

	public static class DeclareBuilder {
		private String m_type;
		private List<String> m_fields = new ArrayList<String>();
		
		public DeclareBuilder setType(String type) {
			m_type = type;
			return this;
		}		

		public DeclareBuilder addField(String field) {
			m_fields.add(field);
			return this;
		}

		public Declare build() {
			return new Declare(m_type, m_fields);
		}
	}

	public static DeclareBuilder builder() {
		return new DeclareBuilder();
	}

	public Declare(String type, List<String> fields) {
		m_type = type;
		m_fields = fields;
	}

	public String getType() {
		return m_type;
	}

	public void setType(String type) {
		m_type = type;
	}
	
	public List<String> getFields() {
		return m_fields;
	}

	public void setFields(List<String> fields) {
		m_fields = fields;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_fields == null) ? 0 : m_fields.hashCode());
		result = prime * result + ((m_type == null) ? 0 : m_type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Declare other = (Declare) obj;
		if (m_fields == null) {
			if (other.m_fields != null)
				return false;
		} 
		else if (!m_fields.equals(other.m_fields))
			return false;
		if (m_type == null) {
			if (other.m_type != null)
				return false;
		} 
		else if (!m_type.equals(other.m_type))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("declare ");
		sbuf.append(m_type);
		sbuf.append("\n");
		for ( String field : m_fields ) {
			sbuf.append("\t");
			sbuf.append(field);
			sbuf.append("\n");
		}
		sbuf.append("end\n");
		
		return sbuf.toString();
	}
}
