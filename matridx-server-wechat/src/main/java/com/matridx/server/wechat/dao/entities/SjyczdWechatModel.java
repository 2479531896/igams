package com.matridx.server.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="SjyczdWechatModel")
public class SjyczdWechatModel extends BaseModel{
	//异常ID
	private String ycid;
	//置顶时间
	private String zdsj;
	//置顶人员
	private String zdry;
	
	//置顶人员
	public String getZdry() {
		return zdry;
	}
	public void setZdry(String zdry) {
		this.zdry = zdry;
	}
	
	//异常ID
	public String getYcid() {
		return ycid;
	}
	
	public void setYcid(String ycid){
		this.ycid = ycid;
	}
	//置顶时间
	public String getZdsj() {
		return zdsj;
	}
	public void setZdsj(String zdsj){
		this.zdsj = zdsj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
