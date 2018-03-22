package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.generator.compiler.POMCompiler

class AgentGenerator implements IGenerator {	

	@Inject extension IQualifiedNameProvider
	@Inject DeviceGenerator deviceGenerator
	@Inject RuleSetGenerator ruleSetGenerator
	@Inject extension CommanderGenerator
	@Inject extension POMCompiler
		
	override doGenerate(Resource resource, IFileSystemAccess fsa) {
		for (e: resource.allContents.toIterable.filter(typeof(AgentDeclaration))) {
			generateMavenProject(e, fsa)
			deviceGenerator.doGenerate(resource, fsa)
			ruleSetGenerator.doGenerate(resource, fsa)
			
			if ( e.commandSets !== null )	 {
				for (c: e.commandSets ) {
					generateCommander(e, c, fsa);
				}
			}		
		}
	}
	
	def generateMavenProject(AgentDeclaration it, IFileSystemAccess fsa) {
		val projectHome  = "org.etri.slice.commons." + eContainer.fullyQualifiedName + "/"
		fsa.generateFile(projectHome + "bundle.properties", compileBundleProperties)
		fsa.generateFile(projectHome + "pom.xml", compileModelPOM)				
	}
	
	def compileBundleProperties(AgentDeclaration it) '''
		# Configure the created bundle
		export.packages=*
		embed.dependency=*,;scope=!provided|test
		embed.directory=lib
		bundle.classpath=.,{maven-dependencies}
		import.packages=*;resolution:=optional	
	'''
}
