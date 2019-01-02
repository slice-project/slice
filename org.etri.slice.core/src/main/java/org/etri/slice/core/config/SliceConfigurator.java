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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate=true)
@Instantiate
public class SliceConfigurator {

	private static Logger s_logger = LoggerFactory.getLogger(SliceConfigurator.class);	
	
	@Requires
    ConfigurationAdmin pm;
	
	@Validate
	void init() {
		try {
			Properties props = new Properties();
			InputStream is = new BufferedInputStream(new FileInputStream("conf/slice.properties"));			
			props.load(is);
			
			configureCore(props);
			configureAgent(props);
			configureDevice(props);
			
			s_logger.info("STARTED: updated the initial configuratiions");
		}
		catch ( Throwable e ) {
			s_logger.error("ERR: " + e.getMessage());
		}
	}
	
	private void configureCore(Properties props) throws IOException {
		Configuration conf = pm.getConfiguration("org.etri.slice.core", "?");
		if ( conf != null ) {
			Properties cloned = (Properties)props.clone();
			@SuppressWarnings("rawtypes")
			Enumeration keys = cloned.keys();
			while ( keys.hasMoreElements() ) {
				String key = (String)keys.nextElement();
				if ( !key.startsWith("core.") ) {
					cloned.remove(key);	
				}
			}
			
			conf.update(cloned);
		}		
	}
	
	private void configureAgent(Properties props) throws IOException {
		Configuration conf = pm.getConfiguration("org.etri.slice.agent", "?");
		if ( conf != null ) {
			Properties cloned = (Properties)props.clone();			
			@SuppressWarnings("rawtypes")
			Enumeration keys = cloned.keys();
			while ( keys.hasMoreElements() ) {
				String key = (String)keys.nextElement();
				if ( !key.startsWith("agent.") ) {
					cloned.remove(key);
				}
			}
			
			conf.update(cloned);
		}		
	}
	
	private void configureDevice(Properties props) throws IOException {
		Configuration conf = pm.getConfiguration("org.etri.slice.device", "?");
		if ( conf != null ) {
			Properties cloned = (Properties)props.clone();			
			@SuppressWarnings("rawtypes")
			Enumeration keys = cloned.keys();
			while ( keys.hasMoreElements() ) {
				String key = (String)keys.nextElement();
				if ( !key.startsWith("device.") ) {
					cloned.remove(key);
				}
			}
			
			conf.update(cloned);
		}		
	}	
	
}
