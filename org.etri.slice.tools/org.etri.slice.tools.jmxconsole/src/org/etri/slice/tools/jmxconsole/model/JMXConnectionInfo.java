package org.etri.slice.tools.jmxconsole.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JMXConnectionInfo {

	/**
	 * JMX Connection url format
	 * 
	 * ex) service:jmx:rmi:///jndi/rmi://localhost:3403/jmxrmi
	 */
	public static final String URL_FORMAT = "service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi";

	/**
	 * JMX URL 관리를 위한 이름
	 */
	private String name;

	/**
	 * ip address
	 */
	private String ip;
	
	/**
	 * port
	 */
	private int port;
	
	/**
	 * MX Connection URL
	 */
	public String getURL()
	{
		return String.format(URL_FORMAT, ip, port);
	}
}
