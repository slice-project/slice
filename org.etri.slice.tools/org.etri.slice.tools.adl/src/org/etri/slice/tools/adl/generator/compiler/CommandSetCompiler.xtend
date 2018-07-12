package org.etri.slice.tools.adl.generator.compiler

import com.google.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.Command
import org.etri.slice.tools.adl.generator.GeneratorUtils

class CommandSetCompiler {
	
	@Inject extension IQualifiedNameProvider
	@Inject extension GeneratorUtils
	
	def compileCommand(AgentDeclaration agent, Command it)  {
		val importManager = new ImportManager(true)
		for ( c : it.contexts ) {
			c.context.shortName(importManager)
		}
		action.shortName(importManager)
		'''
		«IF eContainer !== null»
			package org.etri.slice.rules.«agent.eContainer.fullyQualifiedName».«agent.name.toLowerCase»;
		«ENDIF»

		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
			
		global «action.name» «action.name.toFirstLower»;

		'''
 	}
}
