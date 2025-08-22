package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;


@Alias(value="HtfkmxModel")
public class HtfkmxModel extends BaseBasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//合同付款明细ID
	private String htfkmxid;

	//合同付款ID
	private String htfkid;

	//合同ID
	private String htid;

	//付款金额
	private String fkje;

	//付款百分比
	private String fkbfb;

	//付款日期
	private String fkrq;

	public String getHtfkmxid() {
		return htfkmxid;
	}

	public void setHtfkmxid(String htfkmxid) {
		this.htfkmxid = htfkmxid;
	}

	public String getHtfkid() {
		return htfkid;
	}

	public void setHtfkid(String htfkid) {
		this.htfkid = htfkid;
	}

	public String getHtid() {
		return htid;
	}

	public void setHtid(String htid) {
		this.htid = htid;
	}

	public String getFkje() {
		return fkje;
	}

	public void setFkje(String fkje) {
		this.fkje = fkje;
	}

	public String getFkbfb() {
		return fkbfb;
	}

	public void setFkbfb(String fkbfb) {
		this.fkbfb = fkbfb;
	}

	public String getFkrq() {
		return fkrq;
	}

	public void setFkrq(String fkrq) {
		this.fkrq = fkrq;
	}
}
