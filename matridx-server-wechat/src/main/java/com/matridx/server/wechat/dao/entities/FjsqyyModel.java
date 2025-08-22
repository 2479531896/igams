package com.matridx.server.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="FjsqyyModel")
public class FjsqyyModel extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//复检id
	private String fjid;
	//原因
	private String yy;

	public String getFjid() {
		return fjid;
	}

	public void setFjid(String fjid) {
		this.fjid = fjid;
	}

	public String getYy() {
		return yy;
	}

	public void setYy(String yy) {
		this.yy = yy;
	}
}
