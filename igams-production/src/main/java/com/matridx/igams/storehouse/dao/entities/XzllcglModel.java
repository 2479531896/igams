package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XzllcglModel")
public class XzllcglModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ryid;

	private String rkmxid;

	private String xzkcid;

	public String getXzkcid() {
		return xzkcid;
	}

	public void setXzkcid(String xzkcid) {
		this.xzkcid = xzkcid;
	}

	public String getRkmxid() {
		return rkmxid;
	}

	public void setRkmxid(String rkmxid) {
		this.rkmxid = rkmxid;
	}

	public String getRyid() {
		return ryid;
	}

	public void setRyid(String ryid) {
		this.ryid = ryid;
	}

}
