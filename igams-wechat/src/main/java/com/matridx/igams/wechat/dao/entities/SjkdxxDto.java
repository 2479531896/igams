package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="SjkdxxDto")
public class SjkdxxDto extends SjkdxxModel{
	//快递类型代码
	private String kdlxdm;
	//业务IDs
	private List<String> ywids;

	public List<String> getYwids() {
		return ywids;
	}

	public void setYwids(List<String> ywids) {
		this.ywids = ywids;
	}

	public String getKdlxdm() {
		return kdlxdm;
	}

	public void setKdlxdm(String kdlxdm) {
		this.kdlxdm = kdlxdm;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
