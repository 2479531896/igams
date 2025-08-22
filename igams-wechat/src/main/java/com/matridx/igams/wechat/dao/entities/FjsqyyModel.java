package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;
import com.matridx.igams.common.dao.BaseModel;

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
