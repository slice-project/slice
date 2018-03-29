package org.etri.slice.agents.car.fullbodydetector.stream;

import org.apache.edgent.topology.TStream;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.etri.slice.api.perception.EventStream;	
import org.etri.slice.commons.car.context.BodyPartLength;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate(name=BodyPartLengthStream.SERVICE_NAME)
public class BodyPartLengthStream implements EventStream<BodyPartLength> {

	public static final String SERVICE_NAME = "BodyPartLengthStream";
	
	@Override
	public TStream<BodyPartLength> process(TStream<BodyPartLength> stream) {
		return stream;
	}

}

