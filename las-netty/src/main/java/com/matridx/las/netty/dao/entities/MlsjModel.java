package com.matridx.las.netty.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="MlsjModel")
public class MlsjModel extends BaseModel{
	//事件id
	private String sjid;
	//事件名称
	private String sjmc;
	//事件id
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//事件名称
	public String getSjmc() {
		return sjmc;
	}
	public void setSjmc(String sjmc){
		this.sjmc = sjmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
