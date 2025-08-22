package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XxpzDto")
public class XxpzDto extends XxpzModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//信息类型名称
	private String xxlxmc;
	
	public String getXxlxmc() {
		return xxlxmc;
	}

	public void setXxlxmc(String xxlxmc) {
		this.xxlxmc = xxlxmc;
	}
}
