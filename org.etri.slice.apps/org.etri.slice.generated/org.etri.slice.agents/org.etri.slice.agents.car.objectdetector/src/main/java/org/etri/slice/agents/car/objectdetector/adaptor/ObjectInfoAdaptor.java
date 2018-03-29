package org.etri.slice.agents.car.objectdetector.adaptor;

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
import org.etri.slice.agents.car.objectdetector.stream.ObjectInfoStream;
import org.etri.slice.commons.car.context.ObjectInfo;

@Component
@Instantiate
public class ObjectInfoAdaptor extends EventSubscriber<ObjectInfo> {
	
	private static final long serialVersionUID = -6773782700452198262L;

	@Property(name="topic", value=ObjectInfo.topic)
	private String m_topic;
	
	@Requires
	private WorkingMemory m_wm;

	@Requires
	private Agent m_agent;
	
	@Requires(from=ObjectInfoStream.SERVICE_NAME)
	private EventStream<ObjectInfo> m_streaming;	
	
	protected  String getTopicName() {
		return m_topic;
	}
	
	protected WorkingMemory getWorkingMemory() {
		return m_wm;
	}
	
	protected Agent getAgent() {
		return m_agent;
	}
	
	protected EventStream<ObjectInfo> getEventStream() {
		return m_streaming;
	}
		
	@Subscriber(name="ObjectInfoAdaptor", topics=ObjectInfo.topic,
			dataKey=ObjectInfo.dataKey, dataType=ObjectInfo.dataType)
	public void receive(ObjectInfo event) {
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

