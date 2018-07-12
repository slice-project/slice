package org.etri.slice.tools.console.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


/**
 * Felix Web Console Management Information Model
 * 
 * @author Administrator
 *
 */
@Data
@AllArgsConstructor
public class ConsoleWebInfo {
	
	/**
	 * Felix web console url format
	 * 
	 * ex) http://localhost:8080/system/console/bundles
	 */
	public static final String URL_FORMAT = "http://%s:%d/system/console/bundles";
	
	/**
	 * Felix Web Console ¿Ã∏ß
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
	 * Felix Web Console URL
	 */
	public String getURL()
	{
		return String.format(URL_FORMAT, ip, port);
	}
}
