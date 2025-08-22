package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="CkjgxxDto")
public class CkjgxxDto extends CkjgxxModel{

	//机构名称
	private String jgmc;
	//JSON
	private String ckjgxx_json;

	public String getCkjgxx_json() {
		return ckjgxx_json;
	}

	public void setCkjgxx_json(String ckjgxx_json) {
		this.ckjgxx_json = ckjgxx_json;
	}

	public String getJgmc() {
		return jgmc;
	}

	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
