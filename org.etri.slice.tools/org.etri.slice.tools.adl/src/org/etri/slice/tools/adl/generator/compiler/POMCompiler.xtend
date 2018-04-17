package org.etri.slice.tools.adl.generator.compiler

import com.google.inject.Inject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration

class POMCompiler {	
	
	@Inject extension IQualifiedNameProvider	
	
	def compileDomainPOM(DomainDeclaration it) '''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
		
			<parent>
				<groupId>org.etri.slice</groupId>
				<artifactId>org.etri.slice.models</artifactId>
				<version>0.9.1</version>
				<relativePath>../pom.xml</relativePath>
			</parent>
			
			<groupId>org.etri.slice.commons</groupId>
			<artifactId>org.etri.slice.commons.«fullyQualifiedName»</artifactId>
			<name>The SLICE common data models for «fullyQualifiedName» domain</name>
		
			<packaging>bundle</packaging>
		
			<dependencies>
				<dependency>
					<groupId>org.etri.slice</groupId>
					<artifactId>org.etri.slice.commons</artifactId>
					<version>0.9.1</version>
				</dependency>
			</dependencies>
		
			<build>
				<plugins>
					<plugin>
						<groupId>org.apache.felix</groupId>
						<artifactId>maven-bundle-plugin</artifactId>
						<extensions>true</extensions>
						<configuration>
							<instructions>
								<Bundle-ClassPath>${bundle.classpath}</Bundle-ClassPath>
								<Embed-Transitive>true</Embed-Transitive>
								<Embed-Dependency>${embed.dependency}</Embed-Dependency>
								<Embed-Directory>${embed.directory}</Embed-Directory>
								<_exportcontents>${export.packages}</_exportcontents>
								<Import-Package>${import.packages}</Import-Package>
							</instructions>
						</configuration>
					</plugin>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-eclipse-plugin</artifactId>
						<configuration>
							<downloadSources>false</downloadSources>
						</configuration>
					</plugin>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-compiler-plugin</artifactId>
						<configuration>
							<source>1.8</source>
							<target>1.8</target>
							<encoding>UTF-8</encoding>
						</configuration>
					</plugin>
					<plugin>
						<groupId>org.codehaus.mojo</groupId>
						<artifactId>properties-maven-plugin</artifactId>
						<executions>
							<execution>
								<phase>initialize</phase>
								<goals>
									<goal>read-project-properties</goal>
								</goals>
								<configuration>
									<files>
										<file>bundle.properties</file>
									</files>
								</configuration>
							</execution>
						</executions>
					</plugin>
				</plugins>
			</build>
		
		</project>		
	'''
	
	def compileAgentPOM(AgentDeclaration it) '''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
			<parent>
				<groupId>org.etri.slice</groupId>
				<artifactId>org.etri.slice.agents</artifactId>
				<version>0.9.1</version>
				<relativePath>../pom.xml</relativePath>
			</parent>
			
			<packaging>bundle</packaging>	
			
			<artifactId>org.etri.slice.agents.«eContainer.fullyQualifiedName».«name.toLowerCase»</artifactId>
			<name>... org.etri.slice.agents.«eContainer.fullyQualifiedName».«name.toLowerCase»</name>
			
			<dependencies>
				<dependency>
					<groupId>org.etri.slice</groupId>
					<artifactId>org.etri.slice.core</artifactId>
					<version>0.9.1</version>
				</dependency>
				<dependency>
					<groupId>org.etri.slice.commons</groupId>
					<artifactId>org.etri.slice.commons.«eContainer.fullyQualifiedName»</artifactId>
					<version>0.9.1</version>
				</dependency>			
			</dependencies>
		
			<build>
				<plugins>
					<plugin>
						<groupId>org.apache.felix</groupId>
						<artifactId>maven-bundle-plugin</artifactId>
						<configuration>
							<instructions>
								<Private-Package>${private.packages}</Private-Package>
							</instructions>
						</configuration>
					</plugin>
					<plugin>
						<groupId>org.apache.felix</groupId>
						<artifactId>maven-ipojo-plugin</artifactId>
					</plugin>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-eclipse-plugin</artifactId>
					</plugin>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-compiler-plugin</artifactId>
					</plugin>
					<plugin>
						<groupId>org.codehaus.mojo</groupId>
						<artifactId>properties-maven-plugin</artifactId>
					</plugin>
				</plugins>
			</build>	
			
		</project>
	'''	
	
	def compileDevicePOM(AgentDeclaration it) '''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
			<parent>
				<groupId>org.etri.slice.devices</groupId>
				<artifactId>org.etri.slice.devices</artifactId>
				<version>0.9.1</version>
				<relativePath>../pom.xml</relativePath>
			</parent>
			
			<packaging>bundle</packaging>	
			
			<artifactId>org.etri.slice.devices.«eContainer.fullyQualifiedName».«name.toLowerCase»</artifactId>
			<name>... org.etri.slice.devices.«eContainer.fullyQualifiedName».«name.toLowerCase»</name>
			
			<dependencies>
				<dependency>
					<groupId>org.etri.slice.commons</groupId>
					<artifactId>org.etri.slice.commons.«eContainer.fullyQualifiedName»</artifactId>
					<version>0.9.1</version>
				</dependency>			
			</dependencies>
		
			<build>
				<resources>
					<resource>
						<directory>src/main/resources</directory>
					</resource>
				</resources>
				<plugins>
					<plugin>
						<groupId>org.apache.felix</groupId>
						<artifactId>maven-bundle-plugin</artifactId>
						<configuration>
							<instructions>
								<Bundle-ClassPath>${bundle.classpath}</Bundle-ClassPath>
								<Embed-Dependency>${embed.dependency}</Embed-Dependency>
								<Embed-Directory>${embed.directory}</Embed-Directory>	
								<Private-Package>${private.packages}</Private-Package>
							</instructions>
						</configuration>
					</plugin>
					<plugin>
						<groupId>org.apache.felix</groupId>
						<artifactId>maven-ipojo-plugin</artifactId>
					</plugin>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-eclipse-plugin</artifactId>
					</plugin>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-compiler-plugin</artifactId>
					</plugin>
					<plugin>
						<groupId>org.codehaus.mojo</groupId>
						<artifactId>properties-maven-plugin</artifactId>
					</plugin>
				</plugins>
			</build>
			
		</project>
	'''		
	
	def compileRulePOM(AgentDeclaration it) '''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
		
			<parent>
				<groupId>org.etri.slice</groupId>
				<artifactId>org.etri.slice.rules</artifactId>
				<version>0.9.1</version>
			</parent>
		
			<artifactId>org.etri.slice.rules.«eContainer.fullyQualifiedName».«name.toLowerCase»</artifactId>
			<version>0.9.1</version>
			<name>... rules for a «eContainer.fullyQualifiedName».«name.toLowerCase» agent </name>
		
		</project>
	'''		
}
