package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XzqgqrmxDto")
public class XzqgqrmxDto extends XzqgqrmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//请购单号
	private String qgdh;
	//未付款数量
	private String wfsl;
	//已付款数量
	private String yfsl;
	//是否入库（判断该货物是否需要入库）
	private String sfrk;

	public String getSfrk() {
		return sfrk;
	}

	public void setSfrk(String sfrk) {
		this.sfrk = sfrk;
	}

	public String getWfsl() {
		return wfsl;
	}

	public void setWfsl(String wfsl) {
		this.wfsl = wfsl;
	}

	public String getYfsl() {
		return yfsl;
	}

	public void setYfsl(String yfsl) {
		this.yfsl = yfsl;
	}

	public String getQgdh() {
		return qgdh;
	}

	public void setQgdh(String qgdh) {
		this.qgdh = qgdh;
	}
}
