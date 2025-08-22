package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="LczzznglModel")
public class LczzznglModel extends BaseModel{
	//指南ID
	private String znid;
	//标本类型   sample_type
	private String yblx;
	//起始年龄 age_group
	private String qsnl;
	//结束年龄 age_group
	private String jsnl;
	//性别 sex
	private String xb;
	//关联物种 species
	private String glwz;
	//临床指南的引用格式，实际要插入到报告单里的内容 detail
	private String yygs;
	//指南ID
	private String mc;
	private String ly;
	private String fbsj;
	private String url;
	private String nlz;

	public String getNlz() {
		return nlz;
	}

	public void setNlz(String nlz) {
		this.nlz = nlz;
	}

	public String getZnid() {
		return znid;
	}
	public void setZnid(String znid){
		this.znid = znid;
	}
	//标本类型   sample_type
	public String getYblx() {
		return yblx;
	}
	public void setYblx(String yblx){
		this.yblx = yblx;
	}
	//起始年龄 age_group
	public String getQsnl() {
		return qsnl;
	}
	public void setQsnl(String qsnl){
		this.qsnl = qsnl;
	}
	//结束年龄 age_group
	public String getJsnl() {
		return jsnl;
	}
	public void setJsnl(String jsnl){
		this.jsnl = jsnl;
	}
	//性别 sex
	public String getXb() {
		return xb;
	}
	public void setXb(String xb){
		this.xb = xb;
	}
	//关联物种 species
	public String getGlwz() {
		return glwz;
	}
	public void setGlwz(String glwz){
		this.glwz = glwz;
	}
	//临床指南的引用格式，实际要插入到报告单里的内容 detail
	public String getYygs() {
		return yygs;
	}
	public void setYygs(String yygs){
		this.yygs = yygs;
	}

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public String getLy() {
		return ly;
	}

	public void setLy(String ly) {
		this.ly = ly;
	}

	public String getFbsj() {
		return fbsj;
	}

	public void setFbsj(String fbsj) {
		this.fbsj = fbsj;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
