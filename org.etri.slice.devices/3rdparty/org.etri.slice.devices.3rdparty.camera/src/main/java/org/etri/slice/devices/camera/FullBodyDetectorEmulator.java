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
package org.etri.slice.devices.camera;

import javax.swing.JDialog;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.handlers.event.Publishes;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.etri.slice.commons.SliceException;
import org.etri.slice.commons.car.BodyPartLength;
import org.etri.slice.commons.car.service.FullBodyDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory=false, immediate=true)
@Provides
//@Instantiate
public class FullBodyDetectorEmulator implements FullBodyDetector {
	
	private static Logger s_logger = LoggerFactory.getLogger(CVFullBodyDetector.class);	
	
	@Publishes(name="CVFullBodyDetector", topics=BodyPartLength.topic, dataKey=BodyPartLength.dataKey)
	private Publisher m_publisher;	

	@Override
	public void start() throws SliceException {
		s_logger.info("STARTED : " + this.getClass().getSimpleName());		
	}

	@Override
	public void stop() {
		s_logger.info("STOPPED : " + this.getClass().getSimpleName());		
	}

	@Override
	public void detect(double distance) throws SliceException {
	
		try {
			FullBodyDetectorGUI dialog = new FullBodyDetectorGUI(m_publisher);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
