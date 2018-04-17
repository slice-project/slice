package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration
import org.etri.slice.tools.adl.generator.compiler.POMCompiler

class DomainGenerator implements IGenerator {	

	@Inject extension POMCompiler
	@Inject extension OutputPathUtils
		
	override doGenerate(Resource resource, IFileSystemAccess fsa) {
				
		for (e: resource.allContents.toIterable.filter(typeof(DomainDeclaration))) {
			generateMavenProject(e, fsa)
		}
	}
	
	
	def generateMavenProject(DomainDeclaration it, IFileSystemAccess fsa) {		
		fsa.generateFile(commonsMavenHome + "bundle.properties", compileBundleProperties)
		fsa.generateFile(commonsMavenHome + "pom.xml", compileDomainPOM)				
	}
	
	def compileBundleProperties(DomainDeclaration it) '''
		# Configure the created bundle
		export.packages=!org.eclipse.jetty.*,*
		embed.dependency=*,;scope=!provided|test;inline=true
		embed.directory=lib
		bundle.classpath=.,{maven-dependencies}
		import.packages=*;resolution:=optional	
	'''
}
