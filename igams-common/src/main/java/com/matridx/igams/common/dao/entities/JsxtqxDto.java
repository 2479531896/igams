package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="JsxtqxDto")
public class JsxtqxDto extends JsxtqxModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//角色代码
	private String jsdm;
	//角色名称
	private String jsmc;

	//角色系统权限
	private List<JsxtqxDto> jsxtqxDtos;

	public List<JsxtqxDto> getJsxtqxDtos() {
		return jsxtqxDtos;
	}

	public void setJsxtqxDtos(List<JsxtqxDto> jsxtqxDtos) {
		this.jsxtqxDtos = jsxtqxDtos;
	}

	public String getJsdm() {
		return jsdm;
	}
	public void setJsdm(String jsdm) {
		this.jsdm = jsdm;
	}
	public String getJsmc() {
		return jsmc;
	}
	public void setJsmc(String jsmc) {
		this.jsmc = jsmc;
	}

}
