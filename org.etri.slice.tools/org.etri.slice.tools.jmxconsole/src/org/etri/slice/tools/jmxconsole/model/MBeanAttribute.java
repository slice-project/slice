package org.etri.slice.tools.jmxconsole.model;

import javax.management.MBeanAttributeInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MBeanAttribute {
	private MBeanObject mbean;
	private MBeanAttributeInfo attribute;
}
