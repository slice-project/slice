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
	
	override doGenerate(Resource resource, IFileSystemAccess fsa) {
		for(e: resource.allContents.toIterable.filter(typeof(Exception))) {
			val maven_src  = "org.etri.slice.commons." + e.eContainer.fullyQualifiedName + "/src/main/java/"
			val filePath = maven_src + "org/etri/slice/commons/" + e.fullyQualifiedName.toString("/")
			fsa.generateFile(filePath + ".java", e.compile)
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
