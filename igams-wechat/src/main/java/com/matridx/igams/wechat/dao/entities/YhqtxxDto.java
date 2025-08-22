package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="YhqtxxDto")
public class YhqtxxDto extends YhqtxxModel{
	//真实姓名
	private String zsxm;
	//用户区分名称
	private String yhqfmc;
	//用户子区分名称
	private String yhzqfmc;
	//用户名
	private String yhm;
	//省份
	private String sf;
	//分布式标记
	private String prefix;

	public String getSf() {
		return sf;
	}

	public void setSf(String sf) {
		this.sf = sf;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getYhqfmc() {
		return yhqfmc;
	}

	public void setYhqfmc(String yhqfmc) {
		this.yhqfmc = yhqfmc;
	}

	public String getYhzqfmc() {
		return yhzqfmc;
	}

	public void setYhzqfmc(String yhzqfmc) {
		this.yhzqfmc = yhzqfmc;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
}
