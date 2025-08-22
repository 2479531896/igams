package com.matridx.igams.web.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="JsgnqxDto")
public class JsgnqxDto extends JsgnqxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//角色个人权限集合
	private List<JsgnqxDto> jsgnqxs;
	
	public List<JsgnqxDto> getJsgnqxs()
	{
		return jsgnqxs;
	}
	public void setJsgnqxs(List<JsgnqxDto> jsgnqxs)
	{
		this.jsgnqxs = jsgnqxs;
	}
	
	
}
