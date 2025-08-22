package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WksjmxModel")
public class WksjmxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//文库上机明细ID
	private String wksjmxid;
	//文库上机ID
	private String wksjid;
	//序号
	private String xh;
	//标本编号
	private String ybbh;
	//文库编码
	private String wkbm;
	//项目
	private String xm;
	//接头1
	private String jt1;
	//接头2
	private String jt2;
	//DNA浓度
	private String dnand;
	//核酸浓度
	private String hsnd;
	//文库浓度
	private String wknd;
	//复检或加测申请原因
	private String yy;
	//预计下机数据量
	private String yjxjsjl;
	//稀释倍数
	private String xsbs;
	//实验管理ID
	private String syglid;
	//定量浓度
	private String dlnd;
	//体积
	private String tj;
	//合并浓度
	private String hbnd;

	private String nddw;

	public String getNddw() {
		return nddw;
	}

	public void setNddw(String nddw) {
		this.nddw = nddw;
	}

	public String getDlnd() {
		return dlnd;
	}

	public void setDlnd(String dlnd) {
		this.dlnd = dlnd;
	}

	public String getSyglid() {
		return syglid;
	}

	public void setSyglid(String syglid) {
		this.syglid = syglid;
	}

	public String getXsbs() {
		return xsbs;
	}

	public void setXsbs(String xsbs) {
		this.xsbs = xsbs;
	}

	public String getYjxjsjl() {
		return yjxjsjl;
	}

	public void setYjxjsjl(String yjxjsjl) {
		this.yjxjsjl = yjxjsjl;
	}

	public String getYy() {
		return yy;
	}

	public void setYy(String yy) {
		this.yy = yy;
	}

	public String getWknd() {
		return wknd;
	}

	public void setWknd(String wknd) {
		this.wknd = wknd;
	}

	public String getWksjmxid() {
		return wksjmxid;
	}
	public void setWksjmxid(String wksjmxid) {
		this.wksjmxid = wksjmxid;
	}
	public String getWksjid() {
		return wksjid;
	}
	public void setWksjid(String wksjid) {
		this.wksjid = wksjid;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getYbbh() {
		return ybbh;
	}
	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}
	public String getWkbm() {
		return wkbm;
	}
	public void setWkbm(String wkbm) {
		this.wkbm = wkbm;
	}
	public String getXm() {
		return xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
	public String getJt1() {
		return jt1;
	}
	public void setJt1(String jt1) {
		this.jt1 = jt1;
	}
	public String getJt2() {
		return jt2;
	}
	public void setJt2(String jt2) {
		this.jt2 = jt2;
	}
	public String getDnand() {
		return dnand;
	}
	public void setDnand(String dnand) {
		this.dnand = dnand;
	}
	public String getHsnd() {
		return hsnd;
	}
	public void setHsnd(String hsnd) {
		this.hsnd = hsnd;
	}


	public String getTj() {
		return tj;
	}

	public void setTj(String tj) {
		this.tj = tj;
	}

	public String getHbnd() {
		return hbnd;
	}

	public void setHbnd(String hbnd) {
		this.hbnd = hbnd;
	}
}
