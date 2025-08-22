package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="JgxxModel")
public class JgxxModel extends BaseModel{
	//机构id
	private String jgid;
	//机构名称
	private String jgmc;
	//父机构id
	private String fjgid;
	//机构代码
	private String jgdm;
	//扩展参数1
	private String kzcs1;
	//扩展参数2
	private String kzcs2;
	//外部程序id
	private String wbcxid;
	//是否停用
	private String sfty;
	//部门主管s
	private String bmzgs;
	//分布式标识
	private String fbsbj;

	public String getFbsbj() {
		return fbsbj;
	}

	public void setFbsbj(String fbsbj) {
		this.fbsbj = fbsbj;
	}

	public String getBmzgs() {
		return bmzgs;
	}

	public void setBmzgs(String bmzgs) {
		this.bmzgs = bmzgs;
	}
	public String getSfty() {
		return sfty;
	}

	public void setSfty(String sfty) {
		this.sfty = sfty;
	}

	public String getWbcxid() {
		return wbcxid;
	}

	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}

	public String getKzcs1() {
		return kzcs1;
	}
	public void setKzcs1(String kzcs1) {
		this.kzcs1 = kzcs1;
	}
	
	public String getKzcs2() {
		return kzcs2;
	}
	public void setKzcs2(String kzcs2) {
		this.kzcs2 = kzcs2;
	}
	//机构代码
	public String getJgdm() {
		return jgdm;
	}
	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}
	//机构id
	public String getJgid() {
		return jgid;
	}
	public void setJgid(String jgid){
		this.jgid = jgid;
	}
	//机构名称
	public String getJgmc() {
		return jgmc;
	}
	public void setJgmc(String jgmc){
		this.jgmc = jgmc;
	}
	//父机构id
	public String getFjgid() {
		return fjgid;
	}
	public void setFjgid(String fjgid){
		this.fjgid = fjgid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
