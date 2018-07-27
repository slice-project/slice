package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import java.util.List
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration
import org.etri.slice.tools.adl.generator.compiler.POMCompiler
import org.etri.slice.tools.adl.generator.compiler.RuleSetCompiler

class RuleSetGenerator implements IGeneratorForMultiInput {
	
	@Inject extension RuleSetCompiler
	@Inject extension POMCompiler
	@Inject extension OutputPathUtils
	@Inject extension IQualifiedNameProvider		
	
	override doGenerate(List<Resource> resources, IFileSystemAccess fsa) {		
		for (e: resources.map[allContents.toIterable.filter(typeof(AgentDeclaration))].flatten) {
			generateMavenProject(e, fsa)				
			generateRule(e, fsa)	
			generateMetaINF(e, fsa)
		}
	}
	
	def generateRule(AgentDeclaration it, IFileSystemAccess fsa) {
		val package = ruleFullyQualifiedName.replace(".", "/")
		val file = ruleMavenResHome + package + "/" + name.toLowerCase + "-rules.drl"
		fsa.generateFile(file, compileRuleSet(fsa))
	}
	
	def generateMavenProject(AgentDeclaration it, IFileSystemAccess fsa) {
		fsa.generateFile(ruleMavenHome + "pom.xml", compileRulePOM)				
	}
	
	def generateMetaINF(AgentDeclaration it, IFileSystemAccess fsa) {
		val file = ruleMavenResHome +  "/META-INF/kmodule.xml"
		fsa.generateFile(file, compileKmodule)
	}
	
	def compileKmodule(AgentDeclaration it) '''
		<?xml version="1.0" encoding="UTF-8"?>
		<kmodule xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xmlns="http://jboss.org/kie/6.0.0/kmodule">
		
			<kbase name="org.etri.slice.rules.«eContainer.fullyQualifiedName».«name.toLowerCase»" default="true" eventProcessingMode="stream" packages="org.etri.slice.rules.«eContainer.fullyQualifiedName».«name.toLowerCase»">
				<ksession name="org.etri.slice" type="stateful" default="true"/>
			</kbase>
		</kmodule>	
	'''
	
	override doGenerate(Resource input, IFileSystemAccess fsa) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
}
