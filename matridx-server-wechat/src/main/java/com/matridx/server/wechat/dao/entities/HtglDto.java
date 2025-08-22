package com.matridx.server.wechat.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value = "HtglDto")
public class HtglDto extends HtglModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 付款金额
	private String fkje;
	// 发票方式名称
	private String fpfsmc;
	// 付款方式名称
	private String fkfsmc;
	// 请求部门名称
	private String sqbmmc;
	//请求部分代码
	private String jqdm;
	// 币种名称
	private String bizmc;
	// 合同类型名称
	private String ywlxmc;
	//供应商名称
	private String gysmc;
	//供应商代码
	private String gysdm;
	// 检索用双章标记
	private String[] szbjs;
	// 检索用发票方式（多）
	private String[] fpfss;
	// 检索用付款方式（多）
	private String[] fkfss;
	// 金额最大数
	private Double zjemax;
	// 金额最小数
	private Double zjemin;
	// 创建日期结束日期
	private String shsjend;
	// 创建日期开始日期
	private String shsjstart;
	// 全部(查询条件)
	private String entire;
	private String SqlParam; 	//导出关联标记位//所选择的字段
	//查询参数[多个条件]
	private String searchParam;
	//查询条数
	private String count;
	//负责人名称
	private String fzrmc;
	//请购ID字符串
	private String djhs;
	//请购单据号
	private String djh;
	//合同明细json
	private String htmxJson;
	//访问标记(判断是否为外部接口)
	private String fwbj;
	//申请人姓名
	private String sqrmc;
	//申请日期
	private String sqrq;
	//付款百分比
	private String fkbfb;
	//附件IDS
	private List<String> fjids;
	//合同业务类型（防止与上传附件业务类型字段冲突）
	private String htywlx;
	//供应商负责人
	private String gysfzr;
	//供应商负责人电话
	private String gysfzrdh;
	//付款方名称
	private String fkfmc;
	
	
	public String getFkfmc() {
		return fkfmc;
	}

	public void setFkfmc(String fkfmc) {
		this.fkfmc = fkfmc;
	}

	public String getGysfzr() {
		return gysfzr;
	}

	public void setGysfzr(String gysfzr) {
		this.gysfzr = gysfzr;
	}

	public String getGysfzrdh() {
		return gysfzrdh;
	}

	public void setGysfzrdh(String gysfzrdh) {
		this.gysfzrdh = gysfzrdh;
	}

	public String getHtywlx() {
		return htywlx;
	}

	public void setHtywlx(String htywlx) {
		this.htywlx = htywlx;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}
	
	public String getFkbfb() {
		return fkbfb;
	}

	public void setFkbfb(String fkbfb) {
		this.fkbfb = fkbfb;
	}

	public String getJqdm() {
		return jqdm;
	}

	public void setJqdm(String jqdm) {
		this.jqdm = jqdm;
	}

	public String getGysdm() {
		return gysdm;
	}

	public void setGysdm(String gysdm) {
		this.gysdm = gysdm;
	}

	public String getSqrq() {
		return sqrq;
	}

	public void setSqrq(String sqrq) {
		this.sqrq = sqrq;
	}

	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
	}

	public String getFwbj() {
		return fwbj;
	}

	public void setFwbj(String fwbj) {
		this.fwbj = fwbj;
	}

	public String getHtmxJson() {
		return htmxJson;
	}

	public void setHtmxJson(String htmxJson) {
		this.htmxJson = htmxJson;
	}

	public String getDjhs() {
		return djhs;
	}

	public void setDjhs(String djhs) {
		this.djhs = djhs;
	}

	public String getFzrmc() {
		return fzrmc;
	}

	public void setFzrmc(String fzrmc) {
		this.fzrmc = fzrmc;
	}

	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}

	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public String getSearchParam() {
		return searchParam;
	}

	public void setSearchParam(String searchParam) {
		this.searchParam = searchParam;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getFkje() {
		return fkje;
	}

	public void setFkje(String fkje) {
		this.fkje = fkje;
	}

	public String getFpfsmc() {
		return fpfsmc;
	}

	public void setFpfsmc(String fpfsmc) {
		this.fpfsmc = fpfsmc;
	}

	public String getFkfsmc() {
		return fkfsmc;
	}

	public void setFkfsmc(String fkfsmc) {
		this.fkfsmc = fkfsmc;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String[] getFpfss() {
		return fpfss;
	}

	public void setFpfss(String[] fpfss) {
		this.fpfss = fpfss;
		for (int i = 0; i < fpfss.length; i++) {
			this.fpfss[i] = this.fpfss[i].replace("'", "");
		}
	}

	public String[] getFkfss() {
		return fkfss;
	}

	public void setFkfss(String[] fkfss) {
		this.fkfss = fkfss;
		for (int i = 0; i < fkfss.length; i++) {
			this.fkfss[i] = this.fkfss[i].replace("'", "");
		}
	}

	public String getBizmc() {
		return bizmc;
	}

	public void setBizmc(String bizmc) {
		this.bizmc = bizmc;
	}

	public String getYwlxmc() {
		return ywlxmc;
	}

	public void setYwlxmc(String ywlxmc) {
		this.ywlxmc = ywlxmc;
	}


	public Double getZjemax() {
		return zjemax;
	}

	public void setZjemax(Double zjemax) {
		this.zjemax = zjemax;
	}

	public Double getZjemin() {
		return zjemin;
	}

	public void setZjemin(Double zjemin) {
		this.zjemin = zjemin;
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

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String[] getSzbjs() {
		return szbjs;
	}

	public void setSzbjs(String[] szbjs) {
		this.szbjs = szbjs;
		for (int i = 0; i < szbjs.length; i++) {
			this.szbjs[i] = this.szbjs[i].replace("'", "");
		}
	}
}
