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
package org.etri.slice.core.learning;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.api.learning.Action;
import org.etri.slice.api.learning.ActionLogNotFoundException;
import org.etri.slice.api.learning.ActionLogger;
import org.etri.slice.api.learning.ActionLoggerException;
import org.etri.slice.api.perception.ContextMemory;
import org.etri.slice.api.perception.ContextNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class ActionLoggerImpl implements ActionLogger {
	
	private static Logger s_logger = LoggerFactory.getLogger(ActionLoggerImpl.class);		
	private static final String ARFF = ".arff";
	private static final String NILL = "NILL";
	
	@Property(name="logging.root", value="log")
	private String m_root;

	@Requires
	private ContextMemory m_cm;
	private ConcurrentMap<String, ActionLog> m_loggers = new ConcurrentHashMap<String, ActionLog>();
	
	@Validate
	public void init() throws IOException {
		File root = new File(m_root);
		File logs[] = root.listFiles((dir, name) -> name.endsWith(ARFF));
		for ( File log : logs ) {
			String fileName = log.getName();
			String loggerId = fileName.substring(0, fileName.length() - ARFF.length());
			m_loggers.put(loggerId, new ActionLog(log, m_cm));
			s_logger.info("READY: Action Logger[id=" + loggerId + "]");
		}
	}
	
	@Invalidate
	public void fini() {
		
	}
	
	@Override
	public void log(Action action) throws ActionLoggerException {
		String relation = action.getRelation();
		ActionLog logger = m_loggers.get(relation);
		
		try {
			if ( logger == null ) {
				logger = new ActionLog(new File(m_root, relation + ARFF), m_cm);
				logger.prePareHeader(action);
				m_loggers.put(relation, logger);
			}
	
			logger.log(action);
		}
		catch ( IOException e ) {
			throw new ActionLoggerException(e.getMessage());
		}
	}

	@Override
	public File getActionLogFile(String id) throws ActionLogNotFoundException {
		if ( !m_loggers.containsKey(id) ) {
			throw new ActionLogNotFoundException("ActionLog[id=" + id + "]");
		}
		
		return m_loggers.get(id).getLogFile();
	}

	@Override
	public Collection<String> getActionLogIdsAll() {		
		return m_loggers.keySet();
	}		
	
	static class ActionLog {
		private final File m_file;
		private final ContextMemory m_cm;
		private BufferedWriter m_writer;
		
		public ActionLog(File logFile, ContextMemory cm) {
			m_file = logFile;
			m_cm = cm;
		}
		
		public File getLogFile() {
			return m_file;
		}
		
		public synchronized void log(Action action) throws IOException {			
			m_writer = new BufferedWriter(new FileWriter(m_file, true));
			for ( String context : action.getContext() ) {
				Object cv = null;
				try {
					cv = m_cm.getContext(context);
				} 
				catch ( ContextNotFoundException e ) {
					cv = NILL;
				}
				m_writer.write(cv.toString());
				m_writer.write(", ");
			}
			m_writer.write(action.getValue());
			m_writer.newLine();
			m_writer.close();
			s_logger.info("LOGGED: " + action);
		}
		
		public void prePareHeader(Action action) throws IOException {
			m_writer = new BufferedWriter(new FileWriter(m_file, true));
			m_writer.write("@relation ");
			m_writer.write(action.getRelation());
			m_writer.newLine();
			m_writer.newLine();
			
			for ( String context : action.getContext() ) {
				m_writer.write("@attribute ");
				m_writer.write(context);
				m_writer.write("\tnumeric\n");
			}
			m_writer.write("@attribute ");
			m_writer.write(action.getAction());
			m_writer.write("\tnumeric\n");
			
			m_writer.newLine();
			m_writer.write("@data\n");
			m_writer.close();
		}
	}
}
