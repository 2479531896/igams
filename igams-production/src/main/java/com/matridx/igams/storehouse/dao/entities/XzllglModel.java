package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;



@Alias(value="XzllglModel")
public class XzllglModel extends BaseBasicModel{

	private static final long serialVersionUID = 1L;

	//行政领料ID
	private String xzllid;
	//领料单号
	private String lldh;
	//申请人员
	private String sqry;
	//申请日期
	private String sqrq;
	//申请部门
	private String sqbm;
	//领料人员
	private String llry;
	//备注
	private String bz;
	//状态
	private String zt;
	//钉钉审批ID
	private String ddslid;
	private String lllx;//领料类型

	public String getLllx() {
		return lllx;
	}

	public void setLllx(String lllx) {
		this.lllx = lllx;
	}

	public String getDdslid() {
		return ddslid;
	}

	public void setDdslid(String ddslid) {
		this.ddslid = ddslid;
	}

	public String getSqry() {
		return sqry;
	}

	public void setSqry(String sqry) {
		this.sqry = sqry;
	}

	public String getXzllid() {
		return xzllid;
	}

	public void setXzllid(String xzllid) {
		this.xzllid = xzllid;
	}

	public String getLldh() {
		return lldh;
	}

	public void setLldh(String lldh) {
		this.lldh = lldh;
	}

	public String getSqrq() {
		return sqrq;
	}

	public void setSqrq(String sqrq) {
		this.sqrq = sqrq;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getLlry() {
		return llry;
	}

	public void setLlry(String llry) {
		this.llry = llry;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}
}
