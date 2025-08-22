package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="LlcglModel")
public class LlcglModel extends BaseBasicModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//人员ID
	private String ryid;
	//物料ID
	private String ckhwid;
	public String getRyid() {
		return ryid;
	}
	public void setRyid(String ryid) {
		this.ryid = ryid;
	}

	public String getCkhwid() {
		return ckhwid;
	}

	public void setCkhwid(String ckhwid) {
		this.ckhwid = ckhwid;
	}
}
