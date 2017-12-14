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
package org.etri.slice.api.learning;

import java.util.ArrayList;
import java.util.List;

public class Action {
	
	private String relation;
	private List<String> contexts;
	private String action;
	private String value;
	
	public static class ActionBuilder {
		private String relation;
		private List<String> contexts = new ArrayList<String>();
		private String action;
		private String value;
		
		public ActionBuilder setRelation(String relation) {
			this.relation = relation;
			return this;
		}
		
		public ActionBuilder addContext(String context) {
			contexts.add(context);
			return this;
		}
		
		public ActionBuilder setAction(String action, double value) {
			this.action = action;
			this.value = Double.toString(value);
			return this;
		}
		
		public ActionBuilder setAction(String action, String value) {
			this.action = action;
			this.value = value;
			return this;
		}
		
		public Action build() {
			return new Action(relation, contexts, action, value);
		}
	}
	
	public static ActionBuilder builder() {
		return new ActionBuilder();
	}
	
	public Action(String relation, List<String> contexts, String action, String value) {
		this.relation = relation;
		this.contexts = contexts;
		this.action = action;
		this.value = value;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public List<String> getContext() {
		return contexts;
	}

	public void setContexts(List<String> contexts) {
		this.contexts = contexts;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((contexts == null) ? 0 : contexts.hashCode());
		result = prime * result + ((relation == null) ? 0 : relation.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Action other = (Action) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} 
		else if (!action.equals(other.action))
			return false;
		if (contexts == null) {
			if (other.contexts != null)
				return false;
		} 
		else if (!contexts.equals(other.contexts))
			return false;
		if (relation == null) {
			if (other.relation != null)
				return false;
		} 
		else if (!relation.equals(other.relation))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} 
		else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Action [relation=" + relation + ", contexts=" + contexts + ", action=" + action + ", value=" + value
				+ "]";
	}
}
