/**
 * 
 */
package org.etri.slice.tools.adl.project.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.wizards.buildpaths.BuildPathsBlock;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.ResolverConfiguration;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.xtext.ui.XtextProjectHelper;

/**
 * @author Administrator
 *
 */
public class SLICENewProjectWizard extends Wizard implements INewWizard {

	private SLICENewProjectWizardPage page;
	
	/**
	 * 
	 */
	public SLICENewProjectWizard() {
		super();		
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {		
		page = new SLICENewProjectWizardPage();
		addPage(page);
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		final String projectName = page.getProjectName();
		final String domain = page.getDomain();
		final String location = page.getLocation();
		final boolean isDefaultLocation = page.isDefaultLocation();
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(projectName, domain, location, isDefaultLocation, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}

	private boolean addXtextNature(IProject project) {
		// Because we need a java project/xtest/maven, we have to set the Java nature to
		// the created project:
		IProjectDescription description;
		try {
			
			IProjectDescription desc = project.getDescription();
			String[] prevNatures = desc.getNatureIds();
			String[] newNatures = new String[prevNatures.length + 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length] = XtextProjectHelper.NATURE_ID;
			desc.setNatureIds(newNatures);
			project.setDescription(desc, null);

			return true;
		} catch (CoreException e) {
			e.printStackTrace();			
		}

		return false;
	}

	private boolean addMavenNature(IProject project) {
		ResolverConfiguration resolverConfiguration = new ResolverConfiguration();
		try {
			MavenPlugin.getProjectConfigurationManager().enableMavenNature(project, resolverConfiguration,
					new NullProgressMonitor());

			final IFile pomFile = project.getFile(new Path("pom.xml"));

			InputStream stream = openSLICEPOMContentStream("org.etri.slice", project.getName());
			
			if (pomFile.exists()) {
				pomFile.setContents(stream, true, true, new NullProgressMonitor());
			} else {
				pomFile.create(stream, true, new NullProgressMonitor());
			}
			stream.close();

			MavenPlugin.getProjectConfigurationManager().enableMavenNature(project, resolverConfiguration,
					new NullProgressMonitor());
			
			return true;
		} catch (CoreException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * We will initialize pom.xml file contents.
	 */
	private InputStream openSLICEPOMContentStream(String groupId, String artifactId) {

		String contents = null;

		contents = MessageFormat.format(SLICEPomContants.POM_XML_CONTENTS, groupId, artifactId);

		return new ByteArrayInputStream(contents.getBytes());
	}
	
	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */

	@SuppressWarnings("restriction")
	private void doFinish(
		String projectName,
		String domain, String location,
		boolean isDefaultLocation,
		IProgressMonitor monitor)
		throws CoreException {
		
		String fileNameWithExt = "sample.adl";
		
		if(null != domain && domain.length() != 0)
			fileNameWithExt = domain + ".adl";
		
		// create a sample file
		monitor.beginTask("Creating " + projectName + "...", 6);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
		// First create a simple project of type org.eclipse.core.resources.IProject:
		IProject project = root.getProject(projectName);

		if(!isDefaultLocation)
		{
			BuildPathsBlock.createProject(project, URIUtil.toURI(location), monitor);
		}
		else
		{
			project.create(monitor);	
		}

		project.open(monitor);	
		
		// Because we need a java project, we have to set the Java nature to the created project:
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID, XtextProjectHelper.NATURE_ID });
		project.setDescription(description, null);
		
		// Now we can create our Java project
		IJavaProject javaProject = JavaCore.create(project); 
		
		// However, it's not enough if we want to add Java source code to the project. We have to set the Java build path:
		// 1) We first specify the output location of the compiler (the bin folder):
//		IFolder binFolder = project.getFolder("bin");
//		binFolder.create(false, true, null);
//		javaProject.setOutputLocation(binFolder.getFullPath(), null);
		
		javaProject.setOutputLocation(project.getFullPath(), null);

		// 2) Define the class path entries. Class path entries define the roots of package fragments. Note that you might have to include the necessary plugin "org.eclipse.jdt.launching".
		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		LibraryLocation[] locations = JavaRuntime.getLibraryLocations(vmInstall);
		
		for (LibraryLocation element : locations) {
		 entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
		}
		//add libs to project class path
		javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);
		
		// (3) We have not yet the source folder created:
//		IFolder sourceFolder = project.getFolder("src");
//		sourceFolder.create(false, true, null);
		
		// (4) Now the created source folder should be added to the class entries of the project, otherwise compilation will fail:
//		IPackageFragmentRoot rootPackage = javaProject.getPackageFragmentRoot(sourceFolder);
//		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
//		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
//		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
//		newEntries[oldEntries.length] = JavaCore.newSourceEntry(rootPackage.getPath());
//		javaProject.setRawClasspath(newEntries, null);
		
		monitor.worked(1);
		addXtextNature(project);
		monitor.worked(1);
		addMavenNature(project);
		monitor.worked(1);		
								
		monitor.setTaskName("Creating " + fileNameWithExt + "...");
							
		final IFile file = project.getFile(new Path(fileNameWithExt));
				
		try {
			InputStream stream = openContentStream(domain);
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
		}
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		
		monitor.worked(1);
	}
	
	/**
	 * We will initialize file contents with a sample text.
	 */
	private InputStream openContentStream(String domain) {
		
		String contents = null;
		
		if(null != domain && domain.length() > 0)
			contents = MessageFormat.format(ADLSampleContstants.SIMPLE_ADL_CONTENTS_WITH_DOMAIN, domain);
		else
			contents = "";
		
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "org.etri.slice.tools.adl.project", IStatus.OK, message, null);
		throw new CoreException(status);
	}
}
