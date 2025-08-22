package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="HtfpmxModel")
public class HtfpmxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//发票明细ID
	private String fpmxid;
	//合同发票ID
	private String htfpid;
	//合同明细ID
	private String htmxid;
	//发票金额
	private String fpje;

	public String getFpmxid() {
		return fpmxid;
	}

	public void setFpmxid(String fpmxid) {
		this.fpmxid = fpmxid;
	}

	public String getHtfpid() {
		return htfpid;
	}

	public void setHtfpid(String htfpid) {
		this.htfpid = htfpid;
	}

	public String getHtmxid() {
		return htmxid;
	}

	public void setHtmxid(String htmxid) {
		this.htmxid = htmxid;
	}


	public String getFpje() {
		return fpje;
	}

	public void setFpje(String fpje) {
		this.fpje = fpje;
	}
}
