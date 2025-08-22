package com.matridx.springboot.util.encrypt;

import java.util.List;

public class SFDTO {
	//语言设定
	private String language;
	//查询号类别（顺丰运单号查询为1，客户订单号查询为2）
	private String trackingType;
	//查询号
	private List<String> trackingNumber; 
	//路由查询类别（标准路由查询为1，定制路由查询为2）
	private String methodType;
	//电话号码后四位
	private String checkPhoneNo;
	
	public String getCheckPhoneNo() {
		return checkPhoneNo;
	}
	public void setCheckPhoneNo(String checkPhoneNo) {
		this.checkPhoneNo = checkPhoneNo;
	}
	public List<String> getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(List<String> trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getTrackingType() {
		return trackingType;
	}
	public void setTrackingType(String trackingType) {
		this.trackingType = trackingType;
	}
	public String getMethodType() {
		return methodType;
	}
	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
    
}
