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
package org.etri.slice.devices.pressuresensor;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.handlers.event.Publishes;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.etri.slice.commons.car.event.Pressure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Instantiate
public class PressureSensor implements Runnable {
	
	private static Logger s_logger = LoggerFactory.getLogger(PressureSensor.class);	

	@Property(name="interval", value="7000")
	public long m_interval;	
	
	@Publishes(name="pub:seat_pressure", topics="seat_pressure", dataKey="seat.pressure")
	private Publisher m_publisher;
	
	@Validate
	public void start() {
		new Thread(this).start();
		s_logger.info("PressureSensor started.");
	}
	
	@Invalidate
	public void stop() {
		s_logger.info("PressureSensor stoppted");
		
	}

	@Override
	public void run() {
		while ( true ) {
			sleep();
			
			Pressure pressure = Pressure.builder().value(45).build();
			m_publisher.sendData(pressure);
			
			s_logger.info("PUB: " + pressure);
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
