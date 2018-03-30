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
package org.etri.slice.devices.car.fullbodydetector;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.commons.SliceException;
import org.etri.slice.commons.SliceException;
import org.etri.slice.commons.car.FullBodyException;
import org.etri.slice.commons.car.service.FullBodyDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;		

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class FullBodyDetectorService implements FullBodyDetector {
	
	private static Logger s_logger = LoggerFactory.getLogger(FullBodyDetectorService.class);	
	
	private FullBodyDetector m_service;	
	
	@Validate
	public void init() throws SliceException {
		
	}

	@Invalidate
	public void fini() throws SliceException {
		
	}
	
	@Override
	public void start() throws SliceException {
		m_service.start();
	}
	
	@Override
	public void stop() {
		m_service.stop();
	}
	
	@Override
	public void detect(double distance) throws FullBodyException {
		m_service.detect( distance);
	}
	
}
