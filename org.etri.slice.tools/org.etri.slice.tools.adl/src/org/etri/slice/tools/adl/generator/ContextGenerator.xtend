package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import java.util.List
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.etri.slice.tools.adl.domainmodel.Context
import org.etri.slice.tools.adl.domainmodel.Property

class ContextGenerator implements IGeneratorForMultiInput {
	
	@Inject extension IQualifiedNameProvider
	@Inject extension GeneratorUtils
	@Inject extension OutputPathUtils
	
	override doGenerate(List<Resource> resources, IFileSystemAccess fsa) {
		for ( e: resources.map[allContents.toIterable.filter(typeof(Context))].flatten ) {
			val package = e.sliceFullyQualifiedName.replace(".", "/")
			val file = e.commonsMavenSrcHome + package + "/" + e.name + ".java"
			fsa.generateFile(file, e.compile)
		}
	}
	
	def compile(Context it) '''
		«val importManager = new ImportManager(true)» 
		«val body = body(importManager)»
		«IF eContainer !== null»
			package org.etri.slice.commons.«eContainer.fullyQualifiedName».context;
		«ENDIF»
		
		import org.etri.slice.commons.SliceContext;
		import org.etri.slice.commons.internal.ContextBase;
		import org.kie.api.definition.type.Role;
		
		import lombok.AllArgsConstructor;
		import lombok.Builder;
		import lombok.Data;
		import lombok.NoArgsConstructor;
		
		«FOR i:importManager.imports»
			import «i»;
		«ENDFOR»
		
		@Data
		@Builder
		@AllArgsConstructor
		@NoArgsConstructor
		
		@Role(Role.Type.EVENT)
		@SliceContext
		«body»
	'''
 
	def body(Context it, ImportManager importManager) '''
		public class «name» «IF superType !== null»extends «superType.shortName(importManager)»«ENDIF»«IF superType === null» implements ContextBase«ENDIF» {
			public static class Field {
				«FOR p : properties»
					public static final String «p.name» = "«name».«p.name»";
				«ENDFOR»
			}
			
			public static final String dataType = "«fullyQualifiedName.adaptToSlice("context")»";
			public static final String topic = "«name.toLowerCase»";
			public static final String dataKey = "dataKey:" + dataType;
			
			«IF superType !== null»«superType.shortName(importManager)» «superType.simpleName.toLowerCase»;«ENDIF»
			«FOR p : properties»
				«p.compile(importManager)»
			«ENDFOR»
		}
		'''
    
  	def compile(Property it, ImportManager importManager) '''
		private «type.shortName(importManager)» «name»;
	'''
		
	override doGenerate(Resource input, IFileSystemAccess fsa) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
}
