package com.matridx.igams.common.dao.entities;


import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value = "TsclqkDto")
public class TsclqkDto extends TsclqkModel {
	private static final long serialVersionUID = 1L;

	//部门名称
	private String bmmc;
	private List<FjcfbDto> fjcfbDtos;

	private String tssj;

	private String tsnr;

	private String hzxm;

	private String nbbm;

	private String ybbh;

	private String yhid;

	private String lrsjstart;

	private String lrsjend;

	private String tssjstart;

	private String tssjend;

	private String clsjstart;

	private String clsjend;
	//附件IDS
	private List<String> fjids;
	//处理人员名称
	private String clrymc;
	//类型[多]
	private String[] lxs;

	private String wbcxmc;

	public String[] getLxs() {
		return lxs;
	}

	public void setLxs(String[] lxs) {
		this.lxs = lxs;
	}

	public String getClrymc() {
		return clrymc;
	}

	public void setClrymc(String clrymc) {
		this.clrymc = clrymc;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public List<FjcfbDto> getFjcfbDtos() {
		return fjcfbDtos;
	}

	public void setFjcfbDtos(List<FjcfbDto> fjcfbDtos) {
		this.fjcfbDtos = fjcfbDtos;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getTssj() {
		return tssj;
	}

	public void setTssj(String tssj) {
		this.tssj = tssj;
	}

	public String getTsnr() {
		return tsnr;
	}

	public void setTsnr(String tsnr) {
		this.tsnr = tsnr;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getLrsjstart() {
		return lrsjstart;
	}

	public void setLrsjstart(String lrsjstart) {
		this.lrsjstart = lrsjstart;
	}

	public String getLrsjend() {
		return lrsjend;
	}

	public void setLrsjend(String lrsjend) {
		this.lrsjend = lrsjend;
	}

	public String getTssjstart() {
		return tssjstart;
	}

	public void setTssjstart(String tssjstart) {
		this.tssjstart = tssjstart;
	}

	public String getTssjend() {
		return tssjend;
	}

	public void setTssjend(String tssjend) {
		this.tssjend = tssjend;
	}

	public String getClsjstart() {
		return clsjstart;
	}

	public void setClsjstart(String clsjstart) {
		this.clsjstart = clsjstart;
	}

	public String getClsjend() {
		return clsjend;
	}

	public void setClsjend(String clsjend) {
		this.clsjend = clsjend;
	}

	public String getWbcxmc() {
		return wbcxmc;
	}

	public void setWbcxmc(String wbcxmc) {
		this.wbcxmc = wbcxmc;
	}
}
