package org.etri.slice.devices.room.fancontroller;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.commons.SliceException;
import org.etri.slice.commons.room.service.ElectronicFan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;		

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
		
public class ElectronicFanService implements ElectronicFan {
	
	private static Logger s_logger = LoggerFactory.getLogger(ElectronicFanService.class);	
	
	private ElectronicFan m_service;	
			
	@Validate
	public void init() throws SliceException {
			
	}
			
	@Invalidate
	public void fini() throws SliceException {
		
	}
		
	@Override
	public int getLevel() {
		return m_service.getLevel();
	}
	
	@Override		        
	public void setLevel(int level) {
		m_service.setLevel(level);
	}
	
}
