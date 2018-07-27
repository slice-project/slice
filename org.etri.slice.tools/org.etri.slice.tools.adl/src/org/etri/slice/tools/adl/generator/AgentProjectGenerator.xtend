package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import java.util.List
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.generator.compiler.AgentCompiler
import org.etri.slice.tools.adl.generator.compiler.LogbackCompiler
import org.etri.slice.tools.adl.generator.compiler.MetaDataCompiler
import org.etri.slice.tools.adl.generator.compiler.POMCompiler

class AgentProjectGenerator implements IGeneratorForMultiInput {
	
	@Inject extension IQualifiedNameProvider	
	@Inject extension AgentCompiler
	@Inject extension MetaDataCompiler
	@Inject extension POMCompiler
	@Inject extension LogbackCompiler
	@Inject extension OutputPathUtils
	
	override doGenerate(List<Resource> resources, IFileSystemAccess fsa) {		
		for (e: resources.map[allContents.toIterable.filter(typeof(AgentDeclaration))].flatten) {
			generateMavenProject(e, fsa)
			generateAgent(e, fsa)
			generateMetaData(e, fsa)
			generateLogback(e, fsa)
		}
	}
	
	def generateAgent(AgentDeclaration it, IFileSystemAccess fsa) {
		val package = agentFullyQualifiedName.replace(".", "/")
		val file = agentMavenSrcHome + package + "/" + name + ".java"
		fsa.generateFile(file, agentCompile)			
	}
	
	def generateMavenProject(AgentDeclaration it, IFileSystemAccess fsa) {
		fsa.generateFile(agentMavenHome + "bundle.properties", compileBundleProperties)
		fsa.generateFile(agentMavenHome + "pom.xml", compileAgentPOM)				
	}
	
	def generateMetaData(AgentDeclaration it, IFileSystemAccess fsa) {
		fsa.generateFile(agentMavenResHome + "metadata.xml", compileMetaData)
	}
	
	def generateLogback(AgentDeclaration it, IFileSystemAccess fsa) {
		fsa.generateFile(agentMavenResHome + "logback.xml", compileLogback)
	}		
	
	def compileBundleProperties(AgentDeclaration it) '''
		# Configure the created bundle
		private.packages=org.etri.slice.agents.«eContainer.fullyQualifiedName».«name.toLowerCase».*
	'''
	
	override doGenerate(Resource input, IFileSystemAccess fsa) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
}
