package com.matridx.igams.production.dao.entities;

public class TransVouchModel {
	
	//单据号
	private String cTVCode;
	//日期
	private String dTVDate;
	//转出仓库
	private String cOWhCode;
	//转入仓库
	private String cIWhCode;
	//转出部门
	private String cODepCode;
	//转入部门
	private String cIDepCode;
	//经手人
	private String cPersonCode;
	//入库类别
	private String cIRdCode;
	//出库类别
	private String cORdCode;
	//备注
	private String cTVMemo;
	//录入人员
	private String cMaker;
	//ID
	private String ID;
	//iNetLock
	private String iNetLock;
	//cVerifyPerson
	private String cVerifyPerson;
	//csysbarcode
	private String csysbarcode;
	
	
	public String getCsysbarcode() {
		return csysbarcode;
	}
	public void setCsysbarcode(String csysbarcode) {
		this.csysbarcode = csysbarcode;
	}
	public String getcVerifyPerson() {
		return cVerifyPerson;
	}
	public void setcVerifyPerson(String cVerifyPerson) {
		this.cVerifyPerson = cVerifyPerson;
	}
	public String getiNetLock() {
		return iNetLock;
	}
	public void setiNetLock(String iNetLock) {
		this.iNetLock = iNetLock;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getcMaker() {
		return cMaker;
	}
	public void setcMaker(String cMater) {
		this.cMaker = cMater;
	}
	public String getcTVMemo() {
		return cTVMemo;
	}
	public void setcTVMemo(String cTVMemo) {
		this.cTVMemo = cTVMemo;
	}
	public String getcTVCode() {
		return cTVCode;
	}
	public void setcTVCode(String cTVCode) {
		this.cTVCode = cTVCode;
	}
	public String getdTVDate() {
		return dTVDate;
	}
	public void setdTVDate(String dTVDate) {
		this.dTVDate = dTVDate;
	}
	public String getcOWhCode() {
		return cOWhCode;
	}
	public void setcOWhCode(String cOWhCode) {
		this.cOWhCode = cOWhCode;
	}
	public String getcIWhCode() {
		return cIWhCode;
	}
	public void setcIWhCode(String cIWhCode) {
		this.cIWhCode = cIWhCode;
	}
	public String getcODepCode() {
		return cODepCode;
	}
	public void setcODepCode(String cODepCode) {
		this.cODepCode = cODepCode;
	}
	public String getcPersonCode() {
		return cPersonCode;
	}
	public void setcPersonCode(String cPersonCode) {
		this.cPersonCode = cPersonCode;
	}
	public String getcIRdCode() {
		return cIRdCode;
	}
	public void setcIRdCode(String cIRdCode) {
		this.cIRdCode = cIRdCode;
	}
	public String getcORdCode() {
		return cORdCode;
	}
	public void setcORdCode(String cORdCode) {
		this.cORdCode = cORdCode;
	}
	public String getcIDepCode() {
		return cIDepCode;
	}
	public void setcIDepCode(String cIDepCode) {
		this.cIDepCode = cIDepCode;
	}
	
	
}
