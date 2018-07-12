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

class ServiceCompiler {
	
	@Inject extension IQualifiedNameProvider
	@Inject extension GeneratorUtils
	
	def serviceCompile(Control it, AgentDeclaration agent) '''
		«val importManager = new ImportManager(true)» 
		«val body = compile(importManager)»
		«IF eContainer !== null»
			package org.etri.slice.devices.«agent.eContainer.fullyQualifiedName».«agent.name.toLowerCase»;
		«ENDIF»
		
		import org.apache.felix.ipojo.annotations.Component;
		import org.apache.felix.ipojo.annotations.Instantiate;
		import org.apache.felix.ipojo.annotations.Invalidate;
		import org.apache.felix.ipojo.annotations.Provides;
		import org.apache.felix.ipojo.annotations.Validate;
		import org.etri.slice.commons.SliceException;
		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
		import org.slf4j.Logger;
		import org.slf4j.LoggerFactory;		

		@Component(publicFactory=false, immediate=true)
		@Provides
		@Instantiate
		«body»
	'''
 
	def compile(Control it, ImportManager importManager) '''
		public class «shortName(importManager)»Service implements «name» {
			
			private static Logger s_logger = LoggerFactory.getLogger(«name»Service.class);	
			
			private «name» m_service;	
			
			@Validate
			public void init() throws SliceException {
				
			}
		
			@Invalidate
			public void fini() throws SliceException {
				
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
    			«IF !method.returnType.type.simpleName.equals("void")»return «ENDIF»m_service.«method.simpleName»(«FOR p : method.parameters SEPARATOR ', '»«p.name»«ENDFOR»);
    		}
    		
    	«ENDFOR»    
    '''
          
  	private def compile(Feature it, ImportManager importManager) { 
		switch it {
			Property : '''
				@Override
				public «type.shortName(importManager)» get«name.toFirstUpper»() {
					return m_service.get«name.toFirstUpper»();
				}
				
				@Override		        
				public void set«name.toFirstUpper»(«type.shortName(importManager)» «name») {
					m_service.set«name.toFirstUpper»(«name»);
				}
			'''
		
			Operation :  '''
				@Override
				public «type.shortName(importManager)» «name»«parameters(importManager)»«exceptions(importManager)» {
					«IF !type.type.simpleName.equals("void")»return «ENDIF»m_service.«name»(«FOR p : params SEPARATOR ', '» «p.name»«ENDFOR»);
				}
			'''
		}
   	}
}
