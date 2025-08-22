package com.matridx.igams.sample.dao.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value = "YbglDto")
public class YbglDto extends YbglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8945791171098249355L;
	//入库类型
	private String rklx;
	//入库类型名称
	private String rklxmc;
	//外部编号
	private String wbbh;
	//检验结果
	private String jyjg;
	//来源地
	private String lyd;
	//采样时间
	private String cysj;
	//性别
	private String xb;
	//性别名称
	private String xbmc;
	//孕周
	private String yz;
	//体重
	private String tz;
	//姓名
	private String xm;
	//备注
	private String bz;
	//来源编号
	private String lybh;
	//来源状态
	private String lyzt;
	
	//冰箱号
	private String bxh;
	//抽屉号
	private String chth;
	//盒子号
	private String hzh;
	//存放数
	private String cfs;
	//起始位置 代码
	private String qswzdm;
	//结束位置 代码
	private String jswzdm;
	//标本类型名称
	private String yblxmc;
	//统计
	private String tj;
	//标本明细
	private List<YbglDto> mxs;

	//检索-标本类型
	private String[] yblxs;
	//检索-来源方
	private String[] lyfs;
	//标本ID复数
	private List<String> ybids;
	//盒子ids
	private String[] hzids;
	
	
	public String[] getHzids() {
		return hzids;
	}
	public void setHzids(String[] hzids) {
		this.hzids = hzids;
	}
	public List<String> getYbids() {
		return ybids;
	}
	public void setYbids(String ybids) {
		List<String> list = new ArrayList<String>();
		String str[] = ybids.split(",");
		list = Arrays.asList(str);
		this.ybids = list;
	}
	public void setYbids(List<String> ybids) {
		if(ybids!=null && ybids.size() > 0){
			for(int i=0;i<ybids.size();i++){
				ybids.set(i,ybids.get(i).replace("[", "").replace("]", "").trim());
			}
		}
		this.ybids = ybids;
	}
	public String getBxh() {
		return bxh;
	}
	public void setBxh(String bxh) {
		this.bxh = bxh;
	}
	public String getChth() {
		return chth;
	}
	public void setChth(String chth) {
		this.chth = chth;
	}
	public String getHzh() {
		return hzh;
	}
	public void setHzh(String hzh) {
		this.hzh = hzh;
	}
	public String getCfs() {
		return cfs;
	}
	public void setCfs(String cfs) {
		this.cfs = cfs;
	}
	public String getQswzdm() {
		return qswzdm;
	}
	public void setQswzdm(String qswzdm) {
		this.qswzdm = qswzdm;
	}
	public String getJswzdm() {
		return jswzdm;
	}
	public void setJswzdm(String jswzdm) {
		this.jswzdm = jswzdm;
	}
	public String getLybh() {
		return lybh;
	}
	public void setLybh(String lybh) {
		this.lybh = lybh;
	}
	public String getYblxmc() {
		return yblxmc;
	}
	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}
	public String getLyzt() {
		return lyzt;
	}
	public void setLyzt(String lyzt) {
		this.lyzt = lyzt;
	}
	public List<YbglDto> getMxs() {
		return mxs;
	}
	public void setMxs(List<YbglDto> mxs) {
		this.mxs = mxs;
	}
	public String[] getYblxs() {
		return yblxs;
	}
	public void setYblxs(String[] yblxs) {
		this.yblxs = yblxs;
		for(int i=0;i<this.yblxs.length;i++)
		{
			this.yblxs[i] = this.yblxs[i].replace("'", "");
		}
	}
	public String[] getLyfs() {
		return lyfs;
	}
	public void setLyfs(String[] lyfs) {
		this.lyfs = lyfs;
		for(int i=0;i< this.lyfs.length;i++)
		{
			this.lyfs[i] = this.lyfs[i].replace("'", "");
		}
	}
	public String getTj() {
		return tj;
	}
	public void setTj(String tj) {
		this.tj = tj;
	}
	public String getRklx() {
		return rklx;
	}
	public void setRklx(String rklx) {
		this.rklx = rklx;
	}
	public String getRklxmc() {
		return rklxmc;
	}
	public void setRklxmc(String rklxmc) {
		this.rklxmc = rklxmc;
	}
	public String getWbbh() {
		return wbbh;
	}
	public void setWbbh(String wbbh) {
		this.wbbh = wbbh;
	}
	public String getJyjg() {
		return jyjg;
	}
	public void setJyjg(String jyjg) {
		this.jyjg = jyjg;
	}
	public String getLyd() {
		return lyd;
	}
	public void setLyd(String lyd) {
		this.lyd = lyd;
	}
	public String getCysj() {
		return cysj;
	}
	public void setCysj(String cysj) {
		this.cysj = cysj;
	}
	public String getXb() {
		return xb;
	}
	public void setXb(String xb) {
		this.xb = xb;
	}
	public String getXbmc() {
		return xbmc;
	}
	public void setXbmc(String xbmc) {
		this.xbmc = xbmc;
	}
	public String getYz() {
		return yz;
	}
	public void setYz(String yz) {
		this.yz = yz;
	}
	public String getTz() {
		return tz;
	}
	public void setTz(String tz) {
		this.tz = tz;
	}
	public String getXm() {
		return xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	
}
