package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="MxtzglDto")
public class MxtzglDto extends MxtzglModel {
	//镁信ID
	private String mxid;

	private String yhid;

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getMxid() {
		return mxid;
	}

	public void setMxid(String mxid) {
		this.mxid = mxid;
	}
	//钉钉id
	private String ddid;

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	private String projectCode;
	private String orgName;
	private String orgId;
	private String comboCode;
	private String mxOrderNo;
	private String phoneNo;
	private String amount;
	private String quantity;
	private String totalAmount;
	private String equityName;
	private String payTime;
	private String orderNo;
	private String idName;
	private String idNum;
	private String idType;
	private String orderTime;
	private String status;
	private String hasReport;
	private String reportUrl;
	private String statusTime;
	// 全部(查询条件)
	private String entire;

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getComboCode() {
		return comboCode;
	}

	public void setComboCode(String comboCode) {
		this.comboCode = comboCode;
	}

	public String getMxOrderNo() {
		return mxOrderNo;
	}

	public void setMxOrderNo(String mxOrderNo) {
		this.mxOrderNo = mxOrderNo;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getEquityName() {
		return equityName;
	}

	public void setEquityName(String equityName) {
		this.equityName = equityName;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHasReport() {
		return hasReport;
	}

	public void setHasReport(String hasReport) {
		this.hasReport = hasReport;
	}

	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}

	public String getStatusTime() {
		return statusTime;
	}

	public void setStatusTime(String statusTime) {
		this.statusTime = statusTime;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
