package org.etri.slice.tools.jmxconsole.model;

import java.util.Arrays;
import java.util.List;

import javax.management.ObjectName;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JMXDomain {
	private String domain;
	private Object[] children;
	
	private JMXDomain() {}
	
	public JMXDomain(String domain)
	{
		this.domain = domain;
		this.children = null;
	}

	@Override
	public int hashCode() {
		return domain.hashCode();
	}
}
