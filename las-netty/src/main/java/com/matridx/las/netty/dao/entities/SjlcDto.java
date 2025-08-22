package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjlcDto")
public class SjlcDto extends SjlcModel {


	private  String bz;
	private  String zt;
	private  String deviceid;
	private  String command;
	//baseCommand得id
	private String id;
	private String threadName;
	public SjlcDto() {
	}
	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
