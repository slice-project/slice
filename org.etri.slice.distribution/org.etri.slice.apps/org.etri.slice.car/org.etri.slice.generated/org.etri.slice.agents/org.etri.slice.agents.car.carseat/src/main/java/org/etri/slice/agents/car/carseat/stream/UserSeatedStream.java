package org.etri.slice.agents.car.carseat.stream;

import org.apache.edgent.topology.TStream;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.etri.slice.api.perception.EventStream;	
import org.etri.slice.commons.car.event.UserSeated;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate(name=UserSeatedStream.SERVICE_NAME)
public class UserSeatedStream implements EventStream<UserSeated> {

	public static final String SERVICE_NAME = "UserSeatedStream";
	
	@Override
	public TStream<UserSeated> process(TStream<UserSeated> stream) {
		return stream;
	}

}

