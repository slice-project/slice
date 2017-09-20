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
