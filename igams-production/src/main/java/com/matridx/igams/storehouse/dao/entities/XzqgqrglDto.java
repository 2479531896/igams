package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="XzqgqrglDto")
public class XzqgqrglDto extends XzqgqrglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//用于接收请购确认明细数据
	private String qgqrmx_json;
	//确认明细list
	private List<XzqgqrmxDto> xzqgqrmxlist;
	//付款单号
	private String fkdh;
	//付款方式
	private String fkfs;
	//付款金额
	private String fkje;
	//最晚付款日期
	private String zwfkrq;
	//支付对象
	private String zfdx;
	//支付方开户行
	private String zffkhh;
	//支付方银行账户
	private String zffyhzh;
	//费用归属
	private String fygs;
	//付款事由
	private String fksy;
	//付款方
	private String fkf;
	//确认日期
	private String qrrq;
	//确认人员名称
	private String qrrymc;
	//对账方式名称
	private String dzfsmc;
	private String qrrqstart;
	private String qrrqend;
	private String entire;
	//申请部门名称
	private String sqbmmc;
	//机构代码
	private String jgdm;
	//申请人名称
	private String sqrmc;
	//抄送人
	private String csrs;
	//审批人角色ID
	private String sprjsid;
	//审批人角色名称
	private String sprjsmc;
	//录入人员名称
	private String lrrymc;
	//对账方式代码
	private String dzfsdm;
	//业务ID
	private String ywid;
	//货物名称
	private String hwmc;
	//单据号
	private String djh;
	//单据传达方式
	private String djcdfs;
	//对账方式多
	private String[] dzfss;
	
	private String fylb;

	public String getFylb() {
		return fylb;
	}

	public void setFylb(String fylb) {
		this.fylb = fylb;
	}


	public String[] getDzfss() {
		return dzfss;
	}

	public void setDzfss(String[] dzfss) {
		this.dzfss = dzfss;
	}

	public String getDjcdfs() {
		return djcdfs;
	}

	public void setDjcdfs(String djcdfs) {
		this.djcdfs = djcdfs;
	}

	public String getHwmc() {
		return hwmc;
	}

	public void setHwmc(String hwmc) {
		this.hwmc = hwmc;
	}

	public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getDzfsdm() {
		return dzfsdm;
	}

	public void setDzfsdm(String dzfsdm) {
		this.dzfsdm = dzfsdm;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getSprjsid() {
		return sprjsid;
	}

	public void setSprjsid(String sprjsid) {
		this.sprjsid = sprjsid;
	}

	public String getSprjsmc() {
		return sprjsmc;
	}

	public void setSprjsmc(String sprjsmc) {
		this.sprjsmc = sprjsmc;
	}

	public String getCsrs() {
		return csrs;
	}

	public void setCsrs(String csrs) {
		this.csrs = csrs;
	}

	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String getJgdm() {
		return jgdm;
	}

	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getQrrqstart() {
		return qrrqstart;
	}

	public void setQrrqstart(String qrrqstart) {
		this.qrrqstart = qrrqstart;
	}

	public String getQrrqend() {
		return qrrqend;
	}

	public void setQrrqend(String qrrqend) {
		this.qrrqend = qrrqend;
	}

	public String getDzfsmc() {
		return dzfsmc;
	}

	public void setDzfsmc(String dzfsmc) {
		this.dzfsmc = dzfsmc;
	}

	public String getQrrq() {
		return qrrq;
	}

	public void setQrrq(String qrrq) {
		this.qrrq = qrrq;
	}

	public String getQrrymc() {
		return qrrymc;
	}

	public void setQrrymc(String qrrymc) {
		this.qrrymc = qrrymc;
	}

	public String getFkf() {
		return fkf;
	}

	public void setFkf(String fkf) {
		this.fkf = fkf;
	}

	public String getFkdh() {
		return fkdh;
	}

	public void setFkdh(String fkdh) {
		this.fkdh = fkdh;
	}

	public String getFkfs() {
		return fkfs;
	}

	public void setFkfs(String fkfs) {
		this.fkfs = fkfs;
	}

	public String getFkje() {
		return fkje;
	}

	public void setFkje(String fkje) {
		this.fkje = fkje;
	}

	public String getZwfkrq() {
		return zwfkrq;
	}

	public void setZwfkrq(String zwfkrq) {
		this.zwfkrq = zwfkrq;
	}

	@Override
	public String getZfdx() {
		return zfdx;
	}

	@Override
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

	public String getFygs() {
		return fygs;
	}

	public void setFygs(String fygs) {
		this.fygs = fygs;
	}

	public String getFksy() {
		return fksy;
	}

	public void setFksy(String fksy) {
		this.fksy = fksy;
	}

	public String getQgqrmx_json() {
		return qgqrmx_json;
	}

	public void setQgqrmx_json(String qgqrmx_json) {
		this.qgqrmx_json = qgqrmx_json;
	}

	public List<XzqgqrmxDto> getXzqgqrmxlist() {
		return xzqgqrmxlist;
	}

	public void setXzqgqrmxlist(List<XzqgqrmxDto> xzqgqrmxlist) {
		this.xzqgqrmxlist = xzqgqrmxlist;
	}
}
