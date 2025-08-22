package com.matridx.igams.production.dao.entities;

public class MaterialAppVouchModel {
	//主键id
	private String ID;
	//申请时间
	private String dDate;
	//领料单号
	private String cCode;
	//出库类别编码
	private String cRdCode;
	//部门编码
	private String cDepCode;
	//制单人
	private String cMaker;
	//VT_ID  30718
	private String VT_ID;
	//录入时间
	private String dnmaketime;
	//拼接||st64|+领料单号
	private String csysbarcode;
	//审核人
	private String cHandler;
	private String dVeriDate;
	private String dnverifytime;
	//记录编号
	private String cMemo;
	
	
	public String getcMemo() {
		return cMemo;
	}
	public void setcMemo(String cMemo) {
		this.cMemo = cMemo;
	}
	public String getDnverifytime() {
		return dnverifytime;
	}
	public void setDnverifytime(String dnverifytime) {
		this.dnverifytime = dnverifytime;
	}
	public String getdVeriDate() {
		return dVeriDate;
	}
	public void setdVeriDate(String dVeriDate) {
		this.dVeriDate = dVeriDate;
	}
	public String getcHandler() {
		return cHandler;
	}
	public void setcHandler(String cHandler) {
		this.cHandler = cHandler;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getdDate() {
		return dDate;
	}
	public void setdDate(String dDate) {
		this.dDate = dDate;
	}
	public String getcCode() {
		return cCode;
	}
	public void setcCode(String cCode) {
		this.cCode = cCode;
	}
	public String getcRdCode() {
		return cRdCode;
	}
	public void setcRdCode(String cRdCode) {
		this.cRdCode = cRdCode;
	}
	public String getcDepCode() {
		return cDepCode;
	}
	public void setcDepCode(String cDepCode) {
		this.cDepCode = cDepCode;
	}
	public String getcMaker() {
		return cMaker;
	}
	public void setcMaker(String cMaker) {
		this.cMaker = cMaker;
	}
	public String getVT_ID() {
		return VT_ID;
	}
	public void setVT_ID(String vT_ID) {
		VT_ID = vT_ID;
	}
	public String getDnmaketime() {
		return dnmaketime;
	}
	public void setDnmaketime(String dnmaketime) {
		this.dnmaketime = dnmaketime;
	}
	public String getCsysbarcode() {
		return csysbarcode;
	}
	public void setCsysbarcode(String csysbarcode) {
		this.csysbarcode = csysbarcode;
	}
	
}
