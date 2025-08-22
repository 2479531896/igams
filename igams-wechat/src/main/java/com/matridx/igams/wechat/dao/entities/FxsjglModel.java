package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="FxsjglModel")
public class FxsjglModel extends BaseBasicModel {
	private String fxsjid;//风险上机id
	private String ywid;//送检实验管理id
	private String fxlb;//风险类别 基础数据
	private String tzrq;//通知日期
	private String qrsj;//确认时间
	private String zt;//状态
	private String bz;//备注
	private String bbcl;//标本处理

	public String getBbcl() {
		return bbcl;
	}

	public void setBbcl(String bbcl) {
		this.bbcl = bbcl;
	}

	public String getFxsjid() {
		return fxsjid;
	}

	public void setFxsjid(String fxsjid) {
		this.fxsjid = fxsjid;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getFxlb() {
		return fxlb;
	}

	public void setFxlb(String fxlb) {
		this.fxlb = fxlb;
	}

	public String getTzrq() {
		return tzrq;
	}

	public void setTzrq(String tzrq) {
		this.tzrq = tzrq;
	}

	public String getQrsj() {
		return qrsj;
	}

	public void setQrsj(String qrsj) {
		this.qrsj = qrsj;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
