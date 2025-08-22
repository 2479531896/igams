package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SysjglDto")
public class SysjglDto extends SysjglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//简化显示
	private String jhxs;
	//检测单位名称
	private String jcdwmc;
	//开始日期
	private String startRq;
	//结束日期
	private String endRq;
	//送检ID
	private String sjid;
	//编码
	private String bm;
	//文库类型
	private String wklx;
	//内部编码
	private String nbbh;
	//业务名称，也就是文库名称或者提取名称
	private String ywmc;

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getBm() {
		return bm;
	}

	public void setBm(String bm) {
		this.bm = bm;
	}

	public String getWklx() {
		return wklx;
	}

	public void setWklx(String wklx) {
		this.wklx = wklx;
	}

	public String getNbbh() {
		return nbbh;
	}

	public void setNbbh(String nbbh) {
		this.nbbh = nbbh;
	}

	public String getStartRq() {
		return startRq;
	}

	public void setStartRq(String startRq) {
		this.startRq = startRq;
	}

	public String getEndRq() {
		return endRq;
	}

	public void setEndRq(String endRq) {
		this.endRq = endRq;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String getJhxs() {
		return jhxs;
	}

	public void setJhxs(String jhxs) {
		this.jhxs = jhxs;
	}

    public String getYwmc() {
        return ywmc;
    }

    public void setYwmc(String ywmc) {
        this.ywmc = ywmc;
    }
}
