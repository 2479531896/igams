package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="BmwlModel")
public class BmwlModel extends BaseModel{
	private String wlid;//物料id
	private String bm;	//部门

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getBm() {
		return bm;
	}

	public void setBm(String bm) {
		this.bm = bm;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
