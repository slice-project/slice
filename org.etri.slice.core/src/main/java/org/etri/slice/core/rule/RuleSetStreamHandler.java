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

import java.io.File;
import java.util.function.Consumer;

import org.etri.slice.api.rule.Declare;
import org.etri.slice.api.rule.Rule;
import org.etri.slice.api.rule.RuleBody;
import org.etri.slice.api.rule.RuleExistsException;
import org.etri.slice.api.rule.RuleSetHeader;

public class RuleSetStreamHandler implements Consumer<String>{

	private enum Mode {declare, rule,}
	private enum State {attribute, condtion, action,}
	
	private Mode m_mode = Mode.declare;
	private State m_state = State.attribute;
	private RuleSetHeader.RuleSetHeaderBuilder m_headerBuilder;
	private Declare.DeclareBuilder m_declareBuilder;
	private Rule.RuleBuilder m_ruleBuilder;
	private RuleBody.RuleBodyBuilder m_ruleBodyBuilder;
	private RuleSetImpl m_ruleSet;
	private boolean m_comment = false;
	
	public RuleSetStreamHandler(File root, File ruleFile) {
		m_headerBuilder = RuleSetHeader.builder();
		m_ruleSet = new RuleSetImpl(root, ruleFile.getName().replaceAll("\\.drl", ""));
	}
	
	public RuleSetImpl getRuleSet() {
		m_ruleSet.setRuleSetHeaer(m_headerBuilder.build());
		return m_ruleSet;
	}
	
	@Override
	public void accept(String line) {
		int index = line.indexOf(" ");
		String token = line;
		String content = "";
		if ( index > 0 ) {
	 		token = line.substring(0, index).trim();
			content = line.substring(index).trim();			
		}
				
		if ( token.equals("/*") ) {
			m_comment = true;
		}
		if ( token.equals("*/") ) {
			m_comment = false;
			return;
		}		
		if ( m_comment || token.startsWith("//") ) return;
		
		switch (token) {
			case "package":
				m_headerBuilder.setPackage(line);
				break;
				
			case "import":
				m_headerBuilder.addImport(line);
				break;
				
			case "global":
				m_headerBuilder.addGlobal(line);
				break;
				
			case "declare":
				m_mode = Mode.declare;
				m_declareBuilder = Declare.builder();
				m_declareBuilder.setType(content);
				break;
				
			case "rule":
				m_mode = Mode.rule;
				m_ruleBuilder = Rule.builder();
				m_ruleBuilder.setId(content.replaceAll("\"", ""));
				m_ruleBodyBuilder = RuleBody.builder();				
				break;
				
			case "end":
				if ( m_mode == Mode.rule ) {
					m_ruleBuilder.setBody(m_ruleBodyBuilder.build());
					try {
						m_ruleSet.addRule(m_ruleBuilder.build());
					} catch ( RuleExistsException ignored ) {	}
				}
				else if ( m_mode == Mode.declare ) {
					m_ruleSet.addDeclare(m_declareBuilder.build());
				}	
				m_state = State.attribute;				
				break;
				
			case "when":
				m_mode = Mode.rule;
				m_state = State.condtion;				
				if ( !content.isEmpty() ) {
					m_ruleBodyBuilder.addCondition(content);
				}				
				break;
				
			case "then":
				m_mode = Mode.rule;
				m_state = State.action;				
				if ( !content.isEmpty() ) {
					m_ruleBodyBuilder.addAction(content);
				}				
				break;
				
			default:
				if ( m_mode == Mode.rule ) {
					if ( m_state == State.attribute ) {
						m_ruleBodyBuilder.addAttribute(line);
					}
					else if ( m_state == State.condtion ) {
						m_ruleBodyBuilder.addCondition(line);
					}
					else if ( m_state == State.action ){
						m_ruleBodyBuilder.addAction(line);
					}
				}
				else if ( m_mode == Mode.declare ) {
					m_declareBuilder.addField(line);
				}
				break;
		}
	}
}
