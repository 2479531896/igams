package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="TqglModel")
public class TqglModel extends BaseModel{
	//提取ID
	private String tqid;
	//名称
	private String mc;
	//审核状态   00:未提交 10:审核中  15:审核未通过  80:审核通过
	private String zt;
	//提取日期
	private String tqrq;
	//核酸提取试剂盒
	private String sjph;
	private String czr;//操作人
	private String hdr;//核对人
	//提取试剂
	private String tqsj;

	public String getCzr() {
		return czr;
	}

	public void setCzr(String czr) {
		this.czr = czr;
	}

	public String getHdr() {
		return hdr;
	}

	public void setHdr(String hdr) {
		this.hdr = hdr;
	}

	public String getSjph() {
		return sjph;
	}
	public void setSjph(String sjph) {
		this.sjph = sjph;
	}
	public String getTqrq() {
		return tqrq;
	}
	public void setTqrq(String tqrq) {
		this.tqrq = tqrq;
	}
	//提取ID
	public String getTqid() {
		return tqid;
	}
	public void setTqid(String tqid){
		this.tqid = tqid;
	}
	//名称
	public String getMc() {
		return mc;
	}
	public void setMc(String mc){
		this.mc = mc;
	}
	//审核状态   00:未提交 10:审核中  15:审核未通过  80:审核通过
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

	public String getTqsj() {
		return tqsj;
	}

	public void setTqsj(String tqsj) {
		this.tqsj = tqsj;
	}
}
