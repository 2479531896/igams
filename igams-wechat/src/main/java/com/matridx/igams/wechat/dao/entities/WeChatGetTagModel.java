package com.matridx.igams.wechat.dao.entities;

import java.util.List;

public class WeChatGetTagModel {
	//标签列表
	private List<WeChatTagModel> tags;

	public List<WeChatTagModel> getTags() {
		return tags;
	}

	public void setTags(List<WeChatTagModel> tags) {
		this.tags = tags;
	}
	
}
