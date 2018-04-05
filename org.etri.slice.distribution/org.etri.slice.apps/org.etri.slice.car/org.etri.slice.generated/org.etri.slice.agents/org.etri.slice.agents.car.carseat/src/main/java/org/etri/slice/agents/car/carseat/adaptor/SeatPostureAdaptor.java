package org.etri.slice.agents.car.carseat.adaptor;

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
import org.etri.slice.agents.car.carseat.stream.SeatPostureStream;
import org.etri.slice.commons.car.context.SeatPosture;

@Component
@Instantiate
public class SeatPostureAdaptor extends EventSubscriber<SeatPosture> {
	
	private static final long serialVersionUID = 4862538185832218939L;

	@Property(name="topic", value=SeatPosture.topic)
	private String m_topic;
	
	@Requires
	private WorkingMemory m_wm;

	@Requires
	private Agent m_agent;
	
	@Requires(from=SeatPostureStream.SERVICE_NAME)
	private EventStream<SeatPosture> m_streaming;	
	
	protected  String getTopicName() {
		return m_topic;
	}
	
	protected WorkingMemory getWorkingMemory() {
		return m_wm;
	}
	
	protected Agent getAgent() {
		return m_agent;
	}
	
	protected EventStream<SeatPosture> getEventStream() {
		return m_streaming;
	}
		
	@Subscriber(name="SeatPostureAdaptor", topics=SeatPosture.topic,
			dataKey=SeatPosture.dataKey, dataType=SeatPosture.dataType)
	public void receive(SeatPosture event) {
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
