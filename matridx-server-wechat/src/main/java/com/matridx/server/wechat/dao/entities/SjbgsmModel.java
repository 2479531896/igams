package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjbgsmModel")
public class SjbgsmModel extends BaseModel{
	//送检ID
	private String sjid;
	//高关注度说明
	private String ggzdsm;
	//疑似说明
	private String yssm;
	//高关注度指标
	private String ggzdzb;
	//疑似指标
	private String yszb;
	//参考文献
	private String ckwx;
	//检测类型
	private String jclx;
	//检测子类型
	private String jczlx;
	//报告日期
	private String bgrq;
	//检验人员
	private String jyry;
	//审核人员
	private String shry;
	//定值指标
	private String dzzb;
	//定值说明
	private String dzsm;

	public String getJyry() {
		return jyry;
	}

	public void setJyry(String jyry) {
		this.jyry = jyry;
	}

	public String getShry() {
		return shry;
	}

	public void setShry(String shry) {
		this.shry = shry;
	}

	public String getBgrq() {
		return bgrq;
	}
	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}
	public String getJclx() {
		return jclx;
	}
	public void setJclx(String jclx) {
		this.jclx = jclx;
	}
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//高关注度说明
	public String getGgzdsm() {
		return ggzdsm;
	}
	public void setGgzdsm(String ggzdsm){
		this.ggzdsm = ggzdsm;
	}
	//疑似说明
	public String getYssm() {
		return yssm;
	}
	public void setYssm(String yssm){
		this.yssm = yssm;
	}
	//高关注度指标
	public String getGgzdzb() {
		return ggzdzb;
	}
	public void setGgzdzb(String ggzdzb){
		this.ggzdzb = ggzdzb;
	}
	//疑似指标
	public String getYszb() {
		return yszb;
	}
	public void setYszb(String yszb){
		this.yszb = yszb;
	}
	//参考文献
	public String getCkwx() {
		return ckwx;
	}
	public void setCkwx(String ckwx){
		this.ckwx = ckwx;
	}

	public String getDzzb() {
		return dzzb;
	}

	public void setDzzb(String dzzb) {
		this.dzzb = dzzb;
	}

	public String getDzsm() {
		return dzsm;
	}

	public void setDzsm(String dzsm) {
		this.dzsm = dzsm;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public String getJczlx() {
        return jczlx;
    }

    public void setJczlx(String jczlx) {
        this.jczlx = jczlx;
    }
}
