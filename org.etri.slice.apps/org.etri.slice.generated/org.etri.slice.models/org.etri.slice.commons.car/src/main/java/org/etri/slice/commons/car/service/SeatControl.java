package org.etri.slice.commons.car.service;

import javax.management.MXBean;

import org.etri.slice.commons.car.context.SeatPosture;

@MXBean
public interface SeatControl {
	
	static final String id = "seatControl";
	static final String setHeight = "seatControl.setHeight";
	static final String setPosition = "seatControl.setPosition";
	static final String setTilt = "seatControl.setTilt";
	static final String setPosture = "seatControl.setPosture";
	
	double getHeight();
				        
	void setHeight(double height);
	
	double getPosition();
				        
	void setPosition(double position);
	
	double getTilt();
				        
	void setTilt(double tilt);
	
	SeatPosture getPosture();
				        
	void setPosture(SeatPosture posture);
	
}
