package com.matridx.igams.common.dao.entities;


import org.apache.ibatis.type.Alias;

@Alias(value = "YyxxDto")
public class YyxxDto extends YyxxModel {
	private static final long serialVersionUID = 1L;
	// 单位等级名称
	private String dwdjmc;
	// 单位类别名称
	private String dwlbmc;
	// 省份
	private String sfmc;
	// 城市
	private String csmc;
	//全部(查询条件)
	private String entire;
	private String[] dwlbs;	//检索用单位类别（多）
	private String[] dwdjs;	//检索用单位等级（多）
	private String[] cskz2s;//检索用重点等级（多）
	private String SqlParam; 	//导出关联标记位//所选择的字段
	//查询参数[多个条件]
	private String searchParam;
	//查询条数
	private String count;
	//单位类别标记
	private String dwFlag;

	public String getDwFlag() {
		return dwFlag;
	}

	public void setDwFlag(String dwFlag) {
		this.dwFlag = dwFlag;
	}

	public String[] getDwlbs() {
		return dwlbs;
	}
	public void setDwlbs(String[] dwlbs) {
		this.dwlbs = dwlbs;
		for (int i = 0; i < dwlbs.length; i++) {
			this.dwlbs[i] = this.dwlbs[i].replace("'", "");
		}
	}
	public String[] getDwdjs() {
		return dwdjs;
	}
	public void setDwdjs(String[] dwdjs) {
		this.dwdjs = dwdjs;
		for (int i = 0; i < dwdjs.length; i++) {
			this.dwdjs[i] = this.dwdjs[i].replace("'", "");
		}
	}
	public String[] getCskz2s() {
		return cskz2s;
	}
	public void setCskz2s(String[] cskz2s) {
		this.cskz2s = cskz2s;
		for (int i = 0; i < cskz2s.length; i++) {
			this.cskz2s[i] = this.cskz2s[i].replace("'", "");
		}
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
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

	public String getCsmc() {
		return csmc;
	}

	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}
	public String getSearchParam() {
		return searchParam;
	}
	public void setSearchParam(String searchParam) {
		this.searchParam = searchParam;
	}
	
	
}
