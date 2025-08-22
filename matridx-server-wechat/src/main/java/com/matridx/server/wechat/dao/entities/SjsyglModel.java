package com.matridx.server.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="SjsyglModel")
public class SjsyglModel extends BaseModel{
	private String syglid;
	private String sjid;
	private String jcxmid;
	private String jczxmid;
	private String jclxid;
	private String jcdw;
	private String sfjs;
	private String nbzbm;
	private String ywid;
	private String qyry;
	private String qysj;
	private String jsrq;
	private String jsry;
	private String jcbj;
	private String syrq;
	private String jt;
	private String zt;
	private String bz;
	private String dyid;//对应id
	private String lx;//类型
	private String kzcs4;
	//上机时间
	private String sjsj;
	private String xmmc;//项目名称
	//文库生信编码
	private String wksxbm;

	public String getWksxbm() {
		return wksxbm;
	}

	public void setWksxbm(String wksxbm) {
		this.wksxbm = wksxbm;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public String getSjsj() {
		return sjsj;
	}

	public void setSjsj(String sjsj) {
		this.sjsj = sjsj;
	}

	public String getQysj() {
		return qysj;
	}

	public void setQysj(String qysj) {
		this.qysj = qysj;
	}

	public String getQyry() {
		return qyry;
	}

	public void setQyry(String qyry) {
		this.qyry = qyry;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getKzcs4() {
		return kzcs4;
	}

	public void setKzcs4(String kzcs4) {
		this.kzcs4 = kzcs4;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getDyid() {
		return dyid;
	}

	public void setDyid(String dyid) {
		this.dyid = dyid;
	}
	public String getSyglid() {
		return syglid;
	}

	public void setSyglid(String syglid) {
		this.syglid = syglid;
	}

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getJcxmid() {
		return jcxmid;
	}

	public void setJcxmid(String jcxmid) {
		this.jcxmid = jcxmid;
	}

	public String getJczxmid() {
		return jczxmid;
	}

	public void setJczxmid(String jczxmid) {
		this.jczxmid = jczxmid;
	}

	public String getJclxid() {
		return jclxid;
	}

	public void setJclxid(String jclxid) {
		this.jclxid = jclxid;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getSfjs() {
		return sfjs;
	}

	public void setSfjs(String sfjs) {
		this.sfjs = sfjs;
	}

	public String getNbzbm() {
		return nbzbm;
	}

	public void setNbzbm(String nbzbm) {
		this.nbzbm = nbzbm;
	}

	public String getJsrq() {
		return jsrq;
	}

	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}

	public String getJsry() {
		return jsry;
	}

	public void setJsry(String jsry) {
		this.jsry = jsry;
	}

	public String getJcbj() {
		return jcbj;
	}

	public void setJcbj(String jcbj) {
		this.jcbj = jcbj;
	}

	public String getSyrq() {
		return syrq;
	}

	public void setSyrq(String syrq) {
		this.syrq = syrq;
	}

	public String getJt() {
		return jt;
	}

	public void setJt(String jt) {
		this.jt = jt;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
