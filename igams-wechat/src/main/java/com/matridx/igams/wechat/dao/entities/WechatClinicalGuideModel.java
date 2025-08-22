package com.matridx.igams.wechat.dao.entities;

public class WechatClinicalGuideModel {
	
	//指南ID
	private String id;
	//标本类型
	private String sample_type;
	//起始年龄
	private String age_start;
	//结束年龄
	private String age_end;
	//性别
	private String sex;
	//关联物种
	private String species;
	//引用格式
	private String detail;
	//更新时间
	private String last_update;
	//序号
	private String xh;

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getLast_update() {
		return last_update;
	}
	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}
	public String getSample_type() {
		return sample_type;
	}
	public void setSample_type(String sample_type) {
		this.sample_type = sample_type;
	}
	public String getAge_start() {
		return age_start;
	}
	public void setAge_start(String age_start) {
		this.age_start = age_start;
	}
	public String getAge_end() {
		return age_end;
	}
	public void setAge_end(String age_end) {
		this.age_end = age_end;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
}
