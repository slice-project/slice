
package org.etri.slice.agents.car.carseat.wrapper;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.etri.slice.api.inference.WorkingMemory;

import org.etri.slice.commons.car.context.SeatPosture;
import org.etri.slice.commons.car.service.SeatControl;

@Component
@Instantiate
public class SeatControlWrapper implements SeatControl {
	
	@Requires
	private SeatControl m_proxy;
	
	@Requires
	private WorkingMemory m_wm;
	
	public SeatControlWrapper() {
		m_wm.addServiceWrapper(SeatControl.id, this);
	}
	
	@Override
	public double getHeight() {
		return m_proxy.getHeight();
	}
	
	@Override		        
	public void setHeight(double height) {
		m_proxy.setHeight(height);
	}
	
	@Override
	public double getPosition() {
		return m_proxy.getPosition();
	}
	
	@Override		        
	public void setPosition(double position) {
		m_proxy.setPosition(position);
	}
	
	@Override
	public double getTilt() {
		return m_proxy.getTilt();
	}
	
	@Override		        
	public void setTilt(double tilt) {
		m_proxy.setTilt(tilt);
	}
	
	@Override
	public SeatPosture getPosture() {
		return m_proxy.getPosture();
	}
	
	@Override		        
	public void setPosture(SeatPosture posture) {
		m_proxy.setPosture(posture);
	}
	
}
