package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="GgglModel")
public class GgglModel extends BaseModel{
	private String bt;//标题
	private String fbr;//发布人
	private String ggid;//公告id
	private String nr;//内容
	private String rdjz;//热度截止
	private String rdxs;//热度显示
	private String rq;//日期
	private String ggjzrq;//公告截止日期

	public String getGgjzrq() {
		return ggjzrq;
	}

	public void setGgjzrq(String ggjzrq) {
		this.ggjzrq = ggjzrq;
	}

	public String getBt() {
		return bt;
	}

	public void setBt(String bt) {
		this.bt = bt;
	}

	public String getFbr() {
		return fbr;
	}

	public void setFbr(String fbr) {
		this.fbr = fbr;
	}

	public String getGgid() {
		return ggid;
	}

	public void setGgid(String ggid) {
		this.ggid = ggid;
	}

	public String getNr() {
		return nr;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}

	public String getRdjz() {
		return rdjz;
	}

	public void setRdjz(String rdjz) {
		this.rdjz = rdjz;
	}

	public String getRdxs() {
		return rdxs;
	}

	public void setRdxs(String rdxs) {
		this.rdxs = rdxs;
	}

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
