package com.matridx.igams.wechat.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="YhbqDto")
public class YhbqDto extends YhbqModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//微信ID列表
	private List<String> wxids;

	public List<String> getWxids() {
		return wxids;
	}

	public void setWxids(List<String> wxids) {
		this.wxids = wxids;
	}
	
}
