package com.matridx.server.wechat.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="QgmxDto")
public class QgmxDto extends QgmxModel{
	//物料名称
	private String wlmc;
	//物料类别
	private String wllb;
	//物料编码
	private String wlbm;
	//物料类别名称
	private String wllbmc;
	//物料子类别
	private String wlzlb;
	//物料子类别名称
	private String wlzlbmc;
	//附件IDS
	private List<String> fjids;
	//物料子类别统称
	private  String wlzlbtc;
	//物料子类别统称名称
	private String wlzlbtcmc;
	//筛选条件（下载明细附件）
	private String[] wlzlbtcs;
	//文件路径
	private String wjlj;
	//文件名
	private String wjm;
	//业务类型
	private String ywlx;
	//附件标记(0 表示有附件)
	private String fjbj;
    //生产商
	private String scs;
	//规格
	private String gg;
	//计量单位
	private String jldw;
	//分布式路径
	private String urlPrefix;
	//供应商名称
	private String gysmc;
	
	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}

	public String getScs() {
		return scs;
	}

	public void setScs(String scs) {
		this.scs = scs;
	}

	public String getGg() {
		return gg;
	}

	public void setGg(String gg) {
		this.gg = gg;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public String getWlzlbtcmc() {
		return wlzlbtcmc;
	}

	public void setWlzlbtcmc(String wlzlbtcmc) {
		this.wlzlbtcmc = wlzlbtcmc;
	}

	public String getFjbj() {
		return fjbj;
	}

	public void setFjbj(String fjbj) {
		this.fjbj = fjbj;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getWjlj() {
		return wjlj;
	}

	public void setWjlj(String wjlj) {
		this.wjlj = wjlj;
	}

	public String getWjm() {
		return wjm;
	}

	public void setWjm(String wjm) {
		this.wjm = wjm;
	}

	public String[] getWlzlbtcs() {
		return wlzlbtcs;
	}

	public void setWlzlbtcs(String[] wlzlbtcs) {
		this.wlzlbtcs = wlzlbtcs;
		for(int i=0;i<this.wlzlbtcs.length;i++)
		{
			this.wlzlbtcs[i] = this.wlzlbtcs[i].replace("'", "");
		}
		this.wlzlbtcs = wlzlbtcs;
	}

	public String getWlzlbtc() {
		return wlzlbtc;
	}

	public void setWlzlbtc(String wlzlbtc) {
		this.wlzlbtc = wlzlbtc;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}

	public String getWllb() {
		return wllb;
	}

	public void setWllb(String wllb) {
		this.wllb = wllb;
	}

	public String getWllbmc() {
		return wllbmc;
	}

	public void setWllbmc(String wllbmc) {
		this.wllbmc = wllbmc;
	}

	public String getWlzlb() {
		return wlzlb;
	}

	public void setWlzlb(String wlzlb) {
		this.wlzlb = wlzlb;
	}

	public String getWlzlbmc() {
		return wlzlbmc;
	}

	public void setWlzlbmc(String wlzlbmc) {
		this.wlzlbmc = wlzlbmc;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}
	
	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
