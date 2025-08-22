package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias("BommxModel")
public class BommxModel extends BaseModel {
	private String bommxid;	//bom明细
	private String bomid;	//bomid
	private String zjwlid;	//子件物料id
	private String bzyl;	//标准用量

	public String getBommxid() {
		return bommxid;
	}

	public void setBommxid(String bommxid) {
		this.bommxid = bommxid;
	}

	public String getBomid() {
		return bomid;
	}

	public void setBomid(String bomid) {
		this.bomid = bomid;
	}

	public String getZjwlid() {
		return zjwlid;
	}

	public void setZjwlid(String zjwlid) {
		this.zjwlid = zjwlid;
	}

	public String getBzyl() {
		return bzyl;
	}

	public void setBzyl(String bzyl) {
		this.bzyl = bzyl;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
