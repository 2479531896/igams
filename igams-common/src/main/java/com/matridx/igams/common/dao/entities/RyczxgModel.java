package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="RyczxgModel")
public class RyczxgModel extends BaseModel{
	//用户ID
	private String yhid;
	//区分
	private String qf;
	//对象ID
	private String dxid;
	//操作时间
	private String czsj;
	//操作频率
	private String czpl;
	//用户ID
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid){
		this.yhid = yhid;
	}
	//区分
	public String getQf() {
		return qf;
	}
	public void setQf(String qf){
		this.qf = qf;
	}
	//对象ID
	public String getDxid() {
		return dxid;
	}
	public void setDxid(String dxid){
		this.dxid = dxid;
	}
	//操作时间
	public String getCzsj() {
		return czsj;
	}
	public void setCzsj(String czsj){
		this.czsj = czsj;
	}
	//操作频率
	public String getCzpl() {
		return czpl;
	}
	public void setCzpl(String czpl){
		this.czpl = czpl;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
