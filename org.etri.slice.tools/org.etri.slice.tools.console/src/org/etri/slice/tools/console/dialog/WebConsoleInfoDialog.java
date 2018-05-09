package org.etri.slice.tools.console.dialog;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.etri.slice.tools.console.Activator;
import org.etri.slice.tools.console.model.ConsoleWebInfo;
import org.etri.slice.tools.console.views.SLICEConsoleView;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class WebConsoleInfoDialog extends Dialog{

	ConsoleWebInfo consoleWebInfo = null;
	
	String name;
	String url;
	
	Text nameText;
	Text urlText;
	
	public WebConsoleInfoDialog(Shell parentShell) {
		super(parentShell);
	}
	 
	public WebConsoleInfoDialog(Shell parentShell, ConsoleWebInfo consoleWebInfo) {
		super(parentShell);
		
		this.consoleWebInfo = consoleWebInfo;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		return super.createButtonBar(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		initLayout(container);
		
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText("Name : ");
		
		nameText = new Text(container, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Label urlLabel = new Label(container, SWT.NONE);
		urlLabel.setText("URL : ");
		
		urlText = new Text(container, SWT.BORDER);
		urlText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		// 변경 시 
		if(null != consoleWebInfo)
		{
			nameText.setText(consoleWebInfo.getName());
			urlText.setText(consoleWebInfo.getUrl());
			
			nameText.setEditable(false);
			urlText.setFocus();
		}
		
		return container;
	}

	private void initLayout(Composite container) {
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 15;
		gridLayout.marginHeight = 25;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 10;
		container.setLayout(gridLayout);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(610, 250);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Console Web");
	}

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE);
		setBlockOnOpen(false);
	}

	@Override
	protected void okPressed() {
		name = nameText.getText();
		url = urlText.getText();
		
		if(name.trim().length() == 0 || url.trim().length() == 0)
		{
			MessageDialog.openError(getShell(), "Error - Repository name or URL required", 
					"The repository name or URL input fields are empty.\nPlease enter the repository name and URL.");
			return;
		}
		
		// 업데이트가 아니고 기존에 존재하는 URL 이름 인지를 체크한다.
		if(null == consoleWebInfo && isExist())
		{
			MessageDialog.openError(getShell(), "Error - Duplicated Names", 
					"The web console name already exists.\nPlease enter another name.");
			return;
		}
			
		super.okPressed();
	}

	private boolean isExist()
	{
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		Preferences webConsoleUrlsNode = preferences.node(SLICEConsoleView.PREF_WEB_CONSOLE_URLS_NODE);
		
		try {
			return webConsoleUrlsNode.nodeExists(name);
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public ConsoleWebInfo getConsoleWebInfo() {
		if(null == consoleWebInfo)
		{
			return new ConsoleWebInfo(name, url);
		}
		else
		{
			consoleWebInfo.setName(name);
			consoleWebInfo.setUrl(url);
			
			
			return consoleWebInfo;
		}
	}
}
