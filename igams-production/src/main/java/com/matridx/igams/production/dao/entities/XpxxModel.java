package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XpxxModel")
public class XpxxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//芯片ID
	private String xpid;
	//芯片名
	private String xpm;
	//测序开始时间
	private String cxkssj;
	//测序结束时间
	private String cxjssj;
	//是否双端测序
	private String sfsdcx;
	//读长
	private String dc;
	//操作人员
	private String czry;
	//测序目的
	private String cxmd;
	//分析开始时间
	private String fxkssj;
	//分析结束时间
	private String fxjssj;
	//状态
	private String zt;
	private String sftx;
	private String ygxjs;
	private String cxyid;
	private String totalcycle;
	private String thiscycle;
	private String wksm;
	private String fxjd;
	private String bz;
	private String lrry;
	private String fm;
	private String density;
	private String intensity;
	private String q30;

	private String qcjson;
	private String sfgx;

	public String getQcjson() {
		return qcjson;
	}

	public void setQcjson(String qcjson) {
		this.qcjson = qcjson;
	}

	public String getSfgx() {
		return sfgx;
	}

	public void setSfgx(String sfgx) {
		this.sfgx = sfgx;
	}

	public String getSftx() {
		return sftx;
	}

	public void setSftx(String sftx) {
		this.sftx = sftx;
	}

	public String getYgxjs() {
		return ygxjs;
	}

	public void setYgxjs(String ygxjs) {
		this.ygxjs = ygxjs;
	}

	public String getCxyid() {
		return cxyid;
	}

	public void setCxyid(String cxyid) {
		this.cxyid = cxyid;
	}

	public String getTotalcycle() {
		return totalcycle;
	}

	public void setTotalcycle(String totalcycle) {
		this.totalcycle = totalcycle;
	}

	public String getThiscycle() {
		return thiscycle;
	}

	public void setThiscycle(String thiscycle) {
		this.thiscycle = thiscycle;
	}

	public String getWksm() {
		return wksm;
	}

	public void setWksm(String wksm) {
		this.wksm = wksm;
	}

	public String getFxjd() {
		return fxjd;
	}

	public void setFxjd(String fxjd) {
		this.fxjd = fxjd;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Override
	public String getLrry() {
		return lrry;
	}

	@Override
	public void setLrry(String lrry) {
		this.lrry = lrry;
	}

	public String getFm() {
		return fm;
	}

	public void setFm(String fm) {
		this.fm = fm;
	}

	public String getDensity() {
		return density;
	}

	public void setDensity(String density) {
		this.density = density;
	}

	public String getIntensity() {
		return intensity;
	}

	public void setIntensity(String intensity) {
		this.intensity = intensity;
	}

	public String getQ30() {
		return q30;
	}

	public void setQ30(String q30) {
		this.q30 = q30;
	}

	public String getXpid() {
		return xpid;
	}
	public void setXpid(String xpid) {
		this.xpid = xpid;
	}
	public String getXpm() {
		return xpm;
	}
	public void setXpm(String xpm) {
		this.xpm = xpm;
	}
	public String getCxkssj() {
		return cxkssj;
	}
	public void setCxkssj(String cxkssj) {
		this.cxkssj = cxkssj;
	}
	public String getCxjssj() {
		return cxjssj;
	}
	public void setCxjssj(String cxjssj) {
		this.cxjssj = cxjssj;
	}
	public String getSfsdcx() {
		return sfsdcx;
	}
	public void setSfsdcx(String sfsdcx) {
		this.sfsdcx = sfsdcx;
	}
	public String getDc() {
		return dc;
	}
	public void setDc(String dc) {
		this.dc = dc;
	}
	public String getCzry() {
		return czry;
	}
	public void setCzry(String czry) {
		this.czry = czry;
	}
	public String getCxmd() {
		return cxmd;
	}
	public void setCxmd(String cxmd) {
		this.cxmd = cxmd;
	}
	public String getFxkssj() {
		return fxkssj;
	}
	public void setFxkssj(String fxkssj) {
		this.fxkssj = fxkssj;
	}
	public String getFxjssj() {
		return fxjssj;
	}
	public void setFxjssj(String fxjssj) {
		this.fxjssj = fxjssj;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
}
