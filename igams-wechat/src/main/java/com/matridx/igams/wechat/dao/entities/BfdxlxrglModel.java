package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="BfdxlxrglModel")
public class BfdxlxrglModel extends BaseModel{
	//联系人ID
	private String lxrid;
	//客户ID
	private String dwid;
	//联系人姓名
	private String lxrxm;
	//电话
	private String dh;
	//部门科室
	private String bmks;
	//职位
	private String zw;
	//备注
	private String bz;
	//其他联系方式
	private String qtlxfs;

	public String getQtlxfs() {
		return qtlxfs;
	}

	public void setQtlxfs(String qtlxfs) {
		this.qtlxfs = qtlxfs;
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

	public String getLxrxm() {
		return lxrxm;
	}

	public void setLxrxm(String lxrxm) {
		this.lxrxm = lxrxm;
	}

	public String getDh() {
		return dh;
	}

	public void setDh(String dh) {
		this.dh = dh;
	}

	public String getBmks() {
		return bmks;
	}

	public void setBmks(String bmks) {
		this.bmks = bmks;
	}

	public String getZw() {
		return zw;
	}

	public void setZw(String zw) {
		this.zw = zw;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
