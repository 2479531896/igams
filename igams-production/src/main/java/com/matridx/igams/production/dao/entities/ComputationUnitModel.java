package com.matridx.igams.production.dao.entities;
import org.apache.ibatis.type.Alias;
@Alias(value="ComputationUnitModel")
public class ComputationUnitModel{
	//单位编号
	private String cComunitCode;

	public String getcComunitCode(){
		return cComunitCode;
	}

	public void setcComunitCode(String cComunitCode){
		this.cComunitCode = cComunitCode;
	}
	
}
