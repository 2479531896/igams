package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="TqmxModel")
public class TqmxModel extends BaseModel{
	//提取ID
	private String tqid;
	private String tqmxid;
	//序号
	private String xh;
	//内部编号
	private String nbbh;
	//送检ID
	private String sjid;
	//核算浓度
	private String hsnd;
	//cDNA 浓度
	private String cdna;
	//建库用量
	private String jkyl;
	//缓冲液用量
	private String hcyyl;
	//稀释浓度
	private String xsnd;
	//检测单位
	private String jcdw;
	//提取日期
	private String tqrq;
	//提取代码
	private String tqdm;
	//后缀标记
	private String hzbj;
	//检测项目代码.D,R
	private String jcxmdm;
	//提取仪孔位
	private String tqykw;
	//提取仪编码
	private String tqybm;
	private String spike;
	//稀释倍数
	private String xsbs;

	public String getXsbs() {
		return xsbs;
	}

	public void setXsbs(String xsbs) {
		this.xsbs = xsbs;
	}

	public String getTqmxid() {
		return tqmxid;
	}

	public void setTqmxid(String tqmxid) {
		this.tqmxid = tqmxid;
	}

	public String getSpike() {
		return spike;
	}

	public void setSpike(String spike) {
		this.spike = spike;
	}

	public String getTqykw() {
		return tqykw;
	}

	public void setTqykw(String tqykw) {
		this.tqykw = tqykw;
	}

	public String getTqybm() {
		return tqybm;
	}

	public void setTqybm(String tqybm) {
		this.tqybm = tqybm;
	}

	public String getTqdm() {
		return tqdm;
	}

	public void setTqdm(String tqdm) {
		this.tqdm = tqdm;
	}

	public String getJcxmdm() {
		return jcxmdm;
	}

	public void setJcxmdm(String jcxmdm) {
		this.jcxmdm = jcxmdm;
	}

	public String getHzbj() {
		return hzbj;
	}

	public void setHzbj(String hzbj) {
		this.hzbj = hzbj;
	}

	public String getTqrq() {
		return tqrq;
	}
	public void setTqrq(String tqrq) {
		this.tqrq = tqrq;
	}
	public String getJcdw() {
		return jcdw;
	}
	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}
	//提取ID
	public String getTqid() {
		return tqid;
	}
	public void setTqid(String tqid){
		this.tqid = tqid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//内部编号
	public String getNbbh() {
		return nbbh;
	}
	public void setNbbh(String nbbh){
		this.nbbh = nbbh;
	}
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//核算浓度
	public String getHsnd() {
		return hsnd;
	}
	public void setHsnd(String hsnd){
		this.hsnd = hsnd;
	}
	//cDNA 浓度
	public String getCdna() {
		return cdna;
	}
	public void setCdna(String cdna){
		this.cdna = cdna;
	}
	//建库用量
	public String getJkyl() {
		return jkyl;
	}
	public void setJkyl(String jkyl){
		this.jkyl = jkyl;
	}
	//缓冲液用量
	public String getHcyyl() {
		return hcyyl;
	}
	public void setHcyyl(String hcyyl){
		this.hcyyl = hcyyl;
	}
	//稀释浓度
	public String getXsnd() {
		return xsnd;
	}
	public void setXsnd(String xsnd) {
		this.xsnd = xsnd;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
