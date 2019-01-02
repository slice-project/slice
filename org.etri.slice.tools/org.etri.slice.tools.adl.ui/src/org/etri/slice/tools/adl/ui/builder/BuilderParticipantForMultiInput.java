package org.etri.slice.tools.adl.ui.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.builder.BuilderParticipant;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.resource.IContainer;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescription.Delta;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.etri.slice.tools.adl.DomainModelOutputConfigurationProvider;
import org.etri.slice.tools.adl.generator.IGeneratorForMultiInput;
import org.etri.slice.tools.adl.validation.domain_dependency.DomainManager;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import com.google.inject.Inject;

public class BuilderParticipantForMultiInput extends BuilderParticipant {
	@Inject
	private ResourceDescriptionsProvider resourceDescriptionsProvider;

	@Inject
	private IContainer.Manager containerManager;

	@Inject(optional = true)
	private IGeneratorForMultiInput generator;

	@Inject
	private DomainManager domainManager;

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
    
    IProject project = context.getBuiltProject();
    
    try {
		if (project.hasNature(JavaCore.NATURE_ID)) {
			IJavaProject javaProject = JavaCore.create(project);
			
			IClasspathEntry[] classPath = javaProject.getRawClasspath();			
						
			// exclude path start with '/org.etri.slice.generated', at next step we will create new domain model path
			ArrayList<IClasspathEntry> tmpClassPath = new ArrayList<IClasspathEntry>();
			
			for(IClasspathEntry entry : classPath)
			{
				if(entry.getEntryKind() == IClasspathEntry.CPE_SOURCE 
						&& entry.getPath().toString().contains("/org.etri.slice.generated"))
					continue;
				else
					tmpClassPath.add(entry);
			}		
			
			IClasspathEntry[] newClassPath = new IClasspathEntry[tmpClassPath.size()];
			
			for(int i = 0; i < tmpClassPath.size(); i++)
			{
				newClassPath[i] = tmpClassPath.get(i);
			}
			
			javaProject.setRawClasspath(newClassPath, null);
			
		}
	} catch (CoreException e) {
		e.printStackTrace();
	}
        
    Resource resource = context.getResourceSet().getResource(delta.getUri(), true);
            
    if (shouldGenerate(resource, context)) {  	
      IResourceDescriptions index = resourceDescriptionsProvider.createResourceDescriptions();
      IResourceDescription resDesc = index.getResourceDescription(resource.getURI());
      List<IContainer> visibleContainers = containerManager.getVisibleContainers(resDesc, index);
      
      List<Resource> resources = new ArrayList<Resource>();
      
      for (IContainer c : visibleContainers) {
        for (IResourceDescription rd : c.getResourceDescriptions()) {
        	resources.add(context.getResourceSet().getResource(rd.getURI(), true));
        }
      }
 
      generator.doGenerate(resources, fileSystemAccess);
    }
    else
    {
    	// not shouldGenerate
    }
  }
}