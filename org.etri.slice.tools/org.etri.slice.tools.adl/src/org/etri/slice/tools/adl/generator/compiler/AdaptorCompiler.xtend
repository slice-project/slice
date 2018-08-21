package org.etri.slice.tools.adl.generator.compiler

import com.google.inject.Inject
import java.util.UUID
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.generator.GeneratorUtils
import org.etri.slice.tools.adl.validation.domain_dependency.DomainDependencyUtil

class AdaptorCompiler {
	
	@Inject extension IQualifiedNameProvider
	@Inject extension GeneratorUtils
	@Inject extension DomainDependencyUtil
	
	dispatch def compileContextAdaptor(JvmTypeReference it, AgentDeclaration agent) 
	{
		val domain = type.fullyQualifiedName.toString().getDomain("org.etri.slice.commons", "context")
	'''
		«val importManager = new ImportManager(true)» 
		«val body = contextBody(importManager)»
		«IF domain !== null»
			package org.etri.slice.agents.«domain».«agent.name.toLowerCase».adaptor;
		«ENDIF»
		
		import org.apache.felix.ipojo.annotations.Component;
		import org.apache.felix.ipojo.annotations.Instantiate;
		import org.apache.felix.ipojo.annotations.Invalidate;
		import org.apache.felix.ipojo.annotations.Property;
		import org.apache.felix.ipojo.annotations.Requires;
		import org.apache.felix.ipojo.annotations.Validate;
		import org.apache.felix.ipojo.handlers.event.Subscriber;
		import org.etri.slice.api.agent.Agent;
		import org.etri.slice.api.inference.WorkingMemory;
		import org.etri.slice.api.perception.EventStream;
		import org.etri.slice.core.perception.EventSubscriber;
		import org.etri.slice.agents.«domain».«agent.name.toLowerCase».stream.«simpleName»Stream;
		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
		
		«body»
		
	'''	
	}
	
	def contextBody(JvmTypeReference it, ImportManager importManager) '''
		@Component
		@Instantiate
		public class «simpleName»Adaptor extends EventSubscriber<«shortName(importManager)»> {
			
			private static final long serialVersionUID = «UUID.randomUUID.mostSignificantBits»L;
		
			@Property(name="topic", value=«simpleName».topic)
			private String m_topic;
			
			@Requires
			private WorkingMemory m_wm;
		
			@Requires
			private Agent m_agent;
			
			@Requires(from=«simpleName»Stream.SERVICE_NAME)
			private EventStream<«simpleName»> m_streaming;	
			
			protected  String getTopicName() {
				return m_topic;
			}
			
			protected WorkingMemory getWorkingMemory() {
				return m_wm;
			}
			
			protected Agent getAgent() {
				return m_agent;
			}
			
			protected EventStream<«simpleName»> getEventStream() {
				return m_streaming;
			}
				
			@Subscriber(name="«simpleName»Adaptor", topics=«simpleName».topic,
					dataKey=«simpleName».dataKey, dataType=«simpleName».dataType)
			public void receive(«simpleName» event) {
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
	
	dispatch def compileEventAdaptor(JvmTypeReference it, AgentDeclaration agent) 
	{
		val domain = type.fullyQualifiedName.toString().getDomain("org.etri.slice.commons", "event")
	'''
		«val importManager = new ImportManager(true)» 
		«val body = eventBody(agent, importManager)»
		«IF domain !== null»
			package org.etri.slice.agents.«domain».«agent.name.toLowerCase».adaptor;
		«ENDIF»
		
		import org.apache.felix.ipojo.annotations.Component;
		import org.apache.felix.ipojo.annotations.Instantiate;
		import org.apache.felix.ipojo.annotations.Invalidate;
		import org.apache.felix.ipojo.annotations.Property;
		import org.apache.felix.ipojo.annotations.Requires;
		import org.apache.felix.ipojo.annotations.Validate;
		import org.etri.slice.api.agent.Agent;
		import org.etri.slice.api.inference.WorkingMemory;
		import org.etri.slice.api.perception.EventStream;
		import org.etri.slice.core.perception.MqttEventSubscriber;
		import org.etri.slice.agents.«domain».«agent.name.toLowerCase».stream.«simpleName»Stream;
		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
		
		«body»
		
	'''		
	}

	def eventBody(JvmTypeReference it, AgentDeclaration agent, ImportManager importManager) '''
		@Component
		@Instantiate
		public class «simpleName»Adaptor extends MqttEventSubscriber<«shortName(importManager)»> {
			
			private static final long serialVersionUID = «UUID.randomUUID.mostSignificantBits»L;
		
			@Property(name="topic", value=«simpleName».TOPIC)
			private String m_topic;
			
			@Property(name="url", value="tcp://«agent.agency.ip»:«agent.agency.port»")
			private String m_url;
			
			@Requires
			private WorkingMemory m_wm;
		
			@Requires
			private Agent m_agent;
			
			@Requires(from=«simpleName»Stream.SERVICE_NAME)
			private EventStream<«simpleName»> m_streaming;	
			
			protected  String getTopicName() {
				return m_topic;
			}
			
			protected String getMqttURL() {
				return m_url;
			}
			
			protected WorkingMemory getWorkingMemory() {
				return m_wm;
			}
			
			protected Agent getAgent() {
				return m_agent;
			}
			
			protected Class<?> getEventType() {
				return «simpleName».class;
			}
			
			protected EventStream<«simpleName»> getEventStream() {
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
