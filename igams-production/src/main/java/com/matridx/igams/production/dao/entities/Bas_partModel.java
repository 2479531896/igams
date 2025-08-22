package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="Bas_partModel")
public class Bas_partModel{
	private String PartId;
	private String InvCode;
	private String bPartId;

	public String getbPartId() {
		return bPartId;
	}

	public void setbPartId(String bPartId) {
		this.bPartId = bPartId;
	}

	public String getPartId(){
		return PartId;
	}
	public void setPartId(String partId){
		this.PartId = partId;
	}
	public String getInvCode(){
		return InvCode;
	}
	public void setInvCode(String invCode){
		this.InvCode = invCode;
	}
	
}
