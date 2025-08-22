package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="QcxmModel")
public class QcxmModel extends BaseModel{

	//清场项目ID
	private String qcxmid;
	//清场记录ID
	private String qcjlid;
	//项目ID
	private String xmid;
	//结果
	private String jg;
	//状态
	private String zt;

	public String getQcxmid() {
		return qcxmid;
	}

	public void setQcxmid(String qcxmid) {
		this.qcxmid = qcxmid;
	}

	public String getQcjlid() {
		return qcjlid;
	}

	public void setQcjlid(String qcjlid) {
		this.qcjlid = qcjlid;
	}

	public String getXmid() {
		return xmid;
	}

	public void setXmid(String xmid) {
		this.xmid = xmid;
	}

	public String getJg() {
		return jg;
	}

	public void setJg(String jg) {
		this.jg = jg;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
