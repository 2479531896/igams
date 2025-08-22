package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;


@Alias(value="XzqgqrglModel")
public class XzqgqrglModel extends BaseBasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//确认ID
	private String qrid;
	//确认单号
	private String qrdh;
	//支付对象
	private String zfdx;
	//总金额
	private String zje;
	//备注
	private String bz;
	//对账方式
	private String dzfs;
	//钉钉实例ID
	private String ddslid;
	//状态
	private String zt;
	//申请人
	private String sqr;
	//申请部门
	private String sqbm;
	//付款完成标记
	private String fkwcbj;

	public String getFkwcbj() {
		return fkwcbj;
	}

	public void setFkwcbj(String fkwcbj) {
		this.fkwcbj = fkwcbj;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getQrid() {
		return qrid;
	}

	public void setQrid(String qrid) {
		this.qrid = qrid;
	}

	public String getQrdh() {
		return qrdh;
	}

	public void setQrdh(String qrdh) {
		this.qrdh = qrdh;
	}

	public String getZfdx() {
		return zfdx;
	}

	public void setZfdx(String zfdx) {
		this.zfdx = zfdx;
	}

	public String getZje() {
		return zje;
	}

	public void setZje(String zje) {
		this.zje = zje;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getDzfs() {
		return dzfs;
	}

	public void setDzfs(String dzfs) {
		this.dzfs = dzfs;
	}

	public String getDdslid() {
		return ddslid;
	}

	public void setDdslid(String ddslid) {
		this.ddslid = ddslid;
	}
}
