package org.etri.slice.agents.room.temperaturedetector.adaptor;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.handlers.event.Subscriber;
import org.etri.slice.api.agent.Agent;
import org.etri.slice.api.inference.WorkingMemory;
import org.etri.slice.api.perception.EventStream;
import org.etri.slice.core.perception.EventSubscriber;
import org.etri.slice.agents.room.temperaturedetector.stream.TemperatureStream;
import org.etri.slice.commons.room.context.Temperature;

@Component
@Instantiate
public class TemperatureAdaptor extends EventSubscriber<Temperature> {
	
	private static final long serialVersionUID = 1417861723215970629L;

	@Property(name="topic", value=Temperature.topic)
	private String m_topic;
	
	@Requires
	private WorkingMemory m_wm;

	@Requires
	private Agent m_agent;
	
	@Requires(from=TemperatureStream.SERVICE_NAME)
	private EventStream<Temperature> m_streaming;	
	
	protected  String getTopicName() {
		return m_topic;
	}
	
	protected WorkingMemory getWorkingMemory() {
		return m_wm;
	}
	
	protected Agent getAgent() {
		return m_agent;
	}
	
	protected EventStream<Temperature> getEventStream() {
		return m_streaming;
	}
		
	@Subscriber(name="TemperatureAdaptor", topics=Temperature.topic,
			dataKey=Temperature.dataKey, dataType=Temperature.dataType)
	public void receive(Temperature event) {
		super.subscribe(event);
	}
	
	@Validate
	public void start() {
		super.start(event -> m_wm.insert(event));
	}
	
	@Invalidate
	public void stop() {
		super.stop();
	}
}		

