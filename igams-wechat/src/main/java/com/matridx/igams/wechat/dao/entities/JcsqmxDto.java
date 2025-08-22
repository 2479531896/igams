package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="JcsqmxDto")
public class JcsqmxDto extends JcsqmxModel{

	//样本编号
	private String ybbh;
	//患者姓名
	private String hzxm;
	//样本类型名称
	private String yblxmc;
	//文库编号
	private String wkbh;

	public String getWkbh() {
		return wkbh;
	}

	public void setWkbh(String wkbh) {
		this.wkbh = wkbh;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
