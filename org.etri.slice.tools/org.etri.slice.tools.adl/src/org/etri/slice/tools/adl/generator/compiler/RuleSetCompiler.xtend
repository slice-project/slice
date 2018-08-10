package org.etri.slice.tools.adl.generator.compiler

import com.google.inject.Inject
import java.util.HashSet
import java.util.Set
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.eclipse.xtext.xbase.jvmmodel.JvmModelAssociator
import org.etri.slice.tools.adl.domainmodel.AbstractElement
import org.etri.slice.tools.adl.domainmodel.Action
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.Behavior
import org.etri.slice.tools.adl.domainmodel.Call
import org.etri.slice.tools.adl.domainmodel.Context
import org.etri.slice.tools.adl.domainmodel.Control
import org.etri.slice.tools.adl.domainmodel.Event
import org.etri.slice.tools.adl.domainmodel.Publish
import org.etri.slice.tools.adl.domainmodel.Situation
import org.etri.slice.tools.adl.generator.BehaviorGenerator
import org.etri.slice.tools.adl.generator.GeneratorUtils

class RuleSetCompiler {
	
	IFileSystemAccess m_fsa
	extension Set<Control> m_globals = new HashSet<Control>();	
	@Inject extension GeneratorUtils
	@Inject extension IQualifiedNameProvider	
	@Inject extension BehaviorGenerator
	
	@Inject extension JvmModelAssociator
	
	def compileRuleSet(AgentDeclaration it, IFileSystemAccess fsa) {
		m_fsa = fsa
		m_globals.clear	
	'''
		«val importManager = new ImportManager(true)» 
		«val body = ruleBody(importManager)»
		«IF eContainer !== null»
			package org.etri.slice.rules.«eContainer.fullyQualifiedName»;
		«ENDIF»
		
		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
		
		«FOR c:m_globals.toList»
			global «c.name» «c.name.toFirstLower»;
		«ENDFOR»
		
		«body»
	'''
	}
	 
	def ruleBody(AgentDeclaration it, ImportManager importManager) '''
		«FOR b: it.behaviorSet.behaviors»
			«IF !b.action.action.equals("no-op")»«b.compileBody(it, importManager)»
			«ELSE»«FOR context: b.situation.types»«context.generateAdaptor(it, m_fsa)»«ENDFOR»
			«ENDIF»
			
		«ENDFOR»		
	'''

  	def compileBody(Behavior it, AgentDeclaration agent, ImportManager importManager) '''
		rule "«name»"
			when
				«situation.compileSituation(agent, importManager)»
			then
				«action.compileAction(agent, importManager)»
		end
	'''	
		
	def compileSituation(Situation it, AgentDeclaration agent, ImportManager importManager) '''
		«var index = 1»
		«IF types.size == 1»
			$e: «types.get(0).compileDataType(agent, importManager)»
		«ELSE»
			«FOR context: types»
				$e«index++»: «context.compileDataType(agent, importManager)»
			«ENDFOR»
		«ENDIF»
	'''	
	
	def compileDataType( JvmTypeReference it, AgentDeclaration agent, ImportManager importManager) {
		switch ( it ) {
			Context : '''«compileContextAdaptor(agent, importManager)»(/* */)'''
			Event : '''«compileEventAdaptor(agent, importManager)»(/* */)'''
		}
	}
	
	def compileContextAdaptor(AbstractElement it, AgentDeclaration agent, ImportManager importManager) {
		val JvmGenericType type = toJvmGenericType(fullyQualifiedName, "context")
		(it as Context).generateAdaptor(agent, m_fsa)
		importManager.serialize(type)
	}
	
	def compileEventAdaptor(AbstractElement it, AgentDeclaration agent, ImportManager importManager) {
		val JvmGenericType type = toJvmGenericType(fullyQualifiedName, "event")
		(it as Event).generateAdaptor(agent, m_fsa)
		importManager.serialize(type)
	}	
		
	def compileAction(Action it, AgentDeclaration agent, ImportManager importManager) {
		switch it {
			Publish : '''channels["«(event.type.sourceElements.head as Event).topic.name»"].send(new «event.compileEventWrapper(agent, importManager)»(/* */));'''
			Call : '''«control.compileControlWrapper(agent, importManager).toString.toFirstLower».«it.method»(/* */);'''
		}
	}
	
	def compileEventWrapper(JvmTypeReference it, AgentDeclaration agent, ImportManager importManager) {
//		val JvmGenericType type = toJvmGenericType(fullyQualifiedName, "event")
		val JvmGenericType type = it.type as JvmGenericType
		
		generateEventWrapper(agent, m_fsa)
		importManager.serialize(type)
	}	
	
	def compileControlWrapper(JvmType it, AgentDeclaration agent, ImportManager importManager) {		
		add(it.sourceElements.head as Control)
//		val JvmGenericType type = toJvmGenericType(fullyQualifiedName, "service")
		val JvmGenericType type = it as JvmGenericType
		
		generateControlWrapper(agent, m_fsa)
		importManager.serialize(type)
	}

}
