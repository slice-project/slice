package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import java.util.List
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration
import org.etri.slice.tools.adl.generator.compiler.POMCompiler

class DomainGenerator implements IGeneratorForMultiInput {	

	@Inject extension POMCompiler
	@Inject extension OutputPathUtils
		
	override doGenerate(List<Resource> resources, IFileSystemAccess fsa) {
				
		for ( e: resources.map[allContents.toIterable.filter(typeof(DomainDeclaration))].flatten ) {
			generateMavenProject(e, fsa)
		}
	}
	
	override doGenerate(Resource input, IFileSystemAccess fsa) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	def generateMavenProject(DomainDeclaration it, IFileSystemAccess fsa) {				
		fsa.generateFile(commonsMavenHome + "bundle.properties", compileBundleProperties)
		fsa.generateFile(commonsMavenHome + "pom.xml", compileDomainPOM)				
	}
	
	def compileBundleProperties(DomainDeclaration it) '''
		# Configure the created bundle
		export.packages=!org.eclipse.jetty.*,javax.xml.stream,*
		embed.dependency=*,;scope=!provided|test;inline=true
		embed.directory=lib
		bundle.classpath=.,{maven-dependencies}
		import.packages=*;resolution:=optional	
	'''
	

	
}
