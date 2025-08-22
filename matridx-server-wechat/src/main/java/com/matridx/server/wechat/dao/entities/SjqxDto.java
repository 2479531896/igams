package com.matridx.server.wechat.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="SjqxDto")
public class SjqxDto extends SjqxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//外部程序ID
	private String wbcxid;
	//微信名
	private String wxm;
	// 科室(复数)
	private List<String> kss;
	// 送检单位(复数)
	private List<String> sjdws;
	
	public List<String> getKss() {
		return kss;
	}
	public void setKss(List<String> kss) {
		this.kss = kss;
	}
	public List<String> getSjdws() {
		return sjdws;
	}
	public void setSjdws(List<String> sjdws) {
		this.sjdws = sjdws;
	}
	public String getWxm() {
		return wxm;
	}
	public void setWxm(String wxm) {
		this.wxm = wxm;
	}
	public String getWbcxid() {
		return wbcxid;
	}
	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}
}
