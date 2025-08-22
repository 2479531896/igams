package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="XsmxModel")
public class XsmxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//销售明细id
	private String xsmxid;
	//物料id
	private String wlid;
	//数量
	private String sl;
	//报价
	private String bj;
	//含税单价
	private String hsdj;
	//无税单价
	private String wsdj;
	//无税总金额
	private String wszje;
	//结算总额
	private String jsze;
	//税率
	private String suil;
	//预发货日期
	private String yfhrq;
	//销售ID
	private String xsid;
	//明细关联ID
	private String mxglid;
	//序号
	private String xh;
	//借出借用明细id
	private String jymxid;
	private String sczt;//生产状态
	private String dky;	//到款月

	public String getDky() {
		return dky;
	}

	public void setDky(String dky) {
		this.dky = dky;
	}

	public String getSczt() {
		return sczt;
	}

	public void setSczt(String sczt) {
		this.sczt = sczt;
	}

	public String getJymxid() {
		return jymxid;
	}

	public void setJymxid(String jymxid) {
		this.jymxid = jymxid;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getMxglid() {
		return mxglid;
	}

	public void setMxglid(String mxglid) {
		this.mxglid = mxglid;
	}

	public String getXsid() {
		return xsid;
	}

	public void setXsid(String xsid) {
		this.xsid = xsid;
	}

	public String getXsmxid() {
		return xsmxid;
	}

	public void setXsmxid(String xsmxid) {
		this.xsmxid = xsmxid;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getBj() {
		return bj;
	}

	public void setBj(String bj) {
		this.bj = bj;
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

	public String getWszje() {
		return wszje;
	}

	public void setWszje(String wszje) {
		this.wszje = wszje;
	}

	public String getJsze() {
		return jsze;
	}

	public void setJsze(String jsze) {
		this.jsze = jsze;
	}

	public String getSuil() {
		return suil;
	}

	public void setSuil(String suil) {
		this.suil = suil;
	}

	public String getYfhrq() {
		return yfhrq;
	}

	public void setYfhrq(String yfhrq) {
		this.yfhrq = yfhrq;
	}
}
