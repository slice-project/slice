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
package org.etri.slice.core.rule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etri.slice.api.rule.Declare;
import org.etri.slice.api.rule.Rule;
import org.etri.slice.api.rule.RuleExistsException;
import org.etri.slice.api.rule.RuleNotFoundException;
import org.etri.slice.api.rule.RuleSet;
import org.etri.slice.api.rule.RuleSetHeader;

public class RuleSetImpl implements RuleSet{
	private final String m_id;
	private final File m_root;
	private RuleSetHeader m_header;
	private List<Declare> m_declares = new ArrayList<Declare>();
	private Map<String, Rule> m_rules = new HashMap<String, Rule>();
	
	public RuleSetImpl(File root, String id) {
		m_root = root;
		m_id = id;
	}
	
	@Override
	public String getId() {
		return m_id;
	}
	
	@Override
	public synchronized void setRuleSetHeaer(RuleSetHeader header) {
		m_header = header;
	}
	
	@Override
	public synchronized RuleSetHeader getRuleSetHeader() {
		return m_header;
	}
	
	@Override
	public synchronized List<Declare> getDeclares() {
		return m_declares;
	}
	
	@Override
	public synchronized void addDeclare(Declare dclr) {
		m_declares.add(dclr);
	}
	
	@Override
	public synchronized Rule getRule(String id) throws RuleNotFoundException {
		if ( !m_rules.containsKey(id) ) {
			throw new RuleNotFoundException("Rule[id = " + id + "]");
		}
		
		return m_rules.get(id);
	}
	
	@Override
	public synchronized Collection<Rule> getRules() {
		return m_rules.values();
	}
	
	@Override
	public synchronized Rule replaceRule(Rule newRule) throws RuleNotFoundException {
		String id = newRule.getId();
		
		if ( !m_rules.containsKey(id) ) {
			throw new RuleNotFoundException("Rule[id = " + id + "]");
		}
		
		return m_rules.replace(newRule.getId(), newRule);
	}
	
	@Override
	public synchronized Rule removeRule(String id) throws RuleNotFoundException {
		if ( !m_rules.containsKey(id) ) {
			throw new RuleNotFoundException("Rule[id = " + id + "]");
		}		
		
		return m_rules.remove(id);
	}
	
	@Override
	public synchronized void addRule(Rule newRule) throws RuleExistsException {
		String id = newRule.getId();
		if ( m_rules.containsKey(id) ) {
			throw new RuleExistsException("Rule[id = " + id + "]");
		}
		
		m_rules.put(newRule.getId(), newRule);
	}
	
	@Override
	public synchronized void saveToFile() throws IOException {
		String packageName = m_header.getPackage();
		packageName = packageName.substring(("pakcage").length(), packageName.length() - 1);
		packageName = packageName.replaceAll("\\.", "/");
		packageName = packageName.trim();
		String filePath = packageName + "/" + m_id + ".drl";
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(m_root, filePath)));
		writer.write(this.toString());
		writer.close();		
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_declares == null) ? 0 : m_declares.hashCode());
		result = prime * result + ((m_header == null) ? 0 : m_header.hashCode());
		result = prime * result + ((m_id == null) ? 0 : m_id.hashCode());
		result = prime * result + ((m_rules == null) ? 0 : m_rules.hashCode());
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
		RuleSetImpl other = (RuleSetImpl) obj;
		if (m_declares == null) {
			if (other.m_declares != null)
				return false;
		}
		else if (!m_declares.equals(other.m_declares))
			return false;
		if (m_header == null) {
			if (other.m_header != null)
				return false;
		} 
		else if (!m_header.equals(other.m_header))
			return false;
		if (m_id == null) {
			if (other.m_id != null)
				return false;
		} 
		else if (!m_id.equals(other.m_id))
			return false;
		if (m_rules == null) {
			if (other.m_rules != null)
				return false;
		} 
		else if (!m_rules.equals(other.m_rules))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(m_header);
		sbuf.append("\n");
		
		for ( Declare decl : m_declares ) {
			sbuf.append(decl);
			sbuf.append("\n");
		}
		
		Collection<Rule> rules = getRules();
		for ( Rule rule : rules ) {
			sbuf.append(rule);
			sbuf.append("\n");
		}
		
		return sbuf.toString();
	}
}
