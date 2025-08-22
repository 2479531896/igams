package com.matridx.igams.sample.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="YbsqDto")
public class YbsqDto extends YbsqModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//标本编号
	private String ybbh;
	//标本ID
	private String ybid;
	//标本类型
	private String yblx;
	//标本类型名称
	private String yblxmc;
	//标本来源类型
	private String yblylx;
	//库存数
	private String sl;
	//库存单位
	private String dw;
	//浓度
	private String nd;
	//预定数
	private String yds;
	//用途名称
	private String ytmc;
	//标本起始位置
	private String ybqswz;
	//标本结束位置
	private String ybjswz;
	//标本起始位置 代码
	private String ybqswzdm;
	//标本结束位置 代码
	private String ybjswzdm;
	//起始位置 代码
	private String qswzdm;
	//结束位置 代码
	private String jswzdm;
	//冰箱号
	private String bxh;
	//抽屉号
	private String chth;
	//盒子号
	private String hzh;
	//存放数
	private String cfs;
	//申请人姓名
	private String sqrxm;
	//检索-标本类型
	private String[] yblxs;
	//检索-用途
	private String[] yts;
	//申请开始日期
	private String sqrqstart;
	//申请结束日期
	private String sqrqend;
	//机构ID
	private String jgid;
	//机构名称
	private String jgmc;
	//父机构ID
	private String fjgid;
	
	public String getJgid() {
		return jgid;
	}
	public void setJgid(String jgid) {
		this.jgid = jgid;
	}
	public String getJgmc() {
		return jgmc;
	}
	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}
	public String getFjgid() {
		return fjgid;
	}
	public void setFjgid(String fjgid) {
		this.fjgid = fjgid;
	}
	public String getSqrqstart() {
		return sqrqstart;
	}
	public void setSqrqstart(String sqrqstart) {
		this.sqrqstart = sqrqstart;
	}
	public String getSqrqend() {
		return sqrqend;
	}
	public void setSqrqend(String sqrqend) {
		this.sqrqend = sqrqend;
	}
	public String getYbbh() {
		return ybbh;
	}
	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}
	public String getYbid() {
		return ybid;
	}
	public void setYbid(String ybid) {
		this.ybid = ybid;
	}
	public String getYblx() {
		return yblx;
	}
	public void setYblx(String yblx) {
		this.yblx = yblx;
	}
	public String getYblxmc() {
		return yblxmc;
	}
	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}
	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
	}
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	public String getYds() {
		return yds;
	}
	public void setYds(String yds) {
		this.yds = yds;
	}
	public String getYtmc() {
		return ytmc;
	}
	public void setYtmc(String ytmc) {
		this.ytmc = ytmc;
	}
	public String getYbqswz() {
		return ybqswz;
	}
	public void setYbqswz(String ybqswz) {
		this.ybqswz = ybqswz;
	}
	public String getYbjswz() {
		return ybjswz;
	}
	public void setYbjswz(String ybjswz) {
		this.ybjswz = ybjswz;
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
	public String getSqrxm() {
		return sqrxm;
	}
	public void setSqrxm(String sqrxm) {
		this.sqrxm = sqrxm;
	}
	public String getYbqswzdm() {
		return ybqswzdm;
	}
	public void setYbqswzdm(String ybqswzdm) {
		this.ybqswzdm = ybqswzdm;
	}
	public String getYbjswzdm() {
		return ybjswzdm;
	}
	public void setYbjswzdm(String ybjswzdm) {
		this.ybjswzdm = ybjswzdm;
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
	public String[] getYblxs() {
		return yblxs;
	}
	public void setYblxs(String[] yblxs) {
		this.yblxs = yblxs;
	}
	public String[] getYts() {
		return yts;
	}
	public void setYts(String[] yts) {
		this.yts = yts;
	}
	public String getNd() {
		return nd;
	}
	public void setNd(String nd) {
		this.nd = nd;
	}
	public String getYblylx() {
		return yblylx;
	}
	public void setYblylx(String yblylx) {
		this.yblylx = yblylx;
	}
}
