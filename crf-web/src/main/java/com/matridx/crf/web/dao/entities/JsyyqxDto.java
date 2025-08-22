package com.matridx.crf.web.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value = "JsyyqxDto")
public class JsyyqxDto extends JsyyqxModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 分布式标记
	protected String prefix;
	// 医院名称
	private String yymc;
	//医院id，多个
	private List<String> ids;
	
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	public String getYymc() {
		return yymc;
	}
	public void setYymc(String yymc) {
		this.yymc = yymc;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	

}
