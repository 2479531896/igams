package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="GzpzModel")
public class GzpzModel extends BaseModel{
	//工作类型 
	private String gzlx;
	//页面路径
	private String ymlj;
	//类型名称
	private String lxmc;
	//回调类
	private String hdl;
	//回调模型
	private String hdmx;
	//工作类型 
	public String getGzlx() {
		return gzlx;
	}
	public void setGzlx(String gzlx){
		this.gzlx = gzlx;
	}
	//页面路径
	public String getYmlj() {
		return ymlj;
	}
	public void setYmlj(String ymlj){
		this.ymlj = ymlj;
	}
	//类型名称
	public String getLxmc() {
		return lxmc;
	}
	public void setLxmc(String lxmc){
		this.lxmc = lxmc;
	}
	//回调类
	public String getHdl() {
		return hdl;
	}
	public void setHdl(String hdl){
		this.hdl = hdl;
	}
	//回调模型
	public String getHdmx() {
		return hdmx;
	}
	public void setHdmx(String hdmx){
		this.hdmx = hdmx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
