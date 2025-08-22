package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="LbcxtjszModel")
public class LbcxtjszModel extends BaseModel{
	//业务ID
	private String ywid;
	//序号 页面的顺序
	private String xh;
	//标题名  页面的标题
	private String btm;
	//显示类型 列表，文本   TEXT
	private String xslx;
	//输入ID
	private String srid;
	//输入名
	private String srm;
	//限制范围  数字限制，日期限制 
	private String xzfw;
	//限制内容 ，如果下拉内容，可以以逗号隔开
	private String xznr;
	//关联基础类别
	private String gljclb;
	//高级筛选标记  0：普通 1：高级
	private String gjsxbj;
	//匹配类型  精确查询,模糊查询,IN查询
	private String pplx;
	//扩展参数1
	private String kzcs1;
	//扩展参数2
	private String kzcs2;
	//扩展参数3
	private String kzcs3;
	//关联字段 用于确认页面的字段在哪个字段查询
	private String glzd;
	//业务ID
	public String getYwid() {
		return ywid;
	}
	public void setYwid(String ywid){
		this.ywid = ywid;
	}
	//序号 页面的顺序
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//标题名  页面的标题
	public String getBtm() {
		return btm;
	}
	public void setBtm(String btm){
		this.btm = btm;
	}
	//显示类型 列表，文本   TEXT
	public String getXslx() {
		return xslx;
	}
	public void setXslx(String xslx){
		this.xslx = xslx;
	}
	//输入ID
	public String getSrid() {
		return srid;
	}
	public void setSrid(String srid){
		this.srid = srid;
	}
	//输入名
	public String getSrm() {
		return srm;
	}
	public void setSrm(String srm){
		this.srm = srm;
	}
	//限制范围  数字限制，日期限制 
	public String getXzfw() {
		return xzfw;
	}
	public void setXzfw(String xzfw){
		this.xzfw = xzfw;
	}
	//限制内容 ，如果下拉内容，可以以逗号隔开
	public String getXznr() {
		return xznr;
	}
	public void setXznr(String xznr){
		this.xznr = xznr;
	}
	//关联基础类别
	public String getGljclb() {
		return gljclb;
	}
	public void setGljclb(String gljclb){
		this.gljclb = gljclb;
	}
	//高级筛选标记  0：普通 1：高级
	public String getGjsxbj() {
		return gjsxbj;
	}
	public void setGjsxbj(String gjsxbj){
		this.gjsxbj = gjsxbj;
	}
	//匹配类型  精确查询,模糊查询,IN查询
	public String getPplx() {
		return pplx;
	}
	public void setPplx(String pplx){
		this.pplx = pplx;
	}
	//扩展参数1
	public String getKzcs1() {
		return kzcs1;
	}
	public void setKzcs1(String kzcs1){
		this.kzcs1 = kzcs1;
	}
	//扩展参数2
	public String getKzcs2() {
		return kzcs2;
	}
	public void setKzcs2(String kzcs2){
		this.kzcs2 = kzcs2;
	}
	//扩展参数3
	public String getKzcs3() {
		return kzcs3;
	}
	public void setKzcs3(String kzcs3){
		this.kzcs3 = kzcs3;
	}
	//关联字段 用于确认页面的字段在哪个字段查询
	public String getGlzd() {
		return glzd;
	}
	public void setGlzd(String glzd){
		this.glzd = glzd;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
