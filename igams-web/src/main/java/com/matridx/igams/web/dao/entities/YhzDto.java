package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="YhzDto")
public class YhzDto extends YhzModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//用户姓名
	private String xm;
	//用户id
	private String yhid;
	//用户组成员汇总
	private String yhxms;

	public String getYhxms() {
		return yhxms;
	}

	public void setYhxms(String yhxms) {
		this.yhxms = yhxms;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}
}
