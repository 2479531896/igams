package com.matridx.igams.experiment.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="XmglDto")
public class XmglDto extends XmglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//用户ID
	private String yhid;
	
	//系统用户真实姓名
	private String zsxm;
	
	//系统用户用户名
	private String yhm;

	//开始时间
	private String starttime;
	//开始时间
	private String endtime;
	
	//项目成员ids
	private String yhids;

	//查看任务标记
	private String myflg;
	
	//xmjdxxlist以及带有当前登录用户id的dto
	private List<XmjdxxDto> xmjdxxDtos;
	
	//查询条件(模糊)
	private String entire;
	//负责人(模糊)
	private String fzrmc;
	//任务名称(模糊)
	private String rwmc;
	
	//项目公开性(复数)
	private List<String> xmgkxs;
	//自己创建项目的标志
	private String MySign;

	public String getMySign() {
		return MySign;
	}

	public void setMySign(String mySign) {
		MySign = mySign;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getRwmc() {
		return rwmc;
	}

	public void setRwmc(String rwmc) {
		this.rwmc = rwmc;
	}

	public String getFzrmc() {
		return fzrmc;
	}

	public void setFzrmc(String fzrmc) {
		this.fzrmc = fzrmc;
	}

	public List<String> getXmgkxs() {
		return xmgkxs;
	}

	public void setXmgkxs(List<String> xmgkxs) {
		this.xmgkxs = xmgkxs;
	}

	public List<XmjdxxDto> getXmjdxxDtos() {
		return xmjdxxDtos;
	}

	public void setXmjdxxDtos(List<XmjdxxDto> xmjdxxDtos) {
		this.xmjdxxDtos = xmjdxxDtos;
	}

	public String getYhids() {
		return yhids;
	}

	public void setYhids(String yhids) {
		this.yhids = yhids;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getMyflg() {
		return myflg;
	}

	public void setMyflg(String myflg) {
		this.myflg = myflg;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}
	
}
