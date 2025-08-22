package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="CsmxxModel")
public class CsmxxModel extends BaseModel{
	private static final long serialVersionUID = 1L;
	//场所码ID
	private String csmid;
	//用户ID
	private String yhid;
	//采样点
	private String cyd;
	//送检单位
	private String sjdw;
	//检测单位
	private String jcdw;
	//有效日期
	private String yxrq;

	public String getCsmid() {
		return csmid;
	}

	public void setCsmid(String csmid) {
		this.csmid = csmid;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getCyd() {
		return cyd;
	}

	public void setCyd(String cyd) {
		this.cyd = cyd;
	}

	public String getSjdw() {
		return sjdw;
	}

	public void setSjdw(String sjdw) {
		this.sjdw = sjdw;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getYxrq() {
		return yxrq;
	}

	public void setYxrq(String yxrq) {
		this.yxrq = yxrq;
	}
}
