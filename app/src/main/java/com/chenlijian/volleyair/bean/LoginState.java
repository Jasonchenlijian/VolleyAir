package com.chenlijian.volleyair.bean;


/**
 * 如果你的app中需要用到用户登陆
 */

public class LoginState {
	
	private int _id;
	private String token;
	private String overTime;
	private UserInfo user;
	
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getOverTime() {
		return overTime;
	}
	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	
}
