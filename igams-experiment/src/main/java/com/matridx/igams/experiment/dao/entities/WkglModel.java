package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WkglModel")
public class WkglModel extends BaseModel{
	//文库ID
	private String wkid;
	//审核状态   00:未提交 10:审核中  15:审核未通过  80:审核通过
	private String zt;
	//检测单位
	private String jcdw;
	//序号
	private String xh;
	//文库名称
	private String wkmc;
	//文库日期
	private String wkrq;
	//逆转录试剂盒
	private String sjph2;
	//宏基因组DNA建库试剂盒
	private String sjph1;
	//文库定量试剂盒
	private String sjph3;
	//原文库id，合并数据的wkid
	private String ywkid;
	//仪器类型
	private String yqlx;
	//试剂选择
	private String sjxz;
	//文库试剂
	private String wksj;

	public String getSjxz() {
		return sjxz;
	}

	public void setSjxz(String sjxz) {
		this.sjxz = sjxz;
	}

	public String getYqlx() {
		return yqlx;
	}

	public void setYqlx(String yqlx) {
		this.yqlx = yqlx;
	}

	public String getYwkid() {return ywkid;}
	public void setYwkid(String ywkid) {this.ywkid = ywkid;}
	public String getSjph3() {
		return sjph3;
	}
	public void setSjph3(String sjph3) {
		this.sjph3 = sjph3;
	}
	public String getSjph2() {
		return sjph2;
	}
	public void setSjph2(String sjph2) {
		this.sjph2 = sjph2;
	}
	public String getSjph1() {
		return sjph1;
	}
	public void setSjph1(String sjph1) {
		this.sjph1 = sjph1;
	}
	public String getWkrq() {
		return wkrq;
	}
	public void setWkrq(String wkrq) {
		this.wkrq = wkrq;
	}
	public String getWkmc() {
		return wkmc;
	}
	public void setWkmc(String wkmc) {
		this.wkmc = wkmc;
	}
	public String getJcdw() {
		return jcdw;
	}
	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	//文库ID
	public String getWkid() {
		return wkid;
	}
	public void setWkid(String wkid){
		this.wkid = wkid;
	}
	//审核状态   00:未提交 10:审核中  15:审核未通过  80:审核通过
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getWksj() {
		return wksj;
	}

	public void setWksj(String wksj) {
		this.wksj = wksj;
	}
}
