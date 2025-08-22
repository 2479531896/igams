package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="IA_ST_UnAccountVouchModel")
public class IA_ST_UnAccountVouchModel {
	//关联收发记录主表id
	private String IDUN;
	//关联收发记录表明细id
	private String IDSUN;
	//收发类型
	private String cVouTypeUN;
	//收发类型名称
	private String cBUstypeUN;
	public String getIDUN() {
		return IDUN;
	}
	public void setIDUN(String iDUN) {
		IDUN = iDUN;
	}
	public String getIDSUN() {
		return IDSUN;
	}
	public void setIDSUN(String iDSUN) {
		IDSUN = iDSUN;
	}
	public String getcVouTypeUN() {
		return cVouTypeUN;
	}
	public void setcVouTypeUN(String cVouTypeUN) {
		this.cVouTypeUN = cVouTypeUN;
	}
	public String getcBUstypeUN() {
		return cBUstypeUN;
	}
	public void setcBUstypeUN(String cBUstypeUN) {
		this.cBUstypeUN = cBUstypeUN;
	}
	
}
