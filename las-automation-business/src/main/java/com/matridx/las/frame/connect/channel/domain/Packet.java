package com.matridx.las.frame.connect.channel.domain;

public abstract class Packet {
	
	private String info;
	/**
	 * 获取协议指令
	 * @return 返回指令值
	 */
	public abstract Byte getCommand();

	/**
	 * 设置信息
	 * @param info
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	
	/**
	 * 获取信息
	 * @param info
	 * @return 
	 */
	public String getInfo() {
		return info;
	}
}
