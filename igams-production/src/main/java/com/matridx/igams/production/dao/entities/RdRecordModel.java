package com.matridx.igams.production.dao.entities;

public class RdRecordModel {
	private String bomfirst;
	private String cSourceLs;
	//入库ID
	private int ID;
	//业务类型  普通采购
	private String cBusType;
	private String bredvouch;
	private String bCredit;
	//部门代码
	private String cDepCode;
	//物料编码
	private String cInvCode;
	//数量
	private String iQuantity;
	//生产批号
	private String cBatch;
	//库位
	private String cPosition;
	//AutoID
	private String AutoID;
	//供应商代码
	private String cVenCode;
	//订单编号
	private String cOrderCode;
	//制单人
	private String cHandler;
	//存货期初记账：记账人
	private String cAccounter;
	//期初记账 1
	private String bpufirst;
	//币种
	private String cExch_Name;
	//单据日期
	private String dDate;
	//单据号
	private String cCode;
	//备注
	private String cMemo;
	//类型
	private String cSource;
	//对应申请单号
	private String cBusCode;
	//仓库代码
	private String cWhCode;
	//出库类型代码
	private String cRdCode;
	//制单人
	private String cMaker;
	//审核日期
	private String dveriDate;
	
	private String dnmaketime;
	private String dnverifytime;
	private String csysbarcode;

	private String bRdFlag;
	private String cVouchType;
	private String cPTCode;
	private String cDefine1;
	private String dVeriDate;
	private String biafirst;
	private String VT_ID;
	private String bIsSTQc;
	private String ipurorderid;
	private String ufts;
	private String iTaxRate;
	private String iExchRate;
	private String bFromPreYear;
	private String cModifyPerson;
	
	private String dModifyDate;
	private String dnmodifytime;
	private String cCheckSignFlag;
	private String cGCRouteCode;
	//订单号
	private String cMPoCode;
	
	private String bOMFirst;
	private String bmotran;
	private String bHYVouch;
	
	//销售类型
	private String cStCode;
	//客户代码
	private String cCUsCode;
	//发货单号
	private String cDLCode;
	//收获地址
	private String cShipAddress;
	//客户代码
	private String cinvoicecompany;
	private String iMQuantity;
	private String cPsPcode;
	private String iproorderid;
	//业务员
	private String cPersonCode;
	private String cDefine7;

	public String getcDefine7() {
		return cDefine7;
	}

	public void setcDefine7(String cDefine7) {
		this.cDefine7 = cDefine7;
	}

	public String getcPersonCode() {
		return cPersonCode;
	}

	public void setcPersonCode(String cPersonCode) {
		this.cPersonCode = cPersonCode;
	}

	public String getIproorderid() {
		return iproorderid;
	}

	public void setIproorderid(String iproorderid) {
		this.iproorderid = iproorderid;
	}

	public String getcPsPcode() {
		return cPsPcode;
	}

	public void setcPsPcode(String cPsPcode) {
		this.cPsPcode = cPsPcode;
	}

	public String getbCredit() {
		return bCredit;
	}

	public void setbCredit(String bCredit) {
		this.bCredit = bCredit;
	}

	public String getBredvouch() {
		return bredvouch;
	}

	public void setBredvouch(String bredvouch) {
		this.bredvouch = bredvouch;
	}

	public String getBomfirst() {
		return bomfirst;
	}

	public void setBomfirst(String bomfirst) {
		this.bomfirst = bomfirst;
	}

	public String getcSourceLs() {
		return cSourceLs;
	}

	public void setcSourceLs(String cSourceLs) {
		this.cSourceLs = cSourceLs;
	}

	public String getiMQuantity() {
		return iMQuantity;
	}

	public void setiMQuantity(String iMQuantity) {
		this.iMQuantity = iMQuantity;
	}

	public String getcPosition() {
		return cPosition;
	}

	public void setcPosition(String cPosition) {
		this.cPosition = cPosition;
	}

