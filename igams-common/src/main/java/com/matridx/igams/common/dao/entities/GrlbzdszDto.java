package com.matridx.igams.common.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="GrlbzdszDto")
public class GrlbzdszDto extends GrlbzdszModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> choseList;

	//组件
	private String zj;
	//加载
	private String jz;

	public List<String> getChoseList() {
		return choseList;
	}



	public void setChoseList(List<String> choseList) {
		this.choseList = choseList;
	}

	private List<GrlbzdszDto> choseListVue;
	private String choseListVueStr;


	public List<GrlbzdszDto> getChoseListVue() {
		return choseListVue;
	}

	public void setChoseListVue(List<GrlbzdszDto> choseListVue) {
		this.choseListVue = choseListVue;
	}

	public String getChoseListVueStr() {
		return choseListVueStr;
	}

	public void setChoseListVueStr(String choseListVueStr) {
		this.choseListVueStr = choseListVueStr;
	}

	public String getZj() {
		return zj;
	}

	public void setZj(String zj) {
		this.zj = zj;
	}

	public String getJz() {
		return jz;
	}

	public void setJz(String jz) {
		this.jz = jz;
	}
}
