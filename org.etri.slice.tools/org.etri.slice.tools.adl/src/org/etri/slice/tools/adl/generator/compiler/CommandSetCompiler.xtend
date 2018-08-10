package org.etri.slice.tools.adl.generator.compiler

import com.google.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.Command

class CommandSetCompiler {
	
	@Inject extension IQualifiedNameProvider
	
	def compileCommand(AgentDeclaration agent, Command it)  {
		val importManager = new ImportManager(true)
		for ( c : it.contexts ) {
			c.context.simpleName
		}
		
		action.simpleName
		'''
		«IF agent.eContainer !== null»
			package org.etri.slice.rules.«agent.eContainer.fullyQualifiedName»;
		«ENDIF»

		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
			
		global «action.simpleName» «action.simpleName.toFirstLower»;

		'''
 	}
}

