package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="YhqdxxDto")
public class YhqdxxDto extends YhqdxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//开始时间
	private String kssj;
	//结束时间
	private String jssj;
	//导出路径
	private String path;
	//机构id
	private String jgid;
	
	public String getJgid() {
		return jgid;
	}
	public void setJgid(String jgid) {
		this.jgid = jgid;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getKssj() {
		return kssj;
	}
	public void setKssj(String kssj) {
		this.kssj = kssj;
	}
	public String getJssj() {
		return jssj;
	}
	public void setJssj(String jssj) {
		this.jssj = jssj;
	}
	
	
}
