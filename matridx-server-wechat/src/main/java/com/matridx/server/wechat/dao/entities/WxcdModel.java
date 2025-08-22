package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WxcdModel")
public class WxcdModel extends BaseModel{
	//菜单ID
	private String cdid;
	//父菜单ID
	private String fcdid;
	//显示顺序
	private String xssx;
	//菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型
	private String cdlx;
	//菜单的个性类型
	private String gxlx;
	//菜单标题名
	private String cdm;
	//菜单KEY值，用于消息接口推送，不超过128字节
	private String cdj;
	//网页链接地址
	private String ljdz;
	//一级菜单数组，个数应为1~3个
	private String yjcdsz;
	//二级菜单数组，个数应为1~5个
	private String ejcdsz;
	//null
	private String mtid;
	//小程序的appid
	private String appid;
	//小程序的页面路径
	private String ymlj;
	//微信公众号id
	private String wbcxid;
	//菜单ID
	public String getCdid() {
		return cdid;
	}
	public void setCdid(String cdid){
		this.cdid = cdid;
	}
	//父菜单ID
	public String getFcdid() {
		return fcdid;
	}
	public void setFcdid(String fcdid){
		this.fcdid = fcdid;
	}
	//显示顺序
	public String getXssx() {
		return xssx;
	}
	public void setXssx(String xssx){
		this.xssx = xssx;
	}
	//菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型
	public String getCdlx() {
		return cdlx;
	}
	public void setCdlx(String cdlx){
		this.cdlx = cdlx;
	}
	//菜单标题名
	public String getCdm() {
		return cdm;
	}
	public void setCdm(String cdm){
		this.cdm = cdm;
	}
	//菜单KEY值，用于消息接口推送，不超过128字节
	public String getCdj() {
		return cdj;
	}
	public void setCdj(String cdj){
		this.cdj = cdj;
	}
	//网页链接地址
	public String getLjdz() {
		return ljdz;
	}
	public void setLjdz(String ljdz){
		this.ljdz = ljdz;
	}
	//一级菜单数组，个数应为1~3个
	public String getYjcdsz() {
		return yjcdsz;
	}
	public void setYjcdsz(String yjcdsz){
		this.yjcdsz = yjcdsz;
	}
	//二级菜单数组，个数应为1~5个
	public String getEjcdsz() {
		return ejcdsz;
	}
	public void setEjcdsz(String ejcdsz){
		this.ejcdsz = ejcdsz;
	}
	//null
	public String getMtid() {
		return mtid;
	}
	public void setMtid(String mtid){
		this.mtid = mtid;
	}
	//小程序的appid
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid){
		this.appid = appid;
	}
	//小程序的页面路径
	public String getYmlj() {
		return ymlj;
	}
	public void setYmlj(String ymlj){
		this.ymlj = ymlj;
	}
	
	//菜单个性类型
	public String getGxlx() {
		return gxlx;
	}
	public void setGxlx(String gxlx) {
		this.gxlx = gxlx;
	}
	public String getWbcxid() {
		return wbcxid;
	}
	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
