package org.etri.slice.commons.car;

import java.io.Serializable;

import org.kie.api.definition.type.Role;

@Role(Role.Type.EVENT)
public class UserEntered implements Serializable {
	
	private static final long serialVersionUID = 5119075579871472757L;
	
	private UserInfo userInfo;
	
	public UserEntered() {		
	}
	
	public UserEntered(UserInfo info) {
		this.userInfo = info;
	}
	
	public void setUserInfo(UserInfo info) {
		this.userInfo = info;
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}
}
