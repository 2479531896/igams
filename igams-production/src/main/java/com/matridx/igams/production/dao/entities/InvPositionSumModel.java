package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="InvPositionSumModel")
public class InvPositionSumModel {
	//主键id
	private String Autoid;
	//仓库
	private String cWhCode;
	//库位
	private String cPosCode;
	//物料编码
	private String cInvCode;
	//货位数量
	private String iQuantity;
	//inum 入库为0，调拨为null
	private String inum;
	//生产批号
	private String cBatch;
	//保质期标记
	private String cMassUnit;
	//保质期
	private String iMassDate;
	//生产日期
	private String dMadeDate;
	//有效期
	private String dVDate;
	
	public String getAutoid() {
		return Autoid;
	}
	public void setAutoid(String autoid) {
		Autoid = autoid;
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
	public String getiQuantity() {
		return iQuantity;
	}
	public void setiQuantity(String iQuantity) {
		this.iQuantity = iQuantity;
	}
	public String getInum() {
		return inum;
	}
	public void setInum(String inum) {
		this.inum = inum;
	}
	public String getcBatch() {
		return cBatch;
	}
	public void setcBatch(String cBatch) {
		this.cBatch = cBatch;
	}
	public String getcMassUnit() {
		return cMassUnit;
	}
	public void setcMassUnit(String cMassUnit) {
		this.cMassUnit = cMassUnit;
	}
	public String getiMassDate() {
		return iMassDate;
	}
	public void setiMassDate(String iMassDate) {
		this.iMassDate = iMassDate;
	}
	public String getdMadeDate() {
		return dMadeDate;
	}
	public void setdMadeDate(String dMadeDate) {
		this.dMadeDate = dMadeDate;
	}
	public String getdVDate() {
		return dVDate;
	}
	public void setdVDate(String dVDate) {
		this.dVDate = dVDate;
	}
	
	
}
