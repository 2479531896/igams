package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="DispatchListModel")
public class DispatchListModel {
	//主键id
	private String DLID;
	//发货单号
	private String cDLCode;
	//业务类型
	private String cVouchType;
	//销售类型代码
	private String cSTCode;
	//发货日期
	private String dDate;
	//销售部门
	private String cDepCode;
	//订单号
	private String cSOCode;
	//客户代码
	private String cCusCode;
	//收获地址
	private String cShipAddress;
	//币种
	private String cexch_name;
	//汇率
	private String iExchRate;
	//税率
	private String iTaxRate;
	//备注
	private String cMemo;
	//关联销售出库单
	private String cSaleOut;
	//审核人
	private String cVerifier;
	//制单人
	private String cMaker;
	//客户名称
	private String cCusName;
	//模板
	private String iVTid;
	//销售类型
	private String cBusType;
	//审核时间
	private String dverifydate;
	//创建时间
	private String dcreatesystime;
	//审核时间
	private String dverifysystime;
	//||SA01|+发货单号
	private String cSysBarCode;
	//客户代码
	private String cinvoicecompany;
	private String iNetLock;
	//退货标记
	private String bReturnFlag;
	//
	private String bneedbill;
	//
	private String cDefine7;

	public String getcDefine7() {
		return cDefine7;
	}

	public void setcDefine7(String cDefine7) {
		this.cDefine7 = cDefine7;
	}

	public String getBneedbill() {
		return bneedbill;
	}

	public void setBneedbill(String bneedbill) {
		this.bneedbill = bneedbill;
	}

	public String getbReturnFlag() {
		return bReturnFlag;
	}

	public void setbReturnFlag(String bReturnFlag) {
		this.bReturnFlag = bReturnFlag;
	}

	public String getiNetLock() {
		return iNetLock;
	}

	public void setiNetLock(String iNetLock) {
		this.iNetLock = iNetLock;
	}

	public String getDLID() {
		return DLID;
	}
	public void setDLID(String dLID) {
		DLID = dLID;
	}
	public String getcDLCode() {
		return cDLCode;
	}
	public void setcDLCode(String cDLCode) {
		this.cDLCode = cDLCode;
	}
	public String getcVouchType() {
		return cVouchType;
	}
	public void setcVouchType(String cVouchType) {
		this.cVouchType = cVouchType;
	}
	public String getcSTCode() {
		return cSTCode;
	}
	public void setcSTCode(String cSTCode) {
		this.cSTCode = cSTCode;
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
	public String getcSOCode() {
		return cSOCode;
	}
	public void setcSOCode(String cSOCode) {
		this.cSOCode = cSOCode;
	}
	public String getcCusCode() {
		return cCusCode;
	}
	public void setcCusCode(String cCusCode) {
		this.cCusCode = cCusCode;
	}
	public String getcShipAddress() {
		return cShipAddress;
	}
	public void setcShipAddress(String cShipAddress) {
		this.cShipAddress = cShipAddress;
	}
	public String getCexch_name() {
		return cexch_name;
	}
	public void setCexch_name(String cexch_name) {
		this.cexch_name = cexch_name;
	}
	public String getiExchRate() {
		return iExchRate;
	}
	public void setiExchRate(String iExchRate) {
		this.iExchRate = iExchRate;
	}
	public String getiTaxRate() {
		return iTaxRate;
	}
	public void setiTaxRate(String iTaxRate) {
		this.iTaxRate = iTaxRate;
	}
	public String getcMemo() {
		return cMemo;
	}
	public void setcMemo(String cMemo) {
		this.cMemo = cMemo;
	}
	public String getcSaleOut() {
		return cSaleOut;
	}
	public void setcSaleOut(String cSaleOut) {
		this.cSaleOut = cSaleOut;
	}
	public String getcVerifier() {
		return cVerifier;
	}
	public void setcVerifier(String cVerifier) {
		this.cVerifier = cVerifier;
	}
	public String getcMaker() {
		return cMaker;
	}
	public void setcMaker(String cMaker) {
		this.cMaker = cMaker;
	}
	public String getcCusName() {
		return cCusName;
	}
	public void setcCusName(String cCusName) {
		this.cCusName = cCusName;
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
	public String getDverifydate() {
		return dverifydate;
	}
	public void setDverifydate(String dverifydate) {
		this.dverifydate = dverifydate;
	}
	public String getDcreatesystime() {
		return dcreatesystime;
	}
	public void setDcreatesystime(String dcreatesystime) {
		this.dcreatesystime = dcreatesystime;
	}
	public String getDverifysystime() {
		return dverifysystime;
	}
	public void setDverifysystime(String dverifysystime) {
		this.dverifysystime = dverifysystime;
	}
	public String getcSysBarCode() {
		return cSysBarCode;
	}
	public void setcSysBarCode(String cSysBarCode) {
		this.cSysBarCode = cSysBarCode;
	}
	public String getCinvoicecompany() {
		return cinvoicecompany;
	}
	public void setCinvoicecompany(String cinvoicecompany) {
		this.cinvoicecompany = cinvoicecompany;
	}
	
	
}
