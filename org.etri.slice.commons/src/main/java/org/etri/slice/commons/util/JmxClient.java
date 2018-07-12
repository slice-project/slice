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
package org.etri.slice.commons.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Set;

import javax.management.JMException;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JmxClient {
	private static final String s_queryString = "org.etri.slice.agents:*";
	
	private JMXConnector m_jmxConnector;
	private JMXServiceURL m_serviceUrl;
	private MBeanServerConnection m_mbeanConn;
	
	public JmxClient(int port) throws JMException {
		this("", port);
	}
	
	public JmxClient(String host, int port) throws JMException {
		String jmxUrl = generalJmxUrlForHostNamePort("", port);
		try {
			m_serviceUrl = new JMXServiceURL(generalJmxUrlForHostNamePort(host, port));
		} 
		catch ( MalformedURLException e ) {
			throw createJmException("JmxServiceUrl was malformed: " + jmxUrl, e);
		}

		try {
			m_jmxConnector = JMXConnectorFactory.connect(m_serviceUrl, null);
			m_mbeanConn = m_jmxConnector.getMBeanServerConnection();
		} 
		catch ( IOException e ) {
			if ( m_jmxConnector != null ) {
				closeQuietly(m_jmxConnector);
				m_jmxConnector = null;
			}
			
			throw createJmException("Problems connecting to the server" + e, e);
		}		
	}
	
	public <T> T getProxy(Class<T> interfaeClass) throws JMException {		
		try {
			Set<ObjectName> objNames = m_mbeanConn.queryNames(ObjectName.getInstance(s_queryString), null);
			Iterator<ObjectName> iter = objNames.iterator();
			ObjectName objName = null;
			while ( iter.hasNext() ) {
				objName = iter.next();
				if ( objName.getCanonicalName().contains(interfaeClass.getSimpleName()) ) {
					break;
				}
			}
			
			return JMX.newMXBeanProxy(m_mbeanConn, objName, interfaeClass);
		}
		catch ( Exception e ) {
			throw createJmException("Problems connecting to the server" + e, e);
		}
	}
	
	private void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ioe) {
				// ignore exception
			}
		}
	}	
	
	private JMException createJmException(String message, Exception e) {
		JMException jmException = new JMException(message);
		jmException.initCause(e);
		return jmException;
	}	
	
	private String generalJmxUrlForHostNamePort(String hostName, int port) {
		return "service:jmx:rmi:///jndi/rmi://" + hostName + ":" + port + "/jmxrmi";
	}
}
