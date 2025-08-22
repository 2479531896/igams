package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XzrkmxDto")
public class XzrkmxDto extends XzrkmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//入库日期
	private String rkrq;
	//入库单号
	private String rkdh;
	//入库人员
	private String rkry;
	//状态 00：未提交  10：审核中
	private String zt;
	//仓库名称
	private String ckmc;
	//库位名称
	private String kwmc;
	// 全部(查询条件)
	private String entire;
	//高级筛选用数组
	//警戒提示
	private String[] jjtss;
	//审核状态
	private String[] zts;
	//数量
	private String sl;
	//仓库
	private String ck;
	//库位
	private String kw;
	//货物备注
	private String hwbz;
	//已入库数量
	private String yrksl;
	//行政库存ID
	private String xzkcid;
	//入库货物名称
	private String rkhwmc;
	//入库货物标准
	private String rkhwbz;
	//入库库存ID
	private String rkkcid;
	private String xzrksl;
	//行政库存量
	private String xzkcl;
	//可入库数量
	private String krksl;
	private String djh;
	private String qgid;
	private String rkrymc;
	private String rklbmc;

	public String getRkrymc() {
		return rkrymc;
	}

	public void setRkrymc(String rkrymc) {
		this.rkrymc = rkrymc;
	}

	public String getRklbmc() {
		return rklbmc;
	}

	public void setRklbmc(String rklbmc) {
		this.rklbmc = rklbmc;
	}

	public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
	}

	public String getQgid() {
		return qgid;
	}

	public void setQgid(String qgid) {
		this.qgid = qgid;
	}

	public String getKrksl() {
		return krksl;
	}

	public void setKrksl(String krksl) {
		this.krksl = krksl;
	}

	public String getXzkcl() {
		return xzkcl;
	}

	public void setXzkcl(String xzkcl) {
		this.xzkcl = xzkcl;
	}
	public String getXzrksl() {
		return xzrksl;
	}

	public void setXzrksl(String xzrksl) {
		this.xzrksl = xzrksl;
	}

	public String getRkkcid() {
		return rkkcid;
	}

	public void setRkkcid(String rkkcid) {
		this.rkkcid = rkkcid;
	}

	public String getRkhwmc() {
		return rkhwmc;
	}

	public void setRkhwmc(String rkhwmc) {
		this.rkhwmc = rkhwmc;
	}

	public String getRkhwbz() {
		return rkhwbz;
	}

	public void setRkhwbz(String rkhwbz) {
		this.rkhwbz = rkhwbz;
	}

	public String getXzkcid() {
		return xzkcid;
	}

	public void setXzkcid(String xzkcid) {
		this.xzkcid = xzkcid;
	}

	public String getYrksl() {
		return yrksl;
	}

	public void setYrksl(String yrksl) {
		this.yrksl = yrksl;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getCk() {
		return ck;
	}

	public void setCk(String ck) {
		this.ck = ck;
	}

	public String getKw() {
		return kw;
	}

	public void setKw(String kw) {
		this.kw = kw;
	}

	public String getHwbz() {
		return hwbz;
	}

	public void setHwbz(String hwbz) {
		this.hwbz = hwbz;
	}

	public String getRkrq() {
		return rkrq;
	}

	public void setRkrq(String rkrq) {
		this.rkrq = rkrq;
	}

	public String getRkdh() {
		return rkdh;
	}

	public void setRkdh(String rkdh) {
		this.rkdh = rkdh;
	}

	public String getRkry() {
		return rkry;
	}

	public void setRkry(String rkry) {
		this.rkry = rkry;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	public String getKwmc() {
		return kwmc;
	}

	public void setKwmc(String kwmc) {
		this.kwmc = kwmc;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
		for (int i = 0; i < zts.length; i++){
			this.zts[i]=this.zts[i].replace("'","");
		}
	}

	public String[] getJjtss() {
		return jjtss;
	}

	public void setJjtss(String[] jjtss) {
		this.jjtss = jjtss;
		for (int i = 0; i < jjtss.length; i++){
			this.jjtss[i]=this.jjtss[i].replace("'","");
		}
	}
}
