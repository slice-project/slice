package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator

public class ADLGenerator implements IGenerator {
	
	@Inject AgentGenerator agentGenerator
	@Inject DomainModelGenerator domainGenerator
  	
	override void doGenerate(Resource resource, IFileSystemAccess fsa) {

		agentGenerator.doGenerate(resource, fsa)
		domainGenerator.doGenerate(resource, fsa)
  	}
}
