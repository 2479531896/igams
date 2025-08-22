package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="FhglDto")
public class FhglDto extends FhglModel{
	// 全部(查询条件)
	private String entire;
	private String fhsjstart;
	private String fhsjend;
	private String wlbm;
	private String wlmc;
	private String serial;
	private String prefix;
	private String xsmx_json;
	private String wlmx_json;
	private String thmx_json;
	private String xsbmmc;
	private String czbs;
	//发货人名称
	private String jsrmc;
	//所属机构
	private String ssjg;
	//发货单号
	private String fhdhs;
	//客户简称
	private String khjc;
	//可退货数量
	private String kthsl;
	//客户名称
	private String khmc;
	// 创建日期结束日期
	private String shsjend;
	// 创建日期开始日期
	private String shsjstart;
	//销售部门代码
	private String xsbmdm;
	//销售类型代码
	private String xslxdm;
	//真实姓名
	private String zsxm;

	private String khdm;
	//销售订单
	private String xsdd;

	//钉钉标记
	private String ddbj;

	//区分不同的审核页面
	private String xsbj;
	//状态
	private String ztmc;
	//退货id
	private String thid;
	//生产批号
	private String scph;
	//失效日期
	private String yxq;
	//生产日期
	private String scrq;
	//计量单位
	private String jldw;
	//规格
	private String gg;
	//物料id
	private String wlid;
	//无税单价
	private String wsdj;
	//退货数量
	private String thsl;
	//报价
	private String bj;
	//发货明细id
	private String fhmxid;
	//含税单价
	private String hsdj;
	//发货明细关联id
	private String fhmxglid;
	//税率
	private String suil;
	//仓库id
	private String ckid;
	//销售明细id
	private String xsmxid;
	//货物id
	private String hwid;
	//物流信息
	private String wlxx;
	//U8用户code
	private String grouping;
	//发货状态
	private String fhzt;
	//到款金额
	private String dkje;
	//是否新增到款记录
	private String dkjlbj;
	//销售id
	private String xsid;

	private String sqlParam; 	//导出关联标记位//所选择的字段

	private String jsze;//价税总额

	private String fhsl;//发货数量

	private String ddsl;//订单数量

	private String htdh;//合同单号



	public String getXsid() {
		return xsid;
	}

	public void setXsid(String xsid) {
		this.xsid = xsid;
	}

	public String getDkjlbj() {
		return dkjlbj;
	}

	public void setDkjlbj(String dkjlbj) {
		this.dkjlbj = dkjlbj;
	}

	public String getDkje() {
		return dkje;
	}

	public void setDkje(String dkje) {
		this.dkje = dkje;
	}

	public String getFhzt() {
		return fhzt;
	}

	public void setFhzt(String fhzt) {
		this.fhzt = fhzt;
	}

