package com.matridx.igams.experiment.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="QclglDto")
public class QclglDto extends QclglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//录入人员名称
	private String lrrymc;
	//全部（用于模糊检索）
	private String allrow;
	//状态(多)
	private List<String> zts;
	//检测单位
	private String jcdw;
	//序号
	private String xh;
	//检测单位名称
	private String jcdwmc;
	//检测单位限制
	private List<String> jcdwxz;
	//内部编号(内部编码)
	private String nbbh;
	
 	public List<String> getJcdwxz() {
		return jcdwxz;
	}
	public void setJcdwxz(List<String> jcdwxz) {
		this.jcdwxz = jcdwxz;
	}
	public String getJcdwmc() {
		return jcdwmc;
	}
	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}
	public String getJcdw()
	{
		return jcdw;
	}
	public void setJcdw(String jcdw)
	{
		this.jcdw = jcdw;
	}
	public String getXh()
	{
		return xh;
	}
	public void setXh(String xh)
	{
		this.xh = xh;
	}
	public List<String> getZts()
	{
		return zts;
	}
	public void setZts(List<String> zts)
	{
		this.zts = zts;
	}
	public String getLrrymc()
	{
		return lrrymc;
	}
	public void setLrrymc(String lrrymc)
	{
		this.lrrymc = lrrymc;
	}
	public String getAllrow()
	{
		return allrow;
	}
	public void setAllrow(String allrow)
	{
		this.allrow = allrow;
	}
	public String getNbbh() {
		return nbbh;
	}
	public void setNbbh(String nbbh) {
		this.nbbh = nbbh;
	}
}
