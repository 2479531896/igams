package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="JccglModel")
public class JccglModel extends BaseBasicModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//人员ID
	private String ryid;
	//仓库货物id
	private String ckhwid;
	public String getRyid() {
		return ryid;
	}
	public void setRyid(String ryid) {
		this.ryid = ryid;
	}

	public String getCkhwid() {
		return ckhwid;
	}

	public void setCkhwid(String ckhwid) {
		this.ckhwid = ckhwid;
	}
}
