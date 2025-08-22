package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="HbqxDto")
public class HbqxDto extends HbqxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//合作伙伴
	private String hbmc;
	//省份ID
	private String sfid;
	//省份名称
	private String sfmc;
	//用于权限的伙伴ID
	private String hbxx;
	//筛选省份
	private String[] provinces;
	//伙伴分类扩展参数3，显示伙伴颜色
	private String hbys;
	//伙伴分类名称
	private String hbflmc;
	//伙伴分类id
	private String hbflid;
	private String ptgs;//平台归属

	private String yptgs;//原平台归属

	private String bgfsdm;//报告方式代码

	public String getBgfsdm() {
		return bgfsdm;
	}

	public void setBgfsdm(String bgfsdm) {
		this.bgfsdm = bgfsdm;
	}

	public String getPtgs() {
		return ptgs;
	}

	public void setPtgs(String ptgs) {
		this.ptgs = ptgs;
	}

	public String getHbflmc() {
		return hbflmc;
	}

	public void setHbflmc(String hbflmc) {
		this.hbflmc = hbflmc;
	}

	public String getHbflid() {
		return hbflid;
	}

	public void setHbflid(String hbflid) {
		this.hbflid = hbflid;
	}

	public String getHbys() {
		return hbys;
	}
	public void setHbys(String hbys) {
		this.hbys = hbys;
	}
	public String[] getProvinces() {
		return provinces;
	}
	public void setProvinces(String[] provinces) {
		this.provinces = provinces;
		for(int i=0; i<provinces.length;i++) {
			this.provinces[i] = this.provinces[i].replace("'", "");
		}
	}
	public String getHbmc()
	{
		return hbmc;
	}
	public void setHbmc(String hbmc)
	{
		this.hbmc = hbmc;
	}
	public String getHbxx() {
		return hbxx;
	}
	public void setHbxx(String hbxx) {
		this.hbxx = hbxx;
	}
	public String getSfid() {
		return sfid;
	}
	public void setSfid(String sfid) {
		this.sfid = sfid;
	}
	public String getSfmc() {
		return sfmc;
	}
	public void setSfmc(String sfmc) {
		this.sfmc = sfmc;
	}


	public String getYptgs() {
		return yptgs;
	}

	public void setYptgs(String yptgs) {
		this.yptgs = yptgs;
	}
}
