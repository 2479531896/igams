package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="CkhwxxModel")
public class CkhwxxModel extends BaseBasicModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//仓库货物ID
	private String ckhwid;
	//物料ID
	private String wlid;
	//库存量
	private String kcl;
	//预定数
	private String yds;
	//仓库分类
	private String ckqxlx;
	//仓库id
	private String ckid;

	public String getCkid() {
		return ckid;
	}

	public void setCkid(String ckid) {
		this.ckid = ckid;
	}

	public String getCkhwid() {
		return ckhwid;
	}
	public void setCkhwid(String ckhwid) {
		this.ckhwid = ckhwid;
	}
	public String getWlid() {
		return wlid;
	}
	public void setWlid(String wlid) {
		this.wlid = wlid;
	}
	public String getKcl() {
		return kcl;
	}
	public void setKcl(String kcl) {
		this.kcl = kcl;
	}
	public String getYds() {
		return yds;
	}
	public void setYds(String yds) {
		this.yds = yds;
	}

	public String getCkqxlx() {
		return ckqxlx;
	}

	public void setCkqxlx(String ckqxlx) {
		this.ckqxlx = ckqxlx;
	}
}
