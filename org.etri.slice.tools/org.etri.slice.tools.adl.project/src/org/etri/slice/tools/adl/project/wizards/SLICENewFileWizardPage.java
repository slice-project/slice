package org.etri.slice.tools.adl.project.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

public class SLICENewFileWizardPage extends WizardPage {
	private Text containerText;

	private Text fileText;

	private Text domainText;
	
	private ISelection selection;

	private Button checkButton;

	public SLICENewFileWizardPage(ISelection selection) {
		super("Create a ADL File");
		setTitle("New ADL File");
		setDescription("This wizard creates a new adl file with *.adl extension that can be opened by a adl editor.");
		this.selection = selection;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		Label label = new Label(container, SWT.NULL);
		label.setText("&Container:");

		containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		containerText.setLayoutData(gd);
		containerText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button button = new Button(container, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		
		// File Name
		label = new Label(container, SWT.NULL);
		label.setText("&ADL file name:");

		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		fileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
				
		// dummy
		label = new Label(container, SWT.NULL);
				
		// Domain
		label = new Label(container, SWT.NULL);
		label.setText("&Domain:");

		domainText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		domainText.setLayoutData(gd);
		domainText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
				
		// create sample adl
		label = new Label(container, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
				
		label.setLayoutData(gd);
		label.setText("Would you like to create sample adl?");
		
		// dummy
		label = new Label(container, SWT.NULL);
				
		checkButton = new Button(container, SWT.CHECK);
		checkButton.setText("generate sample adl");
		checkButton.setSelection(false);
		
		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {	
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) 
		{
			System.out.println("selection IStructuredSelection =======> " + selection);
			
			IStructuredSelection ssel = (IStructuredSelection) selection;
			
			if (ssel.size() > 1)
				return;
		
			
			Object obj = ssel.getFirstElement();
			System.out.println("selection first = " + obj.getClass().getName());
			
			if (obj instanceof IResource) {
				IContainer container;
			
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				
				containerText.setText(container.getFullPath().toString());
			}
			else if(obj instanceof JavaProject) {
				JavaProject project = (JavaProject)obj;
				containerText.setText(project.getPath().toString());

				System.out.println("project dir = " + project.getPath().toString());
			}
			else if(obj instanceof PackageFragmentRoot) {
				PackageFragmentRoot root = (PackageFragmentRoot) obj;
				
				System.out.println("root dir = " + root.getPath().toString());
				containerText.setText(root.getPath().toString());
			}
			else if(obj instanceof PackageFragment) {
				PackageFragment pkg = (PackageFragment) obj;
				
				System.out.println("package dir = " + pkg.getPath().toString());
				containerText.setText(pkg.getPath().toString());
			}
			
		}
		
		fileText.setText("new_file");
		domainText.setText("my_domain");
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new adl file container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getContainerName()));
		String fileName = getFileName();

		if (getContainerName().length() == 0) {
			updateStatus("File container must be specified");
			return;
		}
		if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus("File container must exist");
			return;
		}
		if (!container.isAccessible()) {
			updateStatus("Project must be writable");
			return;
		}
		if (fileName.length() == 0) {
			updateStatus("File name must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("File name must be valid");
			return;
		}
		
		if (fileName.length() == 0) {
			updateStatus("Domain must be specified");
			return;
		}
		
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return containerText.getText();
	}

	public String getFileName() {
		return fileText.getText();
	}

	public String getDomain() {
		return domainText.getText();
	}
	
	public boolean isCreateSample() {
		return checkButton.getSelection();
	}
}