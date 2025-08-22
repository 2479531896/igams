package com.matridx.igams.production.dao.entities;

public class CurrentStockModel {
	//主键ID
	private int AutoID;
	//仓库编号
	private String cWhCode;
	//物料编码
	private String cInvCode;
	//相同物料id相同，不同物料id自增
	private String ItemId;
	//生产批号
	private String cBatch;
	//现存数量
	private String iQuantity;
	//失效日期
	private String dVDate;
	//生产日期
	private String dMdate;
	//有效期数
	private String iMassDate;
	//有效期单位
	private String cMassUnit;
	//0
	private String iExpiratDateCalcu;
	
	
	public String getiExpiratDateCalcu() {
		return iExpiratDateCalcu;
	}
	public void setiExpiratDateCalcu(String iExpiratDateCalcu) {
		this.iExpiratDateCalcu = iExpiratDateCalcu;
	}
	public int getAutoID() {
		return AutoID;
	}
	public void setAutoID(int autoID) {
		AutoID = autoID;
	}
	public String getcWhCode() {
		return cWhCode;
	}
	public void setcWhCode(String cWhCode) {
		this.cWhCode = cWhCode;
	}
	public String getcInvCode() {
		return cInvCode;
	}
	public void setcInvCode(String cInvCode) {
		this.cInvCode = cInvCode;
	}
	public String getItemId() {
		return ItemId;
	}
	public void setItemId(String itemId) {
		ItemId = itemId;
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
	public String getdVDate() {
		return dVDate;
	}
	public void setdVDate(String dVDate) {
		this.dVDate = dVDate;
	}
	public String getdMdate() {
		return dMdate;
	}
	public void setdMdate(String dMdate) {
		this.dMdate = dMdate;
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
	
	
}
