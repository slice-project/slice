package org.etri.slice.tools.console.views;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.internal.browser.WebBrowserPreference;
import org.eclipse.ui.internal.browser.WorkbenchBrowserSupport;
import org.eclipse.ui.part.ViewPart;
import org.etri.slice.tools.console.Activator;
import org.etri.slice.tools.console.actions.AddWebConsoleAction;
import org.etri.slice.tools.console.actions.DeleteWebConsoleAction;
import org.etri.slice.tools.console.actions.UpdateWebConsoleAction;
import org.etri.slice.tools.console.model.ConsoleWebInfo;
import org.etri.slice.tools.console.provider.AbstractTableLabelProvider;
import org.etri.slice.tools.console.provider.TableContentProvider;
import org.etri.slice.tools.console.utils.ImageUtil;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class SLICEConsoleView extends ViewPart {

	public static final String PREF_WEB_CONSOLE_URLS_NODE = "WebConsoleURLS";
	public static final String PREF_WEB_CONSOLE_NAME = "NAME";
	public static final String PREF_WEB_CONSOLE_URL = "URL";
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.etri.slice.tools.console.views.SLICEConsoleView";

	private TableViewer viewer;
	private Action addWebConsoleURLAction;
	private Action updateWebConsoleURLAction;
	private Action deleteWebConsoleURLAction;
	
	private Action doubleClickAction;
	 
	List<ConsoleWebInfo> input = new ArrayList<ConsoleWebInfo>();
	
	class ViewLabelProvider extends AbstractTableLabelProvider<ConsoleWebInfo> {
		@Override
		protected Image columnImage(ConsoleWebInfo element, int columnIndex) {
			return getImage(element);
		}

		@Override
		protected String columnText(ConsoleWebInfo element, int columnIndex) {
			ConsoleWebInfo consoleWebInfo = (ConsoleWebInfo) element;
			
			switch(columnIndex)
			{
				case 0:	return consoleWebInfo.getName();
				case 1:	return consoleWebInfo.getUrl();
			}
			
			return "-";
		}	
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getTable().setHeaderVisible(true);
		viewer.setContentProvider(new TableContentProvider());
		
		Table table = viewer.getTable();		
		TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setText("Name");
		tableColumn.setWidth(300);
		tableColumn.setResizable(true);
		tableColumn.setMoveable(false);
		
		tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setText("URL");
		tableColumn.setWidth(1280);
		tableColumn.setResizable(true);
		tableColumn.setMoveable(false);
		
		viewer.setLabelProvider(new ViewLabelProvider());
		
		/*input = new ArrayList<ConsoleWebInfo>();
		input.add(new ConsoleWebInfo("Carseat111111", "http://localhost:8080/system/console/bundles"));
		input.add(new ConsoleWebInfo("object Detector", "http://www.google.com"));
		*/
		
		// Create the help context id for the viewer's control
//		workbench.getHelpSystem().setHelp(viewer.getControl(), "org.etri.slice.tools.console.viewer");
		getSite().setSelectionProvider(viewer);
		makeActions(parent.getShell());
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		
		loadPreferences();
	}

	private void loadPreferences()
	{
		System.out.println("loadPreferences ...................");
		
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		Preferences webConsoleUrlsNode = preferences.node(PREF_WEB_CONSOLE_URLS_NODE);
		
		try {
			for(String name : webConsoleUrlsNode.childrenNames())
			{
				Preferences url = webConsoleUrlsNode.node(name);
				input.add(new ConsoleWebInfo(name, url.get(PREF_WEB_CONSOLE_URL, "")));
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		
		viewer.setInput(input);
	}
	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SLICEConsoleView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(addWebConsoleURLAction);
		manager.add(new Separator());
		manager.add(updateWebConsoleURLAction);
		manager.add(deleteWebConsoleURLAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(addWebConsoleURLAction);
		manager.add(updateWebConsoleURLAction);
		manager.add(deleteWebConsoleURLAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addWebConsoleURLAction);
		manager.add(updateWebConsoleURLAction);
		manager.add(deleteWebConsoleURLAction);
	}

	private void makeActions(Shell shell) {
		addWebConsoleURLAction = new AddWebConsoleAction(getViewSite().getWorkbenchWindow(), viewer, input);
		addWebConsoleURLAction.setText("Add Console URL");
		addWebConsoleURLAction.setToolTipText("Add New Felix Web Console URL");
		addWebConsoleURLAction.setImageDescriptor(ImageUtil.getImageDescriptor("icons/add_url.png"));
		
		updateWebConsoleURLAction = new UpdateWebConsoleAction(getViewSite().getWorkbenchWindow(), viewer, input);
		updateWebConsoleURLAction.setText("Update Console URL");
		updateWebConsoleURLAction.setToolTipText("Update Selected Felix Web Console URL");
		updateWebConsoleURLAction.setImageDescriptor(ImageUtil.getImageDescriptor("icons/edit.png"));
		
		deleteWebConsoleURLAction = new DeleteWebConsoleAction(getViewSite().getWorkbenchWindow(), viewer, input);
		deleteWebConsoleURLAction.setText("Delete Console URL");
		deleteWebConsoleURLAction.setToolTipText("Delete Selected Felix Web Console URL");
		deleteWebConsoleURLAction.setImageDescriptor(ImageUtil.getImageDescriptor("icons/delete.gif"));
		
		doubleClickAction = new Action() {
			public void run() {
				IStructuredSelection selection = viewer.getStructuredSelection();
				
				if(!selection.isEmpty())
				{
					ConsoleWebInfo obj = (ConsoleWebInfo)selection.getFirstElement();
					openUrl(obj.getUrl(), obj.getName(), WebBrowserPreference.INTERNAL);
				}
			}
		};
	}

	public static void openUrl(String location, String title, int browserChoice) {
		try {
			URL url = null;
			if (location != null) {
				url = new URL(location);
			}
			if (browserChoice == WebBrowserPreference.EXTERNAL) {
				try {
					IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
					support.getExternalBrowser().openURL(url);
				} catch (Exception e) {
				}
			} else {
				IWebBrowser browser;
				int flags;
				if (WorkbenchBrowserSupport.getInstance().isInternalWebBrowserAvailable()) {
					flags = IWorkbenchBrowserSupport.AS_EDITOR | IWorkbenchBrowserSupport.LOCATION_BAR
							| IWorkbenchBrowserSupport.NAVIGATION_BAR;
				} else {
					flags = IWorkbenchBrowserSupport.AS_EXTERNAL | IWorkbenchBrowserSupport.LOCATION_BAR
							| IWorkbenchBrowserSupport.NAVIGATION_BAR;
				}
				String generatedId = "org.eclipse.mylyn.web.browser-" + Calendar.getInstance().getTimeInMillis();
				browser = WorkbenchBrowserSupport.getInstance().createBrowser(flags, generatedId, title, null);
				browser.openURL(url);
			}
		} catch (PartInitException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Failed to Open Browser",
					"Browser could not be initiated");
		} catch (MalformedURLException e) {
			if (location == null || location.trim().equals("")) {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Failed to Open Browser",
						"No URL to open." + location);
			} else {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Failed to Open Browser",
						"Could not open URL: " + location);
			}
		}
	}
	
	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"SLICE Console View",
			message);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
