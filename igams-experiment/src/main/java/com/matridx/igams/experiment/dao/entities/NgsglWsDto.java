package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="NgsglWsDto")
public class NgsglWsDto extends NgsglWsModel{

	//检测单位
	private String jcdw;
	//制备编码  制备编码 D 或者R 需程序判断制备法
	private String zbbm;
	public String getJcdw() {
		return jcdw;
	}
	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}
	public String getZbbm() {
		return zbbm;
	}
	public void setZbbm(String zbbm) {
		this.zbbm = zbbm;
	}
	
}
