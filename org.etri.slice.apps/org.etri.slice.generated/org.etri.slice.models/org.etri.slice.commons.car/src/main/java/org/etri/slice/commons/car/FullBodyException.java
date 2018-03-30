package org.etri.slice.commons.car;

import org.etri.slice.commons.SliceException;


public class FullBodyException extends SliceException {
	
	private static final long serialVersionUID = -3692224409835060972L;
	
	public FullBodyException(String msg) {
		super(msg);
	}

	public FullBodyException(Throwable e) {
		super(e);
	}
}
