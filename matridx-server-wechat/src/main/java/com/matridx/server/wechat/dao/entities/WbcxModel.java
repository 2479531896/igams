package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WbcxModel")
public class WbcxModel extends BaseModel{
	//外部程序id
	private String wbcxid;
	//外部程序名称
	private String wbcxmc;
	//appid
	private String appid;
	//callbackurl
	private String callbackurl;
	//secret
	private String secret;
	//token
	private String token;
	//关注平台
	private String gzpt;
	//外部程序代码
	private String wbcxdm;
	//代理标识
	private String agentid;
	//跳转钉钉网址
	private String jumpdingtalkurl;
	//AES 密钥
	private String aeskey;

	//类型
	private String lx;
	//cropid
	private String cropid;
	//排序
	private String px;
	//miniappid
	private String miniappid;
	//是否同步
	private String sftb;

	public String getSftb() {
		return sftb;
	}

	public void setSftb(String sftb) {
		this.sftb = sftb;
	}

	//解密的miniappid
	private String decodeid;

	public String getDecodeid() {
		return decodeid;
	}

	public void setDecodeid(String decodeid) {
		this.decodeid = decodeid;
	}
	public String getCallbackurl() {
		return callbackurl;
	}

	public void setCallbackurl(String callbackurl) {
		this.callbackurl = callbackurl;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getJumpdingtalkurl() {
		return jumpdingtalkurl;
	}

	public void setJumpdingtalkurl(String jumpdingtalkurl) {
		this.jumpdingtalkurl = jumpdingtalkurl;
	}

	public String getAeskey() {
		return aeskey;
	}

	public void setAeskey(String aeskey) {
		this.aeskey = aeskey;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getCropid() {
		return cropid;
	}

	public void setCropid(String cropid) {
		this.cropid = cropid;
	}

	public String getPx() {
		return px;
	}

	public void setPx(String px) {
		this.px = px;
	}

	public String getMiniappid() {
		return miniappid;
	}

	public void setMiniappid(String miniappid) {
		this.miniappid = miniappid;
	}

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


	public String getWbcxmc() {
		return wbcxmc;
	}


	public void setWbcxmc(String wbcxmc) {
		this.wbcxmc = wbcxmc;
	}


	public String getAppid() {
		return appid;
	}


	public void setAppid(String appid) {
		this.appid = appid;
	}


	public String getSecret() {
		return secret;
	}


	public void setSecret(String secret) {
		this.secret = secret;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


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
