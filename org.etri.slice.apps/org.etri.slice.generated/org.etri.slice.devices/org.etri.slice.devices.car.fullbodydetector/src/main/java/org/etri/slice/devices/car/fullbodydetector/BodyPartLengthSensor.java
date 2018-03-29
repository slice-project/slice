package org.etri.slice.devices.car.fullbodydetector;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.handlers.event.Publishes;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.etri.slice.commons.Sensor;
import org.etri.slice.commons.SliceException;
import org.etri.slice.commons.car.context.BodyPartLength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;		

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class BodyPartLengthSensor implements Sensor {
	
	private static Logger s_logger = LoggerFactory.getLogger(BodyPartLengthSensor.class);	
		
	@Publishes(name="pub:bodypartlength", topics=BodyPartLength.topic, dataKey=BodyPartLength.dataKey)
	private Publisher m_publisher;
	
	@Override
	@Validate
	public void start() throws SliceException {
		
	}

	@Override
	@Invalidate
	public void stop() throws SliceException {
		
	}
}
