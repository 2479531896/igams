package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="AA_BatchPropertyModel")
public class AA_BatchPropertyModel {
	//物料编码
	private String cInvCode;
	//生产批号
	private String cBatch;
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
	
	
}
