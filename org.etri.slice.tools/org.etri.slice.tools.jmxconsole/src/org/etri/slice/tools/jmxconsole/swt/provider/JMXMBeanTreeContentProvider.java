package org.etri.slice.tools.jmxconsole.swt.provider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.etri.slice.tools.jmxconsole.model.JMXDomain;
import org.etri.slice.tools.jmxconsole.model.JMXTreeInput;
import org.etri.slice.tools.jmxconsole.model.MBeanAttribute;
import org.etri.slice.tools.jmxconsole.model.MBeanObject;

public class JMXMBeanTreeContentProvider implements ITreeContentProvider{

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof JMXTreeInput)
			return ((JMXTreeInput) parentElement).getChildren();
		else if(parentElement instanceof JMXDomain)
			return ((JMXDomain) parentElement).getChildren();
		else if(parentElement instanceof MBeanObject)
			return ((MBeanObject) parentElement).getAttributes();
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if(element instanceof MBeanObject)
			return ((MBeanObject) element).getDomain();
		else if(element instanceof MBeanAttribute)
			return ((MBeanAttribute) element).getMbean();
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return element instanceof JMXDomain || element instanceof MBeanObject;
	}
}
