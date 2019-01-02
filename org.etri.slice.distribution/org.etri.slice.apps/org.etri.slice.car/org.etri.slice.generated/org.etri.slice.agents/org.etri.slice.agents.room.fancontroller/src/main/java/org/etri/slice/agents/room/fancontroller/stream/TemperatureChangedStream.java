package org.etri.slice.agents.room.fancontroller.stream;

import org.apache.edgent.topology.TStream;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.etri.slice.api.perception.EventStream;	
import org.etri.slice.commons.room.event.TemperatureChanged;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate(name=TemperatureChangedStream.SERVICE_NAME)
public class TemperatureChangedStream implements EventStream<TemperatureChanged> {

	public static final String SERVICE_NAME = "TemperatureChangedStream";
	
	@Override
	public TStream<TemperatureChanged> process(TStream<TemperatureChanged> stream) {
		return stream;
	}

}

