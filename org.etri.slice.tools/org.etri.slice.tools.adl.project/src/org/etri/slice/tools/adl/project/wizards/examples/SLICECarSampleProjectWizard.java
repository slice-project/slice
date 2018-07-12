/**
 * 
 */
package org.etri.slice.tools.adl.project.wizards.examples;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
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
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
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
import org.etri.slice.tools.adl.project.wizards.SLICENewFileWizard;
import org.etri.slice.tools.adl.project.wizards.SLICEPomContants;

/**
 * @author Administrator
 *
 */
public class SLICECarSampleProjectWizard extends Wizard implements INewWizard {

	private static final String SAMPLE_CAR_PROJECT_NAME = "org.etri.slice.examples.car";
	
	private static final String SAMPLE_CAR_PROJECT_DOMAIN = "car";
	
	/**
	 * 
	 */
	public SLICECarSampleProjectWizard() {
		super();		
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {		
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
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(SAMPLE_CAR_PROJECT_NAME, SAMPLE_CAR_PROJECT_DOMAIN, monitor);
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

	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */

	private void doFinish(
		String projectName,
		String domain,
		IProgressMonitor monitor)
		throws CoreException {
		
		String fileNameWithExt = domain + ".adl";
		
		// create a sample file
		monitor.beginTask("Creating " + projectName + "...", 4);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
		// First create a simple project of type org.eclipse.core.resources.IProject:
		IProject project = root.getProject(projectName);
		project.create(monitor);
		project.open(monitor);	
		
		// Because we need a java project/xtest/maven, we have to set the Java nature to the created project:
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID, XtextProjectHelper.NATURE_ID});
		project.setDescription(description, null);
		
		// Now we can create our Java project
		IJavaProject javaProject = JavaCore.create(project); 
						
		// However, it's not enough if we want to add Java source code to the project. We have to set the Java build path:
		// 1) We first specify the output location of the compiler (the bin folder):
		IFolder binFolder = project.getFolder("bin");
		binFolder.create(false, true, null);
		javaProject.setOutputLocation(binFolder.getFullPath(), null);
		
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
		IFolder sourceFolder = project.getFolder("src");
		sourceFolder.create(false, true, null);
		
		// (4) Now the created source folder should be added to the class entries of the project, otherwise compilation will fail:
		IPackageFragmentRoot rootPackage = javaProject.getPackageFragmentRoot(sourceFolder);
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = JavaCore.newSourceEntry(rootPackage.getPath());
		javaProject.setRawClasspath(newEntries, null);
			
		monitor.worked(1);
		
		monitor.setTaskName("Creating pom.xml & enable maven nature ...");
		final IFile pomFile = project.getFile(new Path("pom.xml"));
		
		try {
			InputStream stream = openSLICEPOMContentStream("org.etri.slice", projectName);
			if (pomFile.exists()) {
				pomFile.setContents(stream, true, true, monitor);
			} else {
				pomFile.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
		}
		
		ResolverConfiguration resolverConfiguration = new ResolverConfiguration();
		MavenPlugin.getProjectConfigurationManager().enableMavenNature(project, resolverConfiguration, new NullProgressMonitor());
						
		monitor.worked(1);
		
		monitor.setTaskName("Creating " + fileNameWithExt + "...");
		IResource resource = root.findMember(new Path(projectName));
		
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + projectName + "\" does not exist.");
		}
		
		IContainer container = (IContainer) resource;
		final IFile adlFile = container.getFile(new Path(fileNameWithExt));
		
		try {
			InputStream stream = openADLContentStream(domain);
			if (adlFile.exists()) {
				adlFile.setContents(stream, true, true, monitor);
			} else {
				adlFile.create(stream, true, monitor);
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
					IDE.openEditor(page, adlFile, true);
				} catch (PartInitException e) {
				}
			}
		});
		
		monitor.worked(1);
	}
	
	/**
	 * We will initialize adl file contents with a sample text.
	 */
	private InputStream openADLContentStream(String domain) {
		
		String contents = null;
		
		contents = MessageFormat.format(SLICENewFileWizard.SAMPLE_ADL_CONTENTS, domain);
		
		return new ByteArrayInputStream(contents.getBytes());
	}

	/**
	 * We will initialize pom.xml file contents.
	 */
	private InputStream openSLICEPOMContentStream(String groupId, String artifactId) {
		
		String contents = null;
		
		contents = MessageFormat.format(SLICEPomContants.POM_XML_CONTENTS, groupId, artifactId);
		
		return new ByteArrayInputStream(contents.getBytes());
	}	
	
	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "org.etri.slice.tools.adl.project", IStatus.OK, message, null);
		throw new CoreException(status);
	}
}
