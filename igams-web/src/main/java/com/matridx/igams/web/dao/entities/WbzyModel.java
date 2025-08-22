package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WbzyModel")
public class WbzyModel extends BaseModel{
	//资源ID
	private String zyid;
	//父节点
	private String fjd;
	//资源标题
	private String zybt;
	//标题缩写
	private String btsx;
	//资源路径
	private String zylj;
	//访问路径
	private String fwlj;
	//图标路径
	private String tblj;
	//显示顺序
	private String xssx;
	//菜单层次
	private String cdcc;
	//参数扩展1
	private String cskz1;
	//参数扩展2
	private String cskz2;
	//参数扩展3
	private String cskz3;
	//参数扩展4
	private String cskz4;
	//参数扩展5
	private String cskz5;
	//资源类别
	private String zylb;
	//默认显示标记
	private String mrxs;
	
	public String getZylb() {
		return zylb;
	}
	public void setZylb(String zylb) {
		this.zylb = zylb;
	}
	//资源ID
	public String getZyid() {
		return zyid;
	}
	public void setZyid(String zyid){
		this.zyid = zyid;
	}
	//父节点
	public String getFjd() {
		return fjd;
	}
	public void setFjd(String fjd){
		this.fjd = fjd;
	}
	//资源标题
	public String getZybt() {
		return zybt;
	}
	public void setZybt(String zybt){
		this.zybt = zybt;
	}
	//标题缩写
	public String getBtsx() {
		return btsx;
	}
	public void setBtsx(String btsx){
		this.btsx = btsx;
	}
	//资源路径
	public String getZylj() {
		return zylj;
	}
	public void setZylj(String zylj){
		this.zylj = zylj;
	}
	//访问路径
	public String getFwlj() {
		return fwlj;
	}
	public void setFwlj(String fwlj){
		this.fwlj = fwlj;
	}
	//图标路径
	public String getTblj() {
		return tblj;
	}
	public void setTblj(String tblj){
		this.tblj = tblj;
	}
	//显示顺序
	public String getXssx() {
		return xssx;
	}
	public void setXssx(String xssx){
		this.xssx = xssx;
	}
	//菜单层次
	public String getCdcc() {
		return cdcc;
	}
	public void setCdcc(String cdcc){
		this.cdcc = cdcc;
	}
	//参数扩展1
	public String getCskz1() {
		return cskz1;
	}
	public void setCskz1(String cskz1){
		this.cskz1 = cskz1;
	}
	//参数扩展2
	public String getCskz2() {
		return cskz2;
	}
	public void setCskz2(String cskz2){
		this.cskz2 = cskz2;
	}
	//参数扩展3
	public String getCskz3() {
		return cskz3;
	}
	public void setCskz3(String cskz3){
		this.cskz3 = cskz3;
	}
	//参数扩展4
	public String getCskz4() {
		return cskz4;
	}
	public void setCskz4(String cskz4){
		this.cskz4 = cskz4;
	}
	//参数扩展5
	public String getCskz5() {
		return cskz5;
	}
	public void setCskz5(String cskz5){
		this.cskz5 = cskz5;
	}

	public String getMrxs() {
		return mrxs;
	}
	public void setMrxs(String mrxs) {
		this.mrxs = mrxs;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
