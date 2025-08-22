package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XxpzModel")
public class XxpzModel extends BaseModel{
	//信息类型
	private String xxlx;
	//序号
	private String xh;
	//显示名称
	private String xsmc;
	//文本信息
	private String wbxx;
	//扩展参数1
	private String kzcs1;
	//扩展参数2
	private String kzcs2;
	//是否必填
	private String sfbt;
	
	public String getKzcs1()
	{
		return kzcs1;
	}
	public void setKzcs1(String kzcs1)
	{
		this.kzcs1 = kzcs1;
	}
	public String getKzcs2()
	{
		return kzcs2;
	}
	public void setKzcs2(String kzcs2)
	{
		this.kzcs2 = kzcs2;
	}
	public String getSfbt()
	{
		return sfbt;
	}
	public void setSfbt(String sfbt)
	{
		this.sfbt = sfbt;
	}
	//信息类型
	public String getXxlx() {
		return xxlx;
	}
	public void setXxlx(String xxlx){
		this.xxlx = xxlx;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//显示名称
	public String getXsmc() {
		return xsmc;
	}
	public void setXsmc(String xsmc){
		this.xsmc = xsmc;
	}
	//文本信息
	public String getWbxx() {
		return wbxx;
	}
	public void setWbxx(String wbxx){
		this.wbxx = wbxx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
