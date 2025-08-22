package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YsybxxModel")
public class YsybxxModel extends BaseModel{
	//原始样本id
	private String ysybid;
	//内部编号
	private String nbbh;
	//样本托盘位置序号
	private String tpbh;
	//托盘内位置序号
	private String tpnwzxh;
	//接头号
	private String jth;
	//提纯试剂批号
	private String tcsjph;
	//建库试剂批号
	private String jkysjph;
	//制备法
	private String zbf;
	//制备法名称
	private String zbfmc;
	//建库仪通道位置
	private String jkytdwz;
	//建库仪通道号
	private String jkytdh;
	//原始样本id
	public String getYsybid() {
		return ysybid;
	}
	public void setYsybid(String ysybid){
		this.ysybid = ysybid;
	}
	//内部编号
	public String getNbbh() {
		return nbbh;
	}
	public void setNbbh(String nbbh){
		this.nbbh = nbbh;
	}
	//样本托盘位置序号
	public String getTpbh() {
		return tpbh;
	}
	public void setTpbh(String tpbh){
		this.tpbh = tpbh;
	}
	//托盘内位置序号
	public String getTpnwzxh() {
		return tpnwzxh;
	}
	public void setTpnwzxh(String tpnwzxh){
		this.tpnwzxh = tpnwzxh;
	}
	//接头号
	public String getJth() {
		return jth;
	}
	public void setJth(String jth){
		this.jth = jth;
	}
	//提纯试剂批号
	public String getTcsjph() {
		return tcsjph;
	}
	public void setTcsjph(String tcsjph){
		this.tcsjph = tcsjph;
	}
	//建库试剂批号
	public String getJkysjph() {
		return jkysjph;
	}
	public void setJkysjph(String jkysjph){
		this.jkysjph = jkysjph;
	}
	//制备法
	public String getZbf() {
		return zbf;
	}
	public void setZbf(String zbf){
		this.zbf = zbf;
	}
	//建库仪通道位置
	public String getJkytdwz() {
		return jkytdwz;
	}
	public void setJkytdwz(String jkytdwz){
		this.jkytdwz = jkytdwz;
	}
	//建库仪通道号
	public String getJkytdh() {
		return jkytdh;
	}
	public void setJkytdh(String jkytdh){
		this.jkytdh = jkytdh;
	}

	public String getZbfmc() {
		return zbfmc;
	}

	public void setZbfmc(String zbfmc) {
		this.zbfmc = zbfmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
