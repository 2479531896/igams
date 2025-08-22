package com.matridx.server.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="FzjcxmModel")
public class FzjcxmModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//分子项目ID
	private String fzxmid;
	//分子检测ID
	private String fzjcid;
	//分子检测项目ID
	private String fzjcxmid;
	//分子检测子项目ID
	private String fzjczxmid;

	//实验号
	private String syh;
	//报告日期
	private String bgrq;
	//状态
	private String zt;

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getBgrq() {
		return bgrq;
	}

	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}

	public String getSyh() {
		return syh;
	}

	public void setSyh(String syh) {
		this.syh = syh;
	}

	public String getFzxmid() {
		return fzxmid;
	}

	public void setFzxmid(String fzxmid) {
		this.fzxmid = fzxmid;
	}

	public String getFzjcid() {
		return fzjcid;
	}

	public void setFzjcid(String fzjcid) {
		this.fzjcid = fzjcid;
	}

	public String getFzjcxmid() {
		return fzjcxmid;
	}

	public void setFzjcxmid(String fzjcxmid) {
		this.fzjcxmid = fzjcxmid;
	}

	public String getFzjczxmid() {
		return fzjczxmid;
	}

	public void setFzjczxmid(String fzjczxmid) {
		this.fzjczxmid = fzjczxmid;
	}
}
