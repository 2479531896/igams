package com.matridx.server.detection.molecule.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="FzbbztModel")
public class FzbbztModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//分子检测ID
	private String fzjcid;
	//序号
	private String xh;
	//状态
	private String zt;

	public String getFzjcid() {
		return fzjcid;
	}

	public void setFzjcid(String fzjcid) {
		this.fzjcid = fzjcid;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}
}
