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

class ServiceCompiler {

	@Inject extension IQualifiedNameProvider
	@Inject extension GeneratorUtils
	@Inject extension DomainDependencyUtil

	def serviceCompile(JvmType it, AgentDeclaration agent) 
	{
		val domain = it.fullyQualifiedName.toString().getDomain("org.etri.slice.commons", "service")
		
		'''
		«val importManager = new ImportManager(true)» 
		«val body = compile(importManager)»
		«IF domain !== null»
			package org.etri.slice.devices.«domain».«agent.name.toLowerCase»;
		«ENDIF»
		
		import org.apache.felix.ipojo.annotations.Component;
		import org.apache.felix.ipojo.annotations.Instantiate;
		import org.apache.felix.ipojo.annotations.Invalidate;
		import org.apache.felix.ipojo.annotations.Provides;
		import org.apache.felix.ipojo.annotations.Validate;
		import org.etri.slice.commons.SliceException;
		«FOR i : importManager.imports»
			import «i»;
		«ENDFOR»
		import org.slf4j.Logger;
		import org.slf4j.LoggerFactory;		
		
		@Component(publicFactory=false, immediate=true)
		@Provides
		@Instantiate
		«body»
	'''
	}
	
	def compile(JvmType it, ImportManager importManager) {
		importManager.addImportFor(it)
	'''
			
	public class «simpleName»Service implements «simpleName» {
		
		private static Logger s_logger = LoggerFactory.getLogger(«simpleName»Service.class);	
		
		private «simpleName» m_service;	
				
		@Validate
		public void init() throws SliceException {
				
		}
				
		@Invalidate
		public void fini() throws SliceException {
			
		}
			
		«IF (it as JvmGenericType).superTypes.size > 0»
			«(it as JvmGenericType).compileSuperType(importManager)»
		«ENDIF»
		«FOR f : (it as JvmGenericType).declaredFields»
			«f.compileFeature(importManager)»
			
		«ENDFOR»		
		«FOR o : (it as JvmGenericType).declaredOperations»
			«o.compileFeature(importManager)»
			
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

	private def compile(JvmGenericType it, ImportManager importManager) '''
		«FOR method : declaredOperations»
			@Override
			public «method.returnType.shortName(importManager)» «method.simpleName»«method.parameters(importManager)»«method.exceptions(importManager)» {
				«IF !method.returnType.type.simpleName.equals("void")»return «ENDIF»m_proxy.«method.simpleName»(«FOR p : method.parameters SEPARATOR ', '»«p.name»«ENDFOR»);
			}
			
		«ENDFOR»    
	'''

	private def compileFeature(JvmFeature it, ImportManager importManager) {
		switch it {
			JvmField: '''
				@Override
				public «type.shortName(importManager)» get«simpleName.toFirstUpper»() {
					return m_service.get«simpleName.toFirstUpper»();
				}
				
				@Override		        
				public void set«simpleName.toFirstUpper»(«type.shortName(importManager)» «simpleName») {
					m_service.set«simpleName.toFirstUpper»(«simpleName»);
				}
			'''
			JvmOperation: '''
				@Override
				public «returnType.shortName(importManager)» «simpleName»«parameters(importManager)»«exceptions(importManager)» {
					«IF !returnType.type.simpleName.equals("void")»return «ENDIF»m_service.«simpleName»(«FOR p : parameters SEPARATOR ', '» «p.name»«ENDFOR»);
				}
			'''
		}
	}
}
