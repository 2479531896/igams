package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="BqglDto")
public class BqglDto extends BqglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//外部程序代码
	private String wbcxdm;

	public String getWbcxdm() {
		return wbcxdm;
	}

	public void setWbcxdm(String wbcxdm) {
		this.wbcxdm = wbcxdm;
	}
	
}
