package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="TyszModel")
public class TyszModel extends BaseModel{
	//通用设置ID
	private String tyszid;
	//资源ID
	private String zyid;
	//账套ID
	private String ztid;
	//一级资源ID
	private String yjzyid;
	//二级资源ID
	private String ejzyid;

	public String getTyszid() {
		return tyszid;
	}

	public void setTyszid(String tyszid) {
		this.tyszid = tyszid;
	}

	public String getZyid() {
		return zyid;
	}

	public void setZyid(String zyid) {
		this.zyid = zyid;
	}

	public String getZtid() {
		return ztid;
	}

	public void setZtid(String ztid) {
		this.ztid = ztid;
	}

	public String getYjzyid() {
		return yjzyid;
	}

	public void setYjzyid(String yjzyid) {
		this.yjzyid = yjzyid;
	}

	public String getEjzyid() {
		return ejzyid;
	}

	public void setEjzyid(String ejzyid) {
		this.ejzyid = ejzyid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
