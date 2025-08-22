package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="GlwjxxDto")
public class GlwjxxDto extends GlwjxxModel {
	//文件编号
	private String wjbh;
	//文件名称
	private String wjmc;
	//版本号
	private String bbh;
	//生效日期
	private String sxrq;

	public GlwjxxDto() {
	}

	public GlwjxxDto(String wjid) {
		super.setWjid(wjid);
	}

	public String getWjbh() {
		return wjbh;
	}

	public void setWjbh(String wjbh) {
		this.wjbh = wjbh;
	}

	public String getWjmc() {
		return wjmc;
	}

	public void setWjmc(String wjmc) {
		this.wjmc = wjmc;
	}

	public String getBbh() {
		return bbh;
	}

	public void setBbh(String bbh) {
		this.bbh = bbh;
	}

	public String getSxrq() {
		return sxrq;
	}

	public void setSxrq(String sxrq) {
		this.sxrq = sxrq;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
