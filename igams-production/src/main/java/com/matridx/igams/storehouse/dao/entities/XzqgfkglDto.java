package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XzqgfkglDto")
public class XzqgfkglDto extends XzqgfkglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//对账方式代码
	private String dzfsdm;
	//确认单号
	private String djh;
	//用于接收明细数据
	private String fkmx_json;
	//确认ID
	private String Qrid;
	//对账方式名称
	private String dzfsmc;
	//付款方名称
	private String fkfmc;
	//付款方式名称
	private String fkfsmc;
	//entire
	private String entire;
	//申请人名称
	private String sqrmc;
	//申请部门名称
	private String sqbmmc;
	//机构代码
	private String jgdm;
	//抄送人
	private String csrs;
	//审批人角色ID
	private String sprjsid;
	//审批人角色名称
	private String sprjsmc;
	//付款信息
	private String fkmxJson;
	//费用归属名称
	private String fygsmc;
	//sql导出字段
	private String sqlParam;
	//状态名称
	private String ztmc;
	//单据传达方式名称
	private String djcdfsmc;
	//费用类别
	private String fylbmc;
	//费用类别多
	private String[] fylbs;
	//付款方式多
	private String[] fkfss;
	private String fkrqstart;
	private String fkrqend;
	private String qgdh;//请购单号
	private String xh;//序号
	private String hwmc;//获取名称
	private String hwgg;//规格
	private String sl;//数量
	private String jg;//价格
	private String hwjldw;//单位
	private String ywbm;//往来单位业务编码
	private String fylbdm;//费用类别代码 对应（每刻）行政采购类别代码

	public String getFylbdm() {
		return fylbdm;
	}

	public void setFylbdm(String fylbdm) {
		this.fylbdm = fylbdm;
	}

	public String getYwbm() {
		return ywbm;
	}

	public void setYwbm(String ywbm) {
		this.ywbm = ywbm;
	}
	public String getQgdh() {
		return qgdh;
	}

	public void setQgdh(String qgdh) {
		this.qgdh = qgdh;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getHwmc() {
		return hwmc;
	}

	public void setHwmc(String hwmc) {
		this.hwmc = hwmc;
	}

	public String getHwgg() {
		return hwgg;
	}

	public void setHwgg(String hwgg) {
		this.hwgg = hwgg;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getJg() {
		return jg;
	}

	public void setJg(String jg) {
		this.jg = jg;
	}

	public String getHwjldw() {
		return hwjldw;
	}

	public void setHwjldw(String hwjldw) {
		this.hwjldw = hwjldw;
	}

	public String getFkrqstart() {
		return fkrqstart;
	}

	public void setFkrqstart(String fkrqstart) {
		this.fkrqstart = fkrqstart;
	}

	public String getFkrqend() {
		return fkrqend;
	}

	public void setFkrqend(String fkrqend) {
		this.fkrqend = fkrqend;
	}

	public String[] getFkfss() {
		return fkfss;
	}

	public void setFkfss(String[] fkfss) {
		this.fkfss = fkfss;
	}

	public String[] getFylbs() {
		return fylbs;
	}

	public void setFylbs(String[] fylbs) {
		this.fylbs = fylbs;
	}

	public String getFylbmc() {
		return fylbmc;
	}

	public void setFylbmc(String fylbmc) {
		this.fylbmc = fylbmc;
	}

	public String getDjcdfsmc() {
		return djcdfsmc;
	}
	public void setDjcdfsmc(String djcdfsmc) {
		this.djcdfsmc = djcdfsmc;
	}
	public String getZtmc() {
		return ztmc;
	}

	public void setZtmc(String ztmc) {
		this.ztmc = ztmc;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getFygsmc() {
		return fygsmc;
	}

	public void setFygsmc(String fygsmc) {
		this.fygsmc = fygsmc;
	}

	public String getFkmxJson() {
		return fkmxJson;
	}

	public void setFkmxJson(String fkmxJson) {
		this.fkmxJson = fkmxJson;
	}

	public String getFkmx_json() {
		return fkmx_json;
	}

	public void setFkmx_json(String fkmx_json) {
		this.fkmx_json = fkmx_json;
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

	public String getJgdm() {
		return jgdm;
	}

	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
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

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getDzfsmc() {
		return dzfsmc;
	}

	public void setDzfsmc(String dzfsmc) {
		this.dzfsmc = dzfsmc;
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

	public String getQrid() {
		return Qrid;
	}

	public void setQrid(String qrid) {
		Qrid = qrid;
	}

	public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
	}

	public String getDzfsdm() {
		return dzfsdm;
	}

	public void setDzfsdm(String dzfsdm) {
		this.dzfsdm = dzfsdm;
	}
}
