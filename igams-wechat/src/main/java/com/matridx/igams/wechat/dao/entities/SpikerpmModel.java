package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SpikerpmModel")
public class SpikerpmModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//阈值ID
	private String yzid;
	//标本类型
	private String yblx;
	//检测项目
	private String jcxm;
	//阈值
	private String yz;
	public String getYzid() {
		return yzid;
	}
	public void setYzid(String yzid) {
		this.yzid = yzid;
	}
	public String getYblx() {
		return yblx;
	}
	public void setYblx(String yblx) {
		this.yblx = yblx;
	}
	public String getJcxm() {
		return jcxm;
	}
	public void setJcxm(String jcxm) {
		this.jcxm = jcxm;
	}
	public String getYz() {
		return yz;
	}
	public void setYz(String yz) {
		this.yz = yz;
	}
	
}
