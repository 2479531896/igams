package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="KhyhzcbzModel")
public class KhyhzcbzModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String khyhzcbzid;

	private String khid;

	private String yhfl;

	private String yhzfl;

	private String yhqsfw;

	private String yhjsfw;

	private String yh;

	private String htyhzcid;

	private String htid;

	private String yhxs;

	public String getKhyhzcbzid() {
		return khyhzcbzid;
	}

	public void setKhyhzcbzid(String khyhzcbzid) {
		this.khyhzcbzid = khyhzcbzid;
	}

	public String getKhid() {
		return khid;
	}

	public void setKhid(String khid) {
		this.khid = khid;
	}

	public String getYhfl() {
		return yhfl;
	}

	public void setYhfl(String yhfl) {
		this.yhfl = yhfl;
	}

	public String getYhzfl() {
		return yhzfl;
	}

	public void setYhzfl(String yhzfl) {
		this.yhzfl = yhzfl;
	}

	public String getYhqsfw() {
		return yhqsfw;
	}

	public void setYhqsfw(String yhqsfw) {
		this.yhqsfw = yhqsfw;
	}

	public String getYhjsfw() {
		return yhjsfw;
	}

	public void setYhjsfw(String yhjsfw) {
		this.yhjsfw = yhjsfw;
	}

	public String getYh() {
		return yh;
	}

	public void setYh(String yh) {
		this.yh = yh;
	}

	public String getHtyhzcid() {
		return htyhzcid;
	}

	public void setHtyhzcid(String htyhzcid) {
		this.htyhzcid = htyhzcid;
	}

	public String getHtid() {
		return htid;
	}

	public void setHtid(String htid) {
		this.htid = htid;
	}

	public String getYhxs() {
		return yhxs;
	}

	public void setYhxs(String yhxs) {
		this.yhxs = yhxs;
	}
}
