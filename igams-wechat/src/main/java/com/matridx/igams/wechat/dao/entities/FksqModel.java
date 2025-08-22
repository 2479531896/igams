package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="FksqModel")
public class FksqModel extends BaseBasicModel {
	//合同付款ID
	private String fksqid;
	//申请人
	private String sqr;
	//申请部门
	private String sqbm;
	//费用归属
	private String fygs;
	//付款日期
	private String fkrq;
	//付款总金额
	private String fkzje;
	//付款方
	private String fkf;
	//付款方式
	private String fkfs;
	//付款事由
	private String fksy;
	//最晚支付日期
	private String zwzfrq;
	//支付对象
	private String zfdx;
	//支付方开户行
	private String zffkhh;
	//支付方银行账户
	private String zffyhzh;
	//钉钉实例ID
	private String ddslid;
	//备注
	private String bz;
	//状态
	private String zt;
	//业务类型
	private String ywlx;
	//业务ID
	private String ywid;
	//外部程序ID
	private String wbcxid;

	public String getWbcxid() {
		return wbcxid;
	}

	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}

	public String getFksqid() {
		return fksqid;
	}

	public void setFksqid(String fksqid) {
		this.fksqid = fksqid;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getFygs() {
		return fygs;
	}

	public void setFygs(String fygs) {
		this.fygs = fygs;
	}

	public String getFkrq() {
		return fkrq;
	}

	public void setFkrq(String fkrq) {
		this.fkrq = fkrq;
	}

	public String getFkzje() {
		return fkzje;
	}

	public void setFkzje(String fkzje) {
		this.fkzje = fkzje;
	}

	public String getFkf() {
		return fkf;
	}

	public void setFkf(String fkf) {
		this.fkf = fkf;
	}

	public String getFkfs() {
		return fkfs;
	}

	public void setFkfs(String fkfs) {
		this.fkfs = fkfs;
	}

	public String getFksy() {
		return fksy;
	}

	public void setFksy(String fksy) {
		this.fksy = fksy;
	}

	public String getZwzfrq() {
		return zwzfrq;
	}

	public void setZwzfrq(String zwzfrq) {
		this.zwzfrq = zwzfrq;
	}

	public String getZfdx() {
		return zfdx;
	}

	public void setZfdx(String zfdx) {
		this.zfdx = zfdx;
	}

	public String getZffkhh() {
		return zffkhh;
	}

	public void setZffkhh(String zffkhh) {
		this.zffkhh = zffkhh;
	}

	public String getZffyhzh() {
		return zffyhzh;
	}

	public void setZffyhzh(String zffyhzh) {
		this.zffyhzh = zffyhzh;
	}

	public String getDdslid() {
		return ddslid;
	}

	public void setDdslid(String ddslid) {
		this.ddslid = ddslid;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
