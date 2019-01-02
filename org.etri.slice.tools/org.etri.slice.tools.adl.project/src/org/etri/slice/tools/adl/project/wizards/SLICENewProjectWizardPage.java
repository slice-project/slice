package org.etri.slice.tools.adl.project.wizards;

import org.eclipse.core.internal.resources.OS;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SLICENewProjectWizardPage extends WizardPage {

	private Text projectNameText;
	private Text domainText;
	private Label locationLabel;
	private Text locationText;
	private Button useDefaultLocationCheckBox;
	private Button selectLocationButton;
	
	private String defaultPath;
	private String selectedPath;
	
	public SLICENewProjectWizardPage() {
		super("Create a ADL Project");
		setTitle("New ADL Project ");
		setDescription("This wizard creates a new adl project.");		
	}

	@Override
	public void createControl(Composite parent) {
	
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		
		// Project Name
		Label label = new Label(container, SWT.NULL);
		label.setText("&Project name:");

		projectNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		
		projectNameText.setLayoutData(gd);
		projectNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
				locationText.setText(selectedPath + "\\" + projectNameText.getText());
			}
		});
			
		// Domain Name
		label = new Label(container, SWT.NULL);
		label.setText("&Domain:");

		domainText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		domainText.setLayoutData(gd);
		
		domainText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		useDefaultLocationCheckBox = new Button(container, SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		useDefaultLocationCheckBox.setLayoutData(gd);
		useDefaultLocationCheckBox.setText("Use default location");
		useDefaultLocationCheckBox.setSelection(true);
		useDefaultLocationCheckBox.addMouseListener(new MouseListener() {			
			@Override
			public void mouseUp(MouseEvent e) {			
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				if(useDefaultLocationCheckBox.getSelection())
				{
					locationLabel.setEnabled(true);
					locationText.setEnabled(true);
					selectLocationButton.setEnabled(true);	
				}
				else
				{					
					locationLabel.setEnabled(false);
					locationText.setEnabled(false);
					selectLocationButton.setEnabled(false);
					selectedPath = defaultPath;
				}
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {				
			}
		});
		
		locationLabel = new Label(container, SWT.NULL);
		locationLabel.setText("&Location:");
		locationLabel.setEnabled(false);
		
		locationText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		locationText.setLayoutData(gd);
		locationText.setEnabled(false);
		
		locationText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		selectLocationButton = new Button(container, SWT.PUSH);
		selectLocationButton.setText("Browse...");
		selectLocationButton.setEnabled(false);
		
		selectLocationButton.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("selectLocationButton Selected ");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
	
		});
		
		selectLocationButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog  fd = new DirectoryDialog (getShell(), SWT.OPEN);				
				fd.setFilterPath(locationText.getText());
				String selectedLocation = fd.open();
				
				if(null != selectedLocation)
				{
					selectedPath = selectedLocation;
					locationText.setText(selectedLocation + "/" + projectNameText.getText());
				}
			}
		});
		
		initialize();
		dialogChanged();
		setControl(container);
	}
	

	private void initialize() {	
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		defaultPath = root.getLocation().toOSString();
		selectedPath = defaultPath;
		locationText.setText(defaultPath);
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		if (getProjectName().length() == 0) {
			updateStatus("Project name must be specified");
			return;
		}
		
		if (getProjectName().length() > 0)
		{
			String projectName = getProjectName().trim();
			
		    if(OS.isNameValid(projectName))
		    {
		    	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
						    	
				// First create a simple project of type org.eclipse.core.resources.IProject:
				IProject project = root.getProject(projectName);
				
				if(project.exists())
				{
					updateStatus("Project already exist");
					return;
				}
		    }
		    else
		    {
		    	updateStatus("Invalid projecgt name");
		    }			
		}
		
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	public String getProjectName()
	{
		return projectNameText.getText();
	}
	
	public String getDomain()
	{
		return domainText.getText();
	}	
	
	public String getLocation()
	{
		return locationText.getText();
	}

	public boolean isDefaultLocation() {
		return useDefaultLocationCheckBox.getSelection();
	}	
}
