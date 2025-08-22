package com.matridx.server.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WxbdglModel")
public class WxbdglModel extends BaseModel{
		//微信id
		private String wxid;
		//手机号
		private String sjh;

	public String getWxid() {
		return wxid;
	}

	public void setWxid(String wxid) {
		this.wxid = wxid;
	}

	public String getSjh() {
		return sjh;
	}

	public void setSjh(String sjh) {
		this.sjh = sjh;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
