package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WbzxDto")
public class WbzxDto extends WbzxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//资讯类型名称
	private String zxlxmc;
	//资讯子类型名称
	private String zxzlxmc;
	//查询条数
	private String count;
	//从第几条开始查询
	private String start;
	//资讯清单地址
	private String zxqddz;
	
	public String getZxqddz() {
		return zxqddz;
	}
	public void setZxqddz(String zxqddz) {
		this.zxqddz = zxqddz;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getZxlxmc() {
		return zxlxmc;
	}

	public void setZxlxmc(String zxlxmc) {
		this.zxlxmc = zxlxmc;
	}

	public String getZxzlxmc() {
		return zxzlxmc;
	}

	public void setZxzlxmc(String zxzlxmc) {
		this.zxzlxmc = zxzlxmc;
	}
	
}
