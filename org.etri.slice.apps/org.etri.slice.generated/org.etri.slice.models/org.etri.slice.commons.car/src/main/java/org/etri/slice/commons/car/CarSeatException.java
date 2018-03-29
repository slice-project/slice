package org.etri.slice.commons.car;

import org.etri.slice.commons.SliceException;


public class CarSeatException extends SliceException {
	
	private static final long serialVersionUID = 4572874619014760848L;
	
	public CarSeatException(String msg) {
		super(msg);
	}

	public CarSeatException(Throwable e) {
		super(e);
	}
}
