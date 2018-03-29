package org.etri.slice.agents.car.pressuresensor.stream;

import org.apache.edgent.topology.TStream;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.etri.slice.api.perception.EventStream;	
import org.etri.slice.commons.car.event.FullBodyDetected;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate(name=FullBodyDetectedStream.SERVICE_NAME)
public class FullBodyDetectedStream implements EventStream<FullBodyDetected> {

	public static final String SERVICE_NAME = "FullBodyDetectedStream";
	
	@Override
	public TStream<FullBodyDetected> process(TStream<FullBodyDetected> stream) {
		return stream;
	}

}

