package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XxdjDto")
public class XxdjDto extends XxdjModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//全部
	private String entire;
	//微信名
	private String wxm;
	//用户名
	private String yhm;
	//用户城市
	private String yhcs;
	//关注平台
	private String gzpt;
	//用户性别
	private String yhxb;
	//导出关联标记位
	//所选择的字段
	private String SqlParam;


	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public String getWxm() {
		return wxm;
	}

	public void setWxm(String wxm) {
		this.wxm = wxm;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getYhcs() {
		return yhcs;
	}

	public void setYhcs(String yhcs) {
		this.yhcs = yhcs;
	}

	public String getGzpt() {
		return gzpt;
	}

	public void setGzpt(String gzpt) {
		this.gzpt = gzpt;
	}

	public String getYhxb() {
		return yhxb;
	}

	public void setYhxb(String yhxb) {
		this.yhxb = yhxb;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	
}
