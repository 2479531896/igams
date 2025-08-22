package com.matridx.springboot.common.speech.dao.entities;

public class AudioModel {
	//token
	private String token;
	//有效时间
	private long expireTime;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	
}
