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

public class RuleBody {

	private List<String> m_attributes;	
	private List<String> m_conditions;
	private List<String> m_actions;

	public static class RuleBodyBuilder {
		private List<String> m_attributes = new ArrayList<String>();
		private List<String> m_conditions = new ArrayList<String>();
		private List<String> m_actions = new ArrayList<String>();
		
		public RuleBodyBuilder addAttribute(String attribute) {
			m_attributes.add(attribute);
			return this;
		}		

		public RuleBodyBuilder addCondition(String condition) {
			m_conditions.add(condition);
			return this;
		}

		public RuleBodyBuilder addAction(String action) {
			m_actions.add(action);
			return this;
		}

		public RuleBody build() {
			return new RuleBody(m_attributes, m_conditions, m_actions);
		}
	}

	public static RuleBodyBuilder builder() {
		return new RuleBodyBuilder();
	}

	public RuleBody(List<String> attributes, List<String> conditions, List<String> actions) {
		m_attributes = attributes;
		m_conditions = conditions;
		m_actions = actions;
	}

	public List<String> getAttributes() {
		return m_attributes;
	}

	public void setAttributes(List<String> attributes) {
		m_attributes = attributes;
	}
	
	public List<String> getConditions() {
		return m_conditions;
	}

	public void setCondition(List<String> conditions) {
		m_conditions = conditions;
	}

	public List<String> getActions() {
		return m_actions;
	}

	public void setActions(List<String> actions) {
		m_actions = actions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_actions == null) ? 0 : m_actions.hashCode());
		result = prime * result + ((m_attributes == null) ? 0 : m_attributes.hashCode());
		result = prime * result + ((m_conditions == null) ? 0 : m_conditions.hashCode());
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
		RuleBody other = (RuleBody) obj;
		if (m_actions == null) {
			if (other.m_actions != null)
				return false;
		} 
		else if (!m_actions.equals(other.m_actions))
			return false;
		if (m_attributes == null) {
			if (other.m_attributes != null)
				return false;
		} 
		else if (!m_attributes.equals(other.m_attributes))
			return false;
		if (m_conditions == null) {
			if (other.m_conditions != null)
				return false;
		} 
		else if (!m_conditions.equals(other.m_conditions))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		for ( String attribute : m_attributes ) {
			sbuf.append("\t");
			sbuf.append(attribute);
			sbuf.append("\n");
		}
		
		sbuf.append("\twhen\n");
		for ( String condition : m_conditions ) {
			sbuf.append("\t\t");
			sbuf.append(condition);
			sbuf.append("\n");
		}
		
		sbuf.append("\tthen\n");
		for ( String action : m_actions ) {
			sbuf.append("\t\t");
			sbuf.append(action);
			sbuf.append("\n");
		}
		
		return sbuf.toString();
	}

}
