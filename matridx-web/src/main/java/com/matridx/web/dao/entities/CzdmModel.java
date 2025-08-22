package com.matridx.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="CzdmModel")
public class CzdmModel extends BaseModel{
	//操作代码
	private String czdm;
	//操作名称
	private String czmc;
	//显示顺序
	private String xssx;
	//按钮样式
	private String anys;
	//操作说明
	private String czsm;
	
	//操作代码
	public String getCzdm() {
		return czdm;
	}
	public void setCzdm(String czdm){
		this.czdm = czdm;
	}
	//操作名称
	public String getCzmc() {
		return czmc;
	}
	public void setCzmc(String czmc){
		this.czmc = czmc;
	}
	//显示顺序
	public String getXssx() {
		return xssx;
	}
	public void setXssx(String xssx){
		this.xssx = xssx;
	}
	//按钮样式
	public String getAnys() {
		return anys;
	}
	public void setAnys(String anys){
		this.anys = anys;
	}
	//操作说明
	public String getCzsm() {
		return czsm;
	}
	public void setCzsm(String czsm) {
		this.czsm = czsm;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
