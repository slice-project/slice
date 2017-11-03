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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.edgent.connectors.mqtt.MqttStreams;
import org.apache.edgent.function.Consumer;
import org.apache.edgent.providers.direct.DirectProvider;
import org.apache.edgent.topology.TStream;
import org.apache.edgent.topology.Topology;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.handlers.event.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttEventPublisher<T> implements Consumer<Consumer<T>>, AutoCloseable {

	private static final long serialVersionUID = 1052035835731312302L;

	private static Logger s_logger = LoggerFactory.getLogger(MqttEventPublisher.class);	

	private DirectProvider m_provider = new DirectProvider();
	private Topology m_topology;
	private TStream<String> m_events;
	private MqttStreams m_mqtt;
	private Consumer<T> m_eventSubmitter;
	
	public MqttEventPublisher() {
		m_topology = m_provider.newTopology("xxx");
		m_mqtt = new MqttStreams(m_topology, "tcp://localhost:1883", null);
	}
	
	public void subscribe(String xxx) {
		System.out.println("recieved: " + xxx);		

	}	

	
	
	@Override
	public void accept(Consumer<T> eventSubmitter) {
		m_eventSubmitter = eventSubmitter;
	}

	@Override
	public void close() throws Exception {
		
	}
}
