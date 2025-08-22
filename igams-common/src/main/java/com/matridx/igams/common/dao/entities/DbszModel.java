package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="DbszModel")
public class DbszModel extends BaseModel{
	private String yhid;//用户id
	private String dbszid;//代办设置id
	private String xh;//序号
	private String shlb;//审核类别
	private String fbsbj;//分布式标记
	private String shlbbm;//审核类别别名
	private String zylj;//资源路径

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

	public String getFbsbj() {
		return fbsbj;
	}

	public void setFbsbj(String fbsbj) {
		this.fbsbj = fbsbj;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getDbszid() {
		return dbszid;
	}

	public void setDbszid(String dbszid) {
		this.dbszid = dbszid;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getShlb() {
		return shlb;
	}

	public void setShlb(String shlb) {
		this.shlb = shlb;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
