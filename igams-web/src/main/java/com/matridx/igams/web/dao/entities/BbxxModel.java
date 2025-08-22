package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value = "BbxxModel")
public class BbxxModel extends BaseModel {

	// 版本ID
	private String bbid;
	// 版本标签
	private String bbbq;
	// 更新内容
	private String gxnr;
	// 操作人员
	private String czry;
	// 上线时间
	private String sxsj;

	public String getBbid() {
		return bbid;
	}

	public void setBbid(String bbid) {
		this.bbid = bbid;
	}

	public String getBbbq() {
		return bbbq;
	}

	public void setBbbq(String bbbq) {
		this.bbbq = bbbq;
	}

	public String getGxnr() {
		return gxnr;
	}

	public void setGxnr(String gxnr) {
		this.gxnr = gxnr;
	}

	public String getCzry() {
		return czry;
	}

	public void setCzry(String czry) {
		this.czry = czry;
	}

	public String getSxsj() {
		return sxsj;
	}

	public void setSxsj(String sxsj) {
		this.sxsj = sxsj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
