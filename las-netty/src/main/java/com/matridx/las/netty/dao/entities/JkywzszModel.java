package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias("JkywzszModel")
public class JkywzszModel extends BaseModel{
	 private static final long serialVersionUID = 1L;
	 //位置设置id
	 private String wzszid;
	 //建库仪位置编号
	 private String jkywzbh;
	 //建库仪通道号
	 private String jkytdh;
	 //建库仪deviceid
	private String deviceid;
	//建库仪开关门拍照位置
	private String jkykgmwzbh;
	private String bz;
	private String csdm;
	
	public String getJkykgmwzbh() {
		return jkykgmwzbh;
	}
	public void setJkykgmwzbh(String jkykgmwzbh) {
		this.jkykgmwzbh = jkykgmwzbh;
	}
	public String getWzszid() {
		return wzszid;
	}
	public void setWzszid(String wzszid) {
		this.wzszid = wzszid;
	}
	public String getJkywzbh() {
		return jkywzbh;
	}
	public void setJkywzbh(String jkywzbh) {
		this.jkywzbh = jkywzbh;
	}
	public String getJkytdh() {
		return jkytdh;
	}
	public void setJkytdh(String jkytdh) {
		this.jkytdh = jkytdh;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getCsdm() {
		return csdm;
	}

	public void setCsdm(String csdm) {
		this.csdm = csdm;
	}
}
