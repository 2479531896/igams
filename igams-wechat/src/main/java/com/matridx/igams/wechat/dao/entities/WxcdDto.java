package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WxcdDto")
public class WxcdDto extends WxcdModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String depth;//菜单等级

	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}
	
}
