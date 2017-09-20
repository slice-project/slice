/**
 *
 * Copyright (c) 2017-2017 SLICE project team (yhsuh@etri.re.kr)
 * http://slice.etri.re.kr
 *
 * This file is part of The SLICE components
 *
 * This Program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This Program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with The SLICE components; see the file COPYING.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
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
