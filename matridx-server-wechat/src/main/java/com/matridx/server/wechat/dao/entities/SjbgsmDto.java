package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjbgsmDto")
public class SjbgsmDto extends SjbgsmModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//检测项目ID
	private String jcxmid;

	public String getJcxmid() {
		return jcxmid;
	}

	public void setJcxmid(String jcxmid) {
		this.jcxmid = jcxmid;
	}
	
}
