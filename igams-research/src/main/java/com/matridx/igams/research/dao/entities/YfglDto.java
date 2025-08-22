package com.matridx.igams.research.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="YfglDto")
public class YfglDto extends YfglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String kssjstart;	//开始开始时间
	private String kssjend;		//开始结束时间
	private String sjwcsjstart;	//实际开始开始时间
	private String sjwcsjend;	//实际开始结束时间
	private String[] yflxs;	//检索用研发类型（多）
	private String[] dqjds;	//检索用当前进度（多）
	//附件ID复数
	private List<String> fjids;
	private String yflxmc;//研发类型名称
	private String yflxdm;//研发类型代码
	
	public String getKssjstart() {
		return kssjstart;
	}
	public void setKssjstart(String kssjstart) {
		this.kssjstart = kssjstart;
	}
	public String getKssjend() {
		return kssjend;
	}
	public void setKssjend(String kssjend) {
		this.kssjend = kssjend;
	}
	public String getSjwcsjstart() {
		return sjwcsjstart;
	}
	public void setSjwcsjstart(String sjwcsjstart) {
		this.sjwcsjstart = sjwcsjstart;
	}
	public String getSjwcsjend() {
		return sjwcsjend;
	}
	public void setSjwcsjend(String sjwcsjend) {
		this.sjwcsjend = sjwcsjend;
	}
	public String[] getYflxs() {
		return yflxs;
	}
	public void setYflxs(String[] yflxs) {
		this.yflxs = yflxs;
		for(int i=0;i<this.yflxs.length;i++)
		{
			this.yflxs[i] = this.yflxs[i].replace("'", "");
		}
	}
	public String[] getDqjds() {
		return dqjds;
	}
	public void setDqjds(String[] dqjds) {
		this.dqjds = dqjds;
		for(int i=0;i<this.dqjds.length;i++)
		{
			this.dqjds[i] = this.dqjds[i].replace("'", "");
		}
	}
	public List<String> getFjids() {
		return fjids;
	}
	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}
	public String getYflxmc() {
		return yflxmc;
	}
	public void setYflxmc(String yflxmc) {
		this.yflxmc = yflxmc;
	}
	public String getYflxdm() {
		return yflxdm;
	}
	public void setYflxdm(String yflxdm) {
		this.yflxdm = yflxdm;
	}
	
}
