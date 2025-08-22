package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WxcdDto")
public class WxcdDto extends WxcdModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//菜单等级
	private String depth;
	//外部程序代码
	private String wbcxdm;

	public String getWbcxdm() {
		return wbcxdm;
	}

	public void setWbcxdm(String wbcxdm) {
		this.wbcxdm = wbcxdm;
	}

	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}
	
}
