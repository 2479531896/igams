package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="HtyhzcDto")
public class HtyhzcDto extends HtyhzcModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String yhflmc;//检测项目名称

	private String yhzflmc;//检测子项目名称

	private String zt;//状态

	private String khyhzcbzid;

	private String khid;

	private String yhflcskz1;

	public String getYhflcskz1() {
		return yhflcskz1;
	}

	public void setYhflcskz1(String yhflcskz1) {
		this.yhflcskz1 = yhflcskz1;
	}

	public String getKhid() {
		return khid;
	}

	public void setKhid(String khid) {
		this.khid = khid;
	}

	public String getKhyhzcbzid() {
		return khyhzcbzid;
	}

	public void setKhyhzcbzid(String khyhzcbzid) {
		this.khyhzcbzid = khyhzcbzid;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getYhflmc() {
		return yhflmc;
	}

	public void setYhflmc(String yhflmc) {
		this.yhflmc = yhflmc;
	}

	public String getYhzflmc() {
		return yhzflmc;
	}

	public void setYhzflmc(String yhzflmc) {
		this.yhzflmc = yhzflmc;
	}
}
