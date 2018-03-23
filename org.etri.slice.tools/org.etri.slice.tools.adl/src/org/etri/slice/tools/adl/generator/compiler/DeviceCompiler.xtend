package org.etri.slice.tools.adl.generator.compiler

import com.google.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration

class DeviceCompiler {
	
	@Inject extension IQualifiedNameProvider
	
	def deviceCompile(AgentDeclaration it) '''
		«IF eContainer !== null»
			package org.etri.slice.devices.«eContainer.fullyQualifiedName».«name.toLowerCase»;
		«ENDIF»
		
		import org.apache.felix.ipojo.annotations.Component;
		import org.apache.felix.ipojo.annotations.Instantiate;
		import org.apache.felix.ipojo.annotations.Property;
		import org.apache.felix.ipojo.annotations.Provides;
		import org.etri.slice.api.device.Device;
		import org.etri.slice.core.device.AbstractDevice;

		@Component(publicFactory=false, immediate=true)
		@Provides
		@Instantiate
		«deviceBody»
	'''
 
	def deviceBody(AgentDeclaration it) '''
		public class «name» extends AbstractDevice implements Device {
			
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
