package com.matridx.igams.common.dao.entities;

public class MQQueueModel {
	private String exchageName;
	private String exchageType;
	private String queueName;
	public String getExchageName() {
		return exchageName;
	}
	public void setExchageName(String exchageName) {
		this.exchageName = exchageName;
	}
	public String getExchageType() {
		return exchageType;
	}
	public void setExchageType(String exchageType) {
		this.exchageType = exchageType;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
}
