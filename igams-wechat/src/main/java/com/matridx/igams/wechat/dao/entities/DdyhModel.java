package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="DdyhModel")
public class DdyhModel{

	//用户id
	private String yhid;
	//用户名
	private String yhm;
	//真实姓名
	private String zsxm;
	//钉钉ID
	private String ddid;
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid) {
		this.yhid = yhid;
	}
	public String getYhm() {
		return yhm;
	}
	public void setYhm(String yhm) {
		this.yhm = yhm;
	}
	public String getZsxm() {
		return zsxm;
	}
	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}
	public String getDdid() {
		return ddid;
	}
	public void setDdid(String ddid) {
		this.ddid = ddid;
	}


}
