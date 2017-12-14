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

public class RuleSetHeader {
	
	private final String m_package;
	private final List<String> m_imports;
	private final List<String> m_globals;
	
	public static class RuleSetHeaderBuilder {
		private String m_package;
		private List<String> m_imports = new ArrayList<String>();
		private List<String> m_globals = new ArrayList<String>();
	
		public RuleSetHeaderBuilder setPackage(String _package) {
			m_package = _package;
			return this;
		}
		
		public RuleSetHeaderBuilder addImport(String _import) {
			m_imports.add(_import);
			return this;
		}
		
		public RuleSetHeaderBuilder addGlobal(String _global) {
			m_globals.add(_global);
			return this;
		}
		
		public RuleSetHeader build() {
			return new RuleSetHeader(m_package, m_imports, m_globals);
		}
	}

	public static RuleSetHeaderBuilder builder() {
		return new RuleSetHeaderBuilder();
	}	
	
	public RuleSetHeader(String _package, List<String> _imports, List<String> _globals) {
		m_package = _package;
		m_imports = _imports;
		m_globals = _globals;
	}

	public String getPackage() {
		return m_package;
	}
	
	public List<String> getImports() {
		return m_imports;
	}
	
	public List<String> getGlobals() {
		return m_globals;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_globals == null) ? 0 : m_globals.hashCode());
		result = prime * result + ((m_imports == null) ? 0 : m_imports.hashCode());
		result = prime * result + ((m_package == null) ? 0 : m_package.hashCode());
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
		RuleSetHeader other = (RuleSetHeader) obj;
		if (m_globals == null) {
			if (other.m_globals != null)
				return false;
		} 
		else if (!m_globals.equals(other.m_globals))
			return false;
		if (m_imports == null) {
			if (other.m_imports != null)
				return false;
		} 
		else if (!m_imports.equals(other.m_imports))
			return false;
		if (m_package == null) {
			if (other.m_package != null)
				return false;
		} 
		else if (!m_package.equals(other.m_package))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(m_package);
		sbuf.append("\n\n");
		for ( String _import : m_imports ) {
			sbuf.append(_import);
			sbuf.append("\n");
		}
		sbuf.append("\n");
		
		for ( String global : m_globals) {
			sbuf.append(global);
			sbuf.append("\n");
		}
		
		return sbuf.toString();
	}
}
