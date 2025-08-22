package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="MbglModel")
public class MbglModel extends BaseModel{
	//模板ID
	private String mbid;
	//模板名称
	private String mbmc;
	//模板类型类型 基础数据维护
	private String jdlx;
	//模板备注
	private String bz;
	//模板颜色
	private String mbys;
	//模板图标
	private String mbtb;
	
	public String getMbys() {
		return mbys;
	}
	public void setMbys(String mbys) {
		this.mbys = mbys;
	}
	public String getMbtb() {
		return mbtb;
	}
	public void setMbtb(String mbtb) {
		this.mbtb = mbtb;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getMbid() {
		return mbid;
	}
	public void setMbid(String mbid){
		this.mbid = mbid;
	}
	//模板名称
	public String getMbmc() {
		return mbmc;
	}
	public void setMbmc(String mbmc){
		this.mbmc = mbmc;
	}
	//模板类型类型 基础数据维护
	public String getJdlx() {
		return jdlx;
	}
	public void setJdlx(String jdlx){
		this.jdlx = jdlx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
