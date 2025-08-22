package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias("BomglModel")
public class BomglModel extends BaseModel {
	private String bomid;	//bomid
	private String mjwlid;	//母件物料id
	private String bbrq;	//版本日期
	private String bbh;		//版本号
	private String bbsm;	//版本说明
	private String bomlb;	//bom类别

	public String getBomlb() {
		return bomlb;
	}

	public void setBomlb(String bomlb) {
		this.bomlb = bomlb;
	}

	public String getBomid() {
		return bomid;
	}

	public void setBomid(String bomid) {
		this.bomid = bomid;
	}

	public String getMjwlid() {
		return mjwlid;
	}

	public void setMjwlid(String mjwlid) {
		this.mjwlid = mjwlid;
	}

	public String getBbrq() {
		return bbrq;
	}

	public void setBbrq(String bbrq) {
		this.bbrq = bbrq;
	}

	public String getBbh() {
		return bbh;
	}

	public void setBbh(String bbh) {
		this.bbh = bbh;
	}

	public String getBbsm() {
		return bbsm;
	}

	public void setBbsm(String bbsm) {
		this.bbsm = bbsm;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
