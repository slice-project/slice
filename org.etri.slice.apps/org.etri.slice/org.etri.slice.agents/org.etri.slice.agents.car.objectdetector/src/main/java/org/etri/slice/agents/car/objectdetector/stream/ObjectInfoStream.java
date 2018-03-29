package org.etri.slice.agents.car.objectdetector.stream;

import org.apache.edgent.topology.TStream;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.etri.slice.api.perception.EventStream;	
import org.etri.slice.commons.car.context.ObjectInfo;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate(name=ObjectInfoStream.SERVICE_NAME)
public class ObjectInfoStream implements EventStream<ObjectInfo> {

	public static final String SERVICE_NAME = "ObjectInfoStream";
	
	@Override
	public TStream<ObjectInfo> process(TStream<ObjectInfo> stream) {
		return stream;
	}

}

