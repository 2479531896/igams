package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjyctzDto")
public class SjyctzDto extends SjyctzModel{

	private String ddid;
	private String zsxm;
	private String ddtxlj;

	private String yhm;

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getDdtxlj() {
		return ddtxlj;
	}

	public void setDdtxlj(String ddtxlj) {
		this.ddtxlj = ddtxlj;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//id
	private String id;
	//名称
	private String mc;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMc() {
		return mc;
	}
	public void setMc(String mc) {
		this.mc = mc;
	}
	
}
