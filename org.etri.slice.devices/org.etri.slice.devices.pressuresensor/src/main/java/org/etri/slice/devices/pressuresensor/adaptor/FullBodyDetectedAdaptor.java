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
package org.etri.slice.devices.pressuresensor.adaptor;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.api.device.Device;
import org.etri.slice.api.inference.WorkingMemory;
import org.etri.slice.api.perception.EventStream;
import org.etri.slice.commons.car.event.FullBodyDetected;
import org.etri.slice.core.perception.MqttEventSubscriber;
import org.etri.slice.devices.pressuresensor.stream.FullBodyDetectedStream;

@Component
@Instantiate
public class FullBodyDetectedAdaptor extends MqttEventSubscriber<FullBodyDetected> {
	
	private static final long serialVersionUID = 3605095190247075160L;

	@Property(name="topic", value="full_body_detected")
	private String m_topic;
	
	@Property(name="url", value="tcp://localhost:1883")
	private String m_url;
	
	@Requires
	private WorkingMemory m_wm;

	@Requires
	private Device m_device;
	
	@Requires(from=FullBodyDetectedStream.SERVICE_NAME)
	private EventStream<FullBodyDetected> m_streaming;	
	
	protected  String getTopicName() {
		return m_topic;
	}
	
	protected String getMqttURL() {
		return m_url;
	}
	
	protected WorkingMemory getWorkingMemory() {
		return m_wm;
	}
	
	protected Device getDevice() {
		return m_device;
	}
	
	protected Class<?> getEventType() {
		return FullBodyDetected.class;
	}
	
	protected EventStream<FullBodyDetected> getEventStream() {
		return m_streaming;
	}	
	
	@Validate
	public void start() {
		super.start();
	}
	
	@Invalidate
	public void stop() {
		super.stop();
	}
}
