package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WkhhyypModel")
public class WkhhyypModel extends BaseModel{
	//null
	private String hhyid;
	//null
	private String wkid;
	//null
	private String ybbh;
	//null
	private String x;
	//null
	private String y;
	//null
	private String ispcr;
	//上报时间
	private String sbsj;
	//null
	public String getHhyid() {
		return hhyid;
	}
	public void setHhyid(String hhyid){
		this.hhyid = hhyid;
	}
	//null
	public String getWkid() {
		return wkid;
	}
	public void setWkid(String wkid){
		this.wkid = wkid;
	}
	//null
	public String getYbbh() {
		return ybbh;
	}
	public void setYbbh(String ybbh){
		this.ybbh = ybbh;
	}
	//null
	public String getX() {
		return x;
	}
	public void setX(String x){
		this.x = x;
	}
	//null
	public String getY() {
		return y;
	}
	public void setY(String y){
		this.y = y;
	}
	//null
	public String getIspcr() {
		return ispcr;
	}
	public void setIspcr(String ispcr){
		this.ispcr = ispcr;
	}

	public String getSbsj() {
		return sbsj;
	}
	public void setSbsj(String sbsj) {
		this.sbsj = sbsj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
