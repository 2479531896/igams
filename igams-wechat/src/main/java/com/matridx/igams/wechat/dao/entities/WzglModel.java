package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WzglModel")
public class WzglModel extends BaseModel{
	//物种分类ID
	private String wzid;
	//物种英文名
	private String wzywm;
	//物种中文名
	private String wzzwm;
	//物种分类
	private String wzfl;
	//物种类型
	private String wzlx;
	//物种注释
	private String wzzs;
	//父id
	private String fid;
	//分类等级
	private String fldj;
	//是否高亮
	private String sfgl;
	//传染病级别
	private String crbjb;
	//是否拦截
	private String sflj;
	private String fl;
	private String sfgz;
	private String xjlb;
	private String bdlb;
	private String wzdl;
	private String jycd;
	private String glsrs;
	private String zbx;
	private String taxid;


	public String getFl() {
		return fl;
	}

	public void setFl(String fl) {
		this.fl = fl;
	}

	public String getSfgz() {
		return sfgz;
	}

	public void setSfgz(String sfgz) {
		this.sfgz = sfgz;
	}

	public String getXjlb() {
		return xjlb;
	}

	public void setXjlb(String xjlb) {
		this.xjlb = xjlb;
	}

	public String getBdlb() {
		return bdlb;
	}

	public void setBdlb(String bdlb) {
		this.bdlb = bdlb;
	}

	public String getWzdl() {
		return wzdl;
	}

	public void setWzdl(String wzdl) {
		this.wzdl = wzdl;
	}

	public String getJycd() {
		return jycd;
	}

	public void setJycd(String jycd) {
		this.jycd = jycd;
	}

	public String getGlsrs() {
		return glsrs;
	}

	public void setGlsrs(String glsrs) {
		this.glsrs = glsrs;
	}

	public String getZbx() {
		return zbx;
	}

	public void setZbx(String zbx) {
		this.zbx = zbx;
	}

	public String getTaxid() {
		return taxid;
	}

	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}

	public String getCrbjb() {
		return crbjb;
	}

	public void setCrbjb(String crbjb) {
		this.crbjb = crbjb;
	}

	public String getSflj() {
		return sflj;
	}

	public void setSflj(String sflj) {
		this.sflj = sflj;
	}

	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getFldj() {
		return fldj;
	}
	public void setFldj(String fldj) {
		this.fldj = fldj;
	}
	public String getSfgl() {
		return sfgl;
	}
	public void setSfgl(String sfgl) {
		this.sfgl = sfgl;
	}
	//物种分类ID
	public String getWzid() {
		return wzid;
	}
	public void setWzid(String wzid){
		this.wzid = wzid;
	}
	//物种英文名
	public String getWzywm() {
		return wzywm;
	}
	public void setWzywm(String wzywm){
		this.wzywm = wzywm;
	}
	//物种中文名
	public String getWzzwm() {
		return wzzwm;
	}
	public void setWzzwm(String wzzwm){
		this.wzzwm = wzzwm;
	}
	//物种分类
	public String getWzfl() {
		return wzfl;
	}
	public void setWzfl(String wzfl){
		this.wzfl = wzfl;
	}
	//物种类型
	public String getWzlx() {
		return wzlx;
	}
	public void setWzlx(String wzlx){
		this.wzlx = wzlx;
	}
	//物种注释
	public String getWzzs() {
		return wzzs;
	}
	public void setWzzs(String wzzs){
		this.wzzs = wzzs;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
