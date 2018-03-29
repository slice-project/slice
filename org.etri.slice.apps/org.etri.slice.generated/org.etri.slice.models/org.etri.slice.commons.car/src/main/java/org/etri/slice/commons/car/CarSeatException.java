package org.etri.slice.commons.car;

import org.etri.slice.commons.SliceException;


public class CarSeatException extends SliceException {
	
	private static final long serialVersionUID = 260519917062996743L;
	
	public CarSeatException(String msg) {
		super(msg);
	}

	public CarSeatException(Throwable e) {
		super(e);
	}
}
