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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.eclipse.aether.version.Version;
import org.etri.slice.api.device.Device;
import org.etri.slice.api.perception.ContextMemory;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.scanner.MavenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class DroolsRuleEngineImpl implements DroolsRuleEngine {

	private static Logger s_logger = LoggerFactory.getLogger(DroolsRuleEngineImpl.class);
	
	@Property(name="scan", value="10000")
	public long m_scanInterval;

	@Requires
	private Device m_device;
	@Requires
	private ContextMemory m_cm;
	private String m_groupId;
	private String m_artifactId;	
	private String m_version;	
	private ReleaseId m_releaseId;
	private KieContainer m_container;
	private KieSession m_session;
	private KieScanner m_scanner;
	private KieServices m_services;
	private MavenRepository m_repository;
	private final Lock m_sessionLock = new ReentrantLock();
	
	@Override
	public synchronized ReleaseId getReleaseId() {
		return m_releaseId;
	}

	@Override
	public synchronized ReleaseId newReleaseId(String version) {
		m_releaseId= m_services.newReleaseId(m_groupId, m_artifactId, version);
		return m_releaseId;
	}	
	
	@Override
	public KieContainer getKieContainer() {
		return m_container;
	}

	@Override
	public KieSession getKieSession() {
		return m_session;
	}

	@Override
	public MavenRepository getMavenRepository() {
		return m_repository;
	}

	@Override
	public void fireUntilHalt() {
		new Thread(new FireUntilHalt()).start();
		try {
			Thread.sleep(10);
		} 
		catch ( InterruptedException ignored ) { }
	}
	
	@Override
	public void halt() {
		m_session.halt();
	}
	
	@Override
	public Lock getSessionLock() {
		return m_sessionLock;
	}	
	
	@Validate
	public void start() {
		m_groupId = m_device.getGroupId();
		m_artifactId = m_device.getArtifactId();
		m_version = m_device.getVersion();
		m_repository = MavenRepository.getMavenRepository();		
		m_services = KieServices.Factory.get();
		
		if ( m_version == null ) {
			Version version = m_repository.resolveVersion(m_groupId + ":" + m_artifactId + ":[0,1)");
			m_version = version.toString();
		}
		m_releaseId = m_services.newReleaseId(m_groupId, m_artifactId, m_version);
		m_container = m_services.newKieContainer(m_releaseId);
		m_scanner = m_services.newKieScanner(m_container);		
		m_session = m_container.newKieSession();
		
		m_session.addEventListener(new AgendaEventListenerImpl());
		m_session.addEventListener(new RuleRuntimeEventListenerImpl(m_cm));
		
		m_scanner.start(m_scanInterval);
		fireUntilHalt();
	}
	
	@Invalidate
	public void stop() {
		m_scanner.stop();
		m_session.halt();
		s_logger.info("STOPPED: DroolsRuleEngine");
	}

	class FireUntilHalt implements Runnable {
		
		@Override
		public void run() {
			try {
				s_logger.info("STARTED: fireUntilHalt");
				m_session.fireUntilHalt();
			}
			catch ( IllegalStateException e ) {
				s_logger.error("ERR: " + e.getMessage());
			}
			finally {
				s_logger.info("STOPPED: fireUntilHalt");
			}
		}
	}
}	

