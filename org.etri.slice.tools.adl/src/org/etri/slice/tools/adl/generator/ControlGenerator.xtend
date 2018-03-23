package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.etri.slice.tools.adl.domainmodel.Control
import org.etri.slice.tools.adl.domainmodel.Feature
import org.etri.slice.tools.adl.domainmodel.Operation
import org.etri.slice.tools.adl.domainmodel.Property

class ControlGenerator implements IGenerator {
	
	@Inject extension IQualifiedNameProvider
	@Inject extension GeneratorUtils
	@Inject extension OutputPathUtils	
	
	override doGenerate(Resource resource, IFileSystemAccess fsa) {
		for(e: resource.allContents.toIterable.filter(typeof(Control))) {
			val package = e.sliceFullyQualifiedName.replace(".", "/")
			val file = e.commonsMavenSrcHome + package + "/" + e.name + ".java"
			fsa.generateFile(file, e.compile)
		}
	}
	
	def compile(Control it) '''
		«val importManager = new ImportManager(true)» 
		«val body = body(importManager)»
		«IF eContainer !== null»
			package org.etri.slice.commons.«eContainer.fullyQualifiedName».service;
		«ENDIF»
		
		import javax.management.MXBean;
		
		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
		
		@MXBean
		«body»
	'''
 
	def body(Control it, ImportManager importManager) '''
		public interface «name» «IF superType !== null»extends «superType.shortName(importManager)» «ENDIF»{
			
			«val id = name.toFirstLower»
			static final String id = "«id»";
			«FOR f : features»
				«f.preprocess(id, importManager)»
			«ENDFOR»
			
			«FOR f : features»
				«f.compile(importManager)»
				
			«ENDFOR»
		}
	'''
    
    def preprocess(Feature it, String idStr, ImportManager importManager) {
 		switch it {
			Property : '''
				static final String set«name.toFirstUpper» = "«idStr».set«name.toFirstUpper»";
			'''
		}   	
    }
    
  	def compile(Feature it, ImportManager importManager) { 
		switch it {
			Property : '''
				«type.shortName(importManager)» get«name.toFirstUpper»();
							        
				void set«name.toFirstUpper»(«type.shortName(importManager)» «name»);
			'''
		
			Operation :  '''
				«type.shortName(importManager)» «name»«parameters(importManager)»«exceptions(importManager)»
			'''
		}
   	}
   	
   	def parameters(Operation it, ImportManager importManager) 
   		'''(«FOR p : params SEPARATOR ', '»«p.parameterType.shortName(importManager)» «p.name»«ENDFOR»)'''
   	
   	def exceptions(Operation it, ImportManager importManager) 
 		'''«IF exceptions.size > 0» throws «FOR e : exceptions SEPARATOR ', '»«e.shortName(importManager)»«ENDFOR»;«ELSE»;«ENDIF»'''
		
}
