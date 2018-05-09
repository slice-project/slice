package org.etri.slice.tools.console.model;

public class ConsoleWebInfo {
	private String name;
	private String url;
	
	public ConsoleWebInfo(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "ConsoleWebInfo [name=" + name + ", url=" + url + "]";
	}
}
