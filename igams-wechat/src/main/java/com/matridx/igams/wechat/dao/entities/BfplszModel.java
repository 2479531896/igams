package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="BfplszModel")
public class BfplszModel extends BaseModel{
	////频率设置ID
	private String plszid;
	//联系人ID
	private String lxrid;
	//单位ID
	private String dwid;
	//用户ID
	private String yhid;
	//拜访频次
	private String bfpc;
	//频次类型
	private String pclx;
	//频次周期
	private String pczq;
	//拜访标记
	private String bfbj;
	//起始日期
	private String qsrq;

	public String getPlszid() {
		return plszid;
	}

	public void setPlszid(String plszid) {
		this.plszid = plszid;
	}

	public String getLxrid() {
		return lxrid;
	}

	public void setLxrid(String lxrid) {
		this.lxrid = lxrid;
	}

	public String getDwid() {
		return dwid;
	}

	public void setDwid(String dwid) {
		this.dwid = dwid;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getBfpc() {
		return bfpc;
	}

	public void setBfpc(String bfpc) {
		this.bfpc = bfpc;
	}

	public String getPclx() {
		return pclx;
	}

	public void setPclx(String pclx) {
		this.pclx = pclx;
	}

	public String getPczq() {
		return pczq;
	}

	public void setPczq(String pczq) {
		this.pczq = pczq;
	}

	public String getBfbj() {
		return bfbj;
	}

	public void setBfbj(String bfbj) {
		this.bfbj = bfbj;
	}

	public String getQsrq() {
		return qsrq;
	}

	public void setQsrq(String qsrq) {
		this.qsrq = qsrq;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