	public String getGrouping() {
		return grouping;
	}

	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}

	public String getWlxx() {
		return wlxx;
	}

	public void setWlxx(String wlxx) {
		this.wlxx = wlxx;
	}
	public String getThid() {
		return thid;
	}

	public void setThid(String thid) {
		this.thid = thid;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getYxq() {
		return yxq;
	}

	public void setYxq(String yxq) {
		this.yxq = yxq;
	}

	public String getScrq() {
		return scrq;
	}

	public void setScrq(String scrq) {
		this.scrq = scrq;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public String getGg() {
		return gg;
	}

	public void setGg(String gg) {
		this.gg = gg;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getWsdj() {
		return wsdj;
	}

	public void setWsdj(String wsdj) {
		this.wsdj = wsdj;
	}

	public String getThsl() {
		return thsl;
	}

	public void setThsl(String thsl) {
		this.thsl = thsl;
	}

	public String getBj() {
		return bj;
	}

	public void setBj(String bj) {
		this.bj = bj;
	}

	public String getFhmxid() {
		return fhmxid;
	}

	public void setFhmxid(String fhmxid) {
		this.fhmxid = fhmxid;
	}

	public String getHsdj() {
		return hsdj;
	}

	public void setHsdj(String hsdj) {
		this.hsdj = hsdj;
	}

	public String getFhmxglid() {
		return fhmxglid;
	}

	public void setFhmxglid(String fhmxglid) {
		this.fhmxglid = fhmxglid;
	}

	@Override
	public String getSuil() {
		return suil;
	}

	@Override
	public void setSuil(String suil) {
		this.suil = suil;
	}

	@Override
	public String getCkid() {
		return ckid;
	}

	@Override
	public void setCkid(String ckid) {
		this.ckid = ckid;
	}

	public String getXsmxid() {
		return xsmxid;
	}

	public void setXsmxid(String xsmxid) {
		this.xsmxid = xsmxid;
	}

	public String getHwid() {
		return hwid;
	}

	public void setHwid(String hwid) {
		this.hwid = hwid;
	}
	public String getZtmc() {
		return ztmc;
	}

	public void setZtmc(String ztmc) {
		this.ztmc = ztmc;
	}
	public String getSsjg() {
		return ssjg;
	}

	public void setSsjg(String ssjg) {
		this.ssjg = ssjg;
	}

	public String getDdbj() {
		return ddbj;
	}

	public void setDdbj(String ddbj) {
		this.ddbj = ddbj;
	}

	public String getXsbj() {
		return xsbj;
	}

	public void setXsbj(String xsbj) {
		this.xsbj = xsbj;
	}

	public String getCzbs() {
		return czbs;
	}

	public void setCzbs(String czbs) {
		this.czbs = czbs;
	}

	public String getXsdd() {
		return xsdd;
	}

	public void setXsdd(String xsdd) {
		this.xsdd = xsdd;
	}

	public String getThmx_json() {
		return thmx_json;
	}

	public void setThmx_json(String thmx_json) {
		this.thmx_json = thmx_json;
	}



	public String getXsbmdm() {
		return xsbmdm;
	}

	public void setXsbmdm(String xsbmdm) {
		this.xsbmdm = xsbmdm;
	}

	public String getXslxdm() {
		return xslxdm;
	}

	public void setXslxdm(String xslxdm) {
		this.xslxdm = xslxdm;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getKhdm() {
		return khdm;
	}
	public void setKhdm(String khdm) {
		this.khdm = khdm;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getShsjend() {
		return shsjend;
	}

	public void setShsjend(String shsjend) {
		this.shsjend = shsjend;
	}

	public String getShsjstart() {
		return shsjstart;
	}

	public void setShsjstart(String shsjstart) {
		this.shsjstart = shsjstart;
	}

	public String getKthsl() {
		return kthsl;
	}

	public void setKthsl(String kthsl) {
		this.kthsl = kthsl;
	}

	public String getKhjc() {
		return khjc;
	}

	public void setKhjc(String khjc) {
		this.khjc = khjc;
	}

	public String getFhdhs() {
		return fhdhs;
	}

	public void setFhdhs(String fhdhs) {
		this.fhdhs = fhdhs;
	}

	public String getJsrmc() {
		return jsrmc;
	}

	public void setJsrmc(String jsrmc) {
		this.jsrmc = jsrmc;
	}

	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getXsmx_json() {
		return xsmx_json;
	}

	public void setXsmx_json(String xsmx_json) {
		this.xsmx_json = xsmx_json;
	}

	public String getWlmx_json() {
		return wlmx_json;
	}

	public void setWlmx_json(String wlmx_json) {
		this.wlmx_json = wlmx_json;
	}


	public String getXsbmmc() {
		return xsbmmc;
	}

	public void setXsbmmc(String xsbmmc) {
		this.xsbmmc = xsbmmc;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getFhsjstart() {
		return fhsjstart;
	}

	public void setFhsjstart(String fhsjstart) {
		this.fhsjstart = fhsjstart;
	}

	public String getFhsjend() {
		return fhsjend;
	}

	public void setFhsjend(String fhsjend) {
		this.fhsjend = fhsjend;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//销售类型名称
	private String xslxmc;

	public String getXslxmc() {
		return xslxmc;
	}

	public void setXslxmc(String xslxmc) {
		this.xslxmc = xslxmc;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getHtdh() {
		return htdh;
	}

	public void setHtdh(String htdh) {
		this.htdh = htdh;
	}

	public String getDdsl() {
		return ddsl;
	}

	public void setDdsl(String ddsl) {
		this.ddsl = ddsl;
	}

	public String getFhsl() {
		return fhsl;
	}

	public void setFhsl(String fhsl) {
		this.fhsl = fhsl;
	}

	public String getJsze() {
		return jsze;
	}

	public void setJsze(String jsze) {
		this.jsze = jsze;
	}
}
