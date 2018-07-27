package org.etri.slice.tools.adl.ui.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.builder.BuilderParticipant;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.resource.IContainer;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescription.Delta;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.etri.slice.tools.adl.generator.IGeneratorForMultiInput;

import com.google.inject.Inject;

public class BuilderParticipantForMultiInput extends BuilderParticipant {
	
	@Inject	private ResourceDescriptionsProvider resourceDescriptionsProvider;
	@Inject private IContainer.Manager containerManager;
	@Inject(optional = true) private IGeneratorForMultiInput generator;
	protected ThreadLocal<Boolean> buildSemaphor = new ThreadLocal<Boolean>();

	@Override
	public void build(IBuildContext context, IProgressMonitor monitor) throws CoreException {
		buildSemaphor.set(false);
		super.build(context, monitor);
	}

	@Override
	protected void handleChangedContents(Delta delta, IBuildContext context, IFileSystemAccess fileSystemAccess) {
		if (!buildSemaphor.get() && generator != null) {
			invokeGenerator(delta, context, fileSystemAccess);
		}
	}

	private void invokeGenerator (Delta delta, IBuildContext context, IFileSystemAccess fileSystemAccess) {  
    buildSemaphor.set(true);
    Resource resource = context.getResourceSet().getResource(delta.getUri(), true);
    
    System.out.println("BuilderParticipantForMultiInput invokeGenerator resource " + resource);
        
    if (shouldGenerate(resource, context)) {  	
      IResourceDescriptions index = resourceDescriptionsProvider.createResourceDescriptions();
      IResourceDescription resDesc = index.getResourceDescription(resource.getURI());
      List<IContainer> visibleContainers = containerManager.getVisibleContainers(resDesc, index);
      
      List<Resource> resources = new ArrayList<Resource>();
      
      for (IContainer c : visibleContainers) {
    	  System.out.println("\t visibleContainer " + c);
        for (IResourceDescription rd : c.getResourceDescriptions()) {
        	System.out.println("\t ResourceDescription " + rd);
        	resources.add(context.getResourceSet().getResource(rd.getURI(), true));
        }
      }
 
      generator.doGenerate(resources, fileSystemAccess);
            
//      val xtextDocument = context.xtextDocument
//    	        val firstLetter = xtextDocument.get(issue.offset, 1)
//    	        xtextDocument.replace(issue.offset, 1, firstLetter.toUpperCase)
//    	        (xtextDocument as XtextDocument).validationJob.schedule
    }
    else
    {
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("not shouldGenerate");
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
    }
  }
}