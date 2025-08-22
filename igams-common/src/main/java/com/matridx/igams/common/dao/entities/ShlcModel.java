package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="ShlcModel")
public class ShlcModel extends BaseModel{
	//审核ID
	private String shid;
	//流程序号
	private String lcxh;
	//流程类别
	private String lclb;
	//流程条件
	private String lctj;
	//符合后续流程
	private String fhlc;
	//不符合后续流程
	private String bfhlc;
	//岗位ID
	private String gwid;
	//启用时间
	private String qysj;
	//停用时间
	private String tysj;
	//扩展参数 现用于是否调用外部接口  01:财务接口 02:显示  03:财务接口和显示
	private String kzcs;
	//分布式标识
    private String prefix;

    
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	//审核ID
	public String getShid() {
		return shid;
	}
	public void setShid(String shid){
		this.shid = shid;
	}
	//流程序号
	public String getLcxh() {
		return lcxh;
	}
	public void setLcxh(String lcxh){
		this.lcxh = lcxh;
	}
	//流程类别
	public String getLclb() {
		return lclb;
	}
	public void setLclb(String lclb){
		this.lclb = lclb;
	}
	//流程条件
	public String getLctj() {
		return lctj;
	}
	public void setLctj(String lctj){
		this.lctj = lctj;
	}
	//符合后续流程
	public String getFhlc() {
		return fhlc;
	}
	public void setFhlc(String fhlc){
		this.fhlc = fhlc;
	}
	//不符合后续流程
	public String getBfhlc() {
		return bfhlc;
	}
	public void setBfhlc(String bfhlc){
		this.bfhlc = bfhlc;
	}
	//岗位ID
	public String getGwid() {
		return gwid;
	}
	public void setGwid(String gwid){
		this.gwid = gwid;
	}
	//启用时间
	public String getQysj() {
		return qysj;
	}
	public void setQysj(String qysj){
		this.qysj = qysj;
	}
	//停用时间
	public String getTysj() {
		return tysj;
	}
	public void setTysj(String tysj){
		this.tysj = tysj;
	}
	//扩展参数 现用于是否调用外部接口  01:财务接口 02:显示  03:财务接口和显示
	public String getKzcs() {
		return kzcs;
	}
	public void setKzcs(String kzcs){
		this.kzcs = kzcs;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