	public String getAutoID() {
		return AutoID;
	}

	public void setAutoID(String autoID) {
		AutoID = autoID;
	}

	public String getcBatch() {
		return cBatch;
	}

	public void setcBatch(String cBatch) {
		this.cBatch = cBatch;
	}

	public String getiQuantity() {
		return iQuantity;
	}

	public void setiQuantity(String iQuantity) {
		this.iQuantity = iQuantity;
	}

	public String getcInvCode() {
		return cInvCode;
	}

	public void setcInvCode(String cInvCode) {
		this.cInvCode = cInvCode;
	}

	public String getcCUsCode() {
		return cCUsCode;
	}
	public void setcCUsCode(String cCUsCode) {
		this.cCUsCode = cCUsCode;
	}
	public String getcDLCode() {
		return cDLCode;
	}
	public void setcDLCode(String cDLCode) {
		this.cDLCode = cDLCode;
	}
	public String getcShipAddress() {
		return cShipAddress;
	}
	public void setcShipAddress(String cShipAddress) {
		this.cShipAddress = cShipAddress;
	}
	public String getCinvoicecompany() {
		return cinvoicecompany;
	}
	public void setCinvoicecompany(String cinvoicecompany) {
		this.cinvoicecompany = cinvoicecompany;
	}
	public String getcStCode() {
		return cStCode;
	}
	public void setcStCode(String cStCode) {
		this.cStCode = cStCode;
	}
	public String getbOMFirst() {
		return bOMFirst;
	}
	public void setbOMFirst(String bOMFirst) {
		this.bOMFirst = bOMFirst;
	}
	public String getBmotran() {
		return bmotran;
	}
	public void setBmotran(String bmotran) {
		this.bmotran = bmotran;
	}
	public String getbHYVouch() {
		return bHYVouch;
	}
	public void setbHYVouch(String bHYVouch) {
		this.bHYVouch = bHYVouch;
	}
	public String getcMPoCode() {
		return cMPoCode;
	}
	public void setcMPoCode(String cMPoCode) {
		this.cMPoCode = cMPoCode;
	}
	public String getdModifyDate() {
		return dModifyDate;
	}
	public void setdModifyDate(String dModifyDate) {
		this.dModifyDate = dModifyDate;
	}
	public String getDnmaketime() {
		return dnmaketime;
	}
	public void setDnmaketime(String dnmaketime) {
		this.dnmaketime = dnmaketime;
	}
	public String getDnmodifytime() {
		return dnmodifytime;
	}
	public void setDnmodifytime(String dnmodifytime) {
		this.dnmodifytime = dnmodifytime;
	}
	public String getDnverifytime() {
		return dnverifytime;
	}
	public void setDnverifytime(String dnverifytime) {
		this.dnverifytime = dnverifytime;
	}
	public String getCsysbarcode() {
		return csysbarcode;
	}
	public void setCsysbarcode(String csysbarcode) {
		this.csysbarcode = csysbarcode;
	}
	public String getcCheckSignFlag() {
		return cCheckSignFlag;
	}
	public void setcCheckSignFlag(String cCheckSignFlag) {
		this.cCheckSignFlag = cCheckSignFlag;
	}
	public String getcGCRouteCode() {
		return cGCRouteCode;
	}
	public void setcGCRouteCode(String cGCRouteCode) {
		this.cGCRouteCode = cGCRouteCode;
	}
	public String getbFromPreYear() {
		return bFromPreYear;
	}
	public void setbFromPreYear(String bFromPreYear) {
		this.bFromPreYear = bFromPreYear;
	}
	public String getcModifyPerson() {
		return cModifyPerson;
	}
	public void setcModifyPerson(String cModifyPerson) {
		this.cModifyPerson = cModifyPerson;
	}
	public String getIpurorderid() {
		return ipurorderid;
	}
	public void setIpurorderid(String ipurorderid) {
		this.ipurorderid = ipurorderid;
	}
	public String getUfts() {
		return ufts;
	}
	public void setUfts(String ufts) {
		this.ufts = ufts;
	}
	public String getiTaxRate() {
		return iTaxRate;
	}
	public void setiTaxRate(String iTaxRate) {
		this.iTaxRate = iTaxRate;
	}
	public String getiExchRate() {
		return iExchRate;
	}
	public void setiExchRate(String iExchRate) {
		this.iExchRate = iExchRate;
	}
	public String getbIsSTQc() {
		return bIsSTQc;
	}
	public void setbIsSTQc(String bIsSTQc) {
		this.bIsSTQc = bIsSTQc;
	}
	public String getVT_ID() {
		return VT_ID;
	}
	public void setVT_ID(String vT_ID) {
		VT_ID = vT_ID;
	}
	public String getBiafirst() {
		return biafirst;
	}
	public void setBiafirst(String biafirst) {
		this.biafirst = biafirst;
	}
	public String getdVeriDate() {
		return dVeriDate;
	}
	public void setdVeriDate(String dVeriDate) {
		this.dVeriDate = dVeriDate;
	}
	public String getcDefine1() {
		return cDefine1;
	}
	public void setcDefine1(String cDefine1) {
		this.cDefine1 = cDefine1;
	}
	public String getcMaker() {
		return cMaker;
	}
	public void setcMaker(String cMaker) {
		this.cMaker = cMaker;
	}
	public String getcMemo() {
		return cMemo;
	}
	public void setcMemo(String cMemo) {
		this.cMemo = cMemo;
	}
	public String getcPTCode() {
		return cPTCode;
	}
	public void setcPTCode(String cPTCode) {
		this.cPTCode = cPTCode;
	}
	public String getcRdCode() {
		return cRdCode;
	}
	public void setcRdCode(String cRdCode) {
		this.cRdCode = cRdCode;
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
	public String getbRdFlag() {
		return bRdFlag;
	}
	public void setbRdFlag(String bRdFlag) {
		this.bRdFlag = bRdFlag;
	}
	public String getcVouchType() {
		return cVouchType;
	}
	public void setcVouchType(String cVouchType) {
		this.cVouchType = cVouchType;
	}
	public String getcSource() {
		return cSource;
	}
	public void setcSource(String cSource) {
		this.cSource = cSource;
	}
	public String getcWhCode() {
		return cWhCode;
	}
	public void setcWhCode(String cWhCode) {
		this.cWhCode = cWhCode;
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getcBusType() {
		return cBusType;
	}
	public void setcBusType(String cBusType) {
		this.cBusType = cBusType;
	}
	public String getcDepCode() {
		return cDepCode;
	}
	public void setcDepCode(String cDepCode) {
		this.cDepCode = cDepCode;
	}
	public String getcVenCode() {
		return cVenCode;
	}
	public void setcVenCode(String cVenCode) {
		this.cVenCode = cVenCode;
	}
	public String getcOrderCode() {
		return cOrderCode;
	}
	public void setcOrderCode(String cOrderCode) {
		this.cOrderCode = cOrderCode;
	}
	public String getcHandler() {
		return cHandler;
	}
	public void setcHandler(String cHandler) {
		this.cHandler = cHandler;
	}
	public String getcAccounter() {
		return cAccounter;
	}
	public void setcAccounter(String cAccounter) {
		this.cAccounter = cAccounter;
	}
	public String getBpufirst() {
		return bpufirst;
	}
	public void setBpufirst(String bpufirst) {
		this.bpufirst = bpufirst;
	}
	public String getcExch_Name() {
		return cExch_Name;
	}
	public void setcExch_Name(String cExch_Name) {
		this.cExch_Name = cExch_Name;
	}
	public String getcBusCode() {
		return cBusCode;
	}
	public void setcBusCode(String cBusCode) {
		this.cBusCode = cBusCode;
	}
	public String getDveriDate() {
		return dveriDate;
	}
	public void setDveriDate(String dveriDate) {
		this.dveriDate = dveriDate;
	}
	
}
