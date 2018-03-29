package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.generator.compiler.POMCompiler

class AgentGenerator implements IGenerator {	

	@Inject AgentProjectGenerator agentProjectGenerator
	@Inject RuleSetGenerator ruleSetGenerator
	@Inject extension CommanderGenerator
	@Inject extension POMCompiler
	@Inject extension OutputPathUtils
	@Inject extension IQualifiedNameProvider
		
	override doGenerate(Resource resource, IFileSystemAccess fsa) {
		fsa.generateFile(OutputPathUtils.sliceAgents + "/pom.xml", compileAgentsPOM(resource))
		fsa.generateFile(OutputPathUtils.sliceRules + "/pom.xml", compileRulesPOM(resource))
		
		for (e: resource.allContents.toIterable.filter(typeof(AgentDeclaration))) {
			generateMavenProject(e, fsa)
			agentProjectGenerator.doGenerate(resource, fsa)
			ruleSetGenerator.doGenerate(resource, fsa)
			
			if ( e.commandSets !== null )	 {
				for (c: e.commandSets ) {
					generateCommander(e, c, fsa);
				}
			}		
		}
	}
	
	def compileAgentsPOM(Resource resource) '''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
			
			<parent>
				<groupId>org.etri.slice</groupId>
				<artifactId>org.etri.slice</artifactId>
				<version>0.9.1</version>
				<relativePath>../pom.xml</relativePath>
			</parent>
			
			<artifactId>org.etri.slice.agents</artifactId>
			<name>The SLICE agents</name>
			<packaging>pom</packaging>
			
			<modules>
				«FOR e: resource.allContents.toIterable.filter(typeof(AgentDeclaration))»
					<module>org.etri.slice.agents.«e.eContainer.fullyQualifiedName».«e.name.toLowerCase»</module>
				«ENDFOR»
			</modules>
		</project>
	'''	
	
	def compileRulesPOM(Resource resource) '''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
		
			<modelVersion>4.0.0</modelVersion>
			<groupId>org.etri.slice</groupId>
			<artifactId>org.etri.slice.rules</artifactId>
			<version>0.9.1</version>
		
			<packaging>pom</packaging>
			<name>The SLICE rules</name>
			
				<modules>
				«FOR e: resource.allContents.toIterable.filter(typeof(AgentDeclaration))»
					<module>org.etri.slice.rules.«e.eContainer.fullyQualifiedName».«e.name.toLowerCase»</module>
				«ENDFOR»
				</modules>
		</project>
	'''		
	
	def generateMavenProject(AgentDeclaration it, IFileSystemAccess fsa) {		
		fsa.generateFile(commonsMavenHome + "bundle.properties", compileBundleProperties)
		fsa.generateFile(commonsMavenHome + "pom.xml", compileModelPOM)				
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
