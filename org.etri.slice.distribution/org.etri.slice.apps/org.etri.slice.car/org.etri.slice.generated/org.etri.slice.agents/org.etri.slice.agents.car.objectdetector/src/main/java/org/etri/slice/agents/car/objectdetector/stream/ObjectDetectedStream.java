package org.etri.slice.agents.car.objectdetector.stream;

import org.apache.edgent.topology.TStream;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.etri.slice.api.perception.EventStream;	
import org.etri.slice.commons.car.event.ObjectDetected;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate(name=ObjectDetectedStream.SERVICE_NAME)
public class ObjectDetectedStream implements EventStream<ObjectDetected> {

	public static final String SERVICE_NAME = "ObjectDetectedStream";
	
	@Override
	public TStream<ObjectDetected> process(TStream<ObjectDetected> stream) {
		return stream;
	}

}

