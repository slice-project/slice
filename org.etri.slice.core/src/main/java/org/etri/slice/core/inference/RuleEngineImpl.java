package org.etri.slice.core.inference;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.api.inference.ProductionMemory;
import org.etri.slice.api.inference.RuleEngine;
import org.etri.slice.api.inference.WorkingMemory;
import org.kie.api.builder.ReleaseId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class RuleEngineImpl implements RuleEngine {	
	
	private static Logger logger = LoggerFactory.getLogger(RuleEngineImpl.class);
	
	@Requires
	private DroolsRuleEngine m_drools;
	
	@Requires
	private ProductionMemory m_pm;
	
	@Requires
	private WorkingMemory m_wm;
	
	@Override
	public ReleaseId getReleaseId() {
		return m_drools.getReleaseId();
	}

	@Override
	public ReleaseId newReleaseId(String version) {
		return m_drools.newReleaseId(version);
	}

	@Override
	public ProductionMemory getProductionMemory() {
		return m_pm;
	}

	@Override
	public WorkingMemory getWorkingMemory() {
		return m_wm;
	}

	@Validate
	public void start() {
		logger.info("RuleEngine is started");
	}
	
	@Invalidate
	public void stop() {
		
	}	
}
