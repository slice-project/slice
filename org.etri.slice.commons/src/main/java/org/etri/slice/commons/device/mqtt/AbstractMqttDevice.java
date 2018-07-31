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
package org.etri.slice.commons.device.mqtt;

import java.util.concurrent.Future;

import org.apache.edgent.execution.Job;
import org.apache.edgent.providers.direct.DirectProvider;
import org.apache.edgent.topology.Topology;

import com.google.gson.JsonObject;

public abstract class AbstractMqttDevice implements MqttDevice {

	private final DirectProvider m_provider = new DirectProvider();	
	
	@Override
	public Future<Job> submit(Topology topology) {
		return m_provider.submit(topology);
	}

	@Override
	public Future<Job> submit(Topology topology, JsonObject config) {
		return m_provider.submit(topology, config);
	}

	@Override
	public Topology newTopology() {
		return m_provider.newTopology();
	}

	@Override
	public Topology newTopology(String name) {
		return m_provider.newTopology(name);
	}

}
