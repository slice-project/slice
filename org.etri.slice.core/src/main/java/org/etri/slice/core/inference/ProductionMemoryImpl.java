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

import java.io.File;
import java.util.StringTokenizer;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.api.inference.ProductionMemory;
import org.etri.slice.api.inference.ProductionMemoryException;
import org.etri.slice.api.rule.RuleModule;
import org.etri.slice.core.rule.RuleModuleImpl;
import org.etri.slice.core.rule.RuleVersionManager;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.scanner.MavenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class ProductionMemoryImpl implements ProductionMemory {

	private static Logger s_logger = LoggerFactory.getLogger(ProductionMemoryImpl.class);
	
	private static final String RULE_ROOT = "rule";
	
	@Requires
	private DroolsRuleEngine m_drools;	
	
	private KieContainer m_container;
	private MavenRepository m_repository;
	private RuleModule m_module;
	private RuleVersionManager m_verMgr;
	
	@Validate
	public void start() throws Exception {
		m_container = m_drools.getKieContainer();
		m_repository = m_drools.getMavenRepository();
		ReleaseId releaseId = m_drools.getReleaseId();
		RuleModuleImpl module = new RuleModuleImpl(RULE_ROOT, releaseId);
		module.setUp();
		m_module = module;

		StringTokenizer token = new StringTokenizer(releaseId.getVersion(), ".");
		String prefix = token.nextToken();
		String middle = token.nextToken();
		String suffix = token.nextToken();
		m_verMgr = new RuleVersionManager(prefix, middle, suffix);
		
		s_logger.info("SLICE ProductionMemory is started");
	}
	
	@Invalidate
	public void stop() {
		m_container.dispose();
	}
	
	@Override
	public synchronized String getVersion() {
		ReleaseId releaseId = m_drools.getReleaseId();
		return releaseId.getVersion();
	}
	
	@Override
	public synchronized String getNewVersion() {
		return m_verMgr.getNewVersion();
	}
	
	@Override
	public RuleModule getRuleModule() {
		return m_module;
	}

	@Override
	public synchronized void update(RuleModule ruleModule) throws ProductionMemoryException {
		try {
			m_drools.getSessionLock().lock();
			m_drools.halt();
			ReleaseId currentRelease = m_drools.getReleaseId();
			ReleaseId newRelease = m_drools.newReleaseId(ruleModule.getVersion());
			File jarFile = m_module.getRuleJarFile();
			File pomFile = m_module.getRulePOMFile();
			m_repository.installArtifact(currentRelease, jarFile, pomFile);
			m_container.updateToVersion(currentRelease);
			
			s_logger.info("UPDATED: " + newRelease);
			m_drools.fireUntilHalt();
			m_drools.getSessionLock().unlock();

			m_repository.installArtifact(newRelease, jarFile, pomFile);
		}
		catch ( Exception e ) {
			throw new ProductionMemoryException(e.getMessage());
		}		
	}	
}
