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
package org.etri.slice.core.config;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.PropertiesConfigurationLayout;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Updated;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.cm.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate=true, managedservice="org.etri.slice.agent")
@Instantiate
public class AgentConfigurator {

	private static Logger s_logger = LoggerFactory.getLogger(AgentConfigurator.class);		
	
	private final PropertiesConfiguration m_config = new PropertiesConfiguration();
	private final PropertiesConfigurationLayout m_layout = new PropertiesConfigurationLayout();	
	
	private AtomicBoolean m_init = new AtomicBoolean(false);
	
	@Validate
	public void init() {
		try {
			m_layout.load(m_config, new InputStreamReader(new FileInputStream("conf/slice.properties")));
			m_init.set(true);
			s_logger.info("LOADED: loaded the agent configuratiions");	
		} 
		catch (Throwable e) {
			s_logger.error("ERR: failed to load properties - reason=" + e.getMessage());
		}
	}
	
	@Invalidate
	public void fini() {
		m_init.set(false);
	}
	
	
	@SuppressWarnings("rawtypes")
	@Updated
	public synchronized void updated(Dictionary props) throws ConfigurationException {
		
		if ( m_init.get() == false  || props == null )  {
			return;
		}

		String pid = (String) props.remove("service.pid");
		if ( pid == null ) {
			return;
		}

		if ( !pid.equals("org.etri.slice.agent") ) {
			return;
		}		
		
		int updated = 0;
		Enumeration keys = props.keys();
		while ( keys.hasMoreElements() ) {
			String key = (String)keys.nextElement();
			if ( key.equals("component")) {
				continue;
			}
			
			String value = ((String)props.get(key)).trim();
			m_config.setProperty(key, value);
			updated++;
			s_logger.info("SET[" + key + " = " + value + "]"  );
		}
		
		try {
			if ( updated == 0 ) {
				return;
			}
			
			Writer writer = new FileWriter("conf/slice.properties", false);
			m_layout.save(m_config, writer);
			writer.flush();
			writer.close();
			s_logger.info("UPDATED: updated the agent configuratiions");			
		} 
		catch (Throwable e) {
			s_logger.error("ERR: failed to save properties - reason=" + e.getMessage());
		} 	
	}
}
