package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.etri.slice.tools.adl.domainmodel.Event
import org.etri.slice.tools.adl.domainmodel.Property

class EventGenerator implements IGenerator {
	
	@Inject extension IQualifiedNameProvider
	@Inject extension GeneratorUtils
	
	override doGenerate(Resource resource, IFileSystemAccess fsa) {
		for(e: resource.allContents.toIterable.filter(typeof(Event))) {
			val maven_src  = "org.etri.slice.commons." + e.eContainer.fullyQualifiedName + "/src/main/java/"
			var package = maven_src + "org/etri/slice/commons/" + e.fullyQualifiedName.toString("/")
			var path = package.substring(0, package.lastIndexOf("/") + 1) + "event/"
			fsa.generateFile(path + e.name + ".java", e.compile)
		}
	}
	
	def compile(Event it) '''
		«val importManager = new ImportManager(true)» 
		«val body = body(importManager)»
		«IF eContainer !== null»
			package org.etri.slice.commons.«eContainer.fullyQualifiedName».event;
		«ENDIF»
		
		import org.etri.slice.commons.SliceContext;
		import org.etri.slice.commons.SliceEvent;
		import org.kie.api.definition.type.Role;
		
		import lombok.AllArgsConstructor;
		import lombok.Builder;
		import lombok.Data;
		import lombok.EqualsAndHashCode;
		import lombok.NoArgsConstructor;
		
		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
		
		@Data
		@Builder
		@AllArgsConstructor
		@NoArgsConstructor
		
		@EqualsAndHashCode(callSuper=false)
		
		@Role(Role.Type.EVENT)
		@SliceContext
		«body»
	'''
 
	def body(Event it, ImportManager importManager) '''
		public class «name» extends SliceEvent {
		
			public static final String TOPIC = "«topic.name»";
			private static final long serialVersionUID = «generateSerialVersionUID»L;
			
			«IF superType !== null»«superType.shortName(importManager)» «superType.simpleName.toLowerCase»;«ENDIF»
			«FOR p : properties»
				«p.compile(importManager)»
			«ENDFOR»
		}
	'''
    
  	def compile(Property it, ImportManager importManager) '''
		private «type.shortName(importManager)» «name»;
	'''
}
