package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="RwmbModel")
public class RwmbModel extends BaseModel{
	//任务模板ID
	private String rwmbid;
	//阶段ID
	private String jdid;
	//模板ID
	private String mbid;
	//父任务模板ID
	private String frwmbid;
	//序号
	private String xh;
	//任务名称
	private String rwmc;
	//默认负责人
	private String mrfzr;
	//期限
	private String qx;
	//任务级别 基础数据 普通，紧急
	private String rwjb;
	//任务标签  基础数据
	private String rwbq;
	//任务描述
	private String rwms;
	//扩展参数 暂未使用
	private String kzcs;
	//任务模板ID
	public String getRwmbid() {
		return rwmbid;
	}
	public void setRwmbid(String rwmbid){
		this.rwmbid = rwmbid;
	}
	//阶段ID
	public String getJdid() {
		return jdid;
	}
	public void setJdid(String jdid){
		this.jdid = jdid;
	}
	//模板ID
	public String getMbid() {
		return mbid;
	}
	public void setMbid(String mbid){
		this.mbid = mbid;
	}
	//父任务模板ID
	public String getFrwmbid() {
		return frwmbid;
	}
	public void setFrwmbid(String frwmbid){
		this.frwmbid = frwmbid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//任务名称
	public String getRwmc() {
		return rwmc;
	}
	public void setRwmc(String rwmc) {
		this.rwmc = rwmc;
	}
	//默认负责人
	public String getMrfzr() {
		return mrfzr;
	}
	public void setMrfzr(String mrfzr){
		this.mrfzr = mrfzr;
	}
	//期限
	public String getQx() {
		return qx;
	}
	public void setQx(String qx){
		this.qx = qx;
	}
	//任务级别 基础数据 普通，紧急
	public String getRwjb() {
		return rwjb;
	}
	public void setRwjb(String rwjb){
		this.rwjb = rwjb;
	}
	//任务标签  基础数据
	public String getRwbq() {
		return rwbq;
	}
	public void setRwbq(String rwbq){
		this.rwbq = rwbq;
	}
	//任务描述
	public String getRwms() {
		return rwms;
	}
	public void setRwms(String rwms){
		this.rwms = rwms;
	}
	//扩展参数 暂未使用
	public String getKzcs() {
		return kzcs;
	}
	public void setKzcs(String kzcs){
		this.kzcs = kzcs;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
