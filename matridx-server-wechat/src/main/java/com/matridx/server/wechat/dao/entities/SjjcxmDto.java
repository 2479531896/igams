package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjjcxmDto")
public class SjjcxmDto extends SjjcxmModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//检测项目名称
	private String csmc;

	//基础数据CSID
	private String csid;
	//基础数据的参数代码
	private String csdm;
	//参数扩展1
	private String cskz1;
	//基础数据kzcs3
	private String cskz3;
	//标识
	private String bs;
	private String json;

	//变动实付金额
	private String bdsfje;

	public String getBdsfje() {
		return bdsfje;
	}

	public void setBdsfje(String bdsfje) {
		this.bdsfje = bdsfje;
	}
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getBs() {
		return bs;
	}

	public void setBs(String bs) {
		this.bs = bs;
	}

	public String getCskz3() {
		return cskz3;
	}

	public void setCskz3(String cskz3) {
		this.cskz3 = cskz3;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}

	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public String getCsdm() {
		return csdm;
	}

	public void setCsdm(String csdm) {
		this.csdm = csdm;
	}

	public String getCsmc() {
		return csmc;
	}

	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}
	
}
