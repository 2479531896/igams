package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YqdzxxModel")
public class YqdzxxModel extends BaseModel{
	//动作id
	private String dzid;
	//动作状态
	private String dzzt;
	//仪器类型
	private String yqlx;
	//上报参数
	private String sbcs;
	//上报时间
	private String sbsj;
	//仪器id
	private String yqid;
	//动作id
	public String getDzid() {
		return dzid;
	}
	public void setDzid(String dzid){
		this.dzid = dzid;
	}
	//动作状态
	public String getDzzt() {
		return dzzt;
	}
	public void setDzzt(String dzzt){
		this.dzzt = dzzt;
	}
	//仪器类型
	public String getYqlx() {
		return yqlx;
	}
	public void setYqlx(String yqlx){
		this.yqlx = yqlx;
	}
	//上报参数
	public String getSbcs() {
		return sbcs;
	}
	public void setSbcs(String sbcs){
		this.sbcs = sbcs;
	}
	//上报时间
	public String getSbsj() {
		return sbsj;
	}
	public void setSbsj(String sbsj){
		this.sbsj = sbsj;
	}

	public String getYqid() {
		return yqid;
	}
	public void setYqid(String yqid) {
		this.yqid = yqid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
