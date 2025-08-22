package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;


@Alias(value="LyrkxxModel")
public class LyrkxxModel extends BaseBasicModel {
	//留样入库id
	private String lyrkid;
	//领料id
	private String llid;
	//货物id
	private String hwid;
	//数量
	private String sl;

	public String getLyrkid() {
		return lyrkid;
	}

	public void setLyrkid(String lyrkid) {
		this.lyrkid = lyrkid;
	}

	public String getLlid() {
		return llid;
	}

	public void setLlid(String llid) {
		this.llid = llid;
	}

	public String getHwid() {
		return hwid;
	}

	public void setHwid(String hwid) {
		this.hwid = hwid;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
