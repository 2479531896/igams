package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JnsjhzxxModel")
public class JnsjhzxxModel extends BaseModel{
	//艰难梭菌患者id
	private String jnsjhzid;
	//姓名
	private String xm;
	//性别
	private String xb;
	//年龄
	private String nl;
	//copd
	private String copd;
	//血液系统恶性肿瘤
	private String xyxtexzl;
	//实体器官移植
	private String stqgyz;
	//长期接受激素治疗
	private String cqjsjszl;
	//自身免疫性疾病
	private String zsmyjb;
	//糖尿病
	private String tnb;
	//肿瘤性疾病
	private String zlxjb;
	//冠心病
	private String gxb;
	//脑血管疾病
	private String nxgjb;
	//慢性肾病
	private String mxsb;
	//炎症性肠病
	private String yzxcb;
	//出院诊断
	private String cyzd;
	//3个月前是否有住院史
	private String sgyqsfyzys;
	//入院时间
	private String rysj;
	//出院时间
	private String cysj;
	//住院花费
	private String zyhf;

	//艰难梭菌患者id
	public String getJnsjhzid() { return jnsjhzid; }
	public void setJnsjhzid(String jnsjhzid) { this.jnsjhzid = jnsjhzid; }
	//姓名
	public String getXm() {
		return xm;
	}
	public void setXm(String xm){
		this.xm = xm;
	}
	//性别
	public String getXb() {
		return xb;
	}
	public void setXb(String xb){
		this.xb = xb;
	}
	//年龄
	public String getNl() {
		return nl;
	}
	public void setNl(String nl){
		this.nl = nl;
	}
	//copd
	public String getCopd() {
		return copd;
	}
	public void setCopd(String copd){
		this.copd = copd;
	}
	//血液系统恶性肿瘤
	public String getXyxtexzl() {
		return xyxtexzl;
	}
	public void setXyxtexzl(String xyxtexzl){
		this.xyxtexzl = xyxtexzl;
	}
	//实体器官移植
	public String getStqgyz() {
		return stqgyz;
	}
	public void setStqgyz(String stqgyz){
		this.stqgyz = stqgyz;
	}
	//长期接受激素治疗
	public String getCqjsjszl() {
		return cqjsjszl;
	}
	public void setCqjsjszl(String cqjsjszl){
		this.cqjsjszl = cqjsjszl;
	}
	//自身免疫性疾病
	public String getZsmyjb() {
		return zsmyjb;
	}
	public void setZsmyjb(String zsmyjb){
		this.zsmyjb = zsmyjb;
	}
	//糖尿病
	public String getTnb() {
		return tnb;
	}
	public void setTnb(String tnb){
		this.tnb = tnb;
	}
	//肿瘤性疾病
	public String getZlxjb() {
		return zlxjb;
	}
	public void setZlxjb(String zlxjb){
		this.zlxjb = zlxjb;
	}
	//冠心病
	public String getGxb() {
		return gxb;
	}
	public void setGxb(String gxb){
		this.gxb = gxb;
	}
	//脑血管疾病
	public String getNxgjb() {
		return nxgjb;
	}
	public void setNxgjb(String nxgjb){
		this.nxgjb = nxgjb;
	}
	//慢性肾病
	public String getMxsb() {
		return mxsb;
	}
	public void setMxsb(String mxsb){
		this.mxsb = mxsb;
	}
	//炎症性肠病
	public String getYzxcb() {
		return yzxcb;
	}
	public void setYzxcb(String yzxcb){
		this.yzxcb = yzxcb;
	}
	//出院诊断
	public String getCyzd() {
		return cyzd;
	}
	public void setCyzd(String cyzd){
		this.cyzd = cyzd;
	}
	//3个月前是否有住院史
	public String getSgyqsfyzys() {
		return sgyqsfyzys;
	}
	public void setSgyqsfyzys(String sgyqsfyzys){
		this.sgyqsfyzys = sgyqsfyzys;
	}
	//入院时间
	public String getRysj() {
		return rysj;
	}
	public void setRysj(String rysj){
		this.rysj = rysj;
	}
	//出院时间
	public String getCysj() {
		return cysj;
	}
	public void setCysj(String cysj){
		this.cysj = cysj;
	}
	//住院花费
	public String getZyhf() {
		return zyhf;
	}
	public void setZyhf(String zyhf){
		this.zyhf = zyhf;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
