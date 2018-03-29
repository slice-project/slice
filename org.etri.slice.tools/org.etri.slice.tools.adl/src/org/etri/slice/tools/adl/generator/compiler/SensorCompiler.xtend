package org.etri.slice.tools.adl.generator.compiler

import com.google.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.Context
import org.etri.slice.tools.adl.generator.GeneratorUtils

class SensorCompiler {
	
	@Inject extension IQualifiedNameProvider
	@Inject extension GeneratorUtils
	
	def sensorCompile(Context it, AgentDeclaration agent) '''
		«val importManager = new ImportManager(true)» 
		«val body = compile(importManager)»
		«IF eContainer !== null»
			package org.etri.slice.devices.«agent.eContainer.fullyQualifiedName».«agent.name.toLowerCase»;
		«ENDIF»
		
		import org.apache.felix.ipojo.annotations.Component;
		import org.apache.felix.ipojo.annotations.Instantiate;
		import org.apache.felix.ipojo.annotations.Invalidate;
		import org.apache.felix.ipojo.annotations.Provides;
		import org.apache.felix.ipojo.annotations.Validate;
		import org.apache.felix.ipojo.handlers.event.Publishes;
		import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
		import org.etri.slice.commons.Sensor;
		import org.etri.slice.commons.SliceException;
		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
		import org.slf4j.Logger;
		import org.slf4j.LoggerFactory;		

		@Component(publicFactory=false, immediate=true)
		@Provides
		@Instantiate
		«body»
	'''
 
	def compile(Context it, ImportManager importManager) '''
		public class «shortName(importManager)»Sensor implements Sensor {
			
			private static Logger s_logger = LoggerFactory.getLogger(«name»Sensor.class);	
				
			@Publishes(name="pub:«name.toLowerCase»", topics=«name».topic, dataKey=«name».dataKey)
			private Publisher m_publisher;
			
			@Override
			@Validate
			public void start() throws SliceException {
				
			}
		
			@Override
			@Invalidate
			public void stop() throws SliceException {
				
			}
		}
	'''
}
