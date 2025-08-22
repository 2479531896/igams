package com.matridx.las.netty.channel.domain;

public abstract class Packet {

	/**
	 * 获取协议指令
	 * @return 返回指令值
	 */
	public abstract Byte getCommand();

	/**
	 * 设置信息
	 * @param info
	 */
	public abstract void setInfo(String info);
	
	/**
	 * 获取信息
	 * @param info
	 * @return 
	 */
	public abstract String getInfo();
}
