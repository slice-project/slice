package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration

class AgentGenerator implements IGenerator {	

	@Inject AgentProjectGenerator agentProjectGenerator
	@Inject RuleSetGenerator ruleSetGenerator
	@Inject extension CommanderGenerator
	@Inject extension IQualifiedNameProvider
		
	override doGenerate(Resource resource, IFileSystemAccess fsa) {
		fsa.generateFile(OutputPathUtils.sliceAgents + "/pom.xml", compileAgentsPOM(resource))
		fsa.generateFile(OutputPathUtils.sliceRules + "/pom.xml", compileRulesPOM(resource))
		
		for (e: resource.allContents.toIterable.filter(typeof(AgentDeclaration))) {
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
				
				<distributionManagement>
					<repository>
						<id>slice-mvn-hosted</id>
						<url>http://129.254.88.119:8081/nexus/content/repositories/releases/</url>
					</repository>
				</distributionManagement>	
				
				<build>
					<plugins>
						<plugin>
						   <groupId>org.sonatype.plugins</groupId>
						   <artifactId>nexus-staging-maven-plugin</artifactId>
						   <version>1.5.1</version>
						   <executions>
						      <execution>
						         <id>default-deploy</id>
						         <phase>deploy</phase>
						         <goals>
						            <goal>deploy</goal>
						         </goals>
						      </execution>
						   </executions>
						   <configuration>
						      <serverId>nexus</serverId>
						      <nexusUrl>http://129.254.88.119:8081/nexus/</nexusUrl>
						      <skipStaging>true</skipStaging>
						   </configuration>
						</plugin>			
					</plugins>
				</build>
		</project>
	'''		
}
