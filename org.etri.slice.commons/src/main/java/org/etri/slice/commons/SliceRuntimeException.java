package org.etri.slice.commons;


/**
 * <code>SliceException</code>는 SLICE 프레임워크에서 발생되는  unchecked 예외의
 * 최상위 클래스이다.
 * 
 * @author Young-Ho Suh (ETRI)
 */
public class SliceRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -5071741852389944469L;
	
	private String m_details;

	public SliceRuntimeException(String details) {
		super(details);
		
		m_details = details;
	}

	public SliceRuntimeException(String details, Throwable cause) {
		super(details, cause);
		
		m_details = details + ", cause=" + cause;
	}

	public SliceRuntimeException(Throwable cause) {
		super(cause);
		
		m_details = "cause=" + cause;
	}
	
	public String getDetails() {
		return m_details;
	}
}
