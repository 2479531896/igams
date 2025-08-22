package com.matridx.igams.common.dao.entities;

import java.io.Serializable;

public class InterfaceModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -48140012502541180L;
	
	//资源代码
	private String resourceid;
	//请求方系统ID
	private String fromsysid;
	//请求数据
	private String data;
	//验证码
	private String sign;
	//key
	private String key;
	
	public String getResourceid() {
		return resourceid;
	}
	public void setResourceid(String resourceid) {
		this.resourceid = resourceid;
	}
	public String getFromsysid() {
		return fromsysid;
	}
	public void setFromsysid(String fromsysid) {
		this.fromsysid = fromsysid;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
