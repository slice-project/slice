package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration

class DomainModelGenerator implements IGenerator {
	
	@Inject ContextGenerator contextGenerator
	@Inject ControlGenerator controlGenerator
	@Inject EventGenerator eventGenerator
	@Inject ExceptionGenerator exceptionGenerator
	@Inject extension IQualifiedNameProvider	
	
	override doGenerate(Resource resource, IFileSystemAccess fsa) {
		fsa.generateFile(OutputPathUtils.sliceModels + "/pom.xml", compileModelsPOM(resource))
		
		contextGenerator.doGenerate(resource, fsa)
		controlGenerator.doGenerate(resource, fsa)
		eventGenerator.doGenerate(resource, fsa)
		exceptionGenerator.doGenerate(resource, fsa)
	}
	
	def compileModelsPOM(Resource resource) '''
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
				«FOR e: resource.allContents.toIterable.filter(typeof(DomainDeclaration))»
					<module>org.etri.slice.commons.«e.fullyQualifiedName»</module>
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
