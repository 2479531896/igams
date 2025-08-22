package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjyczdWechatDto")
public class SjyczdWechatDto extends SjyczdWechatModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//是否置顶
	private String sfzd;

	public String getSfzd() {
		return sfzd;
	}

	public void setSfzd(String sfzd) {
		this.sfzd = sfzd;
	}
	

}
