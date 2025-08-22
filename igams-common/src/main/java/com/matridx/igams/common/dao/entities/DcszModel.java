package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="DcszModel")
public class DcszModel extends BaseModel{
	//业务ID
	private String ywid;
	//业务名称
	private String ywmc;
	//导出字段
	private String dczd;
	//导出字段名称  用户导出的EXCEL的标题
	private String dczdmc;
	//显示顺序
	private String xsxx;
	//显示名称 页面显示名称，会加一些特殊字符用于页面显示
	private String xsmc;
	//SQL字段  可以更改显示的格式等
	private String sqlzd;
	//显示属性 用于设置导出的excel里的格子属性，数字可以显示数字类型
	private String xssx;
	//显示宽度 用于设置导出的excel里的导出宽度，空的时候采用默认值
	private String xskd;
	//字段说明 用于鼠标放上去后的辅助说明
	private String zdsm;
	//分类名称
	private String flmc;
	//分类排序
	private String flpx;
	//业务ID
	public String getYwid() {
		return ywid;
	}
	public void setYwid(String ywid){
		this.ywid = ywid;
	}
	//业务名称
	public String getYwmc() {
		return ywmc;
	}
	public void setYwmc(String ywmc){
		this.ywmc = ywmc;
	}
	//导出字段
	public String getDczd() {
		return dczd;
	}
	public void setDczd(String dczd){
		this.dczd = dczd;
	}
	//导出字段名称  用户导出的EXCEL的标题
	public String getDczdmc() {
		return dczdmc;
	}
	public void setDczdmc(String dczdmc){
		this.dczdmc = dczdmc;
	}
	//显示顺序
	public String getXsxx() {
		return xsxx;
	}
	public void setXsxx(String xsxx){
		this.xsxx = xsxx;
	}
	//显示名称 页面显示名称，会加一些特殊字符用于页面显示
	public String getXsmc() {
		return xsmc;
	}
	public void setXsmc(String xsmc){
		this.xsmc = xsmc;
	}
	//SQL字段  可以更改显示的格式等
	public String getSqlzd() {
		return sqlzd;
	}
	public void setSqlzd(String sqlzd){
		this.sqlzd = sqlzd;
	}
	//显示属性 用于设置导出的excel里的格子属性，数字可以显示数字类型
	public String getXssx() {
		return xssx;
	}
	public void setXssx(String xssx){
		this.xssx = xssx;
	}
	//显示宽度 用于设置导出的excel里的导出宽度，空的时候采用默认值
	public String getXskd() {
		return xskd;
	}
	public void setXskd(String xskd){
		this.xskd = xskd;
	}
	//字段说明 用于鼠标放上去后的辅助说明
	public String getZdsm() {
		return zdsm;
	}
	public void setZdsm(String zdsm){
		this.zdsm = zdsm;
	}
	//分类名称
	public String getFlmc() {
		return flmc;
	}
	public void setFlmc(String flmc){
		this.flmc = flmc;
	}
	//分类排序
	public String getFlpx() {
		return flpx;
	}
	public void setFlpx(String flpx){
		this.flpx = flpx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
