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
package org.etri.slice.devices.fullbodydetector;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.etri.slice.api.inference.WorkingMemory;
import org.etri.slice.commons.SliceException;
import org.etri.slice.commons.car.service.FullBodyDetector;

@Component
@Instantiate
public class FullBodyDetectorAdaptor implements FullBodyDetector {

	@Requires
	private FullBodyDetector m_detector;
	
	@Requires
	protected WorkingMemory m_wm;
	
	public FullBodyDetectorAdaptor() {
		m_wm.addServiceAdaptor("fullBodyDetector", this);
	}

	@Override
	public void start() throws SliceException {
		m_detector.start();
	}

	@Override
	public void stop() {
		m_detector.stop();
	}

	@Override
	public void detect() throws SliceException {
		m_detector.detect();
	}
	
}
