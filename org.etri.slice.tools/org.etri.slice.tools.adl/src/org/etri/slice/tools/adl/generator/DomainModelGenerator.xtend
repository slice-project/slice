package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import java.util.List
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.etri.slice.tools.adl.validation.domain_dependency.DomainManager

class DomainModelGenerator implements IGeneratorForMultiInput {

	@Inject ContextGenerator contextGenerator
	@Inject ControlGenerator controlGenerator
	@Inject DomainGenerator domainGenerator
	@Inject EventGenerator eventGenerator
	@Inject ExceptionGenerator exceptionGenerator
	@Inject DomainManager domainManager
	
	override doGenerate(Resource input, IFileSystemAccess fsa) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override doGenerate(List<Resource> resources, IFileSystemAccess fsa) {
		fsa.generateFile(OutputPathUtils.sliceModels + "/pom.xml", compileModelsPOM(domainManager))
	
		domainGenerator.doGenerate(resources, fsa)
		contextGenerator.doGenerate(resources, fsa)
		controlGenerator.doGenerate(resources, fsa)
		eventGenerator.doGenerate(resources, fsa)
		exceptionGenerator.doGenerate(resources, fsa)
	}
	
	def compileModelsPOM(DomainManager domainManager) '''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
		
			<parent>
				<groupId>org.etri.slice</groupId>
				<artifactId>org.etri.slice</artifactId>
				<version>0.9.1</version>
				<relativePath>../pom.xml</relativePath>
			</parent>
			
			<artifactId>org.etri.slice.models</artifactId>
			<name>The common data models for SLICE application domains</name>
		
			<packaging>pom</packaging>
			
			<modules>
				«FOR e : domainManager.buildOrderedDomainList»
					<module>org.etri.slice.commons.«e.domain»</module>
				«ENDFOR»
			</modules>
		
			<dependencies>
				<dependency>
					<groupId>org.etri.slice</groupId>
					<artifactId>org.etri.slice.commons</artifactId>
					<version>0.9.1</version>
				</dependency>
			</dependencies>
		</project>		
	'''	
}
