package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WxyhDto")
public class WxyhDto extends WxyhModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//标签名
	private String bqm;
	//标签id
	private String bqid;
	//用户列表钉钉id
	private String ddid;
	//用户列表岗位名称
	private String gwmc;
	//用户列表真实姓名
	private String zsxm;
	//用户列表微信id
	private String wechatid;
	//关注平台名称
	private String gzptmc;
	//密码
	private String mm;
	//外部程序代码
	private String wbcxdm;
	//外部程序ID
	private String wbcxid;

	public String getWbcxdm() {
		return wbcxdm;
	}

	public void setWbcxdm(String wbcxdm) {
		this.wbcxdm = wbcxdm;
	}

	public String getWbcxid() {
		return wbcxid;
	}

	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}

	public String getGzptmc() {
		return gzptmc;
	}

	public void setGzptmc(String gzptmc) {
		this.gzptmc = gzptmc;
	}

	public String getWechatid() {
		return wechatid;
	}

	public void setWechatid(String wechatid) {
		this.wechatid = wechatid;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getGwmc() {
		return gwmc;
	}

	public void setGwmc(String gwmc) {
		this.gwmc = gwmc;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getBqid() {
		return bqid;
	}

	public void setBqid(String bqid) {
		this.bqid = bqid;
	}

	public String getBqm() {
		return bqm;
	}

	public void setBqm(String bqm) {
		this.bqm = bqm;
	}

	public String getMm() {
		return mm;
	}

	public void setMm(String mm) {
		this.mm = mm;
	}
	

}
