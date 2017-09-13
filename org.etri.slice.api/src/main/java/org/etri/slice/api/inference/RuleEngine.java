package org.etri.slice.api.inference;

import org.kie.api.builder.ReleaseId;

public interface RuleEngine {
	
	ReleaseId getReleaseId();
	
	ReleaseId newReleaseId(String version);
	
	ProductionMemory getProductionMemory();
	
	WorkingMemory getWorkingMemory();
}
