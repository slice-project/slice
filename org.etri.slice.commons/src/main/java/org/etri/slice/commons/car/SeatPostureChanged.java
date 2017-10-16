package org.etri.slice.commons.car;

import java.io.Serializable;

import org.kie.api.definition.type.Role;

@Role(Role.Type.EVENT)
public class SeatPostureChanged implements Serializable {
	private static final long serialVersionUID = -3709125277135630330L;

	private SeatPosture posture;
	
	public SeatPostureChanged() {
		
	}
	
	public SeatPostureChanged(SeatPosture posture) {
		this.posture = posture;
	}
	
	public void setSeatPosture(SeatPosture posture) {
		this.posture = posture;
	}
	
	public SeatPosture getSeatPosture() {
		return posture;
	}
}
