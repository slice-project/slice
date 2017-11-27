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

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.etri.slice.api.inference.ProductionMemory;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.scanner.MavenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class ProductionMemoryImpl implements ProductionMemory {

	private static Logger logger = LoggerFactory.getLogger(ProductionMemoryImpl.class);	
	
	@Requires
	private DroolsRuleEngine m_drools;	
	
	private KieContainer m_container;
	private MavenRepository m_repository;
	
	@Override
	public synchronized String getCurrentVersion() {
		ReleaseId releaseId = m_drools.getReleaseId();
		return releaseId.getVersion();
	}

	@Override
	public synchronized void install(String version, byte[] jarContent, byte[] pomContent) {
		ReleaseId releaseId = m_drools.newReleaseId(version);
		m_repository.installArtifact(releaseId, jarContent, pomContent);
		m_container.updateToVersion(releaseId);
	}

	@Override
	public synchronized void install(String version, File jar, File pomFile) {
		ReleaseId releaseId = m_drools.newReleaseId(version);
		m_repository.installArtifact(releaseId, jar, pomFile);
		m_container.updateToVersion(releaseId);
	}

	@Override
	public synchronized void update(String version, String... rules) {
		ReleaseId releaseId = m_drools.newReleaseId(version);
	}

	@Validate
	public void start() {
		m_container = m_drools.getKieContainer();
		logger.info("SLICE ProductionMemory is started");
	}
	
	@Invalidate
	public void stop() {
		m_container.dispose();
	}
		
}
