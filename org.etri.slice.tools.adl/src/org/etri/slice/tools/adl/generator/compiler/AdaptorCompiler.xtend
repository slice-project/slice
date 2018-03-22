package org.etri.slice.tools.adl.generator.compiler

import com.google.inject.Inject
import java.util.UUID
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.Context
import org.etri.slice.tools.adl.domainmodel.Event
import org.etri.slice.tools.adl.generator.GeneratorUtils

class AdaptorCompiler {
	
	@Inject extension IQualifiedNameProvider
	@Inject extension GeneratorUtils
	
	dispatch def compileAdaptor(Context it, AgentDeclaration agent) '''
		«val importManager = new ImportManager(true)» 
		«val body = body(importManager)»
		«IF eContainer !== null»
			package org.etri.slice.devices.«eContainer.fullyQualifiedName».«agent.name.toLowerCase».adaptor;
		«ENDIF»
		
		import org.apache.felix.ipojo.annotations.Component;
		import org.apache.felix.ipojo.annotations.Instantiate;
		import org.apache.felix.ipojo.annotations.Invalidate;
		import org.apache.felix.ipojo.annotations.Property;
		import org.apache.felix.ipojo.annotations.Requires;
		import org.apache.felix.ipojo.annotations.Validate;
		import org.apache.felix.ipojo.handlers.event.Subscriber;
		import org.etri.slice.api.device.Device;
		import org.etri.slice.api.inference.WorkingMemory;
		import org.etri.slice.api.perception.EventStream;
		import org.etri.slice.core.perception.EventSubscriber;
		import org.etri.slice.devices.«eContainer.fullyQualifiedName».«agent.name.toLowerCase».stream.«name»Stream;
		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
		
		«body»
		
	'''	
	
	def body(Context it, ImportManager importManager) '''
		@Component
		@Instantiate
		public class «name»Adaptor extends EventSubscriber<«shortName(importManager)»> {
			
			private static final long serialVersionUID = «UUID.randomUUID.mostSignificantBits»L;
		
			@Property(name="topic", value=«name».topic)
			private String m_topic;
			
			@Requires
			private WorkingMemory m_wm;
		
			@Requires
			private Device m_device;
			
			@Requires(from=«name»Stream.SERVICE_NAME)
			private EventStream<«name»> m_streaming;	
			
			protected  String getTopicName() {
				return m_topic;
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
				
			@Subscriber(name="«name»Adaptor", topics=«name».topic,
					dataKey=«name».dataKey, dataType=«name».dataType)
			public void receive(«name» event) {
				super.subscribe(event);
			}
			
			@Validate
			public void start() {
				super.start(event -> m_wm.insert(event));
			}
			
			@Invalidate
			public void stop() {
				super.stop();
			}
		}		
	'''		
	
	dispatch def compileAdaptor(Event it, AgentDeclaration agent) '''
		«val importManager = new ImportManager(true)» 
		«val body = body(agent, importManager)»
		«IF eContainer !== null»
			package org.etri.slice.devices.«eContainer.fullyQualifiedName».«agent.name.toLowerCase».adaptor;
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
		import org.etri.slice.core.perception.MqttEventSubscriber;
		import org.etri.slice.devices.«eContainer.fullyQualifiedName».«agent.name.toLowerCase».stream.«name»Stream;
		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
		
		«body»
		
	'''		

	def body(Event it, AgentDeclaration agent, ImportManager importManager) '''
		@Component
		@Instantiate
		public class «name»Adaptor extends MqttEventSubscriber<«shortName(importManager)»> {
			
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
			
			protected Class<?> getEventType() {
				return «name».class;
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
