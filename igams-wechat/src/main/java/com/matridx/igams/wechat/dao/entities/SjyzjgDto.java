package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;


@Alias(value="SjyzjgDto")
public class SjyzjgDto extends SjyzjgModel{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ct	;
	private String tm1;
	private String tm2;
	private String tm3;
	private String fz1;
	private String fz2;
	private String fz3;
	private String qfcskz;//区分参数扩展
	//验证结果名称
	private String yzjgmc;
	private String yzjgjgmc;
	//标本编号
	private String ybbh;
	private String cskz2;
	//验证类别参数
	private String yzlbcs;
	//报告时间
	private String bgsj	;
	//检测项目名称
	private String jcxmmc;
	//参数扩展1
	private String cskz1;
	private String jlmc;
	private String nbbm;
	//操作标识
	private String czbs;
	// 参数代码
	private String csdm;
	//临时序号
	private String lsxh;
	//显示时间
	private String xssj;

	public String getXssj() {
		return xssj;
	}

	public void setXssj(String xssj) {
		this.xssj = xssj;
	}

	public String getCskz2() {
		return cskz2;
	}

	public void setCskz2(String cskz2) {
		this.cskz2 = cskz2;
	}

	public String getQfcskz() {
		return qfcskz;
	}

	public void setQfcskz(String qfcskz) {
		this.qfcskz = qfcskz;
	}

	public String getLsxh() {
		return lsxh;
	}

	public void setLsxh(String lsxh) {
		this.lsxh = lsxh;
	}

	public String getCsdm() {
		return csdm;
	}

	public void setCsdm(String csdm) {
		this.csdm = csdm;
	}

	public String getCzbs() {
		return czbs;
	}

	public void setCzbs(String czbs) {
		this.czbs = czbs;
	}

	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getYzjgjgmc() {
		return yzjgjgmc;
	}

	public void setYzjgjgmc(String yzjgjgmc) {
		this.yzjgjgmc = yzjgjgmc;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getYzlbcs() {
		return yzlbcs;
	}

	public void setYzlbcs(String yzlbcs) {
		this.yzlbcs = yzlbcs;
	}

	public String getBgsj() {
		return bgsj;
	}

	public void setBgsj(String bgsj) {
		this.bgsj = bgsj;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}

	public String getJlmc() {
		return jlmc;
	}

	public void setJlmc(String jlmc) {
		this.jlmc = jlmc;
	}

	public String getYzjgmc() {
		return yzjgmc;
	}

	public void setYzjgmc(String yzjgmc) {
		this.yzjgmc = yzjgmc;
	}

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public String getTm1() {
		return tm1;
	}

	public void setTm1(String tm1) {
		this.tm1 = tm1;
	}

	public String getTm2() {
		return tm2;
	}

	public void setTm2(String tm2) {
		this.tm2 = tm2;
	}

	public String getTm3() {
		return tm3;
	}

	public void setTm3(String tm3) {
		this.tm3 = tm3;
	}

	public String getFz1() {
		return fz1;
	}

	public void setFz1(String fz1) {
		this.fz1 = fz1;
	}

	public String getFz2() {
		return fz2;
	}

	public void setFz2(String fz2) {
		this.fz2 = fz2;
	}

	public String getFz3() {
		return fz3;
	}

	public void setFz3(String fz3) {
		this.fz3 = fz3;
	}
}
