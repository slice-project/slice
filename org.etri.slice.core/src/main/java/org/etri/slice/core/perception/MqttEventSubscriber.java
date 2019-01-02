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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.edgent.connectors.mqtt.MqttStreams;
import org.apache.edgent.function.Consumer;
import org.apache.edgent.topology.TStream;
import org.apache.edgent.topology.Topology;
import org.etri.slice.api.agent.Agent;
import org.etri.slice.api.inference.WorkingMemory;
import org.etri.slice.api.perception.EventStream;
import org.etri.slice.commons.SliceEvent;
import org.etri.slice.commons.SliceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MqttEventSubscriber<T> implements Consumer<T> {	
	private static final long serialVersionUID = 3346078185684269469L;
	private static Logger s_logger = LoggerFactory.getLogger(MqttEventSubscriber.class);	
	
	private MqttStreams m_mqtt;
	private Topology m_topology;
	private TStream<T> m_events;	
	private AtomicBoolean m_started = new AtomicBoolean(false);
	private Lock m_lock = new ReentrantLock();
	
	protected abstract WorkingMemory getWorkingMemory();
	protected abstract Agent getAgent();
	protected abstract String getTopicName();
	protected abstract String getMqttURL();
	protected abstract Class<?> getEventType();
	protected abstract EventStream<T> getEventStream();	
	
	public void start() {
		m_lock.lock();
		if ( m_started.get() == true ) {
			m_lock.unlock();
			return;
		}
		
		String topicName = getTopicName();		
		m_topology = getAgent().newTopology(topicName);

		m_mqtt = new MqttStreams(m_topology, getMqttURL().trim(), null);
		TStream<String> jsonStream = m_mqtt.subscribe(topicName, 0);
		TStream<T> events = jsonStream.map(jsonEvent -> {
			try {
				return SliceEvent.<T> fromJSON(getEventType(), jsonEvent);
			} 
			catch ( SliceException e ) {
				s_logger.error("ERROR: " + e.getDetails());
			}
			return null;
		});
		events.filter(event -> event != null);
		
		m_events = getEventStream().process(events);
		m_events.sink(this);
		
		getAgent().submit(m_topology);
		m_started.set(true);
		s_logger.info("STARTED: MqttEventSubscriber[" + topicName + "]");
		m_lock.unlock();
	}
	
	public void stop() { 
		m_lock.lock();
		if ( m_started.get() == false ) {
			m_lock.unlock();
			return;
		}
		m_started.set(false);
		s_logger.info("STOPPED: MqttEventSubscriber[" + m_mqtt.toString() + "]");
		m_lock.unlock();
	}

	public void restart() {
		m_lock.lock();
		stop();
		start();
		m_lock.unlock();
	}
	
	@Override
	public synchronized void accept(T event) {
		s_logger.info("RECEIVED(MQTT Event): " + event);		
		getWorkingMemory().insert(event);
	}
}
