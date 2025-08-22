package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YhcqtzModel")
public class YhcqtzModel extends BaseModel{
	//用户id
	private String yhid;
	//上级ID
	private String sjid;
	//出勤时间
	private String cqsj;
	//退勤时间
	private String tqsj;
	//备注  remark
	private String bz;
	//用户id
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid){
		this.yhid = yhid;
	}
	//上级ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//出勤时间
	public String getCqsj() {
		return cqsj;
	}
	public void setCqsj(String cqsj){
		this.cqsj = cqsj;
	}
	//退勤时间
	public String getTqsj() {
		return tqsj;
	}
	public void setTqsj(String tqsj){
		this.tqsj = tqsj;
	}
	//备注  remark
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
