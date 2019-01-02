package org.etri.slice.commons.room;

import org.etri.slice.commons.SliceException;


public class RoomException extends SliceException {
	
	private static final long serialVersionUID = 6218417613604277854L;
	
	public RoomException(String msg) {
		super(msg);
	}

	public RoomException(Throwable e) {
		super(e);
	}
}
