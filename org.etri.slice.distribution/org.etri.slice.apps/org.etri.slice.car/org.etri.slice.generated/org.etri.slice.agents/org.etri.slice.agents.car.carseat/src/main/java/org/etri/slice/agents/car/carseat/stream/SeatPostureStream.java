package org.etri.slice.agents.car.carseat.stream;

import org.apache.edgent.topology.TStream;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.etri.slice.api.perception.EventStream;	
import org.etri.slice.commons.car.context.SeatPosture;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate(name=SeatPostureStream.SERVICE_NAME)
public class SeatPostureStream implements EventStream<SeatPosture> {

	public static final String SERVICE_NAME = "SeatPostureStream";
	
	@Override
	public TStream<SeatPosture> process(TStream<SeatPosture> stream) {
		return stream;
	}

}

