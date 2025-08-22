package com.matridx.igams.production.dao.entities;

public class SCM_ItemModel {
	//物料编码
	private String cInvCode;
	//关联库存ItemId
	private String Id;
	//0
	private String PartId;
	public String getcInvCode() {
		return cInvCode;
	}
	public void setcInvCode(String cInvCode) {
		this.cInvCode = cInvCode;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getPartId() {
		return PartId;
	}
	public void setPartId(String partId) {
		PartId = partId;
	}
	
}
