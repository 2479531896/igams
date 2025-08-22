package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="InvPositionModel")
public class InvPositionModel {
	//主键id
	private String AutoID;
	//关联出入库明细表id
	private String RdsID;
	//关联出入库表头id
	private String RDID;
	//仓库编码
	private String cWhCode;
	//库位编号
	private String cPosCode;
	//物料编码
	private String cInvCode;
	//生产批号
	private String cBatch;
	//失效日期
	private String dVDate;
	//出入库数量
	private String iQuantity;
	//审核人
	private String cHandler;
	//出入库日期
	private String dDate;
	// 入库1 出库0
	private String bRdFlag;
	//生产日期
	private String dMadeDate;
	//保质期
	private String iMassDate;
	//保质期标记
	private String cMassUnit;
	//出入库表后缀
	private String cvouchtype;
	//审核日期
	private String dVouchDate;
	public String getAutoID() {
		return AutoID;
	}
	public void setAutoID(String autoID) {
		AutoID = autoID;
	}
	public String getRdsID() {
		return RdsID;
	}
	public void setRdsID(String rdsID) {
		RdsID = rdsID;
	}
	public String getRDID() {
		return RDID;
	}
	public void setRDID(String rDID) {
		RDID = rDID;
	}
	public String getcWhCode() {
		return cWhCode;
	}
	public void setcWhCode(String cWhCode) {
		this.cWhCode = cWhCode;
	}
	public String getcPosCode() {
		return cPosCode;
	}
	public void setcPosCode(String cPosCode) {
		this.cPosCode = cPosCode;
	}
	public String getcInvCode() {
		return cInvCode;
	}
	public void setcInvCode(String cInvCode) {
		this.cInvCode = cInvCode;
	}
	public String getcBatch() {
		return cBatch;
	}
	public void setcBatch(String cBatch) {
		this.cBatch = cBatch;
	}
	public String getdVDate() {
		return dVDate;
	}
	public void setdVDate(String dVDate) {
		this.dVDate = dVDate;
	}
	public String getiQuantity() {
		return iQuantity;
	}
	public void setiQuantity(String iQuantity) {
		this.iQuantity = iQuantity;
	}
	public String getcHandler() {
		return cHandler;
	}
	public void setcHandler(String cHandler) {
		this.cHandler = cHandler;
	}
	public String getdDate() {
		return dDate;
	}
	public void setdDate(String dDate) {
		this.dDate = dDate;
	}
	public String getbRdFlag() {
		return bRdFlag;
	}
	public void setbRdFlag(String bRdFlag) {
		this.bRdFlag = bRdFlag;
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
	public String getCvouchtype() {
		return cvouchtype;
	}
	public void setCvouchtype(String cvouchtype) {
		this.cvouchtype = cvouchtype;
	}
	public String getdVouchDate() {
		return dVouchDate;
	}
	public void setdVouchDate(String dVouchDate) {
		this.dVouchDate = dVouchDate;
	}
	
	
	
}
