package org.etri.slice.agents.car.fullbodydetector.wrapper;

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
import org.etri.slice.agents.car.fullbodydetector.stream.FullBodyDetectedStream;
import org.etri.slice.commons.car.event.FullBodyDetected;

@Component
@Instantiate
public class FullBodyDetectedChannel extends MqttEventPublisher<FullBodyDetected> {

	private static final long serialVersionUID = 2335653131458527542L;

	@Property(name="topic", value=FullBodyDetected.TOPIC)
	private String m_topic;
	
	@Property(name="url", value="tcp://129.254.88.119:1883")
	private String m_url;
	
	@Requires
	private WorkingMemory m_wm;

	@Requires
	private Agent m_agent;
	
	@Requires(from=FullBodyDetectedStream.SERVICE_NAME)
	private EventStream<FullBodyDetected> m_streaming;		
	
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
	
	protected EventStream<FullBodyDetected> getEventStream() {
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
