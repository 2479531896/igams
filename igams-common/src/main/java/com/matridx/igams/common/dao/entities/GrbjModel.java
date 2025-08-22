package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value = "GrbjModel")
public class GrbjModel extends BaseModel {
    private String bj;//笔记
    private String grbjid;//个人笔记id
    private String jjcd;//紧急程度
    private String rq;//日期
    private String tbrc;//同步日程
    private String yhid;//用户id

	public String getBj() {
		return bj;
	}

	public void setBj(String bj) {
		this.bj = bj;
	}

	public String getGrbjid() {
		return grbjid;
	}

	public void setGrbjid(String grbjid) {
		this.grbjid = grbjid;
	}

	public String getJjcd() {
		return jjcd;
	}

	public void setJjcd(String jjcd) {
		this.jjcd = jjcd;
	}

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	public String getTbrc() {
		return tbrc;
	}

	public void setTbrc(String tbrc) {
		this.tbrc = tbrc;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	/**
     *
     */
    private static final long serialVersionUID = 1L;

}
