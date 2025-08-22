package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WlcstxModel")
public class WlcstxModel extends BaseModel{

	private static final long serialVersionUID = 1L;
    private String ywid;//业务ID
	private String ywlx;//业务类型
	private String cscs;//超时次数

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getCscs() {
		return cscs;
	}

	public void setCscs(String cscs) {
		this.cscs = cscs;
	}
}
