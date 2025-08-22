package com.matridx.igams.common.dao.entities;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="DepartmentDto")
public class DepartmentDto extends BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//机构id
	private String jgid;
	//机构名称
	private String jgmc;
	//父机构id
	private String fjgid;
	//机构代码
	private String jgdm;
	//用户id
	private String yhid;
	//序号
	private String xh;
	//分布式标识
	private String prefix;
	//访问标记
	private String fwbj;
	//全部查询内容
	private String entire;
	//列表加载的条数
	private String count;
	//从第几条开始
	private String start;
	//扩展参数1
	private String kzcs1;
	//扩展参数1
	private String wbcxid;
	//外部程序名称
	private String wbcxmc;
	//部门主管s
	private String bmzgs;
	//分布式标识
	private String fbsbj;

	public String getFbsbj() {
		return fbsbj;
	}

	public void setFbsbj(String fbsbj) {
		this.fbsbj = fbsbj;
	}

	public String getBmzgs() {
		return bmzgs;
	}

	public void setBmzgs(String bmzgs) {
		this.bmzgs = bmzgs;
	}

	public String getWbcxmc() {
		return wbcxmc;
	}

	public void setWbcxmc(String wbcxmc) {
		this.wbcxmc = wbcxmc;
	}

	public String getWbcxid() {
		return wbcxid;
	}

	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}

	public String getKzcs1() {
		return kzcs1;
	}

	public void setKzcs1(String kzcs1) {
		this.kzcs1 = kzcs1;
	}

	public String getEntire() {
		return entire;
	}
	public void setEntire(String entire) {
		this.entire = entire;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getJgdm() {
		return jgdm;
	}
	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}
	public String getFwbj() {
		return fwbj;
	}
	public void setFwbj(String fwbj) {
		this.fwbj = fwbj;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getJgid() {
		return jgid;
	}
	public void setJgid(String jgid){
		this.jgid = jgid;
	}
	public String getJgmc() {
		return jgmc;
	}
	public void setJgmc(String jgmc){
		this.jgmc = jgmc;
	}
	public String getFjgid() {
		return fjgid;
	}
	public void setFjgid(String fjgid){
		this.fjgid = fjgid;
	}
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid) {
		this.yhid = yhid;
	}
	
}
