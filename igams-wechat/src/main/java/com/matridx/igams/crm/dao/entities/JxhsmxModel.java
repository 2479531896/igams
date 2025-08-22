package com.matridx.igams.crm.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JxhsmxModel")
public class JxhsmxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//绩效核算明细id
	private String jxhsmxid;
	//绩效核算管理id
	private String jxhsglid;
	//应收账款id
	private String yszkid;
	//绩效核算金额
	private String jxhsje;
	//备注
	private String bz;

	public String getJxhsmxid() {
		return jxhsmxid;
	}

	public void setJxhsmxid(String jxhsmxid) {
		this.jxhsmxid = jxhsmxid;
	}

	public String getJxhsglid() {
		return jxhsglid;
	}

	public void setJxhsglid(String jxhsglid) {
		this.jxhsglid = jxhsglid;
	}

	public String getYszkid() {
		return yszkid;
	}

	public void setYszkid(String yszkid) {
		this.yszkid = yszkid;
	}

	public String getJxhsje() {
		return jxhsje;
	}

	public void setJxhsje(String jxhsje) {
		this.jxhsje = jxhsje;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
}
