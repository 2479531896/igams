package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XpxxDto")
public class XpxxDto extends XpxxModel{
	//测序仪名称
	private String cxymc;
	private String sfsdcxmc;
	private String cxmdmc;
	private String ztmc;
	private String timeOffset;

	public String getZtmc() {
		return ztmc;
	}

	public void setZtmc(String ztmc) {
		this.ztmc = ztmc;
	}

	public String getCxmdmc() {
		return cxmdmc;
	}

	public void setCxmdmc(String cxmdmc) {
		this.cxmdmc = cxmdmc;
	}

	public String getSfsdcxmc() {
		return sfsdcxmc;
	}

	public void setSfsdcxmc(String sfsdcxmc) {
		this.sfsdcxmc = sfsdcxmc;
	}

	public String getCxymc() {
		return cxymc;
	}

	public void setCxymc(String cxymc) {
		this.cxymc = cxymc;
	}

	public String getTimeOffset() {
		return timeOffset;
	}

	public void setTimeOffset(String timeOffset) {
		this.timeOffset = timeOffset;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
