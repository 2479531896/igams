package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SysjglModel")
public class SysjglModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sysjid;//实验试剂ID

	private String ywid;//业务ID

	private String jcdw;//检测单位

	private String type;//区分

	private String sjbm;//试剂编码

	private String sjph;//试剂批号

	private String sjrq;//试剂日期

	private String bz;//备注
	private String sjmc;//试剂名称

	public String getSjmc() {
		return sjmc;
	}

	public void setSjmc(String sjmc) {
		this.sjmc = sjmc;
	}

	public String getSysjid() {
		return sysjid;
	}

	public void setSysjid(String sysjid) {
		this.sysjid = sysjid;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSjbm() {
		return sjbm;
	}

	public void setSjbm(String sjbm) {
		this.sjbm = sjbm;
	}

	public String getSjph() {
		return sjph;
	}

	public void setSjph(String sjph) {
		this.sjph = sjph;
	}

	public String getSjrq() {
		return sjrq;
	}

	public void setSjrq(String sjrq) {
		this.sjrq = sjrq;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
}
