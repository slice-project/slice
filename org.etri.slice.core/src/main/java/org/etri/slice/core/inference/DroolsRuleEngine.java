package org.etri.slice.core.inference;

import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.scanner.MavenRepository;

public interface DroolsRuleEngine {
	
	ReleaseId getReleaseId();
	
	ReleaseId newReleaseId(String version);
	
	KieContainer getKieContainer();
	
	KieSession getKieSession();

	MavenRepository getMavenRepository();	
}
