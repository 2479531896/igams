package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="LshkDto")
public class LshkDto extends LshkModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//标本类型
	private String yblx;
	//标本编号日期
	private String ybbhrq;
	//标本来源
	private String ybly;
	//物料类别
	private String wllb;
	//物料子类别
	private String wlzlb;

	public String getYbbhrq() {
		return ybbhrq;
	}

	public void setYbbhrq(String ybbhrq) {
		this.ybbhrq = ybbhrq;
	}

	public String getYblx() {
		return yblx;
	}

	public void setYblx(String yblx) {
		this.yblx = yblx;
	}

	public String getYbly() {
		return ybly;
	}

	public void setYbly(String ybly) {
		this.ybly = ybly;
	}

	public String getWllb() {
		return wllb;
	}

	public void setWllb(String wllb) {
		this.wllb = wllb;
	}

	public String getWlzlb() {
		return wlzlb;
	}

	public void setWlzlb(String wlzlb) {
		this.wlzlb = wlzlb;
	}
}
