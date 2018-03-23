package org.etri.slice.tools.adl.generator.compiler

import com.google.inject.Inject
import java.util.UUID
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.Event
import org.etri.slice.tools.adl.generator.GeneratorUtils

class EventWrapperCompiler {
	
	@Inject extension IQualifiedNameProvider
	@Inject extension GeneratorUtils
	
	def compileWrapper(Event it, AgentDeclaration agent) '''
		«val importManager = new ImportManager(true)» 
		«val body = body(agent, importManager)»
		«IF eContainer !== null»
			package org.etri.slice.devices.«eContainer.fullyQualifiedName».«agent.name.toLowerCase».wrapper;
		«ENDIF»
		
		import org.apache.felix.ipojo.annotations.Component;
		import org.apache.felix.ipojo.annotations.Instantiate;
		import org.apache.felix.ipojo.annotations.Invalidate;
		import org.apache.felix.ipojo.annotations.Property;
		import org.apache.felix.ipojo.annotations.Requires;
		import org.apache.felix.ipojo.annotations.Validate;
		import org.etri.slice.api.device.Device;
		import org.etri.slice.api.inference.WorkingMemory;
		import org.etri.slice.api.perception.EventStream;
		import org.etri.slice.core.perception.MqttEventPublisher;
		import org.etri.slice.devices.«eContainer.fullyQualifiedName».«agent.name.toLowerCase».stream.«name»Stream;
		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
		
		«body»
		
	'''	
	
	def body(Event it, AgentDeclaration agent, ImportManager importManager) '''
		@Component
		@Instantiate
		public class «name»Channel extends MqttEventPublisher<«importManager.serialize(toJvmGenericType(fullyQualifiedName, "event"))»> {
		
			private static final long serialVersionUID = «UUID.randomUUID.mostSignificantBits»L;
		
			@Property(name="topic", value=«name».TOPIC)
			private String m_topic;
			
			@Property(name="url", value="tcp://«agent.agency.ip»:«agent.agency.port»")
			private String m_url;
			
			@Requires
			private WorkingMemory m_wm;
		
			@Requires
			private Device m_device;
			
			@Requires(from=«name»Stream.SERVICE_NAME)
			private EventStream<«name»> m_streaming;		
			
			protected  String getTopicName() {
				return m_topic;
			}
			
			protected String getMqttURL() {
				return m_url;
			}	
			
			protected WorkingMemory getWorkingMemory() {
				return m_wm;
			}
			
			protected Device getDevice() {
				return m_device;
			}
			
			protected EventStream<«name»> getEventStream() {
				return m_streaming;
			}	
				
			@Validate
			public void start() {
				super.start();
			}
			
			@Invalidate
			public void stop() {
				super.stop();
			}
		}
	'''		
}
