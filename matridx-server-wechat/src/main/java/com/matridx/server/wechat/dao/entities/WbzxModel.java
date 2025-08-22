package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WbzxModel")
public class WbzxModel extends BaseModel{
	//资讯ID
	private String zxid;
	//资讯标题
	private String zxbt;
	//资讯内容
	private String zxnr;
	//资讯地址
	private String zxdz;
	//资讯简介
	private String zxjj;
	//资讯类型
	private String zxlx;
	//子资讯类型
	private String zxzlx;
	//发布日期
	private String fbrq;
	//序号
	private String xh;
	
	public String getZxlx() {
		return zxlx;
	}
	public void setZxlx(String zxlx) {
		this.zxlx = zxlx;
	}
	public String getZxzlx() {
		return zxzlx;
	}
	public void setZxzlx(String zxzlx) {
		this.zxzlx = zxzlx;
	}
	public String getFbrq() {
		return fbrq;
	}
	public void setFbrq(String fbrq) {
		this.fbrq = fbrq;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	//资讯ID
	public String getZxid() {
		return zxid;
	}
	public void setZxid(String zxid){
		this.zxid = zxid;
	}
	//资讯标题
	public String getZxbt() {
		return zxbt;
	}
	public void setZxbt(String zxbt){
		this.zxbt = zxbt;
	}
	//资讯内容
	public String getZxnr() {
		return zxnr;
	}
	public void setZxnr(String zxnr){
		this.zxnr = zxnr;
	}
	//资讯地址
	public String getZxdz() {
		return zxdz;
	}
	public void setZxdz(String zxdz){
		this.zxdz = zxdz;
	}
	//资讯简介
	public String getZxjj() {
		return zxjj;
	}
	public void setZxjj(String zxjj) {
		this.zxjj = zxjj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
