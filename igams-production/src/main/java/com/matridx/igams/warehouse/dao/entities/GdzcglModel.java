package com.matridx.igams.warehouse.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="GdzcglModel")
public class GdzcglModel extends BaseModel{
	//固定资产id
	private String gdzcid;
	//卡片编号
	private String kpbh;
	//货物id
	private String hwid;
	//增加方式
	private String zjfs;
	//使用状况
	private String syzk;
	//类别
	private String lb;
	//资产组
	private String zcz;
	//使用年限
	private String synx;
	//折旧方法
	private String zjff;
	//开始使用日期
	private String kssyrq;
	//已计提月份
	private String yjtyf;
	//币种
	private String biz;
	//原值
	private String yz;
	//净残值率
	private String jczl;
	//净残值
	private String jcz;
	//累计折旧
	private String ljzj;
	//月折旧率
	private String yzjl;
	//本月计提折旧额
	private String byjtzje;
	//净值
	private String jz;
	//对应折旧科目
	private String dyzjkm;
	//项目
	private String xm;
	//部门
	private String bm;

	public String getBm() {
		return bm;
	}

	public void setBm(String bm) {
		this.bm = bm;
	}

	public String getDyzjkm() {
		return dyzjkm;
	}

	public void setDyzjkm(String dyzjkm) {
		this.dyzjkm = dyzjkm;
	}

	public String getGdzcid() {
		return gdzcid;
	}

	public void setGdzcid(String gdzcid) {
		this.gdzcid = gdzcid;
	}

	public String getKpbh() {
		return kpbh;
	}

	public void setKpbh(String kpbh) {
		this.kpbh = kpbh;
	}

	public String getHwid() {
		return hwid;
	}

	public void setHwid(String hwid) {
		this.hwid = hwid;
	}

	public String getZjfs() {
		return zjfs;
	}

	public void setZjfs(String zjfs) {
		this.zjfs = zjfs;
	}

	public String getSyzk() {
		return syzk;
	}

	public void setSyzk(String syzk) {
		this.syzk = syzk;
	}

	public String getLb() {
		return lb;
	}

	public void setLb(String lb) {
		this.lb = lb;
	}

	public String getZcz() {
		return zcz;
	}

	public void setZcz(String zcz) {
		this.zcz = zcz;
	}

	public String getSynx() {
		return synx;
	}

	public void setSynx(String synx) {
		this.synx = synx;
	}

	public String getZjff() {
		return zjff;
	}

	public void setZjff(String zjff) {
		this.zjff = zjff;
	}

	public String getKssyrq() {
		return kssyrq;
	}

	public void setKssyrq(String kssyrq) {
		this.kssyrq = kssyrq;
	}

	public String getYjtyf() {
		return yjtyf;
	}

	public void setYjtyf(String yjtyf) {
		this.yjtyf = yjtyf;
	}

	public String getBiz() {
		return biz;
	}

	public void setBiz(String biz) {
		this.biz = biz;
	}

	public String getYz() {
		return yz;
	}

	public void setYz(String yz) {
		this.yz = yz;
	}

	public String getJczl() {
		return jczl;
	}

	public void setJczl(String jczl) {
		this.jczl = jczl;
	}

	public String getJcz() {
		return jcz;
	}

	public void setJcz(String jcz) {
		this.jcz = jcz;
	}

	public String getLjzj() {
		return ljzj;
	}

	public void setLjzj(String ljzj) {
		this.ljzj = ljzj;
	}

	public String getYzjl() {
		return yzjl;
	}

	public void setYzjl(String yzjl) {
		this.yzjl = yzjl;
	}

	public String getByjtzje() {
		return byjtzje;
	}

	public void setByjtzje(String byjtzje) {
		this.byjtzje = byjtzje;
	}

	public String getJz() {
		return jz;
	}

	public void setJz(String jz) {
		this.jz = jz;
	}


	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
