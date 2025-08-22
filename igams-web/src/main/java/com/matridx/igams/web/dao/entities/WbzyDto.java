package com.matridx.igams.web.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="WbzyDto")
public class WbzyDto extends WbzyModel{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	//角色id
	private String jsid;
	private String yjzyid;//一级资源ID
	private String yjzybt;//一级资源标题
	private String ejzyid;//二级资源ID
	private String ejzybt;//二级资源标题
	private String sjzyid;//三级资源ID
	private String sjzybt;//三级资源标题
	private String menuArray;//菜单数组数据
	private List<YhjsDto> yhjslist;//用户角色
	private String depth;//菜单层次
	private String ddzylb;

	public String getDepth() {
		return depth;
	}


	public void setDepth(String depth) {
		this.depth = depth;
	}


	public List<YhjsDto> getYhjslist() {
		return yhjslist;
	}


	public void setYhjslist(List<YhjsDto> yhjslist) {
		this.yhjslist = yhjslist;
	}


	public String getMenuArray() {
		return menuArray;
	}


	public void setMenuArray(String menuArray) {
		this.menuArray = menuArray;
	}


	public String getYjzyid() {
		return yjzyid;
	}


	public void setYjzyid(String yjzyid) {
		this.yjzyid = yjzyid;
	}


	public String getYjzybt() {
		return yjzybt;
	}


	public void setYjzybt(String yjzybt) {
		this.yjzybt = yjzybt;
	}


	public String getEjzyid() {
		return ejzyid;
	}


	public void setEjzyid(String ejzyid) {
		this.ejzyid = ejzyid;
	}


	public String getEjzybt() {
		return ejzybt;
	}


	public void setEjzybt(String ejzybt) {
		this.ejzybt = ejzybt;
	}


	public String getSjzyid() {
		return sjzyid;
	}


	public void setSjzyid(String sjzyid) {
		this.sjzyid = sjzyid;
	}


	public String getSjzybt() {
		return sjzybt;
	}


	public void setSjzybt(String sjzybt) {
		this.sjzybt = sjzybt;
	}


	public String getJsid() {
		return jsid;
	}


	public void setJsid(String jsid) {
		this.jsid = jsid;
	}


	public String getDdzylb() {
		return ddzylb;
	}

	public void setDdzylb(String ddzylb) {
		this.ddzylb = ddzylb;
	}
}
