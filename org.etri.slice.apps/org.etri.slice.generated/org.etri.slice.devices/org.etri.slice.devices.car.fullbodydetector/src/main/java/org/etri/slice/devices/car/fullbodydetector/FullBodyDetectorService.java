package org.etri.slice.devices.car.fullbodydetector;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.commons.SliceException;
import org.etri.slice.commons.SliceException;
import org.etri.slice.commons.car.FullBodyException;
import org.etri.slice.commons.car.service.FullBodyDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;		

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class FullBodyDetectorService implements FullBodyDetector {
	
	private static Logger s_logger = LoggerFactory.getLogger(FullBodyDetectorServcie.class);	
	
	private FullBodyDetector m_service;	
	
	@Validate
	public void init() throws SliceException {
		
	}

	@Invalidate
	public void fini() throws SliceException {
		
	}
	
	@Override
	public void start() throws SliceException {
		m_service.start();
	}
	
	@Override
	public void stop() {
		m_service.stop();
	}
	
	@Override
	public void detect(double distance) throws FullBodyException {
		m_service.detect( distance);
	}
	
}
