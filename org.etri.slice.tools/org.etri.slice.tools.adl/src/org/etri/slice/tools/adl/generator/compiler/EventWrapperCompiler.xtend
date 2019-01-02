package org.etri.slice.tools.adl.generator.compiler

import com.google.inject.Inject
import java.util.UUID
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.validation.domain_dependency.DomainDependencyUtil

class EventWrapperCompiler {
	
	@Inject extension IQualifiedNameProvider
	@Inject extension DomainDependencyUtil
	
	def compileEventWrapper(JvmType it, AgentDeclaration agent)
	{
		val domain = it.fullyQualifiedName.toString().getDomain("org.etri.slice.commons", "event")
				
		'''
		«val importManager = new ImportManager(true)» 
		«val body = body(agent, importManager)»
		«IF domain !== null»
			package org.etri.slice.agents.«domain».«agent.name.toLowerCase».wrapper;
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
		import org.etri.slice.core.perception.MqttEventPublisher;
		import org.etri.slice.agents.«agent.eContainer.fullyQualifiedName».«agent.name.toLowerCase».stream.«simpleName»Stream;
		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
		
		«body»
		
		'''
	}
	
	def body(JvmType it, AgentDeclaration agent, ImportManager importManager) '''
		@Component(managedservice="org.etri.slice.agent")
		@Instantiate
		public class «simpleName»Channel extends MqttEventPublisher<«importManager.serialize(it as JvmGenericType)»> {
		
			private static final long serialVersionUID = «UUID.randomUUID.mostSignificantBits»L;
		
			@Property(name="topic", value=«simpleName».TOPIC)
			private String m_topic;
			
			@Property(name="agent.agency.url", value="tcp://«agent.agency.ip»:«agent.agency.port»")
			private String m_url;
			private String m_prevUrl;
			
			@Requires
			private WorkingMemory m_wm;
		
			@Requires
			private Agent m_agent;
			
			@Requires(from=«simpleName»Stream.SERVICE_NAME)
			private EventStream<«simpleName»> m_streaming;		
			
			protected  String getTopicName() {
				return m_topic;
			}
			
			protected synchronized String getMqttURL() {
				return m_url;
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
				
			@Validate
			public void start() {
				super.start();
			}
			
			@Invalidate
			public void stop() {
				super.stop();
			}
			
		    @Property(name="agent.agency.url")
		    public synchronized void setAgencyUrl(String url) {
		    	if ( url.trim().equals(m_prevUrl) ) {
		    		return;
		    	}
		    	
		        m_prevUrl = url;   
		        super.restart();
		    }
		}
	'''		
}
