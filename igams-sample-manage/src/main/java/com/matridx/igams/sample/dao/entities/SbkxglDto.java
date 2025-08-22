package com.matridx.igams.sample.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="SbkxglDto")
public class SbkxglDto extends SbkxglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//数量
	private String sl;
	//存放数
	private String cfs;
	//存放开始位置
	private String cfqswz;
	//存放结束位置
	private String cfjswz;
	//修正前存放开始位置
	private String yscfqswz;
	//修正前存放结束位置
	private String yscfjswz;
	//冰箱ID
	private String bxid;
	//标本ID (现用于修改时排除相应的位置)
	private String ybid;
	//盒子数 用于设置默认的盒子位置
	private List<String> hzs;
	//标本类型扩展产生
	private String yblxkzcs;
	
	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
	}
	public String getCfs() {
		return cfs;
	}
	public void setCfs(String cfs) {
		this.cfs = cfs;
	}
	public String getCfqswz() {
		return cfqswz;
	}
	public void setCfqswz(String cfqswz) {
		this.cfqswz = cfqswz;
	}
	public String getCfjswz() {
		return cfjswz;
	}
	public void setCfjswz(String cfjswz) {
		this.cfjswz = cfjswz;
	}
	public String getBxid() {
		return bxid;
	}
	public void setBxid(String bxid) {
		this.bxid = bxid;
	}
	public String getYscfqswz() {
		return yscfqswz;
	}
	public void setYscfqswz(String yscfqswz) {
		this.yscfqswz = yscfqswz;
	}
	public String getYscfjswz() {
		return yscfjswz;
	}
	public void setYscfjswz(String yscfjswz) {
		this.yscfjswz = yscfjswz;
	}
	public String getYbid() {
		return ybid;
	}
	public void setYbid(String ybid) {
		this.ybid = ybid;
	}
	public List<String> getHzs() {
		return hzs;
	}
	public void setHzs(List<String> hzs) {
		this.hzs = hzs;
	}
	public String getYblxkzcs() {
		return yblxkzcs;
	}
	public void setYblxkzcs(String yblxkzcs) {
		this.yblxkzcs = yblxkzcs;
	}
	
}
