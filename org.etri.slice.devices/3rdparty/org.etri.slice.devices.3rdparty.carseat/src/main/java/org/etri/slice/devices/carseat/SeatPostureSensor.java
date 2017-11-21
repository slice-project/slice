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
package org.etri.slice.devices.carseat;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.handlers.event.Publishes;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.etri.slice.commons.car.event.SeatPosture;
import org.etri.slice.commons.car.service.SeatControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Instantiate
public class SeatPostureSensor implements Runnable {
	
	private static Logger s_logger = LoggerFactory.getLogger(SeatPostureSensor.class);	

	@Property(name="interval", value="15000")
	public long m_interval;	
	
	@Publishes(name="pub:seat_posture", topics="seat_posture", dataKey="seat.posture")
	private Publisher m_publisher;
	
	@Requires
	private SeatControl m_posture;
	
	@Validate
	public void start() {
		new Thread(this).start();
		s_logger.info("SeatPostureSensor started.");
	}
	
	@Invalidate
	public void stop() {
		s_logger.info("SeatPostureSensor stoppted");
		
	}

	@Override
	public void run() {
		while ( true ) {
			sleep();
			
			SeatPosture posture = m_posture.getPosture();
			m_publisher.sendData(posture);
			s_logger.info("PUB: " + posture);
		}
	}
	
	private void sleep() {
		try {
			Thread.sleep(m_interval);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}	
}
