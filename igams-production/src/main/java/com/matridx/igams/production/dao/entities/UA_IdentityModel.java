package com.matridx.igams.production.dao.entities;

public class UA_IdentityModel {
	//套账
	private String cAcc_Id;
	//表名
	private String cVouchType;
	//主表最大值
	private String iFatherId;
	//明细表最大值
	private String iChildId;
	public String getcAcc_Id() {
		return cAcc_Id;
	}
	public void setcAcc_Id(String cAcc_Id) {
		this.cAcc_Id = cAcc_Id;
	}
	public String getcVouchType() {
		return cVouchType;
	}
	public void setcVouchType(String cVouchType) {
		this.cVouchType = cVouchType;
	}
	public String getiFatherId() {
		return iFatherId;
	}
	public void setiFatherId(String iFatherId) {
		this.iFatherId = iFatherId;
	}
	public String getiChildId() {
		return iChildId;
	}
	public void setiChildId(String iChildId) {
		this.iChildId = iChildId;
	}
	
}
