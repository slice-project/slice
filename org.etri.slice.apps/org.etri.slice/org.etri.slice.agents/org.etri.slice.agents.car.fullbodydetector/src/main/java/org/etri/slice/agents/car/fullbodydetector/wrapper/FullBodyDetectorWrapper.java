
package org.etri.slice.agents.car.fullbodydetector.wrapper;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.etri.slice.api.inference.WorkingMemory;

import org.etri.slice.commons.SliceException;
import org.etri.slice.commons.car.FullBodyException;
import org.etri.slice.commons.car.service.FullBodyDetector;

@Component
@Instantiate
public class FullBodyDetectorWrapper implements FullBodyDetector {
	
	@Requires
	private FullBodyDetector m_proxy;
	
	@Requires
	private WorkingMemory m_wm;
	
	public FullBodyDetectorWrapper() {
		m_wm.addServiceWrapper(FullBodyDetector.id, this);
	}
	
	@Override
	public void start() throws SliceException {
		m_proxy.start();
	}
	
	@Override
	public void stop() {
		m_proxy.stop();
	}
	
	@Override
	public void detect(double distance) throws FullBodyException {
		m_proxy.detect( distance);
	}
	
}
