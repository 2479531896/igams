package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="LscxszModel")
public class LscxszModel extends BaseModel{
	//查询ID
	private String cxid;
	//查询区分
	private String cxqf;
	//查询名称
	private String cxmc;
	//显示方式
	private String xsfs;
	//备注
	private String bz;
	//查询代码
	private String cxdm;
	//查询编码
	private String cxbm;
	//类别区分  用于区分不同的统计
	private String lbqf;
	//排序
	private String px;
	
	public String getXsfs() {
		return xsfs;
	}
	public void setXsfs(String xsfs) {
		this.xsfs = xsfs;
	}
	public String getCxqf() {
		return cxqf;
	}
	public void setCxqf(String cxqf) {
		this.cxqf = cxqf;
	}
	public String getCxid() {
		return cxid;
	}
	public void setCxid(String cxid){
		this.cxid = cxid;
	}
	//查询名称
	public String getCxmc() {
		return cxmc;
	}
	public void setCxmc(String cxmc){
		this.cxmc = cxmc;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//查询代码
	public String getCxdm() {
		return cxdm;
	}
	public void setCxdm(String cxdm){
		this.cxdm = cxdm;
	}

	public String getCxbm() {
		return cxbm;
	}
	public void setCxbm(String cxbm) {
		this.cxbm = cxbm;
	}

	public String getLbqf() {
		return lbqf;
	}
	public void setLbqf(String lbqf) {
		this.lbqf = lbqf;
	}

	public String getPx() {
		return px;
	}
	public void setPx(String px) {
		this.px = px;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
