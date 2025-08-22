package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="TyszmxModel")
public class TyszmxModel extends BaseModel{

	//通用设置明细ID
	private String tyszmxid;
	//通用设置ID
	private String tyszid;
	//类型 0高级筛选 1右键设置
	private String lx;
	//标题ID
	private String btid;
	//标题
	private String bt;
	//内容
	private String nr;
	//筛选类别
	private String sxlb;
	//参数名
	private String csm;
	//参数值
	private String csz;
	//字段类型
	private String zdlx;
	//序号
	private String xh;
	//内容ID
	private String nrid;
	//父内容ID
	private String fnrid;
	//组件
	private String sx;
	//加载
	private String jz;
	private String mrxs;
	private String pxzd;
	private String sqlzd;
	private String qqlj;

	public String getNrid() {
		return nrid;
	}

	public void setNrid(String nrid) {
		this.nrid = nrid;
	}

	public String getFnrid() {
		return fnrid;
	}

	public void setFnrid(String fnrid) {
		this.fnrid = fnrid;
	}

	public String getZdlx() {
		return zdlx;
	}

	public void setZdlx(String zdlx) {
		this.zdlx = zdlx;
	}

	public String getBt() {
		return bt;
	}

	public void setBt(String bt) {
		this.bt = bt;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getBtid() {
		return btid;
	}

	public void setBtid(String btid) {
		this.btid = btid;
	}

	public String getCsm() {
		return csm;
	}

	public void setCsm(String csm) {
		this.csm = csm;
	}

	public String getCsz() {
		return csz;
	}

	public void setCsz(String csz) {
		this.csz = csz;
	}

	public String getSxlb() {
		return sxlb;
	}

	public void setSxlb(String sxlb) {
		this.sxlb = sxlb;
	}

	public String getTyszmxid() {
		return tyszmxid;
	}

	public void setTyszmxid(String tyszmxid) {
		this.tyszmxid = tyszmxid;
	}

	public String getTyszid() {
		return tyszid;
	}

	public void setTyszid(String tyszid) {
		this.tyszid = tyszid;
	}

	public String getNr() {
		return nr;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public String getSx() {
		return sx;
	}

	public void setSx(String sx) {
		this.sx = sx;
	}

	public String getJz() {
		return jz;
	}

	public void setJz(String jz) {
		this.jz = jz;
	}

	public String getMrxs() {
		return mrxs;
	}

	public void setMrxs(String mrxs) {
		this.mrxs = mrxs;
	}

	public String getPxzd() {
		return pxzd;
	}

	public void setPxzd(String pxzd) {
		this.pxzd = pxzd;
	}

	public String getSqlzd() {
		return sqlzd;
	}

	public void setSqlzd(String sqlzd) {
		this.sqlzd = sqlzd;
	}

	public String getQqlj() {
		return qqlj;
	}

	public void setQqlj(String qqlj) {
		this.qqlj = qqlj;
	}
}
