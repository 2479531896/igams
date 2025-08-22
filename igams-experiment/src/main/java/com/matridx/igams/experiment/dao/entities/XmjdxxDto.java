package com.matridx.igams.experiment.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="XmjdxxDto")
public class XmjdxxDto extends XmjdxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//阶段任务
	private List<XmjdrwDto> xmjdrwDtos;
	//用户ID
	private String yhid;
	//任务状态
	private String zt;
	//查询条件(模糊)
	private String entire;
	//格式化实际开始时间
	private String fsjksrq;
	//格式化实际结束时间
	private String fsjjsrq;
	//期限
	private String qx;
	//负责人名称
	private String fzrmc;
	private String yhm;
	//我创建的任务的标志
	private String MySign;

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getMySign() {
		return MySign;
	}

	public void setMySign(String mySign) {
		MySign = mySign;
	}

	public String getFzrmc()
	{
		return fzrmc;
	}

	public void setFzrmc(String fzrmc)
	{
		this.fzrmc = fzrmc;
	}

	public List<XmjdrwDto> getXmjdrwDtos() {
		return xmjdrwDtos;
	}

	public void setXmjdrwDtos(List<XmjdrwDto> xmjdrwDtos) {
		this.xmjdrwDtos = xmjdrwDtos;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getFsjksrq() {
		return fsjksrq;
	}

	public void setFsjksrq(String fsjksrq) {
		this.fsjksrq = fsjksrq;
	}

	public String getFsjjsrq() {
		return fsjjsrq;
	}

	public void setFsjjsrq(String fsjjsrq) {
		this.fsjjsrq = fsjjsrq;
	}

	public String getQx() {
		return qx;
	}

	public void setQx(String qx) {
		this.qx = qx;
	}

}
