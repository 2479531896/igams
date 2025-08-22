package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="SylcmxModel")
public class SylcmxModel extends BaseModel{
	//流程明细ID
	private String lcmxid;
	//流程ID
	private String lcid;
	//显示名称
	private String xsmc;
	//资源ID
	private String zyid;
	//序号
	private String xh;
	//跳转参数
	private String tzcs;
	//模块ID
	private String mkid;

	public String getMkid() {
		return mkid;
	}

	public void setMkid(String mkid) {
		this.mkid = mkid;
	}

	public String getLcmxid() {
		return lcmxid;
	}

	public void setLcmxid(String lcmxid) {
		this.lcmxid = lcmxid;
	}

	public String getLcid() {
		return lcid;
	}

	public void setLcid(String lcid) {
		this.lcid = lcid;
	}

	public String getXsmc() {
		return xsmc;
	}

	public void setXsmc(String xsmc) {
		this.xsmc = xsmc;
	}

	public String getZyid() {
		return zyid;
	}

	public void setZyid(String zyid) {
		this.zyid = zyid;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getTzcs() {
		return tzcs;
	}

	public void setTzcs(String tzcs) {
		this.tzcs = tzcs;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
