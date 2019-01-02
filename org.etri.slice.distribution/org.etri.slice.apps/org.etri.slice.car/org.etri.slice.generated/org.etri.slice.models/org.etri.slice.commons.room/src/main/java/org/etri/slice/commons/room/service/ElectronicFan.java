package org.etri.slice.commons.room.service;

import javax.management.MXBean;

@MXBean
public interface ElectronicFan {
	
	static final String id = "electronicFan";
	static final String setLevel = "electronicFan.setLevel";
	
	int getLevel();
				        
	void setLevel(int level);
	
}
