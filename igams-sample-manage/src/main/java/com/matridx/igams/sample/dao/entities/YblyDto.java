package com.matridx.igams.sample.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="YblyDto")
public class YblyDto extends YblyModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 443675447516506276L;
	//标本类型代码
	private String yblxdm;
	//标本类型代码
	private String yblxmc;
	//排除的状态代码
	private List<String> notzts;
	//来源地名称
	private String lydmc;
	//来源地(筛选用)
	private String[] lyds;
	//采样开始时间(筛选用)
	private String cykssj;
	//采样结束时间(筛选用)
	private String cyjssj;
	//标本编号日期
	private String ybbhrq;
	//标本编号
	private String ybbh;
	//是否为企参盘
	private String sfqcp;
	
	public String getSfqcp() {
		return sfqcp;
	}

	public void setSfqcp(String sfqcp) {
		this.sfqcp = sfqcp;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getYbbhrq() {
		return ybbhrq;
	}

	public void setYbbhrq(String ybbhrq) {
		this.ybbhrq = ybbhrq;
	}

	public String getCykssj() {
		return cykssj;
	}

	public void setCykssj(String cykssj) {
		this.cykssj = cykssj;
	}

	public String getCyjssj() {
		return cyjssj;
	}

	public void setCyjssj(String cyjssj) {
		this.cyjssj = cyjssj;
	}

	public String[] getLyds() {
		return lyds;
	}

	public void setLyds(String[] lyds) {
		this.lyds = lyds;
		for(int i=0;i<this.lyds.length;i++)
		{
			this.lyds[i] = this.lyds[i].replace("'", "");
		}
	}

	public String getYblxdm() {
		return yblxdm;
	}

	public void setYblxdm(String yblxdm) {
		this.yblxdm = yblxdm;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public List<String> getNotzts() {
		return notzts;
	}

	public void setNotzts(List<String> notzts) {
		this.notzts = notzts;
	}

	public String getLydmc() {
		return lydmc;
	}

	public void setLydmc(String lydmc) {
		this.lydmc = lydmc;
	}
}
