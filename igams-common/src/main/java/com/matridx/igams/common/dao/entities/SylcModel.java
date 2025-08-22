package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="SylcModel")
public class SylcModel extends BaseModel{
	//流程ID
	private String lcid;
	//流程名称
	private String lcmc;
	//序号
	private String xh;
	//类型
	private String lx;
	//流程标记
	private String lcbj;

	public String getLcid() {
		return lcid;
	}

	public void setLcid(String lcid) {
		this.lcid = lcid;
	}

	public String getLcmc() {
		return lcmc;
	}

	public void setLcmc(String lcmc) {
		this.lcmc = lcmc;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getLcbj() {
		return lcbj;
	}

	public void setLcbj(String lcbj) {
		this.lcbj = lcbj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
