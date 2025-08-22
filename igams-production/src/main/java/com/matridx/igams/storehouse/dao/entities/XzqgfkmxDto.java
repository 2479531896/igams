package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XzqgfkmxDto")
public class XzqgfkmxDto extends XzqgfkmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//请购单号
	private String qgdh;
	//确认单号
	private String qrdh;
	//付款单号
	private String fkdh;
	//对账方式
	private String dzfsmc;
	//付款日期
	private String fkrq;
	//付款方
	private String fkfmc;
	//付款方式
	private String fkfsmc;
	//支付对象
	private String zfdx;
	//支付方开户行
	private String zffkhh;
	//支付方银行账号
	private String zffyhzh;
	//最晚支付日期
	private String zwfkrq;
	//申请人
	private String sqrmc;
	//付款事由
	private String fksy;
	//备注
	private String bz;
	//付款金额
	private String fkje;
	//费用归属
	private String fygs;

	public String getFygs() {
		return fygs;
	}

	public void setFygs(String fygs) {
		this.fygs = fygs;
	}

	public String getFkje() {
		return fkje;
	}

	public void setFkje(String fkje) {
		this.fkje = fkje;
	}

	public String getFkdh() {
		return fkdh;
	}

	public void setFkdh(String fkdh) {
		this.fkdh = fkdh;
	}

	public String getDzfsmc() {
		return dzfsmc;
	}

	public void setDzfsmc(String dzfsmc) {
		this.dzfsmc = dzfsmc;
	}

	public String getFkrq() {
		return fkrq;
	}

	public void setFkrq(String fkrq) {
		this.fkrq = fkrq;
	}

	public String getFkfmc() {
		return fkfmc;
	}

	public void setFkfmc(String fkfmc) {
		this.fkfmc = fkfmc;
	}

	public String getFkfsmc() {
		return fkfsmc;
	}

	public void setFkfsmc(String fkfsmc) {
		this.fkfsmc = fkfsmc;
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

	public String getZwfkrq() {
		return zwfkrq;
	}

	public void setZwfkrq(String zwfkrq) {
		this.zwfkrq = zwfkrq;
	}

	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public String getFksy() {
		return fksy;
	}

	public void setFksy(String fksy) {
		this.fksy = fksy;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getQrdh() {
		return qrdh;
	}

	public void setQrdh(String qrdh) {
		this.qrdh = qrdh;
	}

	public String getQgdh() {
		return qgdh;
	}

	public void setQgdh(String qgdh) {
		this.qgdh = qgdh;
	}
}
