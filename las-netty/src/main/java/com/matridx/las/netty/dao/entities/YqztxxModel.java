package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YqztxxModel")
public class YqztxxModel extends BaseModel{
	//仪器id
	private String yqid;
	//仪器名称
	private String yqmc;
	//仪器状态
	private String yqzt;
	//第一帧返回
	private String dyzfh;
	//第二帧返回
	private String dezfh;
	//仪器类型
	private String yqlx;
	//设备id
	private String  sbid;
	//仪器id
	public String getYqid() {
		return yqid;
	}
	public void setYqid(String yqid){
		this.yqid = yqid;
	}
	//仪器名称
	public String getYqmc() {
		return yqmc;
	}
	public void setYqmc(String yqmc){
		this.yqmc = yqmc;
	}
	//仪器状态
	public String getYqzt() {
		return yqzt;
	}
	public void setYqzt(String yqzt){
		this.yqzt = yqzt;
	}
	//第一帧返回
	public String getDyzfh() {
		return dyzfh;
	}
	public void setDyzfh(String dyzfh){
		this.dyzfh = dyzfh;
	}
	//第二帧返回
	public String getDezfh() {
		return dezfh;
	}
	public void setDezfh(String dezfh){
		this.dezfh = dezfh;
	}
	//仪器类型
	public String getYqlx() {
		return yqlx;
	}
	public void setYqlx(String yqlx) {
		this.yqlx = yqlx;
	}

	public String getSbid() {
		return sbid;
	}
	public void setSbid(String sbid) {
		this.sbid = sbid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
