package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="HtmxModel")
public class HtmxModel extends BaseModel{
	//合同明细ID
	private String htmxid;
	//合同ID
	private String htid;
	//请购ID
	private String qgid;
	//请购明细ID
	private String qgmxid;
	//序号
	private String xh;
	//物料ID
	private String wlid;
	//货物名称
	private String hwmc;
	//数量
	private String sl;
	//含税单价
	private String hsdj;
	//无税单价
	private String wsdj;
	//税率  从系统设置表里获取
	private String suil;
	//合计金额  含税
	private String hjje;
	//计划到货日期
	private String jhdhrq;
	//备注
	private String bz;
	//状态   
	private String zt;
	//U8明细ID PO_Podetails  ID
	private String u8mxid;
	//合同明细ID
	public String getHtmxid() {
		return htmxid;
	}
	public void setHtmxid(String htmxid){
		this.htmxid = htmxid;
	}
	//合同ID
	public String getHtid() {
		return htid;
	}
	public void setHtid(String htid){
		this.htid = htid;
	}
	//请购ID
	public String getQgid() {
		return qgid;
	}
	public void setQgid(String qgid){
		this.qgid = qgid;
	}
	//请购明细ID
	public String getQgmxid() {
		return qgmxid;
	}
	public void setQgmxid(String qgmxid){
		this.qgmxid = qgmxid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//物料ID
	public String getWlid() {
		return wlid;
	}
	public void setWlid(String wlid){
		this.wlid = wlid;
	}
	//货物名称
	public String getHwmc() {
		return hwmc;
	}
	public void setHwmc(String hwmc){
		this.hwmc = hwmc;
	}
	//数量
	public String getSl() {
		return sl;
	}
	public void setSl(String sl){
		this.sl = sl;
	}
	//含税单价
	public String getHsdj() {
		return hsdj;
	}
	public void setHsdj(String hsdj){
		this.hsdj = hsdj;
	}
	//无税单价
	public String getWsdj() {
		return wsdj;
	}
	public void setWsdj(String wsdj){
		this.wsdj = wsdj;
	}
	//税率  从系统设置表里获取
	public String getSuil() {
		return suil;
	}
	public void setSuil(String suil){
		this.suil = suil;
	}
	//合计金额  含税
	public String getHjje() {
		return hjje;
	}
	public void setHjje(String hjje){
		this.hjje = hjje;
	}
	//计划到货日期
	public String getJhdhrq() {
		return jhdhrq;
	}
	public void setJhdhrq(String jhdhrq){
		this.jhdhrq = jhdhrq;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//状态   
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}
	//U8明细ID PO_Podetails  ID
	public String getU8mxid() {
		return u8mxid;
	}
	public void setU8mxid(String u8mxid){
		this.u8mxid = u8mxid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
