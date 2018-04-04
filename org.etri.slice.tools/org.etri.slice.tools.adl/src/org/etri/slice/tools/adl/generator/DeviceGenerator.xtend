package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration

class DeviceGenerator implements IGenerator {	

	@Inject DeviceProjectGenerator deviceProjectGenerator
	@Inject extension IQualifiedNameProvider
		
	override doGenerate(Resource resource, IFileSystemAccess fsa) {
		fsa.generateFile(OutputPathUtils.sliceDevices + "/pom.xml", compileDevicesPOM(resource))
		
		for (e: resource.allContents.toIterable.filter(typeof(AgentDeclaration))) {
			deviceProjectGenerator.doGenerate(resource, fsa)
		}
	}
	
	def compileDevicesPOM(Resource resource) '''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
			
			<parent>
				<groupId>org.etri.slice</groupId>
				<artifactId>org.etri.slice</artifactId>
				<version>0.9.1</version>
				<relativePath>../pom.xml</relativePath>
			</parent>
			
			<groupId>org.etri.slice.devices</groupId>
			<artifactId>org.etri.slice.devices</artifactId>
			<name>The 3rd party implementation for the SLICE devices</name>
			<packaging>pom</packaging>
			
			<modules>
				«FOR e: resource.allContents.toIterable.filter(typeof(AgentDeclaration))»
					<module>org.etri.slice.devices.«e.eContainer.fullyQualifiedName».«e.name.toLowerCase»</module>
				«ENDFOR»
			</modules>
		</project>
	'''	
}
