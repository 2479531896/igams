package com.matridx.igams.web.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="YhjgqxDto")
public class YhjgqxDto extends YhjgqxModel{
	//机构id集合
	private List<String> jgids;
	//机构名称
	private String jgmc;
	//分布式标记
	private String prefix;
	
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getJgmc(){
		return jgmc;
	}
	public void setJgmc(String jgmc){
		this.jgmc = jgmc;
	}
	public List<String> getJgids(){
		return jgids;
	}
	public void setJgids(List<String> jgids){
		this.jgids = jgids;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
