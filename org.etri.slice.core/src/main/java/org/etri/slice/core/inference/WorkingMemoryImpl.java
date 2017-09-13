package org.etri.slice.core.inference;

import java.util.Collection;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.api.inference.WorkingMemory;
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
