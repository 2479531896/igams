package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="SjycfkModel")
public class SjycfkModel extends BaseModel{
	//反馈ID
	private String fkid;
	//异常ID
	private String ycid;
	//反馈人
	private String qrr;
	//反馈信息
	private String fkxx;
	//父反馈ID
	private String ffkid;
	//根ID
	private String gid;
	//反馈区分
	private String fkqf;

	public String getFkqf() {
		return fkqf;
	}

	public void setFkqf(String fkqf) {
		this.fkqf = fkqf;
	}

	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	//父反馈ID
	public String getFfkid() {
		return ffkid;
	}
	public void setFfkid(String ffkid) {
		this.ffkid = ffkid;
	}
	//反馈ID
	public String getFkid() {
		return fkid;
	}
	public void setFkid(String fkid){
		this.fkid = fkid;
	}
	//异常ID
	public String getYcid() {
		return ycid;
	}
	public void setYcid(String ycid){
		this.ycid = ycid;
	}
	//反馈人
	public String getQrr() {
		return qrr;
	}
	public void setQrr(String qrr){
		this.qrr = qrr;
	}
	//反馈信息
	public String getFkxx() {
		return fkxx;
	}
	public void setFkxx(String fkxx){
		this.fkxx = fkxx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
