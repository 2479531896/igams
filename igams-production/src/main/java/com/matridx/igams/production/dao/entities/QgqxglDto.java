package com.matridx.igams.production.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="QgqxglDto")
public class QgqxglDto extends QgqxglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//请购取消明细信息
	private String qgqxmx_json;
	//申请部门名称
	private String sqbmmc;
	//申请人名称
	private String sqrmc;
	//业务类型名称
	private String ywlxmc;
	//业务类型
	private String ywlx;
	//请购取消明细数据
	private List<QgqxmxDto> qgqxmxlist;
	//访问标记(判断是否为外部接口)
	private String fwbj;
	//rabbit同步标记
	private String prefixFlg;
	//请购类别名称
	private String qglbmc;
	//请购类别代码
	private String qglbdm;
	
	public String getQglbdm() {
		return qglbdm;
	}

	public void setQglbdm(String qglbdm) {
		this.qglbdm = qglbdm;
	}

	public String getQglbmc() {
		return qglbmc;
	}

	public void setQglbmc(String qglbmc) {
		this.qglbmc = qglbmc;
	}

	public String getPrefixFlg() {
		return prefixFlg;
	}

	public void setPrefixFlg(String prefixFlg) {
		this.prefixFlg = prefixFlg;
	}

	public String getFwbj() {
		return fwbj;
	}

	public void setFwbj(String fwbj) {
		this.fwbj = fwbj;
	}

	public List<QgqxmxDto> getQgqxmxlist() {
		return qgqxmxlist;
	}

	public void setQgqxmxlist(List<QgqxmxDto> qgqxmxlist) {
		this.qgqxmxlist = qgqxmxlist;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getYwlxmc() {
		return ywlxmc;
	}

	public void setYwlxmc(String ywlxmc) {
		this.ywlxmc = ywlxmc;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public String getQgqxmx_json() {
		return qgqxmx_json;
	}

	public void setQgqxmx_json(String qgqxmx_json) {
		this.qgqxmx_json = qgqxmx_json;
	}
	
}
