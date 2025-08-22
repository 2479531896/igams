package com.matridx.igams.production.dao.entities;

public class MaterialAppVouchsModel {
	//主键id
	private String AutoID;
	//主表id
	private String ID;
	//物料编码
	private String cInvCode;
	//实领数量
	private String iQuantity;
	//失效日期
	private String dVDate;
	//项目大类代码
	private String cItem_class;
	//项目编码代码
	private String cItemCode;
	//项目编码名称
	private String cName;
	//项目大类名称
	private String cItemCName;
	//生产日期
	private String dMadeDate;
	//保质期
	private String iMassDate;
	//保质期标记
	private String cMassUnit;
	//仓库编码
	private String cWhCode;
	//备注
	private String cbMemo;
	//拼接
	private String cbsysbarcode;
	//生产批号
	private String cBatch;
	//出库数量
	private String fOutQuantity;
	//排序字段
	private String irowno;
	
	
	public String getIrowno() {
		return irowno;
	}
	public void setIrowno(String irowno) {
		this.irowno = irowno;
	}
	public String getfOutQuantity() {
		return fOutQuantity;
	}
	public void setfOutQuantity(String fOutQuantity) {
		this.fOutQuantity = fOutQuantity;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getcBatch() {
		return cBatch;
	}
	public void setcBatch(String cBatch) {
		this.cBatch = cBatch;
	}
	public String getAutoID() {
		return AutoID;
	}
	public void setAutoID(String autoID) {
		AutoID = autoID;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getcInvCode() {
		return cInvCode;
	}
	public void setcInvCode(String cInvCode) {
		this.cInvCode = cInvCode;
	}
	public String getiQuantity() {
		return iQuantity;
	}
	public void setiQuantity(String iQuantity) {
		this.iQuantity = iQuantity;
	}
	public String getdVDate() {
		return dVDate;
	}
	public void setdVDate(String dVDate) {
		this.dVDate = dVDate;
	}
	public String getcItem_class() {
		return cItem_class;
	}
	public void setcItem_class(String cItem_class) {
		this.cItem_class = cItem_class;
	}
	public String getcItemCode() {
		return cItemCode;
	}
	public void setcItemCode(String cItemCode) {
		this.cItemCode = cItemCode;
	}
	public String getcItemCName() {
		return cItemCName;
	}
	public void setcItemCName(String cItemCName) {
		this.cItemCName = cItemCName;
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
	public String getcMassUnit() {
		return cMassUnit;
	}
	public void setcMassUnit(String cMassUnit) {
		this.cMassUnit = cMassUnit;
	}
	public String getcWhCode() {
		return cWhCode;
	}
	public void setcWhCode(String cWhCode) {
		this.cWhCode = cWhCode;
	}
	public String getCbMemo() {
		return cbMemo;
	}
	public void setCbMemo(String cbMemo) {
		this.cbMemo = cbMemo;
	}
	public String getCbsysbarcode() {
		return cbsysbarcode;
	}
	public void setCbsysbarcode(String cbsysbarcode) {
		this.cbsysbarcode = cbsysbarcode;
	}
	
	
}
