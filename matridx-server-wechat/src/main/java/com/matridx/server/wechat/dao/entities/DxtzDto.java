package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="DxtzDto")
public class DxtzDto extends DxtzModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//业务类型
	private String ywlx;
	
	public String getYwlx() {
		return ywlx;
	}
	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}
	
}
