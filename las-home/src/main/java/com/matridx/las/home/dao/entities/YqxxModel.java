package com.matridx.las.home.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YqxxModel")
public class YqxxModel extends BaseModel{
	//仪器ID
	private String yqid;
	//序号
	private String xh;
	//实验室ID
	private String sysid;
	//仪器名称
	private String yqmc;
	//顶部距离
	private String dbjl;
	//左侧距离
	private String zcjl;
	//高度
	private String gd;
	//宽度
	private String kd;
	// 用标记 0:未使用 1:使用
	private String sybj;
	//样式
	private  String style;
	//父区域
	private String fqy;
	//仪器类型
	private String lx;
	//仪器所属组件类型
	private String yq;
	
	//仪器ID
	public String getYqid() {
		return yqid;
	}
	public void setYqid(String yqid){
		this.yqid = yqid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//实验室ID
	public String getSysid() {
		return sysid;
	}
	public void setSysid(String sysid){
		this.sysid = sysid;
	}
	//仪器名称
	public String getYqmc() {
		return yqmc;
	}
	public void setYqmc(String yqmc){
		this.yqmc = yqmc;
	}
	//顶部距离
	public String getDbjl() {
		return dbjl;
	}
	public void setDbjl(String dbjl){
		this.dbjl = dbjl;
	}
	//左侧距离
	public String getZcjl() {
		return zcjl;
	}
	public void setZcjl(String zcjl){
		this.zcjl = zcjl;
	}
	//高度
	public String getGd() {
		return gd;
	}
	public void setGd(String gd){
		this.gd = gd;
	}
	//宽度
	public String getKd() {
		return kd;
	}
	public void setKd(String kd){
		this.kd = kd;
	}
	// 使用标记
	public String getSybj() {
		return sybj;
	}
	public void setSybj(String sybj) {
		this.sybj = sybj;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getFqy() {
		return fqy;
	}

	public void setFqy(String fqy) {
		this.fqy = fqy;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getYq() {
		return yq;
	}

	public void setYq(String yq) {
		this.yq = yq;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
