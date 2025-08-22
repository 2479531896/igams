package com.matridx.igams.sample.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SbkxglModel")
public class SbkxglModel extends BaseModel{
	//盒子ID
	private String sbid;
	//序号
	private String xh;
	//抽屉ID
	private String fsbid;
	//起始位置
	private String qswz;
	//空闲数
	private String kxs;
	//标本类型限定
	private String yblx;
	//盒子ID
	public String getSbid() {
		return sbid;
	}
	public void setSbid(String sbid){
		this.sbid = sbid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//抽屉ID
	public String getFsbid() {
		return fsbid;
	}
	public void setFsbid(String fsbid){
		this.fsbid = fsbid;
	}
	//起始位置
	public String getQswz() {
		return qswz;
	}
	public void setQswz(String qswz){
		this.qswz = qswz;
	}
	//空闲数
	public String getKxs() {
		return kxs;
	}
	public void setKxs(String kxs){
		this.kxs = kxs;
	}
	//标本类型
	public String getYblx() {
		return yblx;
	}
	public void setYblx(String yblx){
		this.yblx = yblx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
