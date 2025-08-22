package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="FaqxxModel")
public class FaqxxModel extends BaseModel{
	//faqID
	private String faqid;
	//序号
	private String xh;
	//标题
	private String bt;
	//位置
	private String wz;
	//内容
	private String nr;
	//关注平台
	private String gzpt;
	
	//faqID
	public String getFaqid() {
		return faqid;
	}
	public void setFaqid(String faqid){
		this.faqid = faqid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//标题
	public String getBt() {
		return bt;
	}
	public void setBt(String bt){
		this.bt = bt;
	}
	//位置
	public String getWz() {
		return wz;
	}
	public void setWz(String wz){
		this.wz = wz;
	}
	//内容
	public String getNr() {
		return nr;
	}
	public void setNr(String nr){
		this.nr = nr;
	}
	// 关注平台
	public String getGzpt() {
		return gzpt;
	}
	public void setGzpt(String gzpt) {
		this.gzpt = gzpt;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
