package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="CydcszModel")
public class CydcszModel extends BaseModel{
	//常用ID
	private String cyid;
	//业务ID
	private String ywid;
	//常用代码
	private String cydm;
	//常用名称
	private String cymc;
	//常用排序
	private String cypx;
	//备注
	private String bz;
	//常用ID
	public String getCyid() {
		return cyid;
	}
	public void setCyid(String cyid){
		this.cyid = cyid;
	}
	//业务ID
	public String getYwid() {
		return ywid;
	}
	public void setYwid(String ywid){
		this.ywid = ywid;
	}
	//常用代码
	public String getCydm() {
		return cydm;
	}
	public void setCydm(String cydm){
		this.cydm = cydm;
	}
	//常用名称
	public String getCymc() {
		return cymc;
	}
	public void setCymc(String cymc){
		this.cymc = cymc;
	}
	//常用排序
	public String getCypx() {
		return cypx;
	}
	public void setCypx(String cypx){
		this.cypx = cypx;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
