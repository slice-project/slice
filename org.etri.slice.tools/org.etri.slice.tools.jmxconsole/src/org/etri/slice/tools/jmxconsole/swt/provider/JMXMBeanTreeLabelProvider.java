package org.etri.slice.tools.jmxconsole.swt.provider;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.etri.slice.tools.jmxconsole.model.JMXDomain;
import org.etri.slice.tools.jmxconsole.model.MBeanAttribute;
import org.etri.slice.tools.jmxconsole.model.MBeanObject;
import org.etri.slice.tools.jmxconsole.utils.ImageUtil;

public class JMXMBeanTreeLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		if(element instanceof JMXDomain)
			return ImageUtil.getImage("icons/domain.gif");
		else if(element instanceof MBeanObject)
			return ImageUtil.getImage("icons/mbean.png");
		else
			return ImageUtil.getImage("icons/attribute.png");
			
	}
	
	@Override
	public String getText(Object element) {
		if(element instanceof JMXDomain)
			return ((JMXDomain) element).getDomain();
		else if(element instanceof MBeanObject)
			return ((MBeanObject) element).getObjectName().getKeyProperty("interface");
		else if(element instanceof MBeanAttribute)
			return ((MBeanAttribute) element).getAttribute().getName();
		return null;
	}
}