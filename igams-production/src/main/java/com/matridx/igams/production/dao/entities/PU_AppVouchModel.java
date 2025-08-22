package com.matridx.igams.production.dao.entities;

public class PU_AppVouchModel {
	
	//项目大类
	private String cItem_class;
	//项目编码
	private String cItemCode;
	//项目名称
	private String CItemName;
	//采购ID
	private String ID;
	//单据号
	private String cCode;
	//申请日期
	private String dDate;
	//申请部门(采用机构代码)
	private String cDepCode;
	//采购类型
	private String cBusType="普通采购";
	//申请人员
	private String cMaker;
	//确认人员/审核人员
	private String cVerifier;
	//记录编号
	private String cDefine1;
	//创建时间
	private String cMakeTime;
	//修改时间
	private String cModifyTime;
	//审核时间
	private String cAuditTime;
	//审核日期
	private String cAuditDate;
	//修改日期
	private String cModifyDate;
	//修改审核时间
	private String cChangAuditTime;
	//修改审核日期
	private String cChangAuditDate;
	//
	private String iVTid="8171";
	//
	private String cPTCod="01";
	//
	private String csysbarcode;
	//备注
	private String cMemo;

	public String getcMemo() {
		return cMemo;
	}
	public void setcMemo(String cMemo) {
		this.cMemo = cMemo;
	}
	public String getCsysbarcode() {
		return csysbarcode;
	}
	public void setCsysbarcode(String csysbarcode) {
		this.csysbarcode = csysbarcode;
	}
	public String getcPTCod() {
		return cPTCod;
	}
	public void setcPTCod(String cPTCod) {
		this.cPTCod = cPTCod;
	}
	public String getiVTid() {
		return iVTid;
	}
	public void setiVTid(String iVTid) {
		this.iVTid = iVTid;
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
	public String getCItemName() {
		return CItemName;
	}
	public void setCItemName(String cItemName) {
		CItemName = cItemName;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getcCode() {
		return cCode;
	}
	public void setcCode(String cCode) {
		this.cCode = cCode;
	}
	public String getdDate() {
		return dDate;
	}
	public void setdDate(String dDate) {
		this.dDate = dDate;
	}
	public String getcDepCode() {
		return cDepCode;
	}
	public void setcDepCode(String cDepCode) {
		this.cDepCode = cDepCode;
	}
	public String getcBusType() {
		return cBusType;
	}
	public void setcBusType(String cBusType) {
		this.cBusType = cBusType;
	}
	public String getcMaker() {
		return cMaker;
	}
	public void setcMaker(String cMaker) {
		this.cMaker = cMaker;
	}
	public String getcVerifier() {
		return cVerifier;
	}
	public void setcVerifier(String cVerifier) {
		this.cVerifier = cVerifier;
	}
	public String getcDefine1() {
		return cDefine1;
	}
	public void setcDefine1(String cDefine1) {
		this.cDefine1 = cDefine1;
	}
	public String getcMakeTime() {
		return cMakeTime;
	}
	public void setcMakeTime(String cMakeTime) {
		this.cMakeTime = cMakeTime;
	}
	public String getcModifyTime() {
		return cModifyTime;
	}
	public void setcModifyTime(String cModifyTime) {
		this.cModifyTime = cModifyTime;
	}
	public String getcAuditTime() {
		return cAuditTime;
	}
	public void setcAuditTime(String cAuditTime) {
		this.cAuditTime = cAuditTime;
	}
	public String getcAuditDate() {
		return cAuditDate;
	}
	public void setcAuditDate(String cAuditDate) {
		this.cAuditDate = cAuditDate;
	}
	public String getcModifyDate() {
		return cModifyDate;
	}
	public void setcModifyDate(String cModifyDate) {
		this.cModifyDate = cModifyDate;
	}
	public String getcChangAuditTime() {
		return cChangAuditTime;
	}
	public void setcChangAuditTime(String cChangAuditTime) {
		this.cChangAuditTime = cChangAuditTime;
	}
	public String getcChangAuditDate() {
		return cChangAuditDate;
	}
	public void setcChangAuditDate(String cChangAuditDate) {
		this.cChangAuditDate = cChangAuditDate;
	}
	
}
