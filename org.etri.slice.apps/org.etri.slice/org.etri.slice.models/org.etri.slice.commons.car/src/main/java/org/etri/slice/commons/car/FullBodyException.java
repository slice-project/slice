package org.etri.slice.commons.car;

import org.etri.slice.commons.SliceException;


public class FullBodyException extends SliceException {
	
	private static final long serialVersionUID = -8352618423718360851L;
	
	public FullBodyException(String msg) {
		super(msg);
	}

	public FullBodyException(Throwable e) {
		super(e);
	}
}
