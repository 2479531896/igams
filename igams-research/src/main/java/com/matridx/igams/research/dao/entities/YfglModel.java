package com.matridx.igams.research.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YfglModel")
public class YfglModel extends BaseModel{
	//研发ID
	private String yfid;
	//调查名称 用于调查阶段
	private String dcmc;
	//研发代码
	private String yfdm;
	//研发名称 正式立项后的名称
	private String yfmc;
	//研发类型   试剂
	private String yflx;
	//开始时间 立项时间
	private String kssj;
	//当前进度  研发阶段表的jdid
	private String dqjd;
	//期望完成时间
	private String qwwcsj;
	//实际完成时间
	private String sjwcsj;
	//状态
	private String zt;
	//备注
	private String bz;
	//研发ID
	public String getYfid() {
		return yfid;
	}
	public void setYfid(String yfid){
		this.yfid = yfid;
	}
	//调查名称 用于调查阶段
	public String getDcmc() {
		return dcmc;
	}
	public void setDcmc(String dcmc){
		this.dcmc = dcmc;
	}
	//研发代码
	public String getYfdm() {
		return yfdm;
	}
	public void setYfdm(String yfdm){
		this.yfdm = yfdm;
	}
	//研发名称 正式立项后的名称
	public String getYfmc() {
		return yfmc;
	}
	public void setYfmc(String yfmc){
		this.yfmc = yfmc;
	}
	//研发类型   试剂
	public String getYflx() {
		return yflx;
	}
	public void setYflx(String yflx){
		this.yflx = yflx;
	}
	//开始时间 立项时间
	public String getKssj() {
		return kssj;
	}
	public void setKssj(String kssj){
		this.kssj = kssj;
	}
	//当前进度  研发阶段表的jdid
	public String getDqjd() {
		return dqjd;
	}
	public void setDqjd(String dqjd){
		this.dqjd = dqjd;
	}
	//期望完成时间
	public String getQwwcsj() {
		return qwwcsj;
	}
	public void setQwwcsj(String qwwcsj){
		this.qwwcsj = qwwcsj;
	}
	//实际完成时间
	public String getSjwcsj() {
		return sjwcsj;
	}
	public void setSjwcsj(String sjwcsj){
		this.sjwcsj = sjwcsj;
	}
	//状态
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}
	//备注
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
