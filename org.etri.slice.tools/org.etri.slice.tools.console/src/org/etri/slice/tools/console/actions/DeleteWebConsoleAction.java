package org.etri.slice.tools.console.actions;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.statushandlers.StatusManager;
import org.etri.slice.tools.console.Activator;
import org.etri.slice.tools.console.model.ConsoleWebInfo;
import org.etri.slice.tools.console.views.SLICEConsoleView;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class DeleteWebConsoleAction extends Action  implements ISelectionListener{

	IWorkbenchWindow window;
	List<ConsoleWebInfo> input;
	TableViewer viewer;
	
	@SuppressWarnings("unused")
	private DeleteWebConsoleAction()
	{
		
	}
	
	public DeleteWebConsoleAction(IWorkbenchWindow window, TableViewer viewer, List<ConsoleWebInfo> input)
	{
		this.window = window;
		this.window.getSelectionService().addPostSelectionListener(this);
		this.input = input;
		this.viewer = viewer;
	}
	
	@Override
	public void run() {
		ConsoleWebInfo selected = (ConsoleWebInfo) ((IStructuredSelection) window.getSelectionService().getSelection())
				.getFirstElement();
		
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		Preferences webConsoleUrlsNode = preferences.node(SLICEConsoleView.PREF_WEB_CONSOLE_URLS_NODE);
		
		try {
			if(webConsoleUrlsNode.nodeExists(selected.getName()))
			{
				webConsoleUrlsNode.node(selected.getName()).removeNode();
				webConsoleUrlsNode.flush();
			}		
		} catch (BackingStoreException e) {
			Status status= new Status(IStatus.ERROR, Activator.PLUGIN_ID, 
					"A preferences operation could not complete because of a failure in the backing store, or a failure to contact the backing store.");
			StatusManager.getManager().handle(status, StatusManager.LOG);
		}
		
		
		
		input.remove(selected);
		viewer.setInput(input);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(selection instanceof IStructuredSelection)
		{
			IStructuredSelection structuredSelection = (IStructuredSelection)selection;
			
			setEnabled(structuredSelection.size() == 1 &&
					structuredSelection.getFirstElement() instanceof ConsoleWebInfo);
		}
		else
			setEnabled(false);
	}
}
