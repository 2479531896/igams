package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="GlwjxxModel")
public class GlwjxxModel extends BaseModel{
	private String glwjid;	//关联文件id
	private String wjid;	//文件id
	private String zwjid;	//子文件id

	public String getGlwjid() {
		return glwjid;
	}

	public void setGlwjid(String glwjid) {
		this.glwjid = glwjid;
	}

	public String getWjid() {
		return wjid;
	}

	public void setWjid(String wjid) {
		this.wjid = wjid;
	}

	public String getZwjid() {
		return zwjid;
	}

	public void setZwjid(String zwjid) {
		this.zwjid = zwjid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
