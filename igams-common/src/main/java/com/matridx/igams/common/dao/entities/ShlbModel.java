package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="ShlbModel")
public class ShlbModel extends BaseModel{
	//审核类别
	private String shlb;
	//审核类别名称
	private String shlbmc;
	//回调类
	private String hdl;
	//回调Dto
	private String hdmx;
	//分区类别
	private String fqlb;
	//是否允许审核取消  0:不可取消  1：可以取消
	private String qxxk;
	//分布式标识
	private String Prefix;
	//是否广播
	private String sfgb;
	//审核类别别名
	private String shlbbm;
	//资源路径
	private String zylj;

	public String getZylj() {
		return zylj;
	}

	public void setZylj(String zylj) {
		this.zylj = zylj;
	}

	public String getShlbbm() {
		return shlbbm;
	}

	public void setShlbbm(String shlbbm) {
		this.shlbbm = shlbbm;
	}

	public String getSfgb() {
		return sfgb;
	}
	public void setSfgb(String sfgb) {
		this.sfgb = sfgb;
	}
	public String getPrefix() {
		return Prefix;
	}
	public void setPrefix(String prefix) {
		Prefix = prefix;
	}
	//审核类别
	public String getShlb() {
		return shlb;
	}
	public void setShlb(String shlb){
		this.shlb = shlb;
	}
	//审核类别名称
	public String getShlbmc() {
		return shlbmc;
	}
	public void setShlbmc(String shlbmc){
		this.shlbmc = shlbmc;
	}
	//回调类
	public String getHdl() {
		return hdl;
	}
	public void setHdl(String hdl){
		this.hdl = hdl;
	}
	//分区类别
	public String getFqlb() {
		return fqlb;
	}
	public void setFqlb(String fqlb){
		this.fqlb = fqlb;
	}

	public String getHdmx() {
		return hdmx;
	}
	public void setHdmx(String hdmx) {
		this.hdmx = hdmx;
	}

	public String getQxxk() {
		return qxxk;
	}
	public void setQxxk(String qxxk) {
		this.qxxk = qxxk;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
