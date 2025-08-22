package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="ZltsglModel")
public class ZltsglModel extends BaseModel{
	//质量投诉ID
	private String zltsid;
	//来源
	private String ly;
	//业务ID
	private String ywid;
	//业务类型
	private String ywlx;
	//投诉内容
	private String tsnr;
	//参数扩展1
	private String cskz1;
	//参数扩展2
	private String cskz2;
	//状态
	private String zt;
	//问题简述
	private String wtjs;
	//投诉项目
	private String tsxm;
	//需调查数
	private String xdcs;
	//已调查数
	private String ydcs;
	//需整改数
	private String xzgs;
	//已整改数
	private String yzgs;
	//结论
	private String jl;
	//问题点
	private String wtd;
	//是否有效
	private String sfyx;
	//处理完成时间
	private String wcsj;
	//处理完成标记
	private String sfwc;

	public String getSfwc() {
		return sfwc;
	}

	public void setSfwc(String sfwc) {
		this.sfwc = sfwc;
	}

	public String getWcsj() {
		return wcsj;
	}

	public void setWcsj(String wcsj) {
		this.wcsj = wcsj;
	}
	public String getXdcs() {
		return xdcs;
	}

	public void setXdcs(String xdcs) {
		this.xdcs = xdcs;
	}

	public String getYdcs() {
		return ydcs;
	}

	public void setYdcs(String ydcs) {
		this.ydcs = ydcs;
	}

	public String getXzgs() {
		return xzgs;
	}

	public void setXzgs(String xzgs) {
		this.xzgs = xzgs;
	}

	public String getYzgs() {
		return yzgs;
	}

	public void setYzgs(String yzgs) {
		this.yzgs = yzgs;
	}

	public String getZltsid() {
		return zltsid;
	}

	public void setZltsid(String zltsid) {
		this.zltsid = zltsid;
	}

	public String getLy() {
		return ly;
	}

	public void setLy(String ly) {
		this.ly = ly;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getTsnr() {
		return tsnr;
	}

	public void setTsnr(String tsnr) {
		this.tsnr = tsnr;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}

	public String getCskz2() {
		return cskz2;
	}

	public void setCskz2(String cskz2) {
		this.cskz2 = cskz2;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getWtjs() {
		return wtjs;
	}

	public void setWtjs(String wtjs) {
		this.wtjs = wtjs;
	}

	public String getTsxm() {
		return tsxm;
	}

	public void setTsxm(String tsxm) {
		this.tsxm = tsxm;
	}

	public String getJl() {
		return jl;
	}

	public void setJl(String jl) {
		this.jl = jl;
	}

	public String getWtd() {
		return wtd;
	}

	public void setWtd(String wtd) {
		this.wtd = wtd;
	}

	public String getSfyx() {
		return sfyx;
	}

	public void setSfyx(String sfyx) {
		this.sfyx = sfyx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
