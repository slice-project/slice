package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.etri.slice.tools.adl.domainmodel.Exception

class ExceptionGenerator implements IGenerator {
	
	@Inject extension IQualifiedNameProvider
	@Inject extension GeneratorUtils	
	@Inject extension OutputPathUtils
	
	override doGenerate(Resource resource, IFileSystemAccess fsa) {
		for(e: resource.allContents.toIterable.filter(typeof(Exception))) {
			val package = e.sliceFullyQualifiedName.replace(".", "/")
			val file = e.commonsMavenSrcHome + package + "/" + e.name + ".java"			
			fsa.generateFile(file, e.compile)
		}
	}
	
	def compile(Exception it) '''
		«val importManager = new ImportManager(true)» 
		«val body = body(importManager)»
		«IF eContainer !== null»
			package org.etri.slice.commons.«eContainer.fullyQualifiedName»;
		«ENDIF»
		
		«IF superType === null»import org.etri.slice.commons.SliceException;«ENDIF»
		
		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
		
		«body»
	'''
 
	def body(Exception it, ImportManager importManager) '''
		public class «name» extends «IF superType !== null»«superType.shortName(importManager)» «ELSE»SliceException «ENDIF»{
			
			private static final long serialVersionUID = «generateSerialVersionUID»L;
			
			public «name»(String msg) {
				super(msg);
			}
		
			public «name»(Throwable e) {
				super(e);
			}
		}
	'''
}
