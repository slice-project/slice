package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.generator.compiler.DeviceCompiler
import org.etri.slice.tools.adl.generator.compiler.LogbackCompiler
import org.etri.slice.tools.adl.generator.compiler.MetaDataCompiler
import org.etri.slice.tools.adl.generator.compiler.POMCompiler

class DeviceGenerator implements IGenerator {
	
	@Inject extension IQualifiedNameProvider	
	@Inject extension DeviceCompiler
	@Inject extension MetaDataCompiler
	@Inject extension POMCompiler
	@Inject extension LogbackCompiler
	@Inject extension OutputPathUtils
	
	override doGenerate(Resource resource, IFileSystemAccess fsa) {		
		for (e: resource.allContents.toIterable.filter(typeof(AgentDeclaration))) {
			generateMavenProject(e, fsa)
			generateDevice(e, fsa)
			generateMetaData(e, fsa)
			generateLogback(e, fsa)
		}
	}
	
	def generateDevice(AgentDeclaration it, IFileSystemAccess fsa) {
		val package = deviceFullyQualifiedName.replace(".", "/")
		val file = deviceMavenSrcHome + package + "/" + name + ".java"
		fsa.generateFile(file, deviceCompile)			
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
		private.packages=org.etri.slice.devices.«eContainer.fullyQualifiedName».«name.toLowerCase».*
	'''
}
