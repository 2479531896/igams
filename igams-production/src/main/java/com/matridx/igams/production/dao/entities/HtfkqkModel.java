package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="HtfkqkModel")
public class HtfkqkModel extends BaseBasicModel {
	//合同付款ID
	private String htfkid;
	//付款日期
	private String fkrq;
	//付款百分比
	private String fkbfb;
	//付款金额
	private String fkzje;
	//状态
	private String zt;
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
	//备注
	private String bz;
	//钉钉实例ID
	private String ddslid;
	//付款单号
	private String fkdh;
	//申请人
	private String sqr;
	//申请部门
	private String sqbm;
	//用款部门
	private String ykbm;
	//审核通过时间
	private String shtgsj;
	//单据传达方式
	private String djcdfs;

	public String getDjcdfs() {
		return djcdfs;
	}
	public void setDjcdfs(String djcdfs) {
		this.djcdfs = djcdfs;
	}

	public String getShtgsj() {
		return shtgsj;
	}

	public void setShtgsj(String shtgsj) {
		this.shtgsj = shtgsj;
	}

	public String getYkbm() {
		return ykbm;
	}

	public void setYkbm(String ykbm) {
		this.ykbm = ykbm;
	}

	@Override
	public String getSqr() {
		return sqr;
	}

	@Override
	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getFkzje() {
		return fkzje;
	}

	public void setFkzje(String fkzje) {
		this.fkzje = fkzje;
	}

	public String getFkdh() {
		return fkdh;
	}

	public void setFkdh(String fkdh) {
		this.fkdh = fkdh;
	}

	public String getDdslid() {
		return ddslid;
	}

	public void setDdslid(String ddslid) {
		this.ddslid = ddslid;
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

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
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

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	//合同付款ID
	public String getHtfkid() {
		return htfkid;
	}
	public void setHtfkid(String htfkid){
		this.htfkid = htfkid;
	}
	//付款日期
	public String getFkrq() {
		return fkrq;
	}
	public void setFkrq(String fkrq){
		this.fkrq = fkrq;
	}
	//付款百分比
	public String getFkbfb() {
		return fkbfb;
	}
	public void setFkbfb(String fkbfb){
		this.fkbfb = fkbfb;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
