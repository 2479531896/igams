package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SwskglModel")
public class SwskglModel extends BaseModel{

	//商务收款管理ID
	private String swskglid;
	//应收账款ID
	private String yszkid;
	//收款金额
	private String skje;
	//收款日期
	private String skrq;
	//状态
	private String zt;
	//备注
	private String bz;

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getSwskglid() {
		return swskglid;
	}

	public void setSwskglid(String swskglid) {
		this.swskglid = swskglid;
	}

	public String getYszkid() {
		return yszkid;
	}

	public void setYszkid(String yszkid) {
		this.yszkid = yszkid;
	}

	public String getSkje() {
		return skje;
	}

	public void setSkje(String skje) {
		this.skje = skje;
	}

	public String getSkrq() {
		return skrq;
	}

	public void setSkrq(String skrq) {
		this.skrq = skrq;
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
