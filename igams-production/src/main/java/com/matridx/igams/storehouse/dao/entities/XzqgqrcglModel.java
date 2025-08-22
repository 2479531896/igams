package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XzqgqrcglModel")
public class XzqgqrcglModel extends BaseModel{
	//请购明细ID
	private String qgmxid;
	//人员ID
	private String ryid;

	public String getQgmxid() {
		return qgmxid;
	}

	public void setQgmxid(String qgmxid) {
		this.qgmxid = qgmxid;
	}

	public String getRyid() {
		return ryid;
	}

	public void setRyid(String ryid) {
		this.ryid = ryid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
