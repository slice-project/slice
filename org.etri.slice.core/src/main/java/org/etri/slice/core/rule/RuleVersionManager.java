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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class RuleVersionManager {

	private static final String PREFIX = "prefix";
	private static final String MIDDLE = "middle";
	private static final String SUFFIX = "suffix";
	
	private String m_prefix;
	private String m_middle;
	private String m_suffix;
	private Properties m_props = new Properties();
	private final String m_propsPath;
	
	public RuleVersionManager(String propFile) throws IOException {
		m_propsPath = propFile;
		loadProperties();
	}
	
	public synchronized String getCurrentVersion() {
		return m_prefix + "." + m_middle + "." + m_suffix;
	}
	
	private static int max_value = 10;
	public synchronized String getNewVersion() {
		int suffix = Integer.parseInt(m_suffix);
		int middle = Integer.parseInt(m_middle);
		int prefix = Integer.parseInt(m_prefix);
		if ( suffix < max_value) {
			m_suffix = Integer.toString(++suffix);
			m_props.setProperty(SUFFIX, m_suffix);
		}
		else if ( middle < max_value ) {
			m_middle = Integer.toString(++middle);
			m_props.setProperty(MIDDLE, m_middle);
		}
		else {
			m_prefix = Integer.toString(++prefix);
			m_props.setProperty(PREFIX, m_prefix);
		}
		
		saveProperties();
		return m_prefix + "." + m_middle + "." + m_suffix;
	}
	
	private void loadProperties() throws IOException {
		FileInputStream fis = new FileInputStream(m_propsPath);
		m_props.load(new BufferedInputStream(fis));
		
		m_prefix = m_props.getProperty(PREFIX);
		m_middle = m_props.getProperty(MIDDLE);
		m_suffix = m_props.getProperty(SUFFIX);
	}
	
	private static final String comments = "version: prefix.middle.suffix";
	private void saveProperties() {
		try {
			FileOutputStream fos = new FileOutputStream(m_propsPath);
			m_props.store(new BufferedOutputStream(fos), comments);
		}
		catch ( IOException e ) {
			System.err.println(e.getMessage());
		}
	}
}
