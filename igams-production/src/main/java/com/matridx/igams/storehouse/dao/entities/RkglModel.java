package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="RkglModel")
public class RkglModel extends BaseBasicModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//入库ID
	private String rkid;
	//入库单号
	private String rkdh;
	//入库日期
	private String rkrq;
	//入库类别 基础数据
	private String rklb;
	//采购类型 基础数据
	private String cglx;
	//部门
	private String bm;
	//仓库ID
	private String ckid;
	//供应商ID
	private String gysid;
	//备注
	private String bz;
	//关联ID：关联U8系统中的主键ID
	private String glid;
	//状态 00：未提交  10：审核中
	private String zt;
	//调拨关联ID
	private String dbglid;
	//其他入库关联ID
	private String qtrkglid;
	//其他出库关联ID
	private String qtckglid;
	private String dhlxdm;

	public String getDhlxdm() {
		return dhlxdm;
	}

	public void setDhlxdm(String dhlxdm) {
		this.dhlxdm = dhlxdm;
	}

	public String getQtrkglid() {
		return qtrkglid;
	}

	public void setQtrkglid(String qtrkglid) {
		this.qtrkglid = qtrkglid;
	}

	public String getQtckglid() {
		return qtckglid;
	}

	public void setQtckglid(String qtckglid) {
		this.qtckglid = qtckglid;
	}

	public String getDbglid() {
		return dbglid;
	}
	public void setDbglid(String dbglid) {
		this.dbglid = dbglid;
	}
	public String getRkid() {
		return rkid;
	}
	public void setRkid(String rkid) {
		this.rkid = rkid;
	}
	public String getRkdh() {
		return rkdh;
	}
	public void setRkdh(String rkdh) {
		this.rkdh = rkdh;
	}
	public String getRkrq() {
		return rkrq;
	}
	public void setRkrq(String rkrq) {
		this.rkrq = rkrq;
	}
	public String getRklb() {
		return rklb;
	}
	public void setRklb(String rklb) {
		this.rklb = rklb;
	}
	public String getCglx() {
		return cglx;
	}
	public void setCglx(String cglx) {
		this.cglx = cglx;
	}
	
	public String getBm() {
		return bm;
	}
	public void setBm(String bm) {
		this.bm = bm;
	}
	public String getCkid() {
		return ckid;
	}
	public void setCkid(String ckid) {
		this.ckid = ckid;
	}
	public String getGysid() {
		return gysid;
	}
	public void setGysid(String gysid) {
		this.gysid = gysid;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getGlid() {
		return glid;
	}
	public void setGlid(String glid) {
		this.glid = glid;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	
	
}
