package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="DwblxxDto")
public class DwblxxDto {
	private String hzxm;
	private String xb;
	private String nl;
	private String jclxmc;
	private String jg;
	private String ysjg;
	private String parameters;
	private String bytes;
	//标本编号
	private String bbbh;
	//内部编号
	private String nbbm;
	//电话
	private String dh;
	//送检单位
	private String sjdw;
	//科室名称
	private String ksmc;
	//送检医生
	private String sjys;
	//标本类型
	private String bblxmc;
	//采样日期
	private String cyrq;
	//接收日期
	private String jsrq;
	//报告日期
	private String bgrq;

	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getBbbh() {
		return bbbh;
	}

	public void setBbbh(String bbbh) {
		this.bbbh = bbbh;
	}

	public String getJclxmc() {
		return jclxmc;
	}

	public void setJclxmc(String jclxmc) {
		this.jclxmc = jclxmc;
	}

	public String getJg() {
		return jg;
	}

	public void setJg(String jg) {
		this.jg = jg;
	}

	public String getYsjg() {
		return ysjg;
	}

	public void setYsjg(String ysjg) {
		this.ysjg = ysjg;
	}


	public String getDh() {
		return dh;
	}

	public void setDh(String dh) {
		this.dh = dh;
	}

	public String getSjdw() {
		return sjdw;
	}

	public void setSjdw(String sjdw) {
		this.sjdw = sjdw;
	}

	public String getKsmc() {
		return ksmc;
	}

	public void setKsmc(String ksmc) {
		this.ksmc = ksmc;
	}

	public String getSjys() {
		return sjys;
	}

	public void setSjys(String sjys) {
		this.sjys = sjys;
	}

	public String getBblxmc() {
		return bblxmc;
	}

	public void setBblxmc(String bblxmc) {
		this.bblxmc = bblxmc;
	}

	public String getCyrq() {
		return cyrq;
	}

	public void setCyrq(String cyrq) {
		this.cyrq = cyrq;
	}

	public String getJsrq() {
		return jsrq;
	}

	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}

	public String getBgrq() {
		return bgrq;
	}

	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getBytes() {
		return bytes;
	}

	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

}
