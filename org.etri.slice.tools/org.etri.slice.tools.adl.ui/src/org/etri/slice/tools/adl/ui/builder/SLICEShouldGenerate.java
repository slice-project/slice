package org.etri.slice.tools.adl.ui.builder;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.generator.IShouldGenerate;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Inject;

public class SLICEShouldGenerate implements IShouldGenerate {

	@Inject IResourceValidator resourceValidator;
	
	@Override
	public boolean shouldGenerate(Resource resource, CancelIndicator cancelIndicator) 
	{		
//		List<Issue> issues = resourceValidator.validate(resource, CheckMode.FAST_ONLY, cancelIndicator);
//		
//		int errors = 0;
//		
//		for(Issue issue: issues)
//		{
//			if(issue.getSeverity() == Severity.ERROR)
//				errors ++;
//		}
//		
//		return (errors == 0);
		
		return true;
	}

}
