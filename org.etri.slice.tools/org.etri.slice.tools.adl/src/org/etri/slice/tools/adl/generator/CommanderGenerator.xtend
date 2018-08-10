package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.xtext.generator.IFileSystemAccess
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.CommandSet
import org.etri.slice.tools.adl.generator.compiler.CommandSetCompiler
import org.etri.slice.tools.adl.generator.compiler.CommandWrapperCompiler

class CommanderGenerator {
	
	@Inject extension OutputPathUtils
	@Inject extension CommandSetCompiler
	@Inject extension CommandWrapperCompiler
	@Inject extension BehaviorGenerator
	
	def generateCommander(AgentDeclaration it, CommandSet commandSet, IFileSystemAccess fsa) {
		
		commandSet.control.type.generateControlWrapper(it, fsa)
		commandSet.control.type.generateService(it, fsa)
		
		var package = agentFullyQualifiedName.replace(".", "/")
		var file = agentMavenSrcHome + package + "/wrapper/" + commandSet.control.simpleName + "Commander.java"		
		fsa.generateFile(file, compileCommandWrapper(commandSet))
		
		package = ruleFullyQualifiedName.replace(".", "/")
		for ( c : commandSet.commands ) {
			file = ruleMavenResHome + package + "/" + c.name + ".drl"			
			fsa.generateFile(file, compileCommand(c))
		}
	}	

}

