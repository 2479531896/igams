package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="TqglDto")
public class TqglDto extends TqglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//录入人员名称
	private String lrrymc;
	//修改人员名称
	private String xgrymc;
	//检测单位
	private String jcdw;
	//序号
	private String xh;
	//检测单位名称
	private String jcdwmc;
	//检测单位限制
	private List<String> jcdwxz;
	//后缀权限
	private String hzqx;
	//内部编号
	private String nbbh;
	//明细数据
	private String tqmx_json;
	private String czrmc;
	private String hdrmc;
	private String czryhm;
	private String hdryhm;

	public String getCzryhm() {
		return czryhm;
	}

	public void setCzryhm(String czryhm) {
		this.czryhm = czryhm;
	}

	public String getHdryhm() {
		return hdryhm;
	}

	public void setHdryhm(String hdryhm) {
		this.hdryhm = hdryhm;
	}

	public String getCzrmc() {
		return czrmc;
	}

	public void setCzrmc(String czrmc) {
		this.czrmc = czrmc;
	}

	public String getHdrmc() {
		return hdrmc;
	}

	public void setHdrmc(String hdrmc) {
		this.hdrmc = hdrmc;
	}

	public String getTqmx_json() {
		return tqmx_json;
	}

	public void setTqmx_json(String tqmx_json) {
		this.tqmx_json = tqmx_json;
	}

	public String getNbbh() {
		return nbbh;
	}

	public void setNbbh(String nbbh) {
		this.nbbh = nbbh;
	}

	public String getHzqx() {
		return hzqx;
	}
	public void setHzqx(String hzqx) {
		this.hzqx = hzqx;
	}
	public List<String> getJcdwxz() {
		return jcdwxz;
	}
	public void setJcdwxz(List<String> jcdwxz) {
		this.jcdwxz = jcdwxz;
	}
	public String getJcdwmc()
	{
		return jcdwmc;
	}
	public void setJcdwmc(String jcdwmc)
	{
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
	public String getLrrymc() {
		return lrrymc;
	}
	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}
	public String getXgrymc() {
		return xgrymc;
	}
	public void setXgrymc(String xgrymc) {
		this.xgrymc = xgrymc;
	}
	

}
