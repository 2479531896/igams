package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="CkmxModel")
public class CkmxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//出库明细id
	private String ckmxid;
	//出库id
	private String ckid;
	//货物id
	private String hwid;
	//出库数量
	private String cksl;
	//出库明细关联id
	private String ckmxglid;
	//领料明细id
	private String llmxid;
	//借用明细id
	private String jymxid;
	//红冲关联id
	private String hcmxglid;
	//预定数
	private String yds;
	//已红冲数
	private String yhcs;
	//引用明细出库单
	private String yymxckd;
	//成品机构明细ID
	private String cpjgmxid;
	//委外合同明细ID
	private String wwhtmxid;

	public String getWwhtmxid() {
		return wwhtmxid;
	}

	public void setWwhtmxid(String wwhtmxid) {
		this.wwhtmxid = wwhtmxid;
	}

	public String getCpjgmxid() {
		return cpjgmxid;
	}

	public void setCpjgmxid(String cpjgmxid) {
		this.cpjgmxid = cpjgmxid;
	}

	public String getYds() {
		return yds;
	}

	public void setYds(String yds) {
		this.yds = yds;
	}

	public String getYhcs() {
		return yhcs;
	}

	public void setYhcs(String yhcs) {
		this.yhcs = yhcs;
	}

	public String getYymxckd() {
		return yymxckd;
	}

	public void setYymxckd(String yymxckd) {
		this.yymxckd = yymxckd;
	}

	public String getHcmxglid() {
		return hcmxglid;
	}

	public void setHcmxglid(String hcmxglid) {
		this.hcmxglid = hcmxglid;
	}

	public String getJymxid() {
		return jymxid;
	}

	public void setJymxid(String jymxid) {
		this.jymxid = jymxid;
	}

	public String getLlmxid() {
		return llmxid;
	}
	public void setLlmxid(String llmxid) {
		this.llmxid = llmxid;
	}
	public String getCkmxglid() {
		return ckmxglid;
	}
	public void setCkmxglid(String ckmxglid) {
		this.ckmxglid = ckmxglid;
	}
	public String getCkmxid() {
		return ckmxid;
	}
	public void setCkmxid(String ckmxid) {
		this.ckmxid = ckmxid;
	}
	public String getCkid() {
		return ckid;
	}
	public void setCkid(String ckid) {
		this.ckid = ckid;
	}
	public String getHwid() {
		return hwid;
	}
	public void setHwid(String hwid) {
		this.hwid = hwid;
	}
	public String getCksl() {
		return cksl;
	}
	public void setCksl(String cksl) {
		this.cksl = cksl;
	}
	
	
}
