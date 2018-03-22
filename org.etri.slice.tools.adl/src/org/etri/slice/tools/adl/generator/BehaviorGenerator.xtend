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

class BehaviorGenerator {
	
	@Inject extension ControlWrapperCompiler
	@Inject extension AdaptorCompiler
	@Inject extension EventWrapperCompiler
	@Inject extension StreamCompiler
	@Inject extension OutputPathUtils
	
	dispatch def generateAdaptor(Context it, AgentDeclaration agent, IFileSystemAccess fsa) {	
		val package = agent.deviceFullyQualifiedName.replace(".", "/")
		val adaptor = agent.deviceMavenSrcHome + package + "/adaptor/" + name + "Adaptor.java"	
		fsa.generateFile(adaptor, compileAdaptor(agent))

		val stream = agent.deviceMavenSrcHome + package + "/stream/" + name + "Stream.java"		
		fsa.generateFile(stream, compileStream(agent))				
	}
	
	dispatch def generateAdaptor(Event it, AgentDeclaration agent, IFileSystemAccess fsa) {
		val package = agent.deviceFullyQualifiedName.replace(".", "/")
		val adaptor = agent.deviceMavenSrcHome + package + "/adaptor/" + name + "Adaptor.java"			
		fsa.generateFile(adaptor, compileAdaptor(agent))
		
		val stream = agent.deviceMavenSrcHome + package + "/stream/" + name + "Stream.java"	
		fsa.generateFile(stream, compileStream(agent))				
	}
	
	def generateEventWrapper(Event it, AgentDeclaration agent, IFileSystemAccess fsa) {
		val package = agent.deviceFullyQualifiedName.replace(".", "/")
		val wrapper = agent.deviceMavenSrcHome + package + "/wrapper/" + name + "Channel.java"		
		fsa.generateFile(wrapper, compileWrapper(agent))	
		
		val stream = agent.deviceMavenSrcHome + package + "/stream/" + name + "Stream.java"	
		fsa.generateFile(stream, compileStream(agent))				
	}
	
	def generateControlWrapper(Control it, AgentDeclaration agent, IFileSystemAccess fsa) {
		val package = agent.deviceFullyQualifiedName.replace(".", "/")
		val wrapper = agent.deviceMavenSrcHome + package + "/wrapper/" + name + "Wrapper.java"		
		fsa.generateFile(wrapper, compileWrapper(agent))			
	}				

}
