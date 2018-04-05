package org.etri.slice.commons.car.service;

import javax.management.MXBean;

import org.etri.slice.commons.SliceException;

@MXBean
public interface Startable {
	
	static final String id = "startable";
	
	void start() throws SliceException;
	
	void stop();
	
}
