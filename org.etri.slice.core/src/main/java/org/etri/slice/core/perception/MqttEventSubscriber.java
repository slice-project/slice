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
import org.etri.slice.commons.SliceEvent;
import org.etri.slice.commons.SliceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MqttEventSubscriber implements Consumer<String> {
	private static final long serialVersionUID = 3346078185684269469L;
	private static Logger s_logger = LoggerFactory.getLogger(MqttEventSubscriber.class);	
	
	private MqttStreams m_mqtt;
	private Topology m_topology;
	private TStream<String> m_events;
	
	protected abstract WorkingMemory getWorkingMemory();
	protected abstract Device getDevice();
	protected abstract String getTopicName();
	protected abstract String getMqttURL();
	protected abstract Class<?> getEventType();
	
	public void start() {	
		String topicName = getTopicName();
		
		m_topology = getDevice().newTopology(topicName);

		m_mqtt = new MqttStreams(m_topology, getMqttURL(), null);
		m_events = m_mqtt.subscribe(topicName, 0);
		m_events.sink(this);
		getDevice().submit(m_topology);
		
		s_logger.info("MqttEventSubscriber[" + topicName + "] started.");
	}
	
	public void stop() { 
		s_logger.info("MqttEventSubscriber[" + getTopicName() + "] stopped.");
	}

	@Override
	public synchronized void accept(String event) {
		try {
			s_logger.info("MQTT Event received: " + event);				
			Object fact = SliceEvent.fromJSON(getEventType(), event);
			getWorkingMemory().insert(fact);
		} catch (SliceException e) {
			e.printStackTrace();
		}
	}
}
