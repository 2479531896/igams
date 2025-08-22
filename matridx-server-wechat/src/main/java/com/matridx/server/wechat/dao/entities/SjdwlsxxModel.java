package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjdwlsxxModel")
public class SjdwlsxxModel extends BaseModel{
	//单位ID
	private String dwid;
	//序号
	private String xh;
	//父单位ID
	private String fdwid;
	//单位代码
	private String dwdm;
	//单位名称
	private String dwmc;
	//电话
	private String dh;
	//扩展参数
	private String kzcs;

	public String getKzcs() {
		return kzcs;
	}

	public void setKzcs(String kzcs) {
		this.kzcs = kzcs;
	}

	//单位ID
	public String getDwid() {
		return dwid;
	}
	public void setDwid(String dwid){
		this.dwid = dwid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//父单位ID
	public String getFdwid() {
		return fdwid;
	}
	public void setFdwid(String fdwid){
		this.fdwid = fdwid;
	}
	//单位代码
	public String getDwdm() {
		return dwdm;
	}
	public void setDwdm(String dwdm){
		this.dwdm = dwdm;
	}
	//单位名称
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc){
		this.dwmc = dwmc;
	}
	//电话
	public String getDh() {
		return dh;
	}
	public void setDh(String dh){
		this.dh = dh;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
