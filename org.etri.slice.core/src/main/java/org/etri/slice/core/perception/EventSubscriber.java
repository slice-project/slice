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

import org.apache.edgent.function.Consumer;
import org.apache.edgent.topology.TStream;
import org.apache.edgent.topology.Topology;
import org.etri.slice.api.agent.Agent;
import org.etri.slice.api.perception.EventStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EventSubscriber<T> implements Consumer<Consumer<T>>, AutoCloseable {
	private static final long serialVersionUID = 4637974626058983539L;
	private static Logger s_logger = LoggerFactory.getLogger(EventSubscriber.class);	
	
	private Consumer<T> m_eventSubmitter;
	private Topology m_topology;
	private TStream<T> m_events;	
	
	protected abstract Agent getAgent();
	protected abstract String getTopicName();
	protected abstract EventStream<T> getEventStream();
	
	public void start(Consumer<T> consumer) {	
		m_topology = getAgent().newTopology(getTopicName());
		TStream<T> events = m_topology.events(this);
		
		m_events = getEventStream().process(events);		
		m_events.sink(consumer);
		
		getAgent().submit(m_topology);
		s_logger.info("STARTED: EventSubscriber[" + getTopicName() + "]");
	}
	
	public void stop() { 
		s_logger.info("STOPPED: EventSubscriber[" + getTopicName() + "]");
	}
	
	public synchronized void subscribe(T value) {
		if ( m_eventSubmitter != null ) {
			m_eventSubmitter.accept(value);
			s_logger.info("RECEIVED(SLICE Event): " + value);
		}
	}	
	
	@Override
	public synchronized void accept(Consumer<T> eventSubmitter) {
		m_eventSubmitter = eventSubmitter;
	}

	@Override
	public void close() throws Exception {
		
	}	
}
