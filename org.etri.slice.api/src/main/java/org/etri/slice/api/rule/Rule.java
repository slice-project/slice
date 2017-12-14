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

public class Rule {
	
	private String m_id;
	private RuleBody m_body;
	
	public static class RuleBuilder {
		private String m_id;
		private RuleBody m_body;
		
		public RuleBuilder setId(String id) {
			m_id = id;
			return this;
		}
		
		public RuleBuilder setBody(RuleBody body) {
			m_body = body;
			return this;
		}
		
		public Rule build() {
			return new Rule(m_id, m_body);
		}
	}
	
	public static RuleBuilder builder() {
		return new RuleBuilder();
	}

	public Rule(String id, RuleBody body) {
		m_id = id;
		m_body = body;
	}
	
	public String getId() {
		return m_id;
	}
	
	public void setId(String id) {
		m_id = id;
	}
	
	public RuleBody getBody() {
		return m_body;
	}
	
	public void setBody(RuleBody body) {
		m_body = body;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_body == null) ? 0 : m_body.hashCode());
		result = prime * result + ((m_id == null) ? 0 : m_id.hashCode());
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
		Rule other = (Rule) obj;
		if (m_body == null) {
			if (other.m_body != null)
				return false;
		} 
		else if (!m_body.equals(other.m_body))
			return false;
		if (m_id == null) {
			if (other.m_id != null)
				return false;
		} 
		else if (!m_id.equals(other.m_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("rule ");
		sbuf.append("\"" + m_id + "\"");
		sbuf.append("\n");
		sbuf.append(m_body);
		sbuf.append("end\n");
		
		return sbuf.toString();
	}
}
