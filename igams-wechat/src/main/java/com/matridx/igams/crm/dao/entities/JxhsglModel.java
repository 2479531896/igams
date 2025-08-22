package com.matridx.igams.crm.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="JxhsglModel")
public class JxhsglModel extends BaseBasicModel {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	//绩效核算管理ID
	private String jxhsglid;
	//名称
	private String mc;
	//备注
	private String bz;
	//状态
	private String zt;
	//负责人
	private String fzr;
	//税点
	private String sd;
	//绩效核算总额
	private String jxhsze;
	//税后总额
	private String shze;
	//绩效开始日期
	private String jxksrq;
	//绩效结束日期
	private String jxjsrq;
	//是否核对
	private String sfhd;
	//核对人员
	private String hdry;

	public String getJxhsglid() {
		return jxhsglid;
	}

	public void setJxhsglid(String jxhsglid) {
		this.jxhsglid = jxhsglid;
	}

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
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

	public String getFzr() {
		return fzr;
	}

	public void setFzr(String fzr) {
		this.fzr = fzr;
	}

	public String getSd() {
		return sd;
	}

	public void setSd(String sd) {
		this.sd = sd;
	}

	public String getJxhsze() {
		return jxhsze;
	}

	public void setJxhsze(String jxhsze) {
		this.jxhsze = jxhsze;
	}

	public String getShze() {
		return shze;
	}

	public void setShze(String shze) {
		this.shze = shze;
	}

	public String getJxksrq() {
		return jxksrq;
	}

	public void setJxksrq(String jxksrq) {
		this.jxksrq = jxksrq;
	}

	public String getJxjsrq() {
		return jxjsrq;
	}

	public void setJxjsrq(String jxjsrq) {
		this.jxjsrq = jxjsrq;
	}

	public String getSfhd() {
		return sfhd;
	}

	public void setSfhd(String sfhd) {
		this.sfhd = sfhd;
	}

	public String getHdry() {
		return hdry;
	}

	public void setHdry(String hdry) {
		this.hdry = hdry;
	}
}
