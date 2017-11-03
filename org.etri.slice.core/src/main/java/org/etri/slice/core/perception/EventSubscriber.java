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
package org.etri.slice.core.perception;

import org.apache.edgent.connectors.mqtt.MqttStreams;
import org.apache.edgent.function.Consumer;
import org.apache.edgent.providers.direct.DirectProvider;
import org.apache.edgent.topology.TStream;
import org.apache.edgent.topology.Topology;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.handlers.event.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory=false)
@Instantiate
public class EventSubscriber implements Consumer<Consumer<String>>, AutoCloseable {

	private static final long serialVersionUID = -9073017330777438626L;
	private static Logger s_logger = LoggerFactory.getLogger(EventSubscriber.class);	
	
	private Consumer<String> m_eventSubmitter;
	private final DirectProvider m_provider;
	private MqttStreams m_mqtt;
	private Topology m_topology;
	private TStream<String> m_events;	
	
	public EventSubscriber() {
		m_provider = new DirectProvider();
	}
	
	@Validate
	public void start() {	
		m_topology = m_provider.newTopology("xxx");
		m_events = m_topology.events(this);

		m_mqtt = new MqttStreams(m_topology, "tcp://localhost:1883", null);
		m_mqtt.publish(m_events, "xxx", 0, false);	
		m_provider.submit(m_topology);
		
		s_logger.info("EventSubscriber started");
	}
	
	@Invalidate
	public void stop() { 
		s_logger.info("EventSubscriber stopped");
	}
	
	@Subscriber(name="sub", topics="xxx", dataKey="aircon.data", dataType="java.lang.String")
	public synchronized void subscribe(String value) {
		if ( m_eventSubmitter != null ) {
			m_eventSubmitter.accept(value);
			s_logger.info("Event: " + value);
		}
	}	
	
	@Override
	public synchronized void accept(Consumer<String> eventSubmitter) {
		m_eventSubmitter = eventSubmitter;
	}

	@Override
	public void close() throws Exception {
		
	}	
}
