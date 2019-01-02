package org.etri.slice.agents.room.fancontroller;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.etri.slice.api.agent.Agent;
import org.etri.slice.core.agent.AbstractAgent;

@Component(publicFactory=false, immediate=true)
@Provides
@Instantiate
public class FanController extends AbstractAgent implements Agent {
	
	@Property(name="groupId", value="org.etri.slice")
	public String groupId;
	
	@Property(name="artifactId", value="org.etri.slice.rules.room.fancontroller")
	public String artifactId;	
	
//	@Property(name="version", value="0.0.1")
	public String version;

	@Override
	public String getGroupId() {
		return groupId;
	}

	@Override
	public String getArtifactId() {
		return artifactId;
	}

	@Override
	public String getVersion() {
		return version;
	}
}
