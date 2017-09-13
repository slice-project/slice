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
		logger.info("SLICE ProductionMemory is started");
		m_container = m_drools.getKieContainer();
	}
	
	@Invalidate
	public void stop() {
		m_container.dispose();
	}
		
}
