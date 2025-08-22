package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SysxxModel")
public class SysxxModel extends BaseModel{

	//实验室id
	private String sysid;
	//对方实验室名称
	private String dfsysmc;
	//我方实验室（检测单位 参数id）
	private String wfsys;
	//周期
	private String zq;
	//全部(查询条件)
	private String entire;

	public String getZq() {
		return zq;
	}

	public void setZq(String zq) {
		this.zq = zq;
	}

	public String getSysid() {
		return sysid;
	}

	public void setSysid(String sysid) {
		this.sysid = sysid;
	}

	public String getDfsysmc() {
		return dfsysmc;
	}

	public void setDfsysmc(String dfsysmc) {
		this.dfsysmc = dfsysmc;
	}

	public String getWfsys() {
		return wfsys;
	}

	public void setWfsys(String wfsys) {
		this.wfsys = wfsys;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
