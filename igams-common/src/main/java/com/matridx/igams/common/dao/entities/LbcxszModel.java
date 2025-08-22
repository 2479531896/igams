package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="LbcxszModel")
public class LbcxszModel extends BaseModel{
	//业务ID
	private String ywid;
	//序号
	private String xh;
	//父菜单
	private String fcd;
	//关联方式
	private String glfs;
	//关联表
	private String glb;
	//关联表缩写
	private String glbsx;
	//关联字段
	private String glzd;
	//隐藏条件
	private String yctj;
	//排序信息
	private String pxxx;
	
	
	//业务ID
	public String getYwid() {
		return ywid;
	}
	public void setYwid(String ywid){
		this.ywid = ywid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//父菜单
	public String getFcd() {
		return fcd;
	}
	public void setFcd(String fcd){
		this.fcd = fcd;
	}
	//关联方式
	public String getGlfs() {
		return glfs;
	}
	public void setGlfs(String glfs){
		this.glfs = glfs;
	}
	//关联表
	public String getGlb() {
		return glb;
	}
	public void setGlb(String glb){
		this.glb = glb;
	}
	//关联表缩写
	public String getGlbsx() {
		return glbsx;
	}
	public void setGlbsx(String glbsx){
		this.glbsx = glbsx;
	}
	//关联字段
	public String getGlzd() {
		return glzd;
	}
	public void setGlzd(String glzd){
		this.glzd = glzd;
	}
	//隐藏条件
	public String getYctj() {
		return yctj;
	}
	public void setYctj(String yctj){
		this.yctj = yctj;
	}
	//排序信息
	public String getPxxx() {
		return pxxx;
	}
	public void setPxxx(String pxxx){
		this.pxxx = pxxx;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
