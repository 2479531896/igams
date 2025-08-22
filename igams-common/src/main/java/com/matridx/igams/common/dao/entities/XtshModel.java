package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XtshModel")
public class XtshModel extends BaseModel{
	//审核ID
	private String shid;
	//审核名称
	private String shmc;
	//审核类别  一种
	private String shlb;
	//默认设置   00：默认
	private String mrsz;
	//描述
	private String ms;
	//扩展参数 用于针对各种特殊情况 0：经济合同小于指定值 1：经济合同大于指定值
	private String kzcs;
	//分布式标识
	private String prefix;
	//是否广播
	private String sfgb;
	
	public String getSfgb() {
		return sfgb;
	}
	public void setSfgb(String sfgb) {
		this.sfgb = sfgb;
	}
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
	//审核名称
	public String getShmc() {
		return shmc;
	}
	public void setShmc(String shmc){
		this.shmc = shmc;
	}
	//审核类别  一种
	public String getShlb() {
		return shlb;
	}
	public void setShlb(String shlb){
		this.shlb = shlb;
	}
	//默认设置   00：默认
	public String getMrsz() {
		return mrsz;
	}
	public void setMrsz(String mrsz){
		this.mrsz = mrsz;
	}
	//描述
	public String getMs() {
		return ms;
	}
	public void setMs(String ms){
		this.ms = ms;
	}
	//扩展参数 用于针对各种特殊情况 0：经济合同小于指定值 1：经济合同大于指定值
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
