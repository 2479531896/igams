package com.matridx.server.wechat.dao.entities;

import java.util.List;

public class WeChatTagFansModel {
	
	//粉丝openid列表
	private List<String> openid;

	public List<String> getOpenid() {
		return openid;
	}

	public void setOpenid(List<String> openid) {
		this.openid = openid;
	}
		   
}
