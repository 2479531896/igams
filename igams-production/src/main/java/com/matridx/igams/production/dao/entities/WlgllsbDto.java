package com.matridx.igams.production.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="WlgllsbDto")
public class WlgllsbDto extends WlgllsbModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String wllbdm;	//物料类别代码
	private String wllbmc;	//物料类别名称
	private String wlzlbdm;	//物料子类别代码
	private String wlzlbmc;	//物料子类别名称
	private String shsjstart;	//审核通过开始时间
	private String shsjend;		//审核通过结束时间
	private String sqsjstart;	//审核申请开始时间
	private String sqsjend;		//审核申请开始时间
	private String lrryxm;		//录入人员姓名
	private String[] wllbs;	//检索用物料类别（多）
	private String[] wlzlbs;	//检索用物料子类别（多）
	private String[] sfwxps;	//检索用是否危险品（多）
	private List<WlgllsbDto> wllsbDtos;
	private String shtime;		//审核时间格式化
	private String lrtime;	//录入时间格式化
	private String sfwxpmc;//是否危险品名称
	//导出关联标记位
	//所选择的字段
	private String SqlParam;
	//物料类别基础数据
	private String wllb_flg;
	//物料子类别基础数据
	private String wlzlb_flg;
	//物料子类别统称名称
	private String wlzlbtcmc;
	//路径标记
	private String ljbj;
	private String rf;//人份

	public String getRf() {
		return rf;
	}

	public void setRf(String rf) {
		this.rf = rf;
	}

	public String getLjbj() {
		return ljbj;
	}

	public void setLjbj(String ljbj) {
		this.ljbj = ljbj;
	}

	public String getSfwxpmc() {
		return sfwxpmc;
	}

	public void setSfwxpmc(String sfwxpmc) {
		this.sfwxpmc = sfwxpmc;
	}

	public String getWlzlbtcmc() {
		return wlzlbtcmc;
	}

	public void setWlzlbtcmc(String wlzlbtcmc) {
		this.wlzlbtcmc = wlzlbtcmc;
	}

	public String getWllbmc() {
		return wllbmc;
	}

	public void setWllbmc(String wllbmc) {
		this.wllbmc = wllbmc;
	}

	public String[] getWllbs() {
		return wllbs;
	}

	public void setWllbs(String[] wllbs) {
		this.wllbs = wllbs;
		for(int i=0;i<this.wllbs.length;i++)
		{
			this.wllbs[i] = this.wllbs[i].replace("'", "");
		}
	}

	public String[] getSfwxps() {
		return sfwxps;
	}

	public void setSfwxps(String[] sfwxps) {
		this.sfwxps = sfwxps;
		for(int i=0;i<this.sfwxps.length;i++)
		{
			this.sfwxps[i] = this.sfwxps[i].replace("'", "");
		}
	}

	public String getWlzlbmc() {
		return wlzlbmc;
	}

	public void setWlzlbmc(String wlzlbmc) {
		this.wlzlbmc = wlzlbmc;
	}

	public String[] getWlzlbs() {
		return wlzlbs;
	}

	public void setWlzlbs(String[] wlzlbs) {
		this.wlzlbs = wlzlbs;
		for(int i=0;i<this.wlzlbs.length;i++)
		{
			this.wlzlbs[i] = this.wlzlbs[i].replace("'", "");
		}
	}

	public String getWllbdm() {
		return wllbdm;
	}

	public void setWllbdm(String wllbdm) {
		this.wllbdm = wllbdm;
	}

	public String getWlzlbdm() {
		return wlzlbdm;
	}

	public void setWlzlbdm(String wlzlbdm) {
		this.wlzlbdm = wlzlbdm;
	}

	public String getShsjstart() {
		return shsjstart;
	}

	public void setShsjstart(String shsjstart) {
		this.shsjstart = shsjstart;
	}

	public String getShsjend() {
		return shsjend;
	}

	public void setShsjend(String shsjend) {
		this.shsjend = shsjend;
	}

	public String getLrryxm() {
		return lrryxm;
	}

	public void setLrryxm(String lrryxm) {
		this.lrryxm = lrryxm;
	}

	public String getWllb_flg() {
		return wllb_flg;
	}

	public void setWllb_flg(String wllb_flg) {
		this.wllb_flg = wllb_flg;
	}

	public String getWlzlb_flg() {
		return wlzlb_flg;
	}

	public void setWlzlb_flg(String wlzlb_flg) {
		this.wlzlb_flg = wlzlb_flg;
	}

	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public String getSqsjstart() {
		return sqsjstart;
	}

	public void setSqsjstart(String sqsjstart) {
		this.sqsjstart = sqsjstart;
	}

	public String getSqsjend() {
		return sqsjend;
	}

	public void setSqsjend(String sqsjend) {
		this.sqsjend = sqsjend;
	}

	public List<WlgllsbDto> getWllsbDtos() {
		return wllsbDtos;
	}

	public void setWllsbDtos(List<WlgllsbDto> wllsbDtos) {
		this.wllsbDtos = wllsbDtos;
	}

	public String getShtime() {
		return shtime;
	}

	public void setShtime(String shtime) {
		this.shtime = shtime;
	}

	public String getLrtime() {
		return lrtime;
	}

	public void setLrtime(String lrtime) {
		this.lrtime = lrtime;
	}
	
}
