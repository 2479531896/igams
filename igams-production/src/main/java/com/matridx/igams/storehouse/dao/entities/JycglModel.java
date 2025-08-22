package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
/**
 * 
 * @author lifan
 *检验车管理Model
 */
@Alias(value="JycglModel")
public class JycglModel extends BaseBasicModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//人员id
	private String ryid;
	//货物id
	private String hwid;
	
	public String getRyid() {
		return ryid;
	}
	public void setRyid(String ryid) {
		this.ryid = ryid;
	}
	public String getHwid() {
		return hwid;
	}
	public void setHwid(String hwid) {
		this.hwid = hwid;
	}
}
