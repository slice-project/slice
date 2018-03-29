package org.etri.slice.agents.car.carseat.wrapper;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.api.agent.Agent;
import org.etri.slice.api.inference.WorkingMemory;
import org.etri.slice.api.perception.EventStream;
import org.etri.slice.core.perception.MqttEventPublisher;
import org.etri.slice.agents.car.carseat.stream.SeatPostureChangedStream;
import org.etri.slice.commons.car.event.SeatPostureChanged;

@Component
@Instantiate
public class SeatPostureChangedChannel extends MqttEventPublisher<SeatPostureChanged> {

	private static final long serialVersionUID = -2582900155187573424L;

	@Property(name="topic", value=SeatPostureChanged.TOPIC)
	private String m_topic;
	
	@Property(name="url", value="tcp://129.254.88.119:1883")
	private String m_url;
	
	@Requires
	private WorkingMemory m_wm;

	@Requires
	private Agent m_agent;
	
	@Requires(from=SeatPostureChangedStream.SERVICE_NAME)
	private EventStream<SeatPostureChanged> m_streaming;		
	
	protected  String getTopicName() {
		return m_topic;
	}
	
	protected String getMqttURL() {
		return m_url;
	}	
	
	protected WorkingMemory getWorkingMemory() {
		return m_wm;
	}
	
	protected Agent getAgent() {
		return m_agent;
	}
	
	protected EventStream<SeatPostureChanged> getEventStream() {
		return m_streaming;
	}	
		
	@Validate
	public void start() {
		super.start();
	}
	
	@Invalidate
	public void stop() {
		super.stop();
	}
}

