package org.etri.slice.api.inference;

import java.io.File;

public interface ProductionMemory {

	String getCurrentVersion();
	
	void install(String version, byte[] jarContent, byte[] pomContent);
	
	void install(String version, File jar, File pomFile);
	
	void update(String version, String... rules);
}
