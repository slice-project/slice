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
package org.etri.slice.core.inference;

import java.util.Collection;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.api.inference.WorkingMemory;
import org.kie.api.runtime.Channel;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.kie.api.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class WorkingMemoryImpl implements WorkingMemory {

	private static Logger logger = LoggerFactory.getLogger(WorkingMemoryImpl.class);	
	
	@Requires
	private DroolsRuleEngine m_drools;
	
	private KieSession m_session;

	@Override
	public synchronized void insert(Object fact) {
		m_session.insert(fact);
	}

	@Override
	public synchronized void delete(Object fact) {
		FactHandle handle = m_session.getFactHandle(fact);
		m_session.delete(handle);
	}

	@Override
	public synchronized void update(Object before, Object after) {
		FactHandle handle = m_session.getFactHandle(before);
		m_session.update(handle, after);
	}

	@Override
	public synchronized long getFactCount() {
		
		return m_session.getFactCount();
	}

	@Override
	public synchronized Collection<? extends Object> getObjects() {
		
		return m_session.getObjects();
	}

	@Override
	public synchronized Collection<? extends Object> getObjects(ObjectFilter filter) {
		
		return m_session.getObjects(filter);
	}

	@Override
	public synchronized void addServiceAdaptor(String id, Object adaptor) {
		m_session.setGlobal(id, adaptor);		
	}	

	@Override
	public synchronized void addEventAdaptor(String id, Channel adaptor) {
		m_session.registerChannel(id, adaptor);
	}
	
	@Validate
	public void start() {
		logger.info("SLICE Working Memory is started");
		m_session = m_drools.getKieSession();
	}
	
	@Invalidate
	public void stop() {
		m_session.destroy();
	}
}
