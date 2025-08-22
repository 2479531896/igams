package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="QgcglModel")
public class QgcglModel extends BaseModel{
	//人员ID
	private String ryid;
	//物料ID
	private String wlid;
	//人员ID
	public String getRyid() {
		return ryid;
	}
	public void setRyid(String ryid){
		this.ryid = ryid;
	}
	//物料ID
	public String getWlid() {
		return wlid;
	}
	public void setWlid(String wlid){
		this.wlid = wlid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
