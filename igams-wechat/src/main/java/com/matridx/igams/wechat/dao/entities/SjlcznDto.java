package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjlcznDto")
public class SjlcznDto extends SjlcznModel{
	//引用格式
	private String yygs;
	
	public String getYygs() {
		return yygs;
	}


	public void setYygs(String yygs) {
		this.yygs = yygs;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
