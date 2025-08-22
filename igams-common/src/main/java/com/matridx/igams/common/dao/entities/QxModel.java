package com.matridx.igams.common.dao.entities;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias(value="QxModel")
public class QxModel implements Serializable{
	
	//角色id
	private String jsid;
	//资源ID
	private String zyid;
	//操作代码
	private String czdm;
	//对应页面
	private String dyym;
	//操作名称
	private String czmc;
	//显示顺序
	private String xsxx;
	//按钮样式
	private String anys;
	//资源路径
	private String zylj;
	//功能名
	private String gnm;
	//操作说明
	private String czsm;

	//分布式前缀
	private String fbsqz;
	//
	private String fwlj;

	public String getFwlj() {
		return fwlj;
	}

	public void setFwlj(String fwlj) {
		this.fwlj = fwlj;
	}

	public String getFbsqz() {
		return fbsqz;
	}

	public void setFbsqz(String fbsqz) {
		this.fbsqz = fbsqz;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public String getZyid() {
		return zyid;
	}

	public void setZyid(String zyid) {
		this.zyid = zyid;
	}

	public String getCzdm() {
		return czdm;
	}

	public void setCzdm(String czdm) {
		this.czdm = czdm;
	}

	public String getDyym() {
		return dyym;
	}

	public void setDyym(String dyym) {
		this.dyym = dyym;
	}

	public String getCzmc() {
		return czmc;
	}

	public void setCzmc(String czmc) {
		this.czmc = czmc;
	}

	public String getXsxx() {
		return xsxx;
	}

	public void setXsxx(String xsxx) {
		this.xsxx = xsxx;
	}
	
	public String getAnys() {
		return anys;
	}

	public void setAnys(String anys) {
		this.anys = anys;
	}

	public String getZylj() {
		return zylj;
	}

	public void setZylj(String zylj) {
		this.zylj = zylj;
	}



	public String getGnm() {
		return gnm;
	}

	public void setGnm(String gnm) {
		this.gnm = gnm;
	}
	public String getCzsm() {
		return czsm;
	}

	public void setCzsm(String czsm) {
		this.czsm = czsm;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
