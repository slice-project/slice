package org.etri.slice.tools.jmxconsole.views;

import java.util.ArrayList;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.etri.slice.tools.jmxconsole.Activator;
import org.etri.slice.tools.jmxconsole.actions.AddJMXConsoleAction;
import org.etri.slice.tools.jmxconsole.actions.DeleteJMXConsoleAction;
import org.etri.slice.tools.jmxconsole.actions.UpdateJMXConsoleAction;
import org.etri.slice.tools.jmxconsole.editors.JMXEditorInput;
import org.etri.slice.tools.jmxconsole.editors.SLICEJMXEditor;
import org.etri.slice.tools.jmxconsole.model.JMXConnectionInfo;
import org.etri.slice.tools.jmxconsole.swt.provider.AbstractTableLabelProvider;
import org.etri.slice.tools.jmxconsole.swt.provider.TableContentProvider;
import org.etri.slice.tools.jmxconsole.utils.ImageUtil;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class SLICEJMXConsoleView extends ViewPart {
	public static final String PREF_JMX_CONSOLE_URLS_NODE = "JMXConsoleURLS";
	public static final String PREF_JMX_CONSOLE_NAME = "NAME";
	public static final String PREF_JMX_SERVER_IP = "IP";
	public static final String PREF_JMX_SERVER_PORT = "PORT";
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.etri.slice.tools.jmxconsole.views.SLICEJMXConsoleView";

	private TableViewer viewer;
	private Action addJMXConsoleURLAction;
	private Action updateJMXConsoleURLAction;
	private Action deleteJMXConsoleURLAction;
	
	private Action doubleClickAction;
	 
	List<JMXConnectionInfo> input = new ArrayList<JMXConnectionInfo>();
	
	class ViewLabelProvider extends AbstractTableLabelProvider<JMXConnectionInfo> {
		@Override
		protected Image columnImage(JMXConnectionInfo element, int columnIndex) {
			return getImage(element);
		}

		@Override
		protected String columnText(JMXConnectionInfo element, int columnIndex) {
			JMXConnectionInfo consoleWebInfo = (JMXConnectionInfo) element;
			
			switch(columnIndex)
			{
				case 0:	return consoleWebInfo.getName();
				case 1:	return consoleWebInfo.getURL();
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
		
		getSite().setSelectionProvider(viewer);
		makeActions(parent.getShell());
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		
		loadPreferences();
	}

	private void loadPreferences()
	{
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		Preferences webConsoleUrlsNode = preferences.node(PREF_JMX_CONSOLE_URLS_NODE);
		
		try {
			for(String name : webConsoleUrlsNode.childrenNames())
			{
				Preferences url = webConsoleUrlsNode.node(name);
				
				String ip = url.get(PREF_JMX_SERVER_IP, "");
				int port = url.getInt(PREF_JMX_SERVER_PORT, -1);
				
				// if invalid data or old data, skip
				if(0 == ip.length() || -1 == port )
				{
					continue;
				}
				
				input.add(new JMXConnectionInfo(name, ip, port));
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
				SLICEJMXConsoleView.this.fillContextMenu(manager);
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
		manager.add(addJMXConsoleURLAction);
		manager.add(new Separator());
		manager.add(updateJMXConsoleURLAction);
		manager.add(deleteJMXConsoleURLAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(addJMXConsoleURLAction);
		manager.add(updateJMXConsoleURLAction);
		manager.add(deleteJMXConsoleURLAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addJMXConsoleURLAction);
		manager.add(updateJMXConsoleURLAction);
		manager.add(deleteJMXConsoleURLAction);
	}

	private void makeActions(Shell shell) {
		addJMXConsoleURLAction = new AddJMXConsoleAction(getViewSite().getWorkbenchWindow(), viewer, input);
		addJMXConsoleURLAction.setText("Add JMX URL");
		addJMXConsoleURLAction.setToolTipText("Add New JMX URL");
		addJMXConsoleURLAction.setImageDescriptor(ImageUtil.getImageDescriptor("icons/add_url.png"));
		
		updateJMXConsoleURLAction = new UpdateJMXConsoleAction(getViewSite().getWorkbenchWindow(), viewer, input);
		updateJMXConsoleURLAction.setText("Update JMX URL");
		updateJMXConsoleURLAction.setToolTipText("Update Selected JMX URL");
		updateJMXConsoleURLAction.setImageDescriptor(ImageUtil.getImageDescriptor("icons/edit.png"));
		
		deleteJMXConsoleURLAction = new DeleteJMXConsoleAction(getViewSite().getWorkbenchWindow(), viewer, input);
		deleteJMXConsoleURLAction.setText("Delete JMX URL");
		deleteJMXConsoleURLAction.setToolTipText("Delete Selected JMX URL");
		deleteJMXConsoleURLAction.setImageDescriptor(ImageUtil.getImageDescriptor("icons/delete.gif"));
		
		doubleClickAction = new Action() {
			public void run() {
				IStructuredSelection selection = viewer.getStructuredSelection();
				
				if(!selection.isEmpty())
				{
					// open jmx editor					
					JMXConnectionInfo selected = (JMXConnectionInfo) selection.getFirstElement();					
					JMXEditorInput input = new JMXEditorInput(new JMXConnectionInfo(selected.getName(), selected.getIp(), selected.getPort()));
					
					try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, SLICEJMXEditor.ID);
					} catch (PartInitException e) {
						e.printStackTrace();
					}
					
				}
			}
		};
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
			"SLICE JMX Console View",
			message);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
