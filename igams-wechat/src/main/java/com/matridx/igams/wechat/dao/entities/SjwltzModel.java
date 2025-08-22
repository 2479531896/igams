package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjwltzModel")
public class SjwltzModel extends BaseModel{

	private static final long serialVersionUID = 1L;

	//业务ID
	private String ywid;
	//业务类型
	private String ywlx;
	//人员ID
	private String ryid;
	//钉钉ID
	private String ddid;
	//显示名称
	private String xsmc;

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getXsmc() {
		return xsmc;
	}

	public void setXsmc(String xsmc) {
		this.xsmc = xsmc;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getRyid() {
		return ryid;
	}

	public void setRyid(String ryid) {
		this.ryid = ryid;
	}
}
