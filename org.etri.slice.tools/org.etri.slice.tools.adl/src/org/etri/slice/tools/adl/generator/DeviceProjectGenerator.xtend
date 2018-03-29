package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.domainmodel.Call
import org.etri.slice.tools.adl.domainmodel.Context
import org.etri.slice.tools.adl.generator.compiler.LogbackCompiler
import org.etri.slice.tools.adl.generator.compiler.MetaDataCompiler
import org.etri.slice.tools.adl.generator.compiler.POMCompiler

class DeviceProjectGenerator implements IGenerator {
	
	@Inject extension IQualifiedNameProvider	
	@Inject extension BehaviorGenerator
	@Inject extension MetaDataCompiler
	@Inject extension POMCompiler
	@Inject extension LogbackCompiler
	@Inject extension OutputPathUtils
	
	override doGenerate(Resource resource, IFileSystemAccess fsa) {		
		for ( e: resource.allContents.toIterable.filter(typeof(AgentDeclaration)) ) {
			generateDevice(e, fsa)
			generateMavenProject(e, fsa)
			generateMetaData(e, fsa)
			generateLogback(e, fsa)
		}
	}
	
	def generateDevice(AgentDeclaration it, IFileSystemAccess fsa) {
		for ( b: behaviorSet.behaviors ) {
			for ( t: b.situation.types ) {
				var type = t
				if ( type instanceof Context ) {
					generateSensor(type, it, fsa)
				}
			}
			
			var action = b.action
			if ( action instanceof Call ) {
				generateService(action.control, it, fsa)
			}
		}	
	}	
	
	def generateMavenProject(AgentDeclaration it, IFileSystemAccess fsa) {
		fsa.generateFile(deviceMavenHome + "bundle.properties", compileBundleProperties)
		fsa.generateFile(deviceMavenHome + "pom.xml", compileDevicePOM)				
	}
	
	def generateMetaData(AgentDeclaration it, IFileSystemAccess fsa) {
		fsa.generateFile(deviceMavenResHome + "metadata.xml", compileMetaData)
	}
	
	def generateLogback(AgentDeclaration it, IFileSystemAccess fsa) {
		fsa.generateFile(deviceMavenResHome + "logback.xml", compileLogback)
	}		
	
	def compileBundleProperties(AgentDeclaration it) '''
		# Configure the created bundle
		embed.dependency=,;scope=!provided|test
		embed.directory=lib
		bundle.classpath=.,{maven-dependencies}		
		private.packages=org.etri.slice.devices.«eContainer.fullyQualifiedName».«name.toLowerCase».*
	'''
}
