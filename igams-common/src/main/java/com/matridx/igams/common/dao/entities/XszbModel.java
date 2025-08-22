package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XszbModel")
public class XszbModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//指标ID
	private String zbid;
	//用户ID
	private String yhid;
	//指标类型(基础数据的csid)
	private String zblx;
	//开始周期
	private String kszq;
	//结束周期
	private String jszq;
	//数值
	private String sz;
	//指标分类
	private String zbfl;
	//指标子分类
	private String zbzfl;
	//是否确认
	private String sfqr;

	public String getSfqr() {
		return sfqr;
	}

	public void setSfqr(String sfqr) {
		this.sfqr = sfqr;
	}

	public String getZbfl() {
		return zbfl;
	}

	public void setZbfl(String zbfl) {
		this.zbfl = zbfl;
	}

	public String getZbzfl() {
		return zbzfl;
	}

	public void setZbzfl(String zbzfl) {
		this.zbzfl = zbzfl;
	}

	public String getZbid() {
		return zbid;
	}
	public void setZbid(String zbid) {
		this.zbid = zbid;
	}
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid) {
		this.yhid = yhid;
	}
	public String getZblx() {
		return zblx;
	}
	public void setZblx(String zblx) {
		this.zblx = zblx;
	}
	public String getKszq() {
		return kszq;
	}
	public void setKszq(String kszq) {
		this.kszq = kszq;
	}
	public String getJszq() {
		return jszq;
	}
	public void setJszq(String jszq) {
		this.jszq = jszq;
	}
	public String getSz() {
		return sz;
	}
	public void setSz(String sz) {
		this.sz = sz;
	}
	
}
