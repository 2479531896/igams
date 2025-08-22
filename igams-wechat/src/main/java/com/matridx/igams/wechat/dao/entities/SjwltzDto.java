package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjwltzDto")
public class SjwltzDto extends SjwltzModel{
	//送检派单ID
	private String sjpdid;
	//录入人员名称
	private String lrrymc;
	//取样地址
	private String qydz;
	//预计时间
	private String yjsj;
	//标本类型名称
	private String bblxmc;
	//检测单位名称
	private String jcdwmc;
	//关联单号
	private String gldh;
	//entire
	private String entire;
	//人员名称
	private String rymc;
	//用户ddid
	private String ddid;
	//用户名
	private String yhm;

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	@Override
	public String getDdid() {
		return ddid;
	}

	@Override
	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getRymc() {
		return rymc;
	}

	public void setRymc(String rymc) {
		this.rymc = rymc;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getSjpdid() {
		return sjpdid;
	}

	public void setSjpdid(String sjpdid) {
		this.sjpdid = sjpdid;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getQydz() {
		return qydz;
	}

	public void setQydz(String qydz) {
		this.qydz = qydz;
	}

	public String getYjsj() {
		return yjsj;
	}

	public void setYjsj(String yjsj) {
		this.yjsj = yjsj;
	}

	public String getBblxmc() {
		return bblxmc;
	}

	public void setBblxmc(String bblxmc) {
		this.bblxmc = bblxmc;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String getGldh() {
		return gldh;
	}

	public void setGldh(String gldh) {
		this.gldh = gldh;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
