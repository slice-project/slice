package org.etri.slice.tools.adl.generator.compiler

import com.google.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration

class AgentCompiler {
	
	@Inject extension IQualifiedNameProvider
	
	def agentCompile(AgentDeclaration it) '''
		«IF eContainer !== null»
			package org.etri.slice.agents.«eContainer.fullyQualifiedName».«name.toLowerCase»;
		«ENDIF»
		
		import org.apache.felix.ipojo.annotations.Component;
		import org.apache.felix.ipojo.annotations.Instantiate;
		import org.apache.felix.ipojo.annotations.Property;
		import org.apache.felix.ipojo.annotations.Provides;
		import org.etri.slice.api.agent.Agent;
		import org.etri.slice.core.agent.AbstractAgent;

		@Component(publicFactory=false, immediate=true)
		@Provides
		@Instantiate
		«agentBody»
	'''
 
	def agentBody(AgentDeclaration it) '''
		public class «name» extends AbstractAgent implements Agent {
			
			@Property(name="groupId", value="«ruleSet.group_id»")
			public String groupId;
			
			@Property(name="artifactId", value="org.etri.slice.rules.«eContainer.fullyQualifiedName».«ruleSet.artifact_id»")
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
	'''
}
