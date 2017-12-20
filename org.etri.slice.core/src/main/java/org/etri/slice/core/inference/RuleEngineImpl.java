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
	}
	
	@Invalidate
	public void stop() {
		
	}	
}
