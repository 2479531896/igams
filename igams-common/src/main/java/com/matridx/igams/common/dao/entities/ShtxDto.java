package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="ShtxDto")
public class ShtxDto extends ShtxModel{

    //提醒类别名称 采用审批类别的数据库(等于审核类别名称)
	private String txlbmc;
	//业务ID
	private String ywid;
	//审核ID
	private String shid;
	//提交时间(审核过程的申请时间)
	private String tjrq;
  	//延期日数(系统时间-审核过程的修改时间)
	private String yqrs;
	//审核信息
	private ShxxDto shxx;
	//业务名称
	private String ywmc;
	//当前审批岗位名称
	private String gwmc;
	//上次审核时间（审核过程的修改时间）
	private String scshsj;
	//临时序号
	private Integer lsxh;
	//当前审批负责人ID
    private String dqshyh;
    //当前审批人角色
    private String dqshjs;
    //审核用户id
    private String yhid;
    //审核用户钉钉ID
    private String ddid;
    //待审核任务列表个数
    private String tasknum;
    //审核用户名称
    private String yhmc;
	private String yhm;
    //延期小时数
    private String yqxs;
    //高级筛选提醒类别名称
    private String[] txlbs;
    //高级筛选当前筛选类别
    private String dqsxlb;
    //判断是否有未完成的任务
	private String sfywwcdrw;
	//审核总数
	private String zs;
	private String string_agg;
	//岗位id
	private String gwid;
	//审核类别
	private String shlb;

	public String getShlb() {
		return shlb;
	}

	public void setShlb(String shlb) {
		this.shlb = shlb;
	}

	public String getGwid() {
		return gwid;
	}

	public void setGwid(String gwid) {
		this.gwid = gwid;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getString_agg() {
		return string_agg;
	}

	public void setString_agg(String string_agg) {
		this.string_agg = string_agg;
	}

	public String getZs() {
		return zs;
	}

	public void setZs(String zs) {
		this.zs = zs;
	}

	public String getSfywwcdrw() {
		return sfywwcdrw;
	}

	public void setSfywwcdrw(String sfywwcdrw) {
		this.sfywwcdrw = sfywwcdrw;
	}

	public String getDqsxlb() {
		return dqsxlb;
	}

	public void setDqsxlb(String dqsxlb) {
		this.dqsxlb = dqsxlb;
	}

	public String[] getTxlbs() {
		return txlbs;
	}

	public void setTxlbs(String[] txlbs) {
		this.txlbs = txlbs;
		for (int i = 0; i < txlbs.length; i++) {
			this.txlbs[i] = this.txlbs[i].replace("'", "");
		}
	}

	public String getYqxs() {
		return yqxs;
	}

	public void setYqxs(String yqxs) {
		this.yqxs = yqxs;
	}

	public String getYhmc() {
		return yhmc;
	}

	public void setYhmc(String yhmc) {
		this.yhmc = yhmc;
	}

	public String getTasknum() {
		return tasknum;
	}

	public void setTasknum(String tasknum) {
		this.tasknum = tasknum;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public Integer getLsxh() {
		return lsxh;
	}

	public void setLsxh(Integer lsxh) {
		this.lsxh = lsxh;
	}

	public String getDqshyh() {
		return dqshyh;
	}

	public void setDqshyh(String dqshyh) {
		this.dqshyh = dqshyh;
	}

	public String getDqshjs() {
		return dqshjs;
	}

	public void setDqshjs(String dqshjs) {
		this.dqshjs = dqshjs;
	}

	public String getTxlbmc() {
		return txlbmc;
	}

	public void setTxlbmc(String txlbmc) {
		this.txlbmc = txlbmc;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getShid() {
		return shid;
	}

	public void setShid(String shid) {
		this.shid = shid;
	}



	public String getYqrs() {
		return yqrs;
	}

	public void setYqrs(String yqrs) {
		this.yqrs = yqrs;
	}

	public ShxxDto getShxx() {
		return shxx;
	}

	public void setShxx(ShxxDto shxx) {
		this.shxx = shxx;
	}

	public String getYwmc() {
		return ywmc;
	}

	public void setYwmc(String ywmc) {
		this.ywmc = ywmc;
	}

	public String getGwmc() {
		return gwmc;
	}

	public void setGwmc(String gwmc) {
		this.gwmc = gwmc;
	}





	public String getTjrq() {
		return tjrq;
	}

	public void setTjrq(String tjrq) {
		this.tjrq = tjrq;
	}

	public String getScshsj() {
		return scshsj;
	}

	public void setScshsj(String scshsj) {
		this.scshsj = scshsj;
	}





	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
