package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WzysxxModel")
public class WzysxxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sjwzid;//送检物种ID

	private String sjid;//送检ID

	private String wzid;//物种ID

	private String wzzwm;//物种中文名

	private String wzywm;//物种英文名

	private String wzfl;//物种分类
	private String jglx;//结果类型

	private String xpxx;//芯片信息

	private String xh;//序号

	private String jclx;//检测类型

	private String json;//json

	private String llh;//履历号

	private String sfhb;//是否汇报

	public String getSfhb() {
		return sfhb;
	}

	public void setSfhb(String sfhb) {
		this.sfhb = sfhb;
	}

	public String getWzywm() {
		return wzywm;
	}

	public void setWzywm(String wzywm) {
		this.wzywm = wzywm;
	}

	public String getJglx() {
		return jglx;
	}

	public void setJglx(String jglx) {
		this.jglx = jglx;
	}

	public String getXpxx() {
		return xpxx;
	}

	public void setXpxx(String xpxx) {
		this.xpxx = xpxx;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getJclx() {
		return jclx;
	}

	public void setJclx(String jclx) {
		this.jclx = jclx;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getLlh() {
		return llh;
	}

	public void setLlh(String llh) {
		this.llh = llh;
	}

	public String getSjwzid() {
		return sjwzid;
	}

	public void setSjwzid(String sjwzid) {
		this.sjwzid = sjwzid;
	}

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getWzid() {
		return wzid;
	}

	public void setWzid(String wzid) {
		this.wzid = wzid;
	}

	public String getWzzwm() {
		return wzzwm;
	}

	public void setWzzwm(String wzzwm) {
		this.wzzwm = wzzwm;
	}

	public String getWzfl() {
		return wzfl;
	}

	public void setWzfl(String wzfl) {
		this.wzfl = wzfl;
	}
}
