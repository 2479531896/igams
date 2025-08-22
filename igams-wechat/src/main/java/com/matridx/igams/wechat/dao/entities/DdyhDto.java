package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="DdyhDto")
public class DdyhDto  extends DdyhModel {
	//单位限定标记,用于判断查看数据时是否根据伙伴进行限制
	private String dwxdbj;
	//合作伙伴
	private String hbmc;

	public String getHbmc() {
		return hbmc;
	}
	public void setHbmc(String hbmc) {
		this.hbmc = hbmc;
	}
	public String getDwxdbj() {
		return dwxdbj;
	}
	public void setDwxdbj(String dwxdbj) {
		this.dwxdbj = dwxdbj;
	}
}
