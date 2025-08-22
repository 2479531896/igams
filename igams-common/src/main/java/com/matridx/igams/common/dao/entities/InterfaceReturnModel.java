package com.matridx.igams.common.dao.entities;

import java.io.Serializable;

public class InterfaceReturnModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 755063525689049631L;
	//状态
	private String status;
	//消息
	private String msg;
	//请求数据
	private String data;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
