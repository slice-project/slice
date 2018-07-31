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
import org.etri.slice.commons.device.DataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public abstract class AbstractMqttSubscriber<T> implements Consumer<T> {	

	private static final long serialVersionUID = -8035103699457287118L;
	private static Logger s_logger = LoggerFactory.getLogger(AbstractMqttSubscriber.class);	
	
	private MqttStreams m_mqtt;
	private Topology m_topology;
	private ObjectMapper m_jasonMapper = new ObjectMapper();
	
	protected abstract MqttDevice getDevice();
	protected abstract String getTopicName();
	protected abstract Class<?> getEventType();
	protected abstract DataListener<T> getDataListener();
	
	public void start() {
		
		m_jasonMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
		m_jasonMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
		
		String topicName = getTopicName();		
		m_topology = getDevice().newTopology(topicName);

		m_mqtt = new MqttStreams(m_topology, getDevice().getMqttURL(), null);
		TStream<String> jsonStream = m_mqtt.subscribe(topicName, 0);
		TStream<T> events = jsonStream.map(jsonEvent -> {
			try {
				return m_jasonMapper.reader(getEventType()).readValue(jsonEvent);
			} 
			catch ( Throwable e ) {
				s_logger.error("ERROR: " + e.getMessage());
			}
			return null;
		});
		events.filter(event -> event != null);
		events.sink(this);
		
		getDevice().submit(m_topology);
		s_logger.info("STARTED: MqttDataSubscriber[" + topicName + "]");
	}
	
	public void stop() { 
		s_logger.info("STOPPED: MqttDataSubscriber[" + getTopicName() + "]");
	}

	@Override
	public synchronized void accept(T data) {
		s_logger.info("RECEIVED(MQTT Data): " + data);	
		getDataListener().dataReceived(data);
	}
}
