package org.etri.slice.commons.car.service;

import javax.management.MXBean;

import org.etri.slice.commons.car.FullBodyException;
import org.etri.slice.commons.car.service.Startable;

@MXBean
public interface FullBodyDetector extends Startable {
	
	static final String id = "fullBodyDetector";
	
	void detect(double distance) throws FullBodyException;
	
}
