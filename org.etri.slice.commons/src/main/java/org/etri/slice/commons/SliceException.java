package org.etri.slice.commons;


/**
 * <code>SliceException</code>는 SLICE 프레임워크에서 발생되는  checked 예외의
 * 최상위 클래스이다.
 * 
 * @author Young-Ho Suh (ETRI)
 */
public class SliceException extends Exception {
	private static final long serialVersionUID = -925163839577490282L;
	
	private String m_details;

	public SliceException() {
		m_details = null;
	}

	public SliceException(String details) {
		super(details);
		
		m_details = details;
	}

	public SliceException(String details, Throwable cause) {
		super(details, cause);
		
		m_details = details + ", cause=" + cause;
	}
	
	public SliceException(Throwable cause) {
		super(cause);
		
		m_details = "cause=" + cause;
	}
	
	public String getDetails() {
		return m_details;
	}
}
