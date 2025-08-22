package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="DhthclModel")
public class DhthclModel extends BaseBasicModel{
	//退回处理ID
	private String dhthid;
	//到货明细ID
	private String hwid;
	//合同明细ID
	private String htmxid;
	//请购明细ID
	private String qgmxid;
	//类型 区分初验退回，质检退回 提交后再改数据一律不允许
	private String lx;
	//物料ID
	private String wlid;
	//数量
	private String sl;
	//处理编号
	private String clbh;
	//处理措施
	private String clcs;
	//处理人员
	private String clry;
	//处理日期
	private String clrq;
	//备注
	private String bz;
	//状态 处理退回提交进行审核
	private String zt;
	//退回处理ID
	public String getDhthid() {
		return dhthid;
	}
	public void setDhthid(String dhthid){
		this.dhthid = dhthid;
	}
	//到货明细ID
	public String getHwid() {
		return hwid;
	}
	public void setHwid(String hwid){
		this.hwid = hwid;
	}
	//合同明细ID
	public String getHtmxid() {
		return htmxid;
	}
	public void setHtmxid(String htmxid){
		this.htmxid = htmxid;
	}
	//请购明细ID
	public String getQgmxid() {
		return qgmxid;
	}
	public void setQgmxid(String qgmxid){
		this.qgmxid = qgmxid;
	}
	//类型 区分初验退回，质检退回 提交后再改数据一律不允许
	public String getLx() {
		return lx;
	}
	public void setLx(String lx){
		this.lx = lx;
	}
	//物料ID
	public String getWlid() {
		return wlid;
	}
	public void setWlid(String wlid){
		this.wlid = wlid;
	}
	//数量
	public String getSl() {
		return sl;
	}
	public void setSl(String sl){
		this.sl = sl;
	}
	//处理编号
	public String getClbh() {
		return clbh;
	}
	public void setClbh(String clbh){
		this.clbh = clbh;
	}
	//处理措施
	public String getClcs() {
		return clcs;
	}
	public void setClcs(String clcs){
		this.clcs = clcs;
	}
	//处理人员
	public String getClry() {
		return clry;
	}
	public void setClry(String clry){
		this.clry = clry;
	}
	//处理日期
	public String getClrq() {
		return clrq;
	}
	public void setClrq(String clrq){
		this.clrq = clrq;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//状态 处理退回提交进行审核
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
