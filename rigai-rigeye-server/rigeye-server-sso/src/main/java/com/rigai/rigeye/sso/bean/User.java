package com.rigai.rigeye.sso.bean;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 32746783L;
	
	private String userId;
	private String userName;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = (userId == null ? null : userId.trim());
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = (userName == null ? null : userName.trim());
	}

	
}
