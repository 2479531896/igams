package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="XtzyDto")
public class XtzyDto extends XtzyModel implements Cloneable{
	//子菜单
	List<XtzyDto> subzydtos;
	
	private String yjzyid;//一级资源ID
	private String yjzybt;//一级资源标题
	private String ejzyid;//二级资源ID
	private String ejzybt;//二级资源标题
	private String sjzyid;//三级资源ID
	private String sjzybt;//三级资源标题
	private String czdm;//操作代码
	private String czmc;//操作名称
	private String menuArray;//菜单数组数据
	private String jsid;//角色ID
	private String dyym;//操作代码对应页面
	private String depth;//层深，用于判断所在层次
	private String czsm;//操作说明
	private String fwlj;
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
	public String getCzdm() {
		return czdm;
	}
	public void setCzdm(String czdm) {
		this.czdm = czdm;
	}
	public String getCzmc() {
		return czmc;
	}
	public void setCzmc(String czmc) {
		this.czmc = czmc;
	}
	public String getMenuArray() {
		return menuArray;
	}
	public void setMenuArray(String menuArray) {
		this.menuArray = menuArray;
	}
	public String getJsid() {
		return jsid;
	}
	public void setJsid(String jsid) {
		this.jsid = jsid;
	}
	public String getDyym() {
		return dyym;
	}
	public void setDyym(String dyym) {
		this.dyym = dyym;
	}
	public List<XtzyDto> getSubzydtos() {
		return subzydtos;
	}
	public void setSubzydtos(List<XtzyDto> subzydtos) {
		this.subzydtos = subzydtos;
	}
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
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

	@Override
	public Object clone() {
		XtzyDto xtzyDto = null;
		try {
			xtzyDto = (XtzyDto)super.clone();
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		return xtzyDto;
	}

	public String getFwlj() {
		return fwlj;
	}

	public void setFwlj(String fwlj) {
		this.fwlj = fwlj;
	}
}
