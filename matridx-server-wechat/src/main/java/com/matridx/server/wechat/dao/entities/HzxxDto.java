package com.matridx.server.wechat.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="HzxxDto")
public class HzxxDto extends HzxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//查询数
	private String count;
	//开始位置
	private String start;
	//录入时间格式化
	private String flrsj;
	//性别名称
	private String xbmc;
	//业务类型
	private String ywlx;
	//附件ID复数
	private List<String> fjids;
	//记录ID
	private String jlid;
	//方案ID
	private String faid;
	//体温名称
	private String twmc;
	//验证码
	private String yzm;
	//发送时间
	private String fssj;
	//检测项目名称
	private String jcxmmc;
	//检测项目id
	private String jcxmid;

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getJcxmid() {
		return jcxmid;
	}

	public void setJcxmid(String jcxmid) {
		this.jcxmid = jcxmid;
	}
	public String getYzm() {
		return yzm;
	}

	public void setYzm(String yzm) {
		this.yzm = yzm;
	}

	public String getFssj() {
		return fssj;
	}

	public void setFssj(String fssj) {
		this.fssj = fssj;
	}

	public String getTwmc() {
		return twmc;
	}
	public void setTwmc(String twmc) {
		this.twmc = twmc;
	}
	public String getFaid() {
		return faid;
	}
	public void setFaid(String faid) {
		this.faid = faid;
	}
	public String getJlid() {
		return jlid;
	}
	public void setJlid(String jlid) {
		this.jlid = jlid;
	}
	public List<String> getFjids() {
		return fjids;
	}
	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}
	public String getYwlx() {
		return ywlx;
	}
	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}
	public String getXbmc() {
		return xbmc;
	}
	public void setXbmc(String xbmc) {
		this.xbmc = xbmc;
	}
	public String getFlrsj() {
		return flrsj;
	}
	public void setFlrsj(String flrsj) {
		this.flrsj = flrsj;
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

}
