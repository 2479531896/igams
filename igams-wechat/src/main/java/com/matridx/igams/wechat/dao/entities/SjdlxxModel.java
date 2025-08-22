package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjdlxxModel")
public class SjdlxxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sjid;//送检id

	private String wkbh;//文库编号

	private String jy;//基因

	private String dlyz;//毒力因子

	private String ms;//描述

	private String zmc;//种名称

	private String dlyzlx;//毒力因子类型

	private String ysdlyz;//原始毒力因子

	private String xls;//序列数

	private String dlyzid;//毒力因子ID

	private String glwzid;//关联物种ID

	private String knlywz;//可能来源物种

	private String jclx;//检测类型

	private String jczlx;//检测子类型

	public String getJclx() {
		return jclx;
	}

	public void setJclx(String jclx) {
		this.jclx = jclx;
	}

	public String getKnlywz() {
		return knlywz;
	}

	public void setKnlywz(String knlywz) {
		this.knlywz = knlywz;
	}

	public String getYsdlyz() {
		return ysdlyz;
	}

	public void setYsdlyz(String ysdlyz) {
		this.ysdlyz = ysdlyz;
	}

	public String getXls() {
		return xls;
	}

	public void setXls(String xls) {
		this.xls = xls;
	}

	public String getDlyzid() {
		return dlyzid;
	}

	public void setDlyzid(String dlyzid) {
		this.dlyzid = dlyzid;
	}

	public String getGlwzid() {
		return glwzid;
	}

	public void setGlwzid(String glwzid) {
		this.glwzid = glwzid;
	}

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getWkbh() {
		return wkbh;
	}

	public void setWkbh(String wkbh) {
		this.wkbh = wkbh;
	}

	public String getJy() {
		return jy;
	}

	public void setJy(String jy) {
		this.jy = jy;
	}

	public String getDlyz() {
		return dlyz;
	}

	public void setDlyz(String dlyz) {
		this.dlyz = dlyz;
	}

	public String getMs() {
		return ms;
	}

	public void setMs(String ms) {
		this.ms = ms;
	}

	public String getZmc() {
		return zmc;
	}

	public void setZmc(String zmc) {
		this.zmc = zmc;
	}

	public String getDlyzlx() {
		return dlyzlx;
	}

	public void setDlyzlx(String dlyzlx) {
		this.dlyzlx = dlyzlx;
	}

    public String getJczlx() {
        return jczlx;
    }

    public void setJczlx(String jczlx) {
        this.jczlx = jczlx;
    }
}
