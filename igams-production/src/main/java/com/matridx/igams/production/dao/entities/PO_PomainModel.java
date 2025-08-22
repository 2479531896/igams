package com.matridx.igams.production.dao.entities;

public class PO_PomainModel {
	//单据号
	private String cPOID;
	//现在没有关联
	private String dPODate;
	//客商ID
	private String cVenCode;
	//部门ID
	private String cDepCode;
	//单据类型？
	private String cPTCode;
	//币种
	private String cexch_name;
	//1
	private String nflat;
	//税率
	private String iTaxRate;
	//0
	private String iCost;
	//0
	private String iBargain;
	//备注 以前用请购单号，用/连接，现在不考虑
	private String cMemo;
	//1
	private String cState;
	//制单人
	private String cMaker;
	//审核人
	private String cVerifier;
	//自定义 记录编号
	private String cDefine1;
	//关联明细字段
	private int POID;
	//8173
	private String iVTid;
	//普通采购
	private String cBusType;
	//0
	private String iDiscountTaxType;
	//2
	private String iverifystateex;
	//0
	private String IsWfControlled;
	//制单时间
	private String cmaketime;
	//审核时间
	private String cAuditTime;
	//审核日期
	private String cAuditDate;
	//0
	private String iflowid;
	//0
	private String iPrintCount;
	//记录编号
	private String cappcode;
	//和请购单关联
	private String csysbarcode;
	public String getcPOID() {
		return cPOID;
	}
	public void setcPOID(String cPOID) {
		this.cPOID = cPOID;
	}
	public String getdPODate() {
		return dPODate;
	}
	public void setdPODate(String dPODate) {
		this.dPODate = dPODate;
	}
	public String getcVenCode() {
		return cVenCode;
	}
	public void setcVenCode(String cVenCode) {
		this.cVenCode = cVenCode;
	}
	public String getcDepCode() {
		return cDepCode;
	}
	public void setcDepCode(String cDepCode) {
		this.cDepCode = cDepCode;
	}
	public String getcPTCode() {
		return cPTCode;
	}
	public void setcPTCode(String cPTCode) {
		this.cPTCode = cPTCode;
	}
	public String getCexch_name() {
		return cexch_name;
	}
	public void setCexch_name(String cexch_name) {
		this.cexch_name = cexch_name;
	}
	public String getNflat() {
		return nflat;
	}
	public void setNflat(String nflat) {
		this.nflat = nflat;
	}
	public String getiTaxRate() {
		return iTaxRate;
	}
	public void setiTaxRate(String iTaxRate) {
		this.iTaxRate = iTaxRate;
	}
	public String getiCost() {
		return iCost;
	}
	public void setiCost(String iCost) {
		this.iCost = iCost;
	}
	public String getiBargain() {
		return iBargain;
	}
	public void setiBargain(String iBargain) {
		this.iBargain = iBargain;
	}
	public String getcMemo() {
		return cMemo;
	}
	public void setcMemo(String cMemo) {
		this.cMemo = cMemo;
	}
	public String getcState() {
		return cState;
	}
	public void setcState(String cState) {
		this.cState = cState;
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

	public int getPOID() {
		return POID;
	}
	public void setPOID(int pOID) {
		POID = pOID;
	}
	public String getiVTid() {
		return iVTid;
	}
	public void setiVTid(String iVTid) {
		this.iVTid = iVTid;
	}
	public String getcBusType() {
		return cBusType;
	}
	public void setcBusType(String cBusType) {
		this.cBusType = cBusType;
	}
	public String getiDiscountTaxType() {
		return iDiscountTaxType;
	}
	public void setiDiscountTaxType(String iDiscountTaxType) {
		this.iDiscountTaxType = iDiscountTaxType;
	}
	public String getIverifystateex() {
		return iverifystateex;
	}
	public void setIverifystateex(String iverifystateex) {
		this.iverifystateex = iverifystateex;
	}
	public String getIsWfControlled() {
		return IsWfControlled;
	}
	public void setIsWfControlled(String isWfControlled) {
		IsWfControlled = isWfControlled;
	}
	public String getCmaketime() {
		return cmaketime;
	}
	public void setCmaketime(String cmaketime) {
		this.cmaketime = cmaketime;
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
	public String getIflowid() {
		return iflowid;
	}
	public void setIflowid(String iflowid) {
		this.iflowid = iflowid;
	}
	public String getiPrintCount() {
		return iPrintCount;
	}
	public void setiPrintCount(String iPrintCount) {
		this.iPrintCount = iPrintCount;
	}
	public String getCappcode() {
		return cappcode;
	}
	public void setCappcode(String cappcode) {
		this.cappcode = cappcode;
	}
	public String getCsysbarcode() {
		return csysbarcode;
	}
	public void setCsysbarcode(String csysbarcode) {
		this.csysbarcode = csysbarcode;
	}
	
}
