package com.matridx.igams.common.dao.entities;


import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value = "ZltsglDto")
public class ZltsglDto extends ZltsglModel {
	private static final long serialVersionUID = 1L;

	//来源名称
	private String lymc;
	//处理耗时
	private String clhs;
	//来源[多]
	private String[] lys;
	//录入开始时间
	private String lrsjstart;
	//录入结束时间
	private String lrsjend;
	//处理完成开始时间
	private String wcsjstart;
	//处理完成结束时间
	private String wcsjend;
	//投诉项目名称
	private String tsxmmc;
	//内部编码
	private String nbbm;
	//年龄
	private String nl;
	//患者姓名
	private String hzxm;
	//样本类型名称
	private String yblxmc;
	//检测单位名称
	private String jcdwmc;
	//部门
	private List<String> bms;
	//附件IDs
	private List<String> fjids;
	//录入人员名称
	private String lrrymc;
	//调查情况
	private String dcqk;
	//整改情况
	private String zgqk;
	//部门拼凑的字符串
	private String bm_str;
	//业务IDs
	private List<String> ywids;
	//样本编号
	private String ybbh;
	//送检ID
	private String sjid;
	//检测项目ID
	private String jcxmid;
	//检测项目名称
	private String jcxmmc;
	//情况json
	private String clqk_json;
	//查询条件(全部)
	private String entire;

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getClqk_json() {
		return clqk_json;
	}

	public void setClqk_json(String clqk_json) {
		this.clqk_json = clqk_json;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getJcxmid() {
		return jcxmid;
	}

	public void setJcxmid(String jcxmid) {
		this.jcxmid = jcxmid;
	}

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public List<String> getYwids() {
		return ywids;
	}

	public void setYwids(List<String> ywids) {
		this.ywids = ywids;
	}

	public String getBm_str() {
		return bm_str;
	}

	public void setBm_str(String bm_str) {
		this.bm_str = bm_str;
	}

	public String getDcqk() {
		return dcqk;
	}

	public void setDcqk(String dcqk) {
		this.dcqk = dcqk;
	}

	public String getZgqk() {
		return zgqk;
	}

	public void setZgqk(String zgqk) {
		this.zgqk = zgqk;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getWcsjstart() {
		return wcsjstart;
	}

	public void setWcsjstart(String wcsjstart) {
		this.wcsjstart = wcsjstart;
	}

	public String getWcsjend() {
		return wcsjend;
	}

	public void setWcsjend(String wcsjend) {
		this.wcsjend = wcsjend;
	}

	public List<String> getBms() {
		return bms;
	}

	public void setBms(List<String> bms) {
		this.bms = bms;
	}

	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String getTsxmmc() {
		return tsxmmc;
	}

	public void setTsxmmc(String tsxmmc) {
		this.tsxmmc = tsxmmc;
	}

	public String[] getLys() {
		return lys;
	}

	public void setLys(String[] lys) {
		this.lys = lys;
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

	public String getClhs() {
		return clhs;
	}

	public void setClhs(String clhs) {
		this.clhs = clhs;
	}

	public String getLymc() {
		return lymc;
	}

	public void setLymc(String lymc) {
		this.lymc = lymc;
	}
}
