package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.xtext.generator.IFileSystemAccess
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.Context
import org.etri.slice.tools.adl.domainmodel.Control
import org.etri.slice.tools.adl.domainmodel.Event
import org.etri.slice.tools.adl.generator.compiler.AdaptorCompiler
import org.etri.slice.tools.adl.generator.compiler.ControlWrapperCompiler
import org.etri.slice.tools.adl.generator.compiler.EventWrapperCompiler
import org.etri.slice.tools.adl.generator.compiler.StreamCompiler
import org.etri.slice.tools.adl.generator.compiler.SensorCompiler
import org.etri.slice.tools.adl.generator.compiler.ServiceCompiler

class BehaviorGenerator {
	
	@Inject extension ControlWrapperCompiler
	@Inject extension AdaptorCompiler
	@Inject extension EventWrapperCompiler
	@Inject extension StreamCompiler
	@Inject extension ServiceCompiler
	@Inject extension SensorCompiler	
	@Inject extension OutputPathUtils
	
	dispatch def generateAdaptor(Context it, AgentDeclaration agent, IFileSystemAccess fsa) {	
		val package = agent.agentFullyQualifiedName.replace(".", "/")
		val adaptor = agent.agentMavenSrcHome + package + "/adaptor/" + name + "Adaptor.java"	
		fsa.generateFile(adaptor, compileAdaptor(agent))

		val stream = agent.agentMavenSrcHome + package + "/stream/" + name + "Stream.java"		
		fsa.generateFile(stream, compileStream(agent))				
	}
	
	dispatch def generateAdaptor(Event it, AgentDeclaration agent, IFileSystemAccess fsa) {
		val package = agent.agentFullyQualifiedName.replace(".", "/")
		val adaptor = agent.agentMavenSrcHome + package + "/adaptor/" + name + "Adaptor.java"			
		fsa.generateFile(adaptor, compileAdaptor(agent))
		
		val stream = agent.agentMavenSrcHome + package + "/stream/" + name + "Stream.java"	
		fsa.generateFile(stream, compileStream(agent))				
	}
	
	def generateEventWrapper(Event it, AgentDeclaration agent, IFileSystemAccess fsa) {
		val package = agent.agentFullyQualifiedName.replace(".", "/")
		val wrapper = agent.agentMavenSrcHome + package + "/wrapper/" + name + "Channel.java"		
		fsa.generateFile(wrapper, compileWrapper(agent))	
		
		val stream = agent.agentMavenSrcHome + package + "/stream/" + name + "Stream.java"	
		fsa.generateFile(stream, compileStream(agent))				
	}
	
	def generateControlWrapper(Control it, AgentDeclaration agent, IFileSystemAccess fsa) {
		val package = agent.agentFullyQualifiedName.replace(".", "/")
		val wrapper = agent.agentMavenSrcHome + package + "/wrapper/" + name + "Wrapper.java"		
		fsa.generateFile(wrapper, compileWrapper(agent))			
	}
	
	def generateSensor(Context it, AgentDeclaration agent, IFileSystemAccess fsa) {
		val package = agent.deviceFullyQualifiedName.replace(".", "/")
		val file = agent.deviceMavenSrcHome + package + "/" + name + "Sensor.java"	
		fsa.generateFile(file, sensorCompile(agent))			
	}		
					
	def generateService(Control it, AgentDeclaration agent, IFileSystemAccess fsa) {
		val package = agent.deviceFullyQualifiedName.replace(".", "/")
		val file = agent.deviceMavenSrcHome + package + "/" + name + "Service.java"	
		fsa.generateFile(file, serviceCompile(agent))			
	}	
}
