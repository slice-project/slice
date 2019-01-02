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
package org.etri.slice.core.agent;

import java.util.Hashtable;
import java.util.concurrent.Future;

import org.apache.edgent.execution.Job;
import org.apache.edgent.execution.services.ControlService;
import org.apache.edgent.function.BiConsumer;
import org.apache.edgent.providers.direct.DirectProvider;
import org.apache.edgent.runtime.jmxcontrol.JMXControlService;
import org.apache.edgent.topology.Topology;
import org.etri.slice.api.agent.Agent;

import com.google.gson.JsonObject;

public abstract class AbstractAgent implements Agent {

	public static final String JMX_DOMAIN = "org.etri.slice.agents";
	private final DirectProvider m_provider = new DirectProvider();
	
	public AbstractAgent() {
		m_provider.getServices().addService(ControlService.class,
                new JMXControlService(JMX_DOMAIN, new Hashtable<>()));		
	}
	
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

	@Override
	public void addCleaner(BiConsumer<String, String> cleaner) {
		m_provider.getServices().addCleaner(cleaner);		
	}

	@Override
	public <T> T addService(Class<T> serviceClass, T service) {
		return m_provider.getServices().addService(serviceClass, service);
	}

	@Override
	public void cleanOplet(String jobId, String elementId) {
		m_provider.getServices().cleanOplet(jobId, elementId);
	}

	@Override
	public <T> T getService(Class<T> serviceClass) {
		return m_provider.getServices().getService(serviceClass);
	}

	@Override
	public <T> T removeService(Class<T> serviceClass) {
		return m_provider.getServices().removeService(serviceClass);
	}

}
