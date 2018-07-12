package org.etri.slice.tools.jmxconsole.model;

import javax.management.MBeanAttributeInfo;
import javax.management.ObjectName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MBeanObject {
	private JMXDomain domain;
	private ObjectName objectName;
	private MBeanAttribute[] attributes;
	
	public MBeanObject(JMXDomain domain, ObjectName objectName)
	{
		this.domain = domain;
		this.objectName = objectName;
	}
	
	@Override
	public int hashCode() {
		return objectName.hashCode();
	}
}
