package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="JcsqmxModel")
public class JcsqmxModel extends BaseModel{

	//检出明细ID
	private String sqmxid;
	//检出管理ID
	private String sqglid;
	//送检ID
	private String sjid;

	public String getSqmxid() {
		return sqmxid;
	}

	public void setSqmxid(String sqmxid) {
		this.sqmxid = sqmxid;
	}

	public String getSqglid() {
		return sqglid;
	}

	public void setSqglid(String sqglid) {
		this.sqglid = sqglid;
	}

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
