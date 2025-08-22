package com.matridx.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JxcmdModel")
public class JxcmdModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String jxcmdid;

	private String ywid;

	private String lx;

	private String mrz;

	private String xh;

	private String max;

	private String min;

	private String key;

	private String ss;

	private String xzbs;

	private String sfxs;

	private String csms;

	private String ljglid;

	public String getLjglid() {
		return ljglid;
	}

	public void setLjglid(String ljglid) {
		this.ljglid = ljglid;
	}

	public String getCsms() {
		return csms;
	}

	public void setCsms(String csms) {
		this.csms = csms;
	}

	public String getSfxs() {
		return sfxs;
	}

	public void setSfxs(String sfxs) {
		this.sfxs = sfxs;
	}

	public String getXzbs() {
		return xzbs;
	}

	public void setXzbs(String xzbs) {
		this.xzbs = xzbs;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSs() {
		return ss;
	}

	public void setSs(String ss) {
		this.ss = ss;
	}

	public String getJxcmdid() {
		return jxcmdid;
	}

	public void setJxcmdid(String jxcmdid) {
		this.jxcmdid = jxcmdid;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getMrz() {
		return mrz;
	}

	public void setMrz(String mrz) {
		this.mrz = mrz;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}
}
