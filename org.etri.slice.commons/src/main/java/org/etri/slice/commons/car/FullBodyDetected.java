package org.etri.slice.commons.car;

import org.kie.api.definition.type.Role;

@Role(Role.Type.EVENT)
public class FullBodyDetected extends ObjectDetected {

	private static final long serialVersionUID = 483052562807387451L;
	
	private BodyLength length;
	
	public FullBodyDetected() {
		super();
	}
	
	public FullBodyDetected(Double distance, BodyLength length) {
		super(distance);
		this.length = length;
	}
	
	public BodyLength getBodyLength() {
		return length;
	}
	
	public void setBodyLength(BodyLength length) {
		this.length = length;
	}
}
