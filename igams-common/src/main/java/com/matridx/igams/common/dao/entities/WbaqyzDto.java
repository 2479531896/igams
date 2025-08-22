package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WbaqyzDto")
public class WbaqyzDto extends WbaqyzModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//送检伙伴
	private String sjhb;
	//伙伴id
	private String hbid;
	//修改前的代码
	private String beforeCode;
	//全部(查询条件)
	private String entire;
	//伙伴名称
	private String hbmc;




	public String getHbmc() {
		return hbmc;
	}

	public void setHbmc(String hbmc) {
		this.hbmc = hbmc;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getBeforeCode() {
		return beforeCode;
	}

	public void setBeforeCode(String beforeCode) {
		this.beforeCode = beforeCode;
	}

	public String getSjhb() {
		return sjhb;
	}

	public void setSjhb(String sjhb) {
		this.sjhb = sjhb;
	}

	public String getHbid() {
		return hbid;
	}

	public void setHbid(String hbid) {
		this.hbid = hbid;
	}
	
	
}
