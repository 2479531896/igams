package com.matridx.server.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="FzjcjgDto")
public class FzjcjgDto extends FzjcjgModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//结果list
	private List<FzjcjgDto> jglist;
	//检测结果名称
	private String jcjgmc;
	//检测结果名称s
	private List<FzjcxxDto> fzjcxxmcs;

	public List<FzjcxxDto> getFzjcxxmcs() {
		return fzjcxxmcs;
	}

	public void setFzjcxxmcs(List<FzjcxxDto> fzjcxxmcs) {
		this.fzjcxxmcs = fzjcxxmcs;
	}

	public String getJcjgmc() {
		return jcjgmc;
	}

	public void setJcjgmc(String jcjgmc) {
		this.jcjgmc = jcjgmc;
	}

	public List<FzjcjgDto> getJglist() {
		return jglist;
	}

	public void setJglist(List<FzjcjgDto> jglist) {
		this.jglist = jglist;
	}
}
