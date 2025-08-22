package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="FhmxModel")
public class FhmxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//发货明细id
	private String fhmxid;
	//发货id
	private String fhid;
	//出库id
	private String ckmxid;
	//仓库
	private String ck;
	//货位
	private String hw;
	//货物id
	private String hwid;
	//发货数量
	private String fhsl;
	//退货数量
	private String thsl;
	//备注
	private String bz;
	//销售订单
	private String xsdd;
	//订单明细id
	private String ddmxid;
	//扣率
	private String kl;
	//扣率2
	private String kl2;
	//税率
	private String suil;
	//含税单价
	private String hsdj;
	//无税单价
	private String wsdj;
	//报价
	private String bj;
	//订单序号
	private String ddxh;
	//发货明细关联id
	private String fhmxglid;
	//发货明细关联id
	private String glfhmxid;
	//销售明细ID
	private String  xsmxid;

	public String getXsmxid() {
		return xsmxid;
	}

	public void setXsmxid(String xsmxid) {
		this.xsmxid = xsmxid;
	}

	//关联发货明细id
	public String getGlfhmxid() {
		return glfhmxid;
	}

	public void setGlfhmxid(String glfhmxid) {
		this.glfhmxid = glfhmxid;
	}

	public String getFhmxglid() {
		return fhmxglid;
	}
	public void setFhmxglid(String fhmxglid) {
		this.fhmxglid = fhmxglid;
	}
	public String getDdxh() {
		return ddxh;
	}
	public void setDdxh(String ddxh) {
		this.ddxh = ddxh;
	}
	public String getKl() {
		return kl;
	}
	public void setKl(String kl) {
		this.kl = kl;
	}
	public String getKl2() {
		return kl2;
	}
	public void setKl2(String kl2) {
		this.kl2 = kl2;
	}
	public String getSuil() {
		return suil;
	}
	public void setSuil(String suil) {
		this.suil = suil;
	}
	public String getHsdj() {
		return hsdj;
	}
	public void setHsdj(String hsdj) {
		this.hsdj = hsdj;
	}
	public String getWsdj() {
		return wsdj;
	}
	public void setWsdj(String wsdj) {
		this.wsdj = wsdj;
	}
	public String getBj() {
		return bj;
	}
	public void setBj(String bj) {
		this.bj = bj;
	}
	public String getXsdd() {
		return xsdd;
	}
	public void setXsdd(String xsdd) {
		this.xsdd = xsdd;
	}
	public String getDdmxid() {
		return ddmxid;
	}
	public void setDdmxid(String ddmxid) {
		this.ddmxid = ddmxid;
	}
	public String getFhmxid() {
		return fhmxid;
	}
	public void setFhmxid(String fhmxid) {
		this.fhmxid = fhmxid;
	}
	public String getFhid() {
		return fhid;
	}
	public void setFhid(String fhid) {
		this.fhid = fhid;
	}

	public String getCkmxid() {
		return ckmxid;
	}
	public void setCkmxid(String ckmxid) {
		this.ckmxid = ckmxid;
	}
	public String getCk() {
		return ck;
	}
	public void setCk(String ck) {
		this.ck = ck;
	}
	public String getHw() {
		return hw;
	}
	public void setHw(String hw) {
		this.hw = hw;
	}
	public String getHwid() {
		return hwid;
	}
	public void setHwid(String hwid) {
		this.hwid = hwid;
	}
	public String getFhsl() {
		return fhsl;
	}
	public void setFhsl(String fhsl) {
		this.fhsl = fhsl;
	}
	public String getThsl() {
		return thsl;
	}
	public void setThsl(String thsl) {
		this.thsl = thsl;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	
	
}
