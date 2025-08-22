package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;


@Alias(value="CkjgxxModel")
public class CkjgxxModel extends BaseModel {

	//仓库机构ID
	private String ckjgid;
	//仓库ID
	private String ckid;
	//机构ID
	private String jgid;

	public String getCkjgid() {
		return ckjgid;
	}

	public void setCkjgid(String ckjgid) {
		this.ckjgid = ckjgid;
	}

	public String getCkid() {
		return ckid;
	}

	public void setCkid(String ckid) {
		this.ckid = ckid;
	}

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
