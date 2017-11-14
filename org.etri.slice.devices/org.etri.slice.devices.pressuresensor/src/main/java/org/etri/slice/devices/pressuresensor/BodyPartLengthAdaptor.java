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
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.handlers.event.Subscriber;
import org.etri.slice.api.device.Device;
import org.etri.slice.api.inference.WorkingMemory;
import org.etri.slice.commons.car.BodyPartLength;
import org.etri.slice.core.perception.EventSubscriber;

@Component
@Instantiate
public class BodyPartLengthAdaptor extends EventSubscriber<BodyPartLength> {
	
	private static final long serialVersionUID = -8022799918758850247L;

	@Property(name="topic", value="body_part_length")
	private String m_topic;
	
	@Requires
	protected WorkingMemory m_wm;

	@Requires
	private Device m_device;
	
	protected  String getTopicName() {
		return m_topic;
	}
	
	protected WorkingMemory getWorkingMemory() {
		return m_wm;
	}
	
	protected Device getDevice() {
		return m_device;
	}
		
	@Subscriber(name="sub", topics="body_part_length",
			dataKey="body.part.length", dataType="org.etri.slice.commons.car.BodyPartLength")
	public void receive(BodyPartLength data) {
		super.subscribe(data);
	}
	
	@Validate
	public void start() {
		super.start(data -> {
			m_wm.insert(data); 
		});
	}
	
	@Invalidate
	public void stop() {
		super.stop();
	}
}
