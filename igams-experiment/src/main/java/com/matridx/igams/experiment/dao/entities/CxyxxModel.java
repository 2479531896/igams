package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="CxyxxModel")
public class CxyxxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//测序仪ID
	private String cxyid;
	//测序仪名称
	private String mc;
	//说明
	private String sm;
	//操作系统
	private String czxt;
	//型号
	private String xh;
	//接头2标记,0或1.判断Index2带不带R
	private String jt2bj;
	//城市
	private String cs;
	//测序消耗时间
	private String cxxhsj;
	//测序仪编号
	private String cxybh;
	
	public String getCxybh() {
		return cxybh;
	}
	public void setCxybh(String cxybh) {
		this.cxybh = cxybh;
	}
	public String getCxyid() {
		return cxyid;
	}
	public void setCxyid(String cxyid) {
		this.cxyid = cxyid;
	}
	public String getMc() {
		return mc;
	}
	public void setMc(String mc) {
		this.mc = mc;
	}
	public String getSm() {
		return sm;
	}
	public void setSm(String sm) {
		this.sm = sm;
	}
	public String getCzxt() {
		return czxt;
	}
	public void setCzxt(String czxt) {
		this.czxt = czxt;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getJt2bj() {
		return jt2bj;
	}
	public void setJt2bj(String jt2bj) {
		this.jt2bj = jt2bj;
	}
	public String getCs() {
		return cs;
	}
	public void setCs(String cs) {
		this.cs = cs;
	}
	public String getCxxhsj() {
		return cxxhsj;
	}
	public void setCxxhsj(String cxxhsj) {
		this.cxxhsj = cxxhsj;
	}
	
}
