package org.etri.slice.commons.car;

import org.etri.slice.commons.SliceException;


public class FullBodyException extends SliceException {
	
	private static final long serialVersionUID = 5134623469358170462L;
	
	public FullBodyException(String msg) {
		super(msg);
	}

	public FullBodyException(Throwable e) {
		super(e);
	}
}
