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

import org.apache.edgent.connectors.mqtt.MqttStreams;
import org.apache.edgent.function.Consumer;
import org.apache.edgent.topology.TStream;
import org.apache.edgent.topology.Topology;
import org.etri.slice.commons.device.DataPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public abstract class AbstractMqttPublisher<T> implements Consumer<Consumer<T>>, DataPublisher<T> {	

	private static final long serialVersionUID = -5893481147943400895L;
	private static Logger s_logger = LoggerFactory.getLogger(AbstractMqttPublisher.class);	
	
	private MqttStreams m_mqtt;
	private Topology m_topology;
	private Consumer<T> m_eventSubmitter;
	private ObjectMapper m_jasonMapper = new ObjectMapper();
	
	protected abstract MqttDevice getDevice();	
	protected abstract String getTopicName();
	
	public void start() {	
		
		m_jasonMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
		m_jasonMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
		
		String topicName = getTopicName();
		
		m_topology = getDevice().newTopology(topicName);
		TStream<T> events = m_topology.events(this);
		
		TStream<String> jsonStream = events.map(event -> {
			try {
				return m_jasonMapper.writeValueAsString(event);
			} 
			catch ( Throwable e ) {
				s_logger.error("ERROR: " + e.getMessage());
			}
			return null;
		});
		jsonStream.filter(event -> event != null);
		m_mqtt = new MqttStreams(m_topology, getDevice().getMqttURL(), null);
		m_mqtt.publish(jsonStream, topicName, 0, false);
		
		getDevice().submit(m_topology);		
		s_logger.info("STARTED: MqttDataPublisher[" + topicName + "]");
	}
	
	public void stop() { 
		s_logger.info("STOPPED: MqttDataPublisher[" + getTopicName() + "]");
	}

	@Override
	public synchronized void accept(Consumer<T> eventSubmitter) {
		m_eventSubmitter = eventSubmitter;
	}

	@Override
	public void publish(T object) {
		if ( m_eventSubmitter != null ) {
			m_eventSubmitter.accept((T)object);
			s_logger.info("PUBLISHED(MQTT Data): " + object);	
		}
	}	
}
