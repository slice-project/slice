package org.etri.slice.tools.jmxconsole.jmx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;
import org.etri.slice.tools.jmxconsole.Activator;

public class JMXClient {

	private String url;
	private JMXServiceURL jmxServiceURL;
	private MBeanServerConnection mBeanServerConnection;
	
	private JMXClient() {}
	
	public JMXClient(String url) {
		this.url = url;

		if (null == this.url || 0 == this.url.trim().length()) {
			Status status= new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Null or Invalid JMX URL.");
			StatusManager.getManager().handle(status, StatusManager.LOG);
			return;
		}
		
		connect();
	}

	private void connect() {
		if (null == this.url || 0 == this.url.trim().length())
			return;

		// rmi를 이용하여 JVM의 Platform MBean Server에 연결합니다.
		try {
			jmxServiceURL = new JMXServiceURL(this.url);

			JMXConnector jmxc = JMXConnectorFactory.connect(jmxServiceURL, null);
			mBeanServerConnection = jmxc.getMBeanServerConnection();
		} catch (MalformedURLException e) {
			Status status= new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Malformed JMX URL.");
			StatusManager.getManager().handle(status, StatusManager.LOG);
		} catch (IOException e) {
			Status status= new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Cannot Connect To JMX Service.");
			StatusManager.getManager().handle(status, StatusManager.LOG);
		}
	}
	
	/**
	 * Object Name이 "org.etri.slice"로 시작하고  type이 "job"이 아닌  MBean 들을 출력한다.  
	 * 
	 */
	public List<ObjectName> getSLICEMBeans() {
				
		if (null == mBeanServerConnection) {
			return null;
		}
		
		try {
			List<ObjectName> ret = new ArrayList<ObjectName>();
			
			ObjectName namePattern = new ObjectName("org.etri.slice.*:*");
						
			for (ObjectName objectName : mBeanServerConnection.queryNames(namePattern, null)) {				
				if(!objectName.getKeyProperty("type").equalsIgnoreCase("\"job\""))
					ret.add(objectName);
			}
			
			return ret;
			
		} catch (IOException e) {
			Status status= new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Cannot Connect To JMX Service.");
			StatusManager.getManager().handle(status, StatusManager.LOG);
		} catch (MalformedObjectNameException e) {
			Status status= new Status(IStatus.ERROR, Activator.PLUGIN_ID, "MalformedObjectName Exception: " + e.getMessage());
			StatusManager.getManager().handle(status, StatusManager.LOG);
		}
		
		return null;
	}
		
	public MBeanAttributeInfo[] getAttributes(ObjectName objectName)
	{
		if (null == mBeanServerConnection) {
			return null;
		}
		
		try {
			MBeanInfo mbeanInfo = mBeanServerConnection.getMBeanInfo(objectName);
			return mbeanInfo.getAttributes();
		} catch (InstanceNotFoundException e) {
			Status status= new Status(IStatus.ERROR, Activator.PLUGIN_ID, 
					"Object instance for " + objectName + " is not found.");
			StatusManager.getManager().handle(status, StatusManager.LOG);
		} catch (IntrospectionException e) {
			Status status= new Status(IStatus.ERROR, Activator.PLUGIN_ID, 
					"An exception occurred during the introspection of " + objectName + " MBean");
			StatusManager.getManager().handle(status, StatusManager.LOG);
		} catch (ReflectionException e) {
			Status status= new Status(IStatus.ERROR, Activator.PLUGIN_ID, 
					"exceptions thrown in the MBean server when using the java.lang.reflect classes to invoke methods on " + objectName);
			StatusManager.getManager().handle(status, StatusManager.LOG);
		} catch (IOException e) {
			Status status= new Status(IStatus.ERROR, Activator.PLUGIN_ID, 
					"Signals that an I/O exception of some sort has occurred. "
					+ "This class is the general class of exceptions produced by failed or interrupted I/O operations on " + objectName);
			StatusManager.getManager().handle(status, StatusManager.LOG);
		}
		
		return null;
	}
		
	public Object readAttribute(ObjectName objectName, String name)
	{
		if (null == mBeanServerConnection) {
			return null;
		}
		
		try {
			Object value = mBeanServerConnection.getAttribute(objectName, name);
						
			// javax.management.openmbean.OpenType
			// ArrayType, CompositeType, SimpleType, TabularType
			if(value instanceof CompositeDataSupport)
			{								
				return mBeanServerConnection.getAttribute(objectName, name);
			}
			else 
				return mBeanServerConnection.getAttribute(objectName, name);
		} catch (AttributeNotFoundException e) {
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		} catch (MBeanException e) {
			e.printStackTrace();
		} catch (ReflectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Write Primitive Type Attribute
	 * @param objectName
	 * @param attributeInfo
	 * @param value
	 * @return
	 */
	public boolean writeAttribute(ObjectName objectName, MBeanAttributeInfo attributeInfo, String value)
	{
		if (null == mBeanServerConnection) {
			return false;
		}
				
		Object convertedValue = null;
		
		switch(attributeInfo.getType())
		{
			case "int":
				convertedValue = Integer.parseInt(value);
				break;
			case "float":
				convertedValue = Float.parseFloat(value);
				break;
			case "double":
				convertedValue = Double.parseDouble(value);
				break;
			default:
				convertedValue = value;
				break;
		}
		
		Attribute attribute = new Attribute(attributeInfo.getName(), convertedValue);
		try {
			mBeanServerConnection.setAttribute(objectName, attribute);
			
			return true;
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		} catch (AttributeNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidAttributeValueException e) {
			e.printStackTrace();
		} catch (MBeanException e) {
			e.printStackTrace();
		} catch (ReflectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
