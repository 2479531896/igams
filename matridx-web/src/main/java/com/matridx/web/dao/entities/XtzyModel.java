package com.matridx.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XtzyModel")
public class XtzyModel extends BaseModel{
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
	//图标路径
	private String tblj;
	//显示顺序
	private String xssx;
	//菜单层次
	private String cdcc;
	//资源标记  00:审核标记
	private String zybj;
	//显示扩展标记  现用于手机端菜单是否显示  01：为显示
	private String xskzbj;
	//功能名 主要用于统一功能名称 ，当改变功能时只需更改一个地方
	private String gnm;
	private String fwlj; //服务路径
	private String fbsqz;//分布式前缀
	public String getFwlj() {
		return fwlj;
	}

	public void setFwlj(String fwlj) {
		this.fwlj = fwlj;
	}

	public String getFbsqz() {
		return fbsqz;
	}

	public void setFbsqz(String fbsqz) {
		this.fbsqz = fbsqz;
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
	//资源路径
	public String getZylj() {
		return zylj;
	}
	public void setZylj(String zylj){
		this.zylj = zylj;
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
	//资源标记  00:审核标记
	public String getZybj() {
		return zybj;
	}
	public void setZybj(String zybj){
		this.zybj = zybj;
	}
	//显示扩展标记  现用于手机端菜单是否显示  01：为显示
	public String getXskzbj() {
		return xskzbj;
	}
	public void setXskzbj(String xskzbj){
		this.xskzbj = xskzbj;
	}
	//功能名 主要用于统一功能名称 ，当改变功能时只需更改一个地方
	public String getGnm() {
		return gnm;
	}
	public void setGnm(String gnm){
		this.gnm = gnm;
	}
	//标题缩写
	public String getBtsx() {
		return btsx;
	}
	public void setBtsx(String btsx) {
		this.btsx = btsx;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
