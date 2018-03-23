package org.etri.slice.tools.adl.generator

import com.google.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator

class DomainModelGenerator implements IGenerator {
	
	@Inject ContextGenerator contextGenerator
	@Inject ControlGenerator controlGenerator
	@Inject EventGenerator eventGenerator
	@Inject ExceptionGenerator exceptionGenerator
	
	override doGenerate(Resource resource, IFileSystemAccess fsa) {
		contextGenerator.doGenerate(resource, fsa)
		controlGenerator.doGenerate(resource, fsa)
		eventGenerator.doGenerate(resource, fsa)
		exceptionGenerator.doGenerate(resource, fsa)
	}
}
