package com.matridx.igams.web.dao.entities;


import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="JsdwqxDto")
public class JsdwqxDto extends JsdwqxModel{
	//机构id[多]
	private List<String> jgids;
	
	public List<String> getJgids()
	{
		return jgids;
	}
	public void setJgids(List<String> jgids)
	{
		this.jgids = jgids;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//机构名称
	private String jgmc;
	//机构代码
	private String jgdm;
	//分布式标记
	protected String prefix;
	//机构标记
	private int jgflg;
	
	public int getJgflg() {
		return jgflg;
	}
	public void setJgflg(int jgflg) {
		this.jgflg = jgflg;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getJgmc() {
		return jgmc;
	}
	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}
	public String getJgdm() {
		return jgdm;
	}
	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}
	
	
}
