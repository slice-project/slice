package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.generator.IFileSystemAccess
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.Context
import org.etri.slice.tools.adl.domainmodel.Event
import org.etri.slice.tools.adl.generator.compiler.AdaptorCompiler
import org.etri.slice.tools.adl.generator.compiler.ControlWrapperCompiler
import org.etri.slice.tools.adl.generator.compiler.EventWrapperCompiler
import org.etri.slice.tools.adl.generator.compiler.SensorCompiler
import org.etri.slice.tools.adl.generator.compiler.ServiceCompiler
import org.etri.slice.tools.adl.generator.compiler.StreamCompiler

class BehaviorGenerator {
	
	@Inject extension ControlWrapperCompiler
	@Inject extension AdaptorCompiler
	@Inject extension EventWrapperCompiler
	@Inject extension StreamCompiler
	@Inject extension ServiceCompiler
	@Inject extension SensorCompiler	
	@Inject extension OutputPathUtils
	
	// it : Context
	dispatch def generateContextAdaptor(JvmTypeReference it, AgentDeclaration agent, IFileSystemAccess fsa) {	
		val package = agent.agentFullyQualifiedName.replace(".", "/")
		val adaptor = agent.agentMavenSrcHome + package + "/adaptor/" + simpleName + "Adaptor.java"	
		fsa.generateFile(adaptor, compileContextAdaptor(agent))

		val stream = agent.agentMavenSrcHome + package + "/stream/" + simpleName + "Stream.java"		
		fsa.generateFile(stream, compileStream(agent))				
	}
	
	dispatch def generateEventAdaptor(JvmTypeReference it, AgentDeclaration agent, IFileSystemAccess fsa) {
		val package = agent.agentFullyQualifiedName.replace(".", "/")
		val adaptor = agent.agentMavenSrcHome + package + "/adaptor/" + simpleName + "Adaptor.java"			
		fsa.generateFile(adaptor, compileEventAdaptor(agent))
		
		val stream = agent.agentMavenSrcHome + package + "/stream/" + simpleName + "Stream.java"	
		fsa.generateFile(stream, compileStream(agent))				
	}
	
	def generateEventWrapper(JvmTypeReference it, AgentDeclaration agent, IFileSystemAccess fsa) {
		val package = agent.agentFullyQualifiedName.replace(".", "/")
		val wrapper = agent.agentMavenSrcHome + package + "/wrapper/" + simpleName + "Channel.java"		
		fsa.generateFile(wrapper, it.type.compileEventWrapper(agent))	
		
		val stream = agent.agentMavenSrcHome + package + "/stream/" + simpleName + "Stream.java"	
		fsa.generateFile(stream, compileStream(agent))				
	}
	
	def generateControlWrapper(JvmType it, AgentDeclaration agent, IFileSystemAccess fsa) {
		val package = agent.agentFullyQualifiedName.replace(".", "/")
		val wrapper = agent.agentMavenSrcHome + package + "/wrapper/" + simpleName + "Wrapper.java"		
		fsa.generateFile(wrapper, compileWrapper(agent))			
	}
	
	def generateSensor(JvmTypeReference it, AgentDeclaration agent, IFileSystemAccess fsa) {
		val package = agent.deviceFullyQualifiedName.replace(".", "/")
		val file = agent.deviceMavenSrcHome + package + "/" + simpleName + "Sensor.java"	
		fsa.generateFile(file, sensorCompile(agent))			
	}		
					
	def generateService(JvmType it, AgentDeclaration agent, IFileSystemAccess fsa) {
		val package = agent.deviceFullyQualifiedName.replace(".", "/")
		val file = agent.deviceMavenSrcHome + package + "/" + it.simpleName + "Service.java"	
		fsa.generateFile(file, serviceCompile(agent))			
	}	
}
