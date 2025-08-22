package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YhqdxxModel")
public class YhqdxxModel extends BaseModel{
	//签到id
	private String qdid;
	//姓名
	private String xm;
	//详细地址 detailPlace
	private String xxdz;
	//地址  place
	private String dz;
	//钉钉ID  userId
	private String ddid;
	//签到时间  timestamp 转换
	private String qdsj;
	//签到时刻 timestamp
	private String dqsk;
	//经度
	private String jd;
	//维度
	private String wd;
	//备注  remark
	private String bz;
	//签到id
	public String getQdid() {
		return qdid;
	}
	public void setQdid(String qdid){
		this.qdid = qdid;
	}
	//姓名
	public String getXm() {
		return xm;
	}
	public void setXm(String xm){
		this.xm = xm;
	}
	//详细地址 detailPlace
	public String getXxdz() {
		return xxdz;
	}
	public void setXxdz(String xxdz){
		this.xxdz = xxdz;
	}
	//地址  place
	public String getDz() {
		return dz;
	}
	public void setDz(String dz){
		this.dz = dz;
	}
	//钉钉ID  userId
	public String getDdid() {
		return ddid;
	}
	public void setDdid(String ddid){
		this.ddid = ddid;
	}
	//签到时间  timestamp 转换
	public String getQdsj() {
		return qdsj;
	}
	public void setQdsj(String qdsj){
		this.qdsj = qdsj;
	}
	//签到时刻 timestamp
	public String getDqsk() {
		return dqsk;
	}
	public void setDqsk(String dqsk){
		this.dqsk = dqsk;
	}
	//经度
	public String getJd() {
		return jd;
	}
	public void setJd(String jd){
		this.jd = jd;
	}
	//维度
	public String getWd() {
		return wd;
	}
	public void setWd(String wd){
		this.wd = wd;
	}
	//备注  remark
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
