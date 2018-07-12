package org.etri.slice.tools.adl.generator.compiler

import com.google.inject.Inject
import java.util.HashMap
import java.util.Map
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.impl.JvmGenericTypeImplCustom
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.Command
import org.etri.slice.tools.adl.domainmodel.CommandSet
import org.etri.slice.tools.adl.domainmodel.Control
import org.etri.slice.tools.adl.domainmodel.Feature
import org.etri.slice.tools.adl.domainmodel.Operation
import org.etri.slice.tools.adl.domainmodel.Property
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
		
		«IF eContainer !== null»
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
 
	private def body(CommandSet it, ImportManager importManager) '''
		public class «control.name»Commander implements «control.shortName(importManager)» {
			private static Logger s_logger = LoggerFactory.getLogger(«control.name»Commander.class);
			
			@Requires
			private «control.name» m_proxy;
			
			@Requires
			private ActionLogger m_logger;
			
			@Requires
			private Agent m_agent;
			
			public «control.name»Commander() {
				ControlService control = m_agent.getService(ControlService.class);
				control.registerControl(this.getClass().getSimpleName(), «control.name».id, null, «control.name».class, this);
			}
			
			«IF control.superTypes.size > 0»
				«control.toInterface.compileSuperType(importManager)»
			«ENDIF»
			«FOR f : control.features»
				«f.compileFeature(control, importManager)»
				
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
 
  	private def compileFeature(Feature it, Control control, ImportManager importManager) { 
		switch it {
			Property : '''
				@Override
				public «type.shortName(importManager)» get«name.toFirstUpper»() {
					return m_proxy.get«name.toFirstUpper»();
				}
				
				«val methodName = "set" + name.toFirstUpper»
				«val actionKey = toKey(control, methodName)»
				@Override		        
				public void «methodName»(«type.shortName(importManager)» «name») {
					m_proxy.«methodName»(«name»);
					
					«actionLog(actionKey, importManager)»
				}
			'''
		
			Operation :  '''
				@Override
				public «type.shortName(importManager)» «name»«parameters(importManager)»«exceptions(importManager)» {
					«IF !type.type.simpleName.equals("void")»return «ENDIF»m_proxy.«name»(«FOR p : params SEPARATOR ', '» «p.name»«ENDFOR»);
				}
			'''
		}
   	}
   	
   	private def actionLog(Property it, CharSequence actionKey, ImportManager importManager) '''
		«IF m_commands.containsKey(actionKey)»
			«val command = m_commands.get(actionKey)»
			ActionBuilder builder = Action.builder();
			builder.setRelation("«command.name»");
			«FOR c : command.contexts»
			builder.addContext(«importManager.serialize(c.context.toJvmGenericType(c.context.fullyQualifiedName, "context"))».Field.«c.property»);
			«ENDFOR»
			builder.setAction(«actionKey», «name»);
			logAction(builder.build());		
		«ENDIF»   	
   	'''

 	private def toKey(Control it, String method)  {
 		return name + "." + method;
 	}
}
