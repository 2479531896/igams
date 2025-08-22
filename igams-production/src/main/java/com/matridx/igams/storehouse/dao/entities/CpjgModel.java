package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;


@Alias(value="CpjgModel")
public class CpjgModel extends BaseBasicModel {

	private static final long serialVersionUID = 1L;


	private String cpjgid;
	private String lb;
	private String mjwlid;
	private String mjshl;
	private String bbdh;
	private String bbsm;
	private String bbrq;
	private String zt;
	private String u8cpjgid;
	private String quepartid;

	public String getQuepartid() {
		return quepartid;
	}

	public void setQuepartid(String quepartid) {
		this.quepartid = quepartid;
	}

	public String getU8cpjgid() {
		return u8cpjgid;
	}

	public void setU8cpjgid(String u8cpjgid) {
		this.u8cpjgid = u8cpjgid;
	}

	public String getCpjgid() {
		return cpjgid;
	}

	public void setCpjgid(String cpjgid) {
		this.cpjgid = cpjgid;
	}

	public String getLb() {
		return lb;
	}

	public void setLb(String lb) {
		this.lb = lb;
	}

	public String getMjwlid() {
		return mjwlid;
	}

	public void setMjwlid(String mjwlid) {
		this.mjwlid = mjwlid;
	}

	public String getMjshl() {
		return mjshl;
	}

	public void setMjshl(String mjshl) {
		this.mjshl = mjshl;
	}

	public String getBbdh() {
		return bbdh;
	}

	public void setBbdh(String bbdh) {
		this.bbdh = bbdh;
	}

	public String getBbsm() {
		return bbsm;
	}

	public void setBbsm(String bbsm) {
		this.bbsm = bbsm;
	}

	public String getBbrq() {
		return bbrq;
	}

	public void setBbrq(String bbrq) {
		this.bbrq = bbrq;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

}
