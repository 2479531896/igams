package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="CxyxxDto")
public class CxyxxDto extends CxyxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//id 测序仪主键id
	private String id;
	//name 测序仪名称
	private String name;
	//测序仪编号
	private String uid;
	//第二端index是否反向互补
	private String index2_reversed;
	//测序时长
	private String expect_seq_hour;
	//该测序仪属于哪个组织
	private String organization;
	//测序仪操作系统
	private String os;
	//model
	private String model;
	//检测单位
	private String jcdw;

	private String cxycskz3;

	private String wkid;

	public String getCxycskz3() {
		return cxycskz3;
	}

	public void setCxycskz3(String cxycskz3) {
		this.cxycskz3 = cxycskz3;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getIndex2_reversed() {
		return index2_reversed;
	}
	public void setIndex2_reversed(String index2_reversed) {
		this.index2_reversed = index2_reversed;
	}
	public String getExpect_seq_hour() {
		return expect_seq_hour;
	}
	public void setExpect_seq_hour(String expect_seq_hour) {
		this.expect_seq_hour = expect_seq_hour;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}

	public String getWkid() {
		return wkid;
	}

	public void setWkid(String wkid) {
		this.wkid = wkid;
	}
}
