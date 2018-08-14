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
package org.etri.slice.commons.device.mqtt;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.etri.slice.commons.device.DataListener;
import org.etri.slice.commons.util.JmxClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttCommandInvoker<T> implements DataListener<MqttCommand> {
	
	private static Logger s_logger = LoggerFactory.getLogger(MqttCommandInvoker.class);		

	private final T m_proxy;
	private final Map<String, Method> m_methods = new HashMap<>();
	private final Class<T> m_class;
	
	public MqttCommandInvoker(Class<T> cls, int port) throws Exception {
		m_class = cls;
		JmxClient client = new JmxClient(port);
		m_proxy = client.getProxy(cls);
		if ( m_proxy == null ) {
			throw new NullPointerException("failed to connect JMS server(port=" + port + ")");
		}
		
        for( Method method: cls.getDeclaredMethods() ) {
            m_methods.put(method.getName(), method);
        }	
	}
	
	@Override
	public void dataReceived(MqttCommand data) {
		try {
			Method method = m_methods.get(data.getMethod());
			method.invoke(m_proxy, data.getArgs());
		
			s_logger.info("CMD[" +  m_class.getSimpleName() + "] - " + data.getMethod() + "(" + data.getArgs() + ")");
		}
		catch ( Throwable e ) {
			s_logger.error("ERR : " + e.getMessage());
		}
		
	}

}
