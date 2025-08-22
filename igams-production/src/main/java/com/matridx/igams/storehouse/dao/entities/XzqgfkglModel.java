package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="XzqgfkglModel")
public class XzqgfkglModel extends BaseBasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//付款ID
	private String fkid;
	//对账方式
	private String dzfs;
	//付款单号
	private String fkdh;
	//最晚付款日期
	private String zwfkrq;
	//付款金额
	private String fkje;
	//付款方
	private String fkf;
	//付款方式
	private String fkfs;
	//支付对象
	private String zfdx;
	//支付方开户行
	private String zffkhh;
	//支付方银行账户
	private String zffyhzh;
	//备注
	private String bz;
	//费用归属
	private String fygs;
	//钉钉实例ID
	private String ddslid;
	//付款事由
	private String fksy;
	//状态
	private String zt;
	//申请人
	private String sqr;
	//申请部门
	private String sqbm;
	//单据传达方式
	private String djcdfs;
	//费用类别
	private String  fylb;
	//供应商id
	private String  zfdxid;

	public String getZfdxid() {
		return zfdxid;
	}

	public void setZfdxid(String zfdxid) {
		this.zfdxid = zfdxid;
	}

	public String getFylb() {
		return fylb;
	}

	public void setFylb(String fylb) {
		this.fylb = fylb;
	}

	public String getDjcdfs() {
		return djcdfs;
	}
	public void setDjcdfs(String djcdfs) {
		this.djcdfs = djcdfs;
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

	public String getFkid() {
		return fkid;
	}

	public void setFkid(String fkid) {
		this.fkid = fkid;
	}

	public String getDzfs() {
		return dzfs;
	}

	public void setDzfs(String dzfs) {
		this.dzfs = dzfs;
	}

	public String getFkdh() {
		return fkdh;
	}

	public void setFkdh(String fkdh) {
		this.fkdh = fkdh;
	}

	public String getZwfkrq() {
		return zwfkrq;
	}

	public void setZwfkrq(String zwfkrq) {
		this.zwfkrq = zwfkrq;
	}

	public String getFkje() {
		return fkje;
	}

	public void setFkje(String fkje) {
		this.fkje = fkje;
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

	public String getFygs() {
		return fygs;
	}

	public void setFygs(String fygs) {
		this.fygs = fygs;
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

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}
}
