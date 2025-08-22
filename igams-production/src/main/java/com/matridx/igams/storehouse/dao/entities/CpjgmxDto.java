package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="CpjgmxDto")
public class CpjgmxDto extends CpjgmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//供应类型
	private String gylxmc;
	//领料部门
	private String llbmmc;
	//产出类型
	private String cclxmc;
	//仓库
	private String ckmc;
	//物料名称
	private String wlmc;
	//物料编码
	private String wlbm;
	//产出品名称
	private String ccpmc;
	//成本相关名称
	private String cbxgmc;
	//供应类型代码
	private String gylxdm;
	//固定用量名称
	private String gdylmc;
	//仓库代码
	private String ckdm;
	//产出类型代码
	private String cclxdm;
	//规格
	private String gg;
	//单位
	private String jldw;
	//物料id
	private String wlid;
	//有效期
	private String yxq;

	public String getYxq() {
		return yxq;
	}

	public void setYxq(String yxq) {
		this.yxq = yxq;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getGg() {
		return gg;
	}

	public void setGg(String gg) {
		this.gg = gg;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public String getCclxdm() {
		return cclxdm;
	}

	public void setCclxdm(String cclxdm) {
		this.cclxdm = cclxdm;
	}

	public String getCkdm() {
		return ckdm;
	}

	public void setCkdm(String ckdm) {
		this.ckdm = ckdm;
	}

	public String getGylxdm() {
		return gylxdm;
	}

	public void setGylxdm(String gylxdm) {
		this.gylxdm = gylxdm;
	}

	public String getGdylmc() {
		return gdylmc;
	}

	public void setGdylmc(String gdylmc) {
		this.gdylmc = gdylmc;
	}

	public String getGylxmc() {
		return gylxmc;
	}

	public void setGylxmc(String gylxmc) {
		this.gylxmc = gylxmc;
	}

	public String getLlbmmc() {
		return llbmmc;
	}

	public void setLlbmmc(String llbmmc) {
		this.llbmmc = llbmmc;
	}

	public String getCclxmc() {
		return cclxmc;
	}

	public void setCclxmc(String cclxmc) {
		this.cclxmc = cclxmc;
	}

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}
	public String getCcpmc() {
		return ccpmc;
	}

	public void setCcpmc(String ccpmc) {
		this.ccpmc = ccpmc;
	}

	public String getCbxgmc() {
		return cbxgmc;
	}

	public void setCbxgmc(String cbxgmc) {
		this.cbxgmc = cbxgmc;
	}
}
