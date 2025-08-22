package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="HtfkmxDto")
public class HtfkmxDto extends HtfkmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//总付款金额
	private String zfkje;
	//未付款金额
	private String wfkje;
	//合同内部编号
	private String htnbbh;
	//合同外部编号
	private String htwbbh;
	//订单日期
	private String ddrq;
	//合同业务类型名称
	private String ywlxmc;
	//付款日期
	private String fkrq;
	//付款提醒ID
	private String fktxid;
	//已付金额
	private String yfje;
	//未付金额
	private String wfje;
	//付款中金额
	private String fkzje;
	//总金额
	private String zje;
	//录入人员名称
	private String lrrymc;
	// 创建日期结束日期
	private String fksjend;
	// 创建日期开始日期
	private String fksjstart;
	private String zt;
	private String zfdxmc;
	//已付百分比
	private String yfbfb;
	//未付百分比
	private String wfbfb;
	//付款中百分比
	private String fkzbfb;
	//合同钉钉实例ID
	private String htddslid;
	private String sqlParam;
	private String xzje;

	public String getXzje() {
		return xzje;
	}

	public void setXzje(String xzje) {
		this.xzje = xzje;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getHtddslid() {
		return htddslid;
	}

	public void setHtddslid(String htddslid) {
		this.htddslid = htddslid;
	}

	public String getYfbfb() {
		return yfbfb;
	}

	public void setYfbfb(String yfbfb) {
		this.yfbfb = yfbfb;
	}

	public String getWfbfb() {
		return wfbfb;
	}

	public void setWfbfb(String wfbfb) {
		this.wfbfb = wfbfb;
	}

	public String getFkzbfb() {
		return fkzbfb;
	}

	public void setFkzbfb(String fkzbfb) {
		this.fkzbfb = fkzbfb;
	}

	public String getZfdxmc() {
		return zfdxmc;
	}

	public void setZfdxmc(String zfdxmc) {
		this.zfdxmc = zfdxmc;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getFksjend() {
		return fksjend;
	}

	public void setFksjend(String fksjend) {
		this.fksjend = fksjend;
	}

	public String getFksjstart() {
		return fksjstart;
	}

	public void setFksjstart(String fksjstart) {
		this.fksjstart = fksjstart;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getZje() {
		return zje;
	}

	public void setZje(String zje) {
		this.zje = zje;
	}

	public String getYfje() {
		return yfje;
	}

	public void setYfje(String yfje) {
		this.yfje = yfje;
	}

	public String getWfje() {
		return wfje;
	}

	public void setWfje(String wfje) {
		this.wfje = wfje;
	}

	public String getFkzje() {
		return fkzje;
	}

	public void setFkzje(String fkzje) {
		this.fkzje = fkzje;
	}


	public String getHtwbbh() {
		return htwbbh;
	}

	public void setHtwbbh(String htwbbh) {
		this.htwbbh = htwbbh;
	}

	public String getDdrq() {
		return ddrq;
	}

	public void setDdrq(String ddrq) {
		this.ddrq = ddrq;
	}



	public String getYwlxmc() {
		return ywlxmc;
	}

	public void setYwlxmc(String ywlxmc) {
		this.ywlxmc = ywlxmc;
	}

	public String getFktxid() {
		return fktxid;
	}

	public void setFktxid(String fktxid) {
		this.fktxid = fktxid;
	}

	public String getFkrq() {
		return fkrq;
	}

	public void setFkrq(String fkrq) {
		this.fkrq = fkrq;
	}

	public String getHtnbbh() {
		return htnbbh;
	}

	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}

	public String getZfkje() {
		return zfkje;
	}

	public void setZfkje(String zfkje) {
		this.zfkje = zfkje;
	}

	public String getWfkje() {
		return wfkje;
	}

	public void setWfkje(String wfkje) {
		this.wfkje = wfkje;
	}
}
