/**
 * 
 */
package org.etri.slice.tools.jmxconsole.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.etri.slice.tools.jmxconsole.model.JMXConnectionInfo;

/**
 * @author Administrator
 *
 */
public class JMXEditorInput implements IEditorInput {

	private JMXConnectionInfo jmxConnectionInfo;
	
	public JMXEditorInput() {}
	
	public JMXEditorInput(JMXConnectionInfo jmxConnectionInfo)
	{
		this.jmxConnectionInfo = jmxConnectionInfo;
	}
	
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return jmxConnectionInfo.getName();
	}

	public String getURL() {
		return jmxConnectionInfo.getURL();
	}
	
	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return getName();
	}
}
