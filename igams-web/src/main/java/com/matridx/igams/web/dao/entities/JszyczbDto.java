package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="JszyczbDto")
public class JszyczbDto extends JszyczbModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//对应页面
	private String dyym;
	//操作名称
	private String czmc;
	//显示顺序
	private String xsxx;
	//按钮样式
	private String anys;
	//资源路径
	private String zylj;
	//操作说明
	private String czsm;
	
	public String getDyym() {
		return dyym;
	}
	public void setDyym(String dyym) {
		this.dyym = dyym;
	}
	public String getCzmc() {
		return czmc;
	}
	public void setCzmc(String czmc) {
		this.czmc = czmc;
	}
	public String getXsxx() {
		return xsxx;
	}
	public void setXsxx(String xsxx) {
		this.xsxx = xsxx;
	}
	public String getAnys() {
		return anys;
	}
	public void setAnys(String anys) {
		this.anys = anys;
	}
	public String getZylj() {
		return zylj;
	}
	public void setZylj(String zylj) {
		this.zylj = zylj;
	}
	public String getCzsm() {
		return czsm;
	}
	public void setCzsm(String czsm) {
		this.czsm = czsm;
	}
}
