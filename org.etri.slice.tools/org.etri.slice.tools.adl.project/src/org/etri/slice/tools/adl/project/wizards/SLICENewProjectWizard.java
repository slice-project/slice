/**
 * 
 */
package org.etri.slice.tools.adl.project.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.ResolverConfiguration;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.xtext.ui.XtextProjectHelper;

/**
 * @author Administrator
 *
 */
public class SLICENewProjectWizard extends NewElementWizard implements IExecutableExtension {

	private final static String TITLE = "New SLICE Project";
		
	private SLICENewProjectWizardPageOne fFirstPage;
	private SLICENewProjectWizardPageTwo fSecondPage;

	private IConfigurationElement fConfigElement;
	
	public SLICENewProjectWizard() {
		this(null, null);
	}

	public SLICENewProjectWizard(SLICENewProjectWizardPageOne pageOne, SLICENewProjectWizardPageTwo pageTwo) {
		setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWJPRJ);
		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle(TITLE);

		fFirstPage= pageOne;
		fSecondPage= pageTwo;
	}

	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {
		if (fFirstPage == null)
			fFirstPage = new SLICENewProjectWizardPageOne();
		addPage(fFirstPage);

		if (fSecondPage == null)
			fSecondPage = new SLICENewProjectWizardPageTwo(fFirstPage);
		addPage(fSecondPage);
		
		fFirstPage.init(getSelection(), getActivePart());
	}

	private IWorkbenchPart getActivePart() {
		IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWindow != null) {
			IWorkbenchPage activePage = activeWindow.getActivePage();
			if (activePage != null) {
				return activePage.getActivePart();
			}
		}
		return null;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

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

	private String getADLFileName(String domain)
	{
		return domain.length() == 0 ? "sample.adl" : domain + ".adl";
	}
	
	private IFile getADLFile(IContainer container, String domain)
	{
		return container.getFile(new Path(getADLFileName(domain)));
	}
	
	private boolean createADLFileContents(IProject project, String domain) {
		IContainer container = (IContainer) project;
		
		String adlFileName = getADLFileName(domain);
		
		final IFile adlFile = container.getFile(new Path(adlFileName));

		try {
			InputStream stream = openADLContentStream(domain);
			if (adlFile.exists()) {
				adlFile.setContents(stream, true, true, new NullProgressMonitor());
			} else {
				adlFile.create(stream, true, new NullProgressMonitor());
			}
			stream.close();
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean performFinish() {
		String domain = fFirstPage.getDomain();
		
		boolean res = super.performFinish();

		if (res) {
			final IJavaProject newElement = (IJavaProject) getCreatedElement();

			if (!addXtextNature(newElement.getProject()))
				return false;

			if (!addMavenNature(newElement.getProject()))
				return false;
			
			if(!createADLFileContents(newElement.getProject(), domain))
				return false;
			
			IWorkingSet[] workingSets = fFirstPage.getWorkingSets();
			
			if (workingSets.length > 0) {
				PlatformUI.getWorkbench().getWorkingSetManager().addToWorkingSets(newElement, workingSets);
			}

			BasicNewProjectResourceWizard.updatePerspective(fConfigElement);

			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					IWorkbenchPart activePart = getActivePart();
					if (activePart instanceof IPackagesViewPart) {
						PackageExplorerPart view = PackageExplorerPart.openInActivePerspective();
						view.tryToReveal(newElement);
					}
					
					IWorkbenchPage page =
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						try {
							IDE.openEditor(page, getADLFile(newElement.getProject(), domain), true);
						} catch (PartInitException e) {
						}
				}
			});
		}
		
		return res;
	}

	/**
	 * We will initialize adl file contents.
	 */
	private InputStream openADLContentStream(String domain) {

		String contents = null;

		if(domain.trim().length() > 0)
			contents = MessageFormat.format(ADLSampleContstants.SIMPLE_ADL_CONTENTS_WITH_DOMAIN, domain);
		else
			contents = ADLSampleContstants.SIMPLE_ADL_CONTENTS;
		
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
		IStatus status = new Status(IStatus.ERROR, "org.etri.slice.tools.adl.project", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		fConfigElement = config;
	}

	@Override
	protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		fSecondPage.performFinish(monitor); // use the full progress monitor
	}

	@Override
	public boolean performCancel() {
		fSecondPage.performCancel();
		return super.performCancel();
	}

	@Override
	public IJavaElement getCreatedElement() {
		return fSecondPage.getJavaProject();
	}
}
