package com.matridx.las.frame.connect.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="ZyxxLasModel")
public class ZyxxLasModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//资源ID
	private String zyid;

	//资源名称
	private String zymc;

	//资源类型 print
	private String zylx;

	//资源地址 ip地址
	private String zydz;

	//调用DLL 的名称
	private String dydll;

	//回调类 打算用反射 包含包和类
	private String hdl;

	//状态确认地址
	private String ztqrdz;
	//设备ID
	private String deviceid;
	//协议类型
	private String xylx;
	//参数
	private String cs;

	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getXylx() {
		return xylx;
	}

	public void setXylx(String xylx) {
		this.xylx = xylx;
	}

	public String getZyid() {
		return zyid;
	}

	public void setZyid(String zyid) {
		this.zyid = zyid;
	}

	public String getZymc() {
		return zymc;
	}

	public void setZymc(String zymc) {
		this.zymc = zymc;
	}

	public String getZylx() {
		return zylx;
	}

	public void setZylx(String zylx) {
		this.zylx = zylx;
	}

	public String getZydz() {
		return zydz;
	}

	public void setZydz(String zydz) {
		this.zydz = zydz;
	}

	public String getDydll() {
		return dydll;
	}

	public void setDydll(String dydll) {
		this.dydll = dydll;
	}

	public String getHdl() {
		return hdl;
	}

	public void setHdl(String hdl) {
		this.hdl = hdl;
	}

	public String getZtqrdz() {
		return ztqrdz;
	}

	public void setZtqrdz(String ztqrdz) {
		this.ztqrdz = ztqrdz;
	}
}
