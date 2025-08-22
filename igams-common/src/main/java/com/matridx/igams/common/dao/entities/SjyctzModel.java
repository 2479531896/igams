package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="SjyctzModel")
public class SjyctzModel extends BaseModel{
	//异常ID
	private String ycid;
	//类型  
	private String lx;
	//人员ID
	private String ryid;
	//异常通知ID
	private String yctzid;

	public String getYctzid() {
		return yctzid;
	}

	public void setYctzid(String yctzid) {
		this.yctzid = yctzid;
	}

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
