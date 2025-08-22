package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="HtfktxModel")
public class HtfktxModel extends BaseBasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//合同ID
	private String htid;

	//序号
	private String xh;

	//预付比例
	private String yfbl;

	//预付日期
	private String yfrq;

	//付款ID
	private String htfkid;

	//备注
	private String bz;

	//预付金额
	private String yfje;

	//付款提醒ID
	private String fktxid;

	public String getYfje() {
		return yfje;
	}

	public void setYfje(String yfje) {
		this.yfje = yfje;
	}

	public String getFktxid() {
		return fktxid;
	}

	public void setFktxid(String fktxid) {
		this.fktxid = fktxid;
	}

	public String getHtid() {
		return htid;
	}

	public void setHtid(String htid) {
		this.htid = htid;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getYfbl() {
		return yfbl;
	}

	public void setYfbl(String yfbl) {
		this.yfbl = yfbl;
	}

	public String getYfrq() {
		return yfrq;
	}

	public void setYfrq(String yfrq) {
		this.yfrq = yfrq;
	}

	public String getHtfkid() {
		return htfkid;
	}

	public void setHtfkid(String htfkid) {
		this.htfkid = htfkid;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
}
