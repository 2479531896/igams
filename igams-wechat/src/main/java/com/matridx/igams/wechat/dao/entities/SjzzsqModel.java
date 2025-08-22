package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="SjzzsqModel")
public class SjzzsqModel extends BaseBasicModel {
	//纸质报告申请ID
	private String zzsqid;
	//送检ID
	private String sjid;
	//收件人
	private String sjr;
	//电话
	private String dh;
	//地址
	private String dz;
	//份数  默认:1
	private String fs;
	//发票随寄  默认:0(否)
	private String fpsj;
	//状态 00,10,15,80
	private String zt;
	//备注
	private String bz;
	//是否锁定
	private String sfsd;
	//锁定人员
	private String sdry;
	//锁定日期
	private String sdrq;
	//包号
	private String bh;
	//打包人员
	private String dbry;
	//打包时间
	private String dbsj;

	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}

	public String getDbry() {
		return dbry;
	}

	public void setDbry(String dbry) {
		this.dbry = dbry;
	}

	public String getDbsj() {
		return dbsj;
	}

	public void setDbsj(String dbsj) {
		this.dbsj = dbsj;
	}

	public String getSdrq() {
		return sdrq;
	}

	public void setSdrq(String sdrq) {
		this.sdrq = sdrq;
	}

	public String getSdry() {
		return sdry;
	}

	public void setSdry(String sdry) {
		this.sdry = sdry;
	}

	public String getSfsd() {
		return sfsd;
	}

	public void setSfsd(String sfsd) {
		this.sfsd = sfsd;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public String getZzsqid() {
		return zzsqid;
	}

	public void setZzsqid(String zzsqid) {
		this.zzsqid = zzsqid;
	}

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getSjr() {
		return sjr;
	}

	public void setSjr(String sjr) {
		this.sjr = sjr;
	}

	public String getDh() {
		return dh;
	}

	public void setDh(String dh) {
		this.dh = dh;
	}

	public String getDz() {
		return dz;
	}

	public void setDz(String dz) {
		this.dz = dz;
	}

	public String getFs() {
		return fs;
	}

	public void setFs(String fs) {
		this.fs = fs;
	}

	public String getFpsj() {
		return fpsj;
	}

	public void setFpsj(String fpsj) {
		this.fpsj = fpsj;
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
}
