package org.etri.slice.tools.adl.project.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class SLICENewFileWizard extends Wizard implements INewWizard {
	
	public static final String SIMPLE_ADL_CONTENTS = "import org.etri.slice.commons.SliceException\n" + 
			"\n";
	
	public static final String SIMPLE_ADL_CONTENTS_WITH_DOMAIN = "import org.etri.slice.commons.SliceException\n" + 
			"\n" + 
			"domain {0}\n" + 
			"'{'\n" + 
			"\n" +
			"\n" + 
			"}";
	
	public static final String SAMPLE_ADL_CONTENTS = 
			"import org.etri.slice.commons.SliceException\n" + 
			"import org.etri.slice.commons.{0}.FullBodyException\n" + 
			"import org.etri.slice.commons.{0}.^context.BodyPartLength\n" + 
			"import org.etri.slice.commons.{0}.^context.ObjectInfo\n" + 
			"import org.etri.slice.commons.{0}.^context.SeatPosture\n" + 
			"import org.etri.slice.commons.{0}.^context.UserInfo\n" + 
			"\n" + 
			"domain {0} '{'\n" + 
			"	\n" + 
			"	context ObjectInfo '{'\n" + 
			"		String objectId;\n" + 
			"		double distance;		\n" + 
			"	} \n" + 
			"	 \n" + 
			"	@topic(\"object_detected\")\n" + 
			"	event ObjectDetected '{'\n" + 
			"		ObjectInfo info;\n" + 
			"	}\n" + 
			"	\n" + 
			"	@agency(ip=\"127.0.0.1\", port=1883)	\n" + 
			"	agent ObjectDetector '{'\n" + 
			"	   hasRuleSet objectdetector_rules '{'\n" + 
			"	      group-id \"org.etri.slice\"\n" + 
			"	      artifact-id \"objectdetector\"\n" + 
			"	   }\n" + 
			"	\n" + 
			"	   hasBehaviors '{'\n" + 
			"	      behavior \"Object Detection Notification\"\n" + 
			"	         on ObjectInfo\n" + 
			"	         do publish ObjectDetected \n" + 
			"	      end \n" + 
			"	   }\n" + 
			"	}	\n" + 
			"	\n" + 
			"	context BodyPartLength '{'\n" + 
			"		double head;\n" + 
			"		double torso;\n" + 
			"		double arms;\n" + 
			"		double legs;\n" + 
			"		double height;\n" + 
			"	}	\n" + 
			"	\n" + 
			"    @topic(\"full_body_detected\")\n" + 
			"    event FullBodyDetected '{'\n" + 
			"   		BodyPartLength bodyLength;\n" + 
			"    }\n" + 
			"\n" + 
			"	control Startable '{'\n" + 
			"		op void start() throws SliceException;\n" + 
			"		op void stop();\n" + 
			"	}\n" + 
			"	\n" + 
			"	exception FullBodyException;\n" + 
			"	\n" + 
			"	control FullBodyDetector extends Startable '{'\n" + 
			"		op void detect(double distance) throws FullBodyException;\n" + 
			"	}\n" + 
			"	\n" + 
			"	@agency(ip=\"127.0.0.1\", port=1883)	\n" + 
			"	agent FullBodyDetector '{'\n" + 
			"	   hasRuleSet fullbodydetector_rules '{'\n" + 
			"	      group-id \"org.etri.slice\"\n" + 
			"	      artifact-id \"fullbodydetector\" \n" + 
			"	   }\n" + 
			"	\n" + 
			"	   hasBehaviors '{'\n" + 
			"	      behavior \"Initiate full-body Detection\"\n" + 
			"	         on ObjectDetected\n" + 
			"	         do call FullBodyDetector.detect \n" + 
			"	      end\n" + 
			"	\n" + 
			"	      behavior \"FullBody Detection Notificiation\"\n" + 
			"	         on BodyPartLength\n" + 
			"	         do publish FullBodyDetected\n" + 
			"	      end \n" + 
			"	   }\n" + 
			"	}\n" + 
			"\n" + 
			"	context Pressure '{'\n" + 
			"		double value;\n" + 
			"	}\n" + 
			"\n" + 
			"	context UserInfo '{'\n" + 
			"		String userId;\n" + 
			"		BodyPartLength bodyLength;\n" + 
			"	}\n" + 
			"\n" + 
			"    @topic(\"user_seated\")\n" + 
			"    event UserSeated '{'\n" + 
			"   		UserInfo info;\n" + 
			"    }\n" + 
			"    \n" + 
			"	@agency(ip=\"127.0.0.1\", port=1883)	\n" + 
			"	agent PressureSensor '{'\n" + 
			"	   hasRuleSet pressuresensor_rules '{'\n" + 
			"	      group-id \"org.etri.slice\"\n" + 
			"	      artifact-id \"pressuresensor\"\n" + 
			"	   }\n" + 
			"	\n" + 
			"	   hasBehaviors '{'\n" + 
			"	      behavior \"UserSeated Notification\"\n" + 
			"	         on FullBodyDetected, Pressure\n" + 
			"	         do publish UserSeated\n" + 
			"	      end\n" + 
			"	   }\n" + 
			"	}\n" + 
			"	\n" + 
			"    context SeatPosture '{'\n" + 
			"    		double height;\n" + 
			"      	double position;\n" + 
			"      	double tilt;\n" + 
			"    }\n" + 
			"\n" + 
			"    @topic(\"seat_posture_changed\")\n" + 
			"    event SeatPostureChanged '{'\n" + 
			"      	SeatPosture posture;\n" + 
			"    }\n" + 
			"	\n" + 
			"	control SeatControl '{'\n" + 
			"		double height;\n" + 
			"		double position;\n" + 
			"		double tilt;	\n" + 
			"		SeatPosture posture;		\n" + 
			"	}\n" + 
			"	\n" + 
			"	@agency(ip=\"127.0.0.1\", port=1883)	\n" + 
			"	agent CarSeat '{'\n" + 
			"		hasRuleSet carseat_rules '{'\n" + 
			"			group-id \"org.etri.slice\"\n" + 
			"			artifact-id \"carseat\"\n" + 
			"		}\n" + 
			"		\n" + 
			"	    hasBehaviors '{'\n" + 
			"	      	behavior \"Insert BodyPartLength\"\n" + 
			"	         	on UserSeated\n" + 
			"	         	do no-op\n" + 
			"	     	 end\n" + 
			"	     	 behavior \"SeatPosture Change Notification\"\n" + 
			"	        		 on SeatPosture\n" + 
			"	        		 do publish SeatPostureChanged\n" + 
			"	     	 end\n" + 
			"	    }\n" + 
			"		\n" + 
			"		hasCommandsOf SeatControl '{'\n" + 
			"			command carseat_height '{'\n" + 
			"				context BodyPartLength.height\n" + 
			"				action SeatControl.setHeight\n" + 
			"			}\n" + 
			"			\n" + 
			"			command carseat_position '{'\n" + 
			"				context BodyPartLength.height\n" + 
			"				action SeatControl.setPosition\n" + 
			"			}\n" + 
			"			\n" + 
			"			command carseat_tilt '{'\n" + 
			"				context BodyPartLength.height\n" + 
			"				action SeatControl.setTilt\n" + 
			"			}\n" + 
			"		}\n" + 
			"	}\n" + 
			"}";
	
	private SLICENewFileWizardPage page;
	
	private ISelection selection;
	
	/**
	 * Constructor for SLICENewFileWizard.
	 */
	public SLICENewFileWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {
		page = new SLICENewFileWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		final String containerName = page.getContainerName();
		final String fileName = page.getFileName();
		final String domain = page.getDomain();
		final boolean isCreateSample = page.isCreateSample();
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerName, fileName, domain, isCreateSample, monitor);
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
		String containerName,
		String fileName,
		String domain,
		boolean isCreateSample,
		IProgressMonitor monitor)
		throws CoreException {
		
		String fileNameWithExt = fileName + ".adl";
		
		// create a sample file
		monitor.beginTask("Creating " + fileNameWithExt, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileNameWithExt));
		
		try {
			InputStream stream = openContentStream(domain, isCreateSample);
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
	private InputStream openContentStream(String domain, boolean isCreateSample) {
		
		String contents = null;
		
		if(isCreateSample)
			contents = MessageFormat.format(SAMPLE_ADL_CONTENTS, domain);
		else
			contents = MessageFormat.format(SIMPLE_ADL_CONTENTS, domain);
		
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "org.etri.slice.tools.adl.project", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}