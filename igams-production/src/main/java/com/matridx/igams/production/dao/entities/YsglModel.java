package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="YsglModel")
public class YsglModel extends BaseBasicModel {

	//预算管理ID
	private String ysglid;
	//年度
	private String nd;
	//部门
	private String bm;

	public String getYsglid() {
		return ysglid;
	}

	public void setYsglid(String ysglid) {
		this.ysglid = ysglid;
	}

	public String getNd() {
		return nd;
	}

	public void setNd(String nd) {
		this.nd = nd;
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
