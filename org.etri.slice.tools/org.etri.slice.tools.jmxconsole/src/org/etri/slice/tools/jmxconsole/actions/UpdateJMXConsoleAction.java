package org.etri.slice.tools.jmxconsole.actions;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.statushandlers.StatusManager;
import org.etri.slice.tools.jmxconsole.Activator;
import org.etri.slice.tools.jmxconsole.model.JMXConnectionInfo;
import org.etri.slice.tools.jmxconsole.swt.dialog.JMXConsoleInfoDialog;
import org.etri.slice.tools.jmxconsole.views.SLICEJMXConsoleView;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class UpdateJMXConsoleAction extends Action  implements ISelectionListener{

	IWorkbenchWindow window;
	List<JMXConnectionInfo> input;
	TableViewer viewer;
	@SuppressWarnings("unused")
	private UpdateJMXConsoleAction()
	{
		
	}
	
	public UpdateJMXConsoleAction(IWorkbenchWindow window, TableViewer viewer, List<JMXConnectionInfo> input)
	{
		this.window = window;
		this.window.getSelectionService().addPostSelectionListener(this);
		this.input = input;
		this.viewer = viewer;
	}
	
	@Override
	public void run() {
		JMXConnectionInfo selected = (JMXConnectionInfo) ((IStructuredSelection) window.getSelectionService().getSelection())
				.getFirstElement();
		
		JMXConsoleInfoDialog dialog = new JMXConsoleInfoDialog(window.getShell(),
				selected);
		int ret = dialog.open();
		
		if(ret == Dialog.CANCEL)
			return;
		
		dialog.getSLICEJMXEditorInput();
		
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		Preferences webConsoleUrlsNode = preferences.node(SLICEJMXConsoleView.PREF_JMX_CONSOLE_URLS_NODE);
		
		Preferences consoleURLNode = webConsoleUrlsNode.node(selected.getName());
		consoleURLNode.put(SLICEJMXConsoleView.PREF_JMX_CONSOLE_NAME, selected.getName());
		consoleURLNode.put(SLICEJMXConsoleView.PREF_JMX_SERVER_IP, selected.getIp());
		consoleURLNode.putInt(SLICEJMXConsoleView.PREF_JMX_SERVER_PORT, selected.getPort());
		
		try {
			webConsoleUrlsNode.flush();
		} catch (BackingStoreException e) {
			Status status= new Status(IStatus.ERROR, Activator.PLUGIN_ID, 
					"A preferences operation could not complete because of a failure in the backing store, or a failure to contact the backing store.");
			StatusManager.getManager().handle(status, StatusManager.LOG);
		}
		
		viewer.setInput(input);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(selection instanceof IStructuredSelection)
		{
			IStructuredSelection structuredSelection = (IStructuredSelection)selection;
			
			setEnabled(structuredSelection.size() == 1 &&
					structuredSelection.getFirstElement() instanceof JMXConnectionInfo);
		}
		else
			setEnabled(false);
	}
}
