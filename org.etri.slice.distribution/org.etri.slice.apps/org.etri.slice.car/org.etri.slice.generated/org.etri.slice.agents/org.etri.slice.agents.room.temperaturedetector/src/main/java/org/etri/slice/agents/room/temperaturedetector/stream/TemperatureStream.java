package org.etri.slice.agents.room.temperaturedetector.stream;

import org.apache.edgent.topology.TStream;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.etri.slice.api.perception.EventStream;	
import org.etri.slice.commons.room.context.Temperature;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate(name=TemperatureStream.SERVICE_NAME)
public class TemperatureStream implements EventStream<Temperature> {

	public static final String SERVICE_NAME = "TemperatureStream";
	
	@Override
	public TStream<Temperature> process(TStream<Temperature> stream) {
		return stream;
	}

}

