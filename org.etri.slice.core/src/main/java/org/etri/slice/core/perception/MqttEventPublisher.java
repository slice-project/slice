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
import org.apache.edgent.topology.TStream;
import org.apache.edgent.topology.Topology;
import org.etri.slice.api.device.Device;
import org.etri.slice.api.inference.WorkingMemory;
import org.etri.slice.api.perception.EventStream;
import org.etri.slice.commons.SliceEvent;
import org.etri.slice.commons.SliceException;
import org.kie.api.runtime.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MqttEventPublisher<T> implements Consumer<Consumer<T>>, Channel {	
	private static final long serialVersionUID = -9073017330777438626L;
	private static Logger s_logger = LoggerFactory.getLogger(MqttEventPublisher.class);	
	
	private MqttStreams m_mqtt;
	private Topology m_topology;
	private TStream<T> m_events;
	private Consumer<T> m_eventSubmitter;
	
	protected abstract WorkingMemory getWorkingMemory();
	protected abstract Device getDevice();
	protected abstract String getTopicName();
	protected abstract String getMqttURL();
	protected abstract EventStream<T> getEventStream();
	
	public void start() {	
		String topicName = getTopicName();
		getWorkingMemory().addEventAdaptor(topicName, this);		
		
		m_topology = getDevice().newTopology(topicName);
		TStream<T> events = m_topology.events(this);
		m_events = getEventStream().process(events);		
		
		TStream<String> jsonStream = m_events.map(event -> {
			try {
				return ((SliceEvent)event).toJSON();
			} catch ( SliceException e ) {	}
			return null;
		});
		jsonStream.filter(event -> event != null);
		m_mqtt = new MqttStreams(m_topology, getMqttURL(), null);
		m_mqtt.publish(jsonStream, topicName, 0, false);
		
		getDevice().submit(m_topology);		
		s_logger.info("STARTED: MqttEventPublisher[" + topicName + "]");
	}
	
	public void stop() { 
		s_logger.info("STOPPED: MqttEventPublisher[" + getTopicName() + "]");
	}

	@Override
	public synchronized void accept(Consumer<T> eventSubmitter) {
		m_eventSubmitter = eventSubmitter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void send(Object object) {
		if ( m_eventSubmitter != null ) {
			m_eventSubmitter.accept((T)object);
			s_logger.info("PUBLISHED(MQTT Event): " + object);	
		}
	}	
}
