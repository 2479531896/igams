package com.matridx.igams.wechat.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="BfdxglDto")
public class BfdxglDto extends BfdxglModel{
	//单位分类名称
	private String dwflmc;
	//单位等级名称
	private String dwdjmc;
	//单位类别名称
	private String dwlbmc;
	//省份
	private String sfmc;
	//来源名称
	private String lymc;
	//单位分类[多]
	private String[] dwfls;
	//单位等级[多]
	private String[] dwdjs;
	//单位类别[多]
	private String[] dwlbs;
	//省份[多]
	private String[] sfs;
	//来源[多]
	private String[] lys;
	//是否分配
	private String sffp;
	//联系人Json
	private String lxr_json;
	//导出sql
	private String sqlParam;
	//录入人员名称
	private String lrrymc;
	//业务员名称
	private String ywymc;
	//业务员用户名
	private String ywyyhm;
	//VUE导出通用字段
	private String _key;
	//单位分类代码
	private String dwfldm;
	//全部(查询条件)
	private String entire;
	//资源分配Json
	private String zyfp_json;
	//单位分类参数扩展1
	private String dwflcskz1;
	//拜访时间
	private String bfsj;
	//机构ID[多]
	private String[] jgids;
	//业务员[多]
	private String[] ywys;
	//客户分级名称
	private String khfjmc;

	public String getKhfjmc() {
		return khfjmc;
	}

	public void setKhfjmc(String khfjmc) {
		this.khfjmc = khfjmc;
	}

	public String[] getJgids() {
		return jgids;
	}

	public void setJgids(String[] jgids) {
		this.jgids = jgids;
	}

	public String[] getYwys() {
		return ywys;
	}

	public void setYwys(String[] ywys) {
		this.ywys = ywys;
	}

	public String getBfsj() {
		return bfsj;
	}

	public void setBfsj(String bfsj) {
		this.bfsj = bfsj;
	}

	public String getDwflcskz1() {
		return dwflcskz1;
	}

	public void setDwflcskz1(String dwflcskz1) {
		this.dwflcskz1 = dwflcskz1;
	}

	public String getZyfp_json() {
		return zyfp_json;
	}

	public void setZyfp_json(String zyfp_json) {
		this.zyfp_json = zyfp_json;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getDwfldm() {
		return dwfldm;
	}

	public void setDwfldm(String dwfldm) {
		this.dwfldm = dwfldm;
	}

	public String get_key() {
		return _key;
	}

	public void set_key(String _key) {
		this._key = _key;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getYwymc() {
		return ywymc;
	}

	public void setYwymc(String ywymc) {
		this.ywymc = ywymc;
	}

	public String getYwyyhm() {
		return ywyyhm;
	}

	public void setYwyyhm(String ywyyhm) {
		this.ywyyhm = ywyyhm;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getLxr_json() {
		return lxr_json;
	}

	public void setLxr_json(String lxr_json) {
		this.lxr_json = lxr_json;
	}

	public String getDwflmc() {
		return dwflmc;
	}

	public void setDwflmc(String dwflmc) {
		this.dwflmc = dwflmc;
	}

	public String getDwdjmc() {
		return dwdjmc;
	}

	public void setDwdjmc(String dwdjmc) {
		this.dwdjmc = dwdjmc;
	}

	public String getDwlbmc() {
		return dwlbmc;
	}

	public void setDwlbmc(String dwlbmc) {
		this.dwlbmc = dwlbmc;
	}

	public String getSfmc() {
		return sfmc;
	}

	public void setSfmc(String sfmc) {
		this.sfmc = sfmc;
	}

	public String getLymc() {
		return lymc;
	}

	public void setLymc(String lymc) {
		this.lymc = lymc;
	}

	public String[] getDwfls() {
		return dwfls;
	}

	public void setDwfls(String[] dwfls) {
		this.dwfls = dwfls;
	}

	public String[] getDwdjs() {
		return dwdjs;
	}

	public void setDwdjs(String[] dwdjs) {
		this.dwdjs = dwdjs;
	}

	public String[] getDwlbs() {
		return dwlbs;
	}

	public void setDwlbs(String[] dwlbs) {
		this.dwlbs = dwlbs;
	}

	public String[] getSfs() {
		return sfs;
	}

	public void setSfs(String[] sfs) {
		this.sfs = sfs;
	}

	public String[] getLys() {
		return lys;
	}

	public void setLys(String[] lys) {
		this.lys = lys;
	}

	public String getSffp() {
		return sffp;
	}

	public void setSffp(String sffp) {
		this.sffp = sffp;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
