package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="HbqxDto")
public class HbqxDto extends HbqxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//合作伙伴
	private String hbmc;
	//省份ID
	private String sfid;
	//省份名称
	private String sfmc;
	//用于权限的伙伴ID
	private String hbxx;
	
	public String getHbmc()
	{
		return hbmc;
	}
	public void setHbmc(String hbmc)
	{
		this.hbmc = hbmc;
	}
	public String getHbxx() {
		return hbxx;
	}
	public void setHbxx(String hbxx) {
		this.hbxx = hbxx;
	}
	public String getSfid() {
		return sfid;
	}
	public void setSfid(String sfid) {
		this.sfid = sfid;
	}
	public String getSfmc() {
		return sfmc;
	}
	public void setSfmc(String sfmc) {
		this.sfmc = sfmc;
	}
	
	
}
