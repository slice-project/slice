package org.etri.slice.tools.adl.generator.compiler

import com.google.inject.Inject
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.impl.JvmGenericTypeImplCustom
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.Control
import org.etri.slice.tools.adl.domainmodel.Feature
import org.etri.slice.tools.adl.domainmodel.Operation
import org.etri.slice.tools.adl.domainmodel.Property
import org.etri.slice.tools.adl.generator.GeneratorUtils

class ControlWrapperCompiler {
	
	@Inject extension IQualifiedNameProvider
	@Inject extension GeneratorUtils
	
	public def compileWrapper(Control it, AgentDeclaration agent) '''
		«val importManager = new ImportManager(true)» 
		«val body = body(importManager)»
		
		«IF eContainer !== null»
			package org.etri.slice.agents.«eContainer.fullyQualifiedName».«agent.name.toLowerCase».wrapper;
		«ENDIF»
		
		import org.apache.felix.ipojo.annotations.Component;
		import org.apache.felix.ipojo.annotations.Instantiate;
		import org.apache.felix.ipojo.annotations.Requires;
		import org.etri.slice.api.inference.WorkingMemory;
		
		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
		
		@Component
		@Instantiate
		«body»
	'''
 
	private def body(Control it, ImportManager importManager) '''
		public class «name»Wrapper implements «shortName(importManager)» {
			
			@Requires
			private «name» m_proxy;
			
			@Requires
			private WorkingMemory m_wm;
			
			public «name»Wrapper() {
				m_wm.addServiceWrapper(«name».id, this);
			}
			
			«IF superTypes.size > 0»
				«toInterface.compileSuperType(importManager)»
			«ENDIF»
			«FOR f : features»
				«f.compile(importManager)»
				
			«ENDFOR»
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
    
  	private def compile(Feature it, ImportManager importManager) { 
		switch it {
			Property : '''
				@Override
				public «type.shortName(importManager)» get«name.toFirstUpper»() {
					return m_proxy.get«name.toFirstUpper»();
				}
				
				@Override		        
				public void set«name.toFirstUpper»(«type.shortName(importManager)» «name») {
					m_proxy.set«name.toFirstUpper»(«name»);
				}
			'''
		
			Operation :  '''
				@Override
				public «type.shortName(importManager)» «name»«parameters(importManager)»«exceptions(importManager)» {
					«IF !type.type.simpleName.equals("void")»return «ENDIF»m_proxy.«name»(«FOR p : params SEPARATOR ', '»«p.name»«ENDFOR»);
				}
			'''
		}
   	}
}
