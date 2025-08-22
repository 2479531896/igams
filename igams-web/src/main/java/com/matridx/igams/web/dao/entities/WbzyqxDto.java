package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WbzyqxDto")
public class WbzyqxDto extends WbzyqxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] jsids;
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
	//菜单层次
	private String depth;

	
	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}

	public String getFjd() {
		return fjd;
	}

	public void setFjd(String fjd) {
		this.fjd = fjd;
	}

	public String getZybt() {
		return zybt;
	}

	public void setZybt(String zybt) {
		this.zybt = zybt;
	}

	public String getBtsx() {
		return btsx;
	}

	public void setBtsx(String btsx) {
		this.btsx = btsx;
	}

	public String getZylj() {
		return zylj;
	}

	public void setZylj(String zylj) {
		this.zylj = zylj;
	}

	public String getFwlj() {
		return fwlj;
	}

	public void setFwlj(String fwlj) {
		this.fwlj = fwlj;
	}

	public String getTblj() {
		return tblj;
	}

	public void setTblj(String tblj) {
		this.tblj = tblj;
	}

	public String getXssx() {
		return xssx;
	}

	public void setXssx(String xssx) {
		this.xssx = xssx;
	}

	public String getCdcc() {
		return cdcc;
	}

	public void setCdcc(String cdcc) {
		this.cdcc = cdcc;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}

	public String getCskz2() {
		return cskz2;
	}

	public void setCskz2(String cskz2) {
		this.cskz2 = cskz2;
	}

	public String getCskz3() {
		return cskz3;
	}

	public void setCskz3(String cskz3) {
		this.cskz3 = cskz3;
	}

	public String getCskz4() {
		return cskz4;
	}

	public void setCskz4(String cskz4) {
		this.cskz4 = cskz4;
	}

	public String getCskz5() {
		return cskz5;
	}

	public void setCskz5(String cskz5) {
		this.cskz5 = cskz5;
	}

	public String[] getJsids() {
		return jsids;
	}

	public void setJsids(String[] jsids) {
		this.jsids = jsids;
	}
	
}
