package org.etri.slice.tools.adl.generator.compiler

import com.google.inject.Inject
import java.util.HashMap
import java.util.Map
import org.eclipse.xtext.common.types.JvmFeature
import org.eclipse.xtext.common.types.JvmField
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmOperation
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.impl.JvmGenericTypeImplCustom
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.Command
import org.etri.slice.tools.adl.domainmodel.CommandSet
import org.etri.slice.tools.adl.generator.GeneratorUtils

class CommandWrapperCompiler {
	
	@Inject extension IQualifiedNameProvider
	@Inject extension GeneratorUtils
	
	Map<String, Command> m_commands = new HashMap<String, Command>();	
	
	def compileCommandWrapper(AgentDeclaration it, CommandSet commandSet) {
		m_commands.clear
		for ( c : commandSet.commands ) {
			m_commands.put(toKey(c.action, c.method), c)
		}
		compileWrapper(commandSet)
	}
	
	def compileWrapper(AgentDeclaration agent, CommandSet it) '''
		«val importManager = new ImportManager(true)» 
		«val body = body(importManager)»
		
		«IF agent.eContainer !== null»
			package org.etri.slice.agents.«agent.eContainer.fullyQualifiedName».«agent.name.toLowerCase».wrapper;
		«ENDIF»

		import org.apache.edgent.execution.services.ControlService;
		import org.apache.felix.ipojo.annotations.Component;
		import org.apache.felix.ipojo.annotations.Instantiate;
		import org.apache.felix.ipojo.annotations.Requires;
		import org.etri.slice.api.agent.Agent;
		import org.etri.slice.api.learning.Action;
		import org.etri.slice.api.learning.Action.ActionBuilder;
		import org.etri.slice.api.learning.ActionLogger;
		import org.slf4j.Logger;
		import org.slf4j.LoggerFactory;		
		
		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
		
		@Component
		@Instantiate
		«body»
	'''
 
	private def body(CommandSet it, ImportManager importManager)
		'''
		public class «control.simpleName»Commander implements «control.shortName(importManager)» {
			private static Logger s_logger = LoggerFactory.getLogger(«control.simpleName»Commander.class);
			
			@Requires
			private «control.simpleName» m_proxy;
			
			@Requires
			private ActionLogger m_logger;
			
			@Requires
			private Agent m_agent;
			
			public «control.simpleName»Commander() {
				ControlService control = m_agent.getService(ControlService.class);
				control.registerControl(this.getClass().getSimpleName(), «control.simpleName».id, null, «control.simpleName».class, this);
			}
			
			«IF (control.type as JvmGenericType).superTypes.size > 0»
				«(control.type as JvmGenericType).compileSuperType(importManager)»
			«ENDIF»
			«FOR f : (control.type as JvmGenericType).declaredFields»
				«f.compileFeature(control, importManager)»
			«ENDFOR»
			«FOR o : (control.type as JvmGenericType).declaredOperations»
				«o.compileFeature(control, importManager)»								
			«ENDFOR»
			private void logAction(Action action) {
				try {
					m_logger.log(action);
				} 
				catch ( Exception e ) {
					s_logger.error("ERR : " + e.getMessage());
				}			
			}			
		}
	'''
	
	private def compileSuperType(JvmGenericType it, ImportManager importManager) '''
		«FOR superType : superTypes»
			«val jvmType = superType.type as JvmGenericTypeImplCustom»
			«IF jvmType.isInterface»
				«jvmType.compile(importManager)»				
				«compileSuperType(jvmType, importManager)»
			«ENDIF»
		«ENDFOR»
	'''	
    
    private def compile(JvmGenericType it, ImportManager importManager)	'''
    	«FOR method : declaredOperations»
    		@Override
    		public «method.returnType.shortName(importManager)» «method.simpleName»«method.parameters(importManager)»«method.exceptions(importManager)» {
    			«IF !method.returnType.type.simpleName.equals("void")»return «ENDIF»m_proxy.«method.simpleName»(«FOR p : method.parameters SEPARATOR ', '»«p.name»«ENDFOR»);
    		}
    		
    	«ENDFOR»    
    '''
 
  	private def compileFeature(JvmFeature it, JvmTypeReference control, ImportManager importManager) { 
		switch it {
			JvmField : '''
				@Override
				public «type.shortName(importManager)» get«simpleName.toFirstUpper»() {
					return m_proxy.get«simpleName.toFirstUpper»();
				}
				
				«val methodName = "set" + simpleName.toFirstUpper»
				«val actionKey = toKey(control.type, methodName)»
				@Override		        
				public void «methodName»(«type.shortName(importManager)» «simpleName») {
					m_proxy.«methodName»(«simpleName»);
					
					«actionLog(actionKey, importManager)»
				}
			'''
		
			JvmOperation :  '''
				@Override
				public «returnType.shortName(importManager)» «simpleName»«parameters(importManager)»«exceptions(importManager)» {
					«IF !returnType.type.simpleName.equals("void")»return «ENDIF»m_proxy.«simpleName»(«FOR p : parameters SEPARATOR ', '» «p.name»«ENDFOR»);
				}
			'''
		}
   	}
   	
   	private def actionLog(JvmField it, CharSequence actionKey, ImportManager importManager) '''
		«IF m_commands.containsKey(actionKey)»
			«val command = m_commands.get(actionKey)»
			ActionBuilder builder = Action.builder();
			builder.setRelation("«command.name»");
			«FOR c : command.contexts»
			builder.addContext(«importManager.serialize(c.context as JvmGenericType)».Field.«c.property»);
			«ENDFOR»
			builder.setAction(«actionKey», «simpleName»);
			logAction(builder.build());		
		«ENDIF»   	
   	'''

 	private def toKey(JvmType it, String method)  {
 		return it.simpleName + "." + method;
 	}
}
