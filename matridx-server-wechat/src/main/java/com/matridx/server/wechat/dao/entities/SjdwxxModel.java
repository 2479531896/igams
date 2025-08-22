package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjdwxxModel")
public class SjdwxxModel extends BaseModel{
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
	//扩展参数2
	private String kzcs2;
	//扩展参数3
	private String kzcs3;

	public String getKzcs3() {
		return kzcs3;
	}

	public void setKzcs3(String kzcs3) {
		this.kzcs3 = kzcs3;
	}

	public String getKzcs2() {
		return kzcs2;
	}

	public void setKzcs2(String kzcs2) {
		this.kzcs2 = kzcs2;
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
	//扩展参数
	public String getKzcs() {
		return kzcs;
	}
	public void setKzcs(String kzcs) {
		this.kzcs = kzcs;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
