package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="QgqxmxModel")
public class QgqxmxModel extends BaseBasicModel{
	//请购取消明细ID
	private String qgqxmxid;
	//请购取消ID
	private String qgqxid;
	//请购明细ID
	private String qgmxid;
	//请购ID
	private String qgid;
	//物料ID
	private String wlid;
	//取消数量
	private String qxsl;
	//备注  审批备注，采购备注是否需要分开
	private String bz;
	//状态   审批状态 如果物料不通过  80：正常采购 15：取消采购  30：拒绝审批
	private String zt;
	//请购取消明细ID
	public String getQgqxmxid() {
		return qgqxmxid;
	}
	public void setQgqxmxid(String qgqxmxid){
		this.qgqxmxid = qgqxmxid;
	}
	//请购取消ID
	public String getQgqxid() {
		return qgqxid;
	}
	public void setQgqxid(String qgqxid){
		this.qgqxid = qgqxid;
	}
	//请购明细ID
	public String getQgmxid() {
		return qgmxid;
	}
	public void setQgmxid(String qgmxid){
		this.qgmxid = qgmxid;
	}
	//请购ID
	public String getQgid() {
		return qgid;
	}
	public void setQgid(String qgid){
		this.qgid = qgid;
	}
	//物料ID
	public String getWlid() {
		return wlid;
	}
	public void setWlid(String wlid){
		this.wlid = wlid;
	}
	//取消数量
	public String getQxsl() {
		return qxsl;
	}
	public void setQxsl(String qxsl){
		this.qxsl = qxsl;
	}
	//备注  审批备注，采购备注是否需要分开
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//状态   审批状态 如果物料不通过  80：正常采购 15：取消采购  30：拒绝审批
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
