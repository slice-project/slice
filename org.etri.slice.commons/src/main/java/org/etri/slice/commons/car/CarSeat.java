package org.etri.slice.commons.car;

public interface CarSeat {
			
	public void setPosture(double height, double position, double tilt);
	
	public SeatPosture getPosture();
	
	public void setPosture(BodyLength size);
}
