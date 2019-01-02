
package org.etri.slice.agents.room.fancontroller.wrapper;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.etri.slice.api.inference.WorkingMemory;

import org.etri.slice.commons.room.service.ElectronicFan;

@Component
@Instantiate
public class ElectronicFanWrapper implements ElectronicFan {
	
	@Requires
	private ElectronicFan m_proxy;
	
	@Requires
	private WorkingMemory m_wm;
	
	public ElectronicFanWrapper() {
		m_wm.addServiceWrapper(ElectronicFan.id, this);
	}
	
	@Override
	public int getLevel() {
		return m_proxy.getLevel();
	}
	
	@Override		        
	public void setLevel(int level) {
		m_proxy.setLevel(level);
	}
}
