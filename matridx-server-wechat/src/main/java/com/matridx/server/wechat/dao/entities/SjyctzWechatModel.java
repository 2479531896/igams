package com.matridx.server.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="SjyctzWechatModel")
public class SjyctzWechatModel extends BaseModel{
	//异常ID
	private String ycid;
	//类型  
	private String lx;
	//人员ID
	private String ryid;
	//异常ID
	public String getYcid() {
		return ycid;
	}
	public void setYcid(String ycid){
		this.ycid = ycid;
	}
	//类型  
	public String getLx() {
		return lx;
	}
	public void setLx(String lx){
		this.lx = lx;
	}
	//人员ID
	public String getRyid() {
		return ryid;
	}
	public void setRyid(String ryid){
		this.ryid = ryid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
