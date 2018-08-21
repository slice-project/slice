package org.etri.slice.tools.adl.generator.compiler

import com.google.inject.Inject
import org.eclipse.xtext.common.types.JvmFeature
import org.eclipse.xtext.common.types.JvmField
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmOperation
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.common.types.impl.JvmGenericTypeImplCustom
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.generator.GeneratorUtils
import org.etri.slice.tools.adl.validation.domain_dependency.DomainDependencyUtil

class ControlWrapperCompiler {
	
	@Inject extension IQualifiedNameProvider
	@Inject extension GeneratorUtils
	@Inject extension DomainDependencyUtil
	
	public def compileWrapper(JvmType it, AgentDeclaration agent) 
	{
		System.out.println(">>>>>>>>>>>>>>>>>> FQN : " + it.fullyQualifiedName.toString())
		val domain = it.fullyQualifiedName.toString().getDomain("org.etri.slice.commons", "service")
		
		'''
		«val importManager = new ImportManager(true)» 
		«val body = body(importManager)»
		
		«IF domain !== null»
			package org.etri.slice.agents.«domain».«agent.name.toLowerCase».wrapper;
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
 	}
	private def body(JvmType it, ImportManager importManager)
	{
		importManager.addImportFor(it)
		
	 '''
		public class «simpleName»Wrapper implements «simpleName» {
			
			@Requires
			private «simpleName» m_proxy;
			
			@Requires
			private WorkingMemory m_wm;
			
			public «simpleName»Wrapper() {
				m_wm.addServiceWrapper(«simpleName».id, this);
			}
			
			«IF (it as JvmGenericType).superTypes.size > 0»
				«(it as JvmGenericType).compileSuperType(importManager)»
			«ENDIF»
			«FOR f : (it as JvmGenericType).declaredFields»
				«f.compile(importManager)»				
			«ENDFOR»
			«FOR o : (it as JvmGenericType).declaredOperations»
				«o.compile(importManager)»				
			«ENDFOR»
		}
	'''
	}

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
    
  	private def compile(JvmFeature it, ImportManager importManager) { 
		switch it {
			JvmField : '''
				@Override
				public «type.shortName(importManager)» get«simpleName.toFirstUpper»() {
					return m_proxy.get«simpleName.toFirstUpper»();
				}
				
				@Override		        
				public void set«simpleName.toFirstUpper»(«type.shortName(importManager)» «simpleName») {
					m_proxy.set«simpleName.toFirstUpper»(«simpleName»);
				}
			'''
		
			JvmOperation :  '''
				@Override
				public «returnType.shortName(importManager)» «simpleName»«parameters(importManager)»«exceptions(importManager)» {
					«IF !returnType.type.simpleName.equals("void")»return «ENDIF»m_proxy.«simpleName»(«FOR p : parameters SEPARATOR ', '»«p.name»«ENDFOR»);
				}
			'''
		}
   	}
}
