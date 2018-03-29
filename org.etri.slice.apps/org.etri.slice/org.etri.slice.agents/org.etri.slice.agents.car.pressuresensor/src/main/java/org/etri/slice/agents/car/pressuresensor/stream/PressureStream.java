package org.etri.slice.agents.car.pressuresensor.stream;

import org.apache.edgent.topology.TStream;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.etri.slice.api.perception.EventStream;	
import org.etri.slice.commons.car.context.Pressure;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate(name=PressureStream.SERVICE_NAME)
public class PressureStream implements EventStream<Pressure> {

	public static final String SERVICE_NAME = "PressureStream";
	
	@Override
	public TStream<Pressure> process(TStream<Pressure> stream) {
		return stream;
	}

}

