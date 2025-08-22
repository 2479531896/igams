package com.matridx.igams.production.dao.entities;

public class TransVouchsModel {
	
	//调拨单号
	private String cTVCode;
	//物料编码
	private String cInvCode;
	//数量
	private String iTVQuantity;
	//批号
	private String cTVBatch;
	//失效日期
	private String dDisDate;
	//序号
	private String irowno;
	//cbsysbarcode
	private String cbsysbarcode;
	//ID
	private String ID;
	//autoID
	private int autoID;
	//调入库位
	private String cinposcode;
	//调出库位
	private String coutposcode;
	//保质期
	private String iMassDate;
	//dMadeDate
	private String dMadeDate;
	//保质期标记
	private String cMassUnit;
	//单价
	private String iTVACost;
	
	public String getiTVACost() {
		return iTVACost;
	}
	public void setiTVACost(String iTVACost) {
		this.iTVACost = iTVACost;
	}
	public String getcMassUnit() {
		return cMassUnit;
	}
	public void setcMassUnit(String cMassUnit) {
		this.cMassUnit = cMassUnit;
	}
	public String getdMadeDate() {
		return dMadeDate;
	}
	public void setdMadeDate(String dMadeDate) {
		this.dMadeDate = dMadeDate;
	}
	public String getiMassDate() {
		return iMassDate;
	}
	public void setiMassDate(String iMassDate) {
		this.iMassDate = iMassDate;
	}
	public String getCinposcode() {
		return cinposcode;
	}
	public void setCinposcode(String cinposcode) {
		this.cinposcode = cinposcode;
	}
	public String getCoutposcode() {
		return coutposcode;
	}
	public void setCoutposcode(String coutposcode) {
		this.coutposcode = coutposcode;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public int getAutoID() {
		return autoID;
	}
	public void setAutoID(int autoID) {
		this.autoID = autoID;
	}
	public String getIrowno() {
		return irowno;
	}
	public void setIrowno(String irowno) {
		this.irowno = irowno;
	}
	public String getCbsysbarcode() {
		return cbsysbarcode;
	}
	public void setCbsysbarcode(String cbsysbarcode) {
		this.cbsysbarcode = cbsysbarcode;
	}
	public String getcTVCode() {
		return cTVCode;
	}
	public void setcTVCode(String cTVCode) {
		this.cTVCode = cTVCode;
	}
	public String getcInvCode() {
		return cInvCode;
	}
	public void setcInvCode(String cInvCode) {
		this.cInvCode = cInvCode;
	}
	public String getiTVQuantity() {
		return iTVQuantity;
	}
	public void setiTVQuantity(String iTVQuantity) {
		this.iTVQuantity = iTVQuantity;
	}
	public String getcTVBatch() {
		return cTVBatch;
	}
	public void setcTVBatch(String cTVBatch) {
		this.cTVBatch = cTVBatch;
	}
	public String getdDisDate() {
		return dDisDate;
	}
	public void setdDisDate(String dDisDate) {
		this.dDisDate = dDisDate;
	}
	
}
