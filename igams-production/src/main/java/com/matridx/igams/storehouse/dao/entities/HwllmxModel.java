package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="HwllmxModel")
public class HwllmxModel extends BaseModel{
	//领料明细ID
	private String llmxid;
	//货物领料ID
	private String hwllid;
	//领料ID
	private String llid;
	//序号
	private String xh;
	//货物ID
	private String hwid;
	//请领数量
	private String qlsl;
	//实领数量
	private String slsl;
	//关联id
	private String glid;
	//允许数量
	private String yxsl;
	//出库关联id
	private String ckglid;
	
	
	
	public String getCkglid() {
		return ckglid;
	}
	public void setCkglid(String ckglid) {
		this.ckglid = ckglid;
	}
	public String getYxsl() {
		return yxsl;
	}
	public void setYxsl(String yxsl) {
		this.yxsl = yxsl;
	}
	public String getGlid() {
		return glid;
	}
	public void setGlid(String glid) {
		this.glid = glid;
	}
	//领料明细ID
	public String getLlmxid() {
		return llmxid;
	}
	public void setLlmxid(String llmxid){
		this.llmxid = llmxid;
	}
	//货物领料ID
	public String getHwllid() {
		return hwllid;
	}
	public void setHwllid(String hwllid){
		this.hwllid = hwllid;
	}
	//领料ID
	public String getLlid() {
		return llid;
	}
	public void setLlid(String llid){
		this.llid = llid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//货物ID
	public String getHwid() {
		return hwid;
	}
	public void setHwid(String hwid){
		this.hwid = hwid;
	}
	//请领数量
	public String getQlsl() {
		return qlsl;
	}
	public void setQlsl(String qlsl){
		this.qlsl = qlsl;
	}
	//实领数量
	public String getSlsl() {
		return slsl;
	}
	public void setSlsl(String slsl){
		this.slsl = slsl;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
