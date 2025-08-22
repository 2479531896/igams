package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WbhbyzDto")
public class WbhbyzDto extends WbhbyzModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//合作伙伴
	private String hbmc;
	//送检id
	private String sjid;
	//密钥
	private String key;
	//关键词
	private String word;
	//调用地址
	private String address;
	//参数id
	private String csid;
	//错误次数
	private Integer cwcs;
	//参数扩展1
	private String cskz1;
	//参数扩展3
	private String cskz3;
	//修改前的代码
	private String beforeCode;

	public String getBeforeCode() {
		return beforeCode;
	}

	public void setBeforeCode(String beforeCode) {
		this.beforeCode = beforeCode;
	}

	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}

	public String getCskz3() {
		return cskz3;
	}

	public void setCskz3(String cskz3) {
		this.cskz3 = cskz3;
	}

	public Integer getCwcs() {
		return cwcs;
	}

	public void setCwcs(Integer cwcs) {
		this.cwcs = cwcs;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getHbmc() {
		return hbmc;
	}

	public void setHbmc(String hbmc) {
		this.hbmc = hbmc;
	}
}
