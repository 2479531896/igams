package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "MxtzglModel")
public class MxtzglModel extends BaseModel {
	//人员ID
	private String ryid;
	//通知时间
	private String tzsj;

	public String getRyid() {
		return ryid;
	}

	public void setRyid(String ryid) {
		this.ryid = ryid;
	}

	public String getTzsj() {
		return tzsj;
	}

	public void setTzsj(String tzsj) {
		this.tzsj = tzsj;
	}

	/**
     *
     */
    private static final long serialVersionUID = 1L;
}
