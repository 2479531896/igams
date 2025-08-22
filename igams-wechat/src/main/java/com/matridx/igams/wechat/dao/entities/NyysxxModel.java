package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="NyysxxModel")
public class NyysxxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sjid;//送检ID

	private String xh;//序号

	private String jclx;//检测类型

	private String sjnyxid;//送检耐药性ID

	private String json;//json

	private String llh;//履历号

	private String bbh;//版本号

	public String getBbh() {
		return bbh;
	}

	public void setBbh(String bbh) {
		this.bbh = bbh;
	}

	public String getLlh() {
		return llh;
	}

	public void setLlh(String llh) {
		this.llh = llh;
	}

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getJclx() {
		return jclx;
	}

	public void setJclx(String jclx) {
		this.jclx = jclx;
	}

	public String getSjnyxid() {
		return sjnyxid;
	}

	public void setSjnyxid(String sjnyxid) {
		this.sjnyxid = sjnyxid;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}
