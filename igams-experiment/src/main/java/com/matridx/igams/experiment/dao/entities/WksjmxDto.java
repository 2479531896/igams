package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WksjmxDto")
public class WksjmxDto extends WksjmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//文库ID
	private String wkid;
	//文库浓度
	private String wknd;
	//项目代码
	private String xmdm;
	//芯片名
	private String xpm;
	private String wkcxid;
	//复测加测代码
	private String fcjcdm;
	//内部编码
	private String nbbm;
	//内部编码带后缀
	private String nbbh;
	//文库明细序号
	private String wkmxxh;
	private String spike;
	//上传生信用
	private String project_type;

	private String ndpm;

	private String xs;

	public String getXs() {
		return xs;
	}

	public void setXs(String xs) {
		this.xs = xs;
	}

	public String getNdpm() {
		return ndpm;
	}

	public void setNdpm(String ndpm) {
		this.ndpm = ndpm;
	}

	public String getProject_type() {
		return project_type;
	}

	public void setProject_type(String project_type) {
		this.project_type = project_type;
	}

	public String getSpike() {
		return spike;
	}

	public void setSpike(String spike) {
		this.spike = spike;
	}

	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getXmdm() {
		return xmdm;
	}

	public void setXmdm(String xmdm) {
		this.xmdm = xmdm;
	}

	public String getFcjcdm() {
		return fcjcdm;
	}

	public void setFcjcdm(String fcjcdm) {
		this.fcjcdm = fcjcdm;
	}

	public String getWkcxid() {
		return wkcxid;
	}

	public void setWkcxid(String wkcxid) {
		this.wkcxid = wkcxid;
	}

	public String getXpm() {
		return xpm;
	}

	public void setXpm(String xpm) {
		this.xpm = xpm;
	}

	public String getWknd() {
		return wknd;
	}

	public void setWknd(String wknd) {
		this.wknd = wknd;
	}

	public String getWkid() {
		return wkid;
	}

	public void setWkid(String wkid) {
		this.wkid = wkid;
	}

	public String getWkmxxh() {
		return wkmxxh;
	}

	public void setWkmxxh(String wkmxxh) {
		this.wkmxxh = wkmxxh;
	}

    public String getNbbh() {
        return nbbh;
    }

    public void setNbbh(String nbbh) {
        this.nbbh = nbbh;
    }
}
