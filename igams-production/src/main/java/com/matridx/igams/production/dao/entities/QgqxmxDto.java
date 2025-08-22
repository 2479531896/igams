package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="QgqxmxDto")
public class QgqxmxDto extends QgqxmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//实际数量
	private String sjsl;
	//原始数量
	private String yssl;
	//剩余数量
	private String sysl;
	//请购明细数量
	private String qgmxsl;
	//预定数量
	private String ydsl;
	//物料名称
	private String wlmc;
	//物料类别
	private String wllb;
	//物料编码
	private String wlbm;
	//物料类别名称
	private String wllbmc;
	//物料子类别
	private String wlzlb;
	//物料子类别名称
	private String wlzlbmc;
	//物料子类别统称
	private  String wlzlbtc;
	//物料子类别统称名称
	private String wlzlbtcmc;
	private String[] wlzlbtcs;
	//生产商
	private String scs;
	//规格
	private String gg;
	//计量单位
	private String jldw;
	//原厂货号
	private String ychh;
	//单据号
	private String djh;
	// 全部(查询条件)
	private String entire;
	private String SqlParam; 	//导出关联标记位//所选择的字段
	//查询参数[多个条件]
	private String searchParam;
	//价格
	private String jg;
	//总取消数量(取消但没有审核通过的取消请购数量)
	private String zqxsl;
	//请购明细数量字段
	private String sl;
	//请购取消人员
	private String qgqxry;
	//请购取消时间
	private String qgqxsj;
	//可取消数量
	private String kqxsl;
	//期望日期
	private String qwrq;
	//请购取消状态
	private String qxqgzt;
	//rabbit同步标记
	private String prefixFlg;
	//申请理由
	private String sqly;
	//请购类别名称
	private String qglbmc;
	//货物名称
	private String hwmc;
	//获取标准
	private String hwbz;
	//要求
	private String yq;
	//货物用途
	private String hwyt;
	//请购类别代码
	private String qglbdm;
	//货物计量单位
	private String hwjldw;
	//申请人名称
	private String sqrmc;
	
	
	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public String getHwjldw() {
		return hwjldw;
	}

	public void setHwjldw(String hwjldw) {
		this.hwjldw = hwjldw;
	}

	public String getQglbdm() {
		return qglbdm;
	}

	public void setQglbdm(String qglbdm) {
		this.qglbdm = qglbdm;
	}

	public String getHwmc() {
		return hwmc;
	}

	public void setHwmc(String hwmc) {
		this.hwmc = hwmc;
	}

	public String getHwbz() {
		return hwbz;
	}

	public void setHwbz(String hwbz) {
		this.hwbz = hwbz;
	}

	public String getYq() {
		return yq;
	}

	public void setYq(String yq) {
		this.yq = yq;
	}

	public String getHwyt() {
		return hwyt;
	}

	public void setHwyt(String hwyt) {
		this.hwyt = hwyt;
	}

	public String getQglbmc() {
		return qglbmc;
	}

	public void setQglbmc(String qglbmc) {
		this.qglbmc = qglbmc;
	}

	public String getSqly() {
		return sqly;
	}

	public void setSqly(String sqly) {
		this.sqly = sqly;
	}

	public String getPrefixFlg() {
		return prefixFlg;
	}

	public void setPrefixFlg(String prefixFlg) {
		this.prefixFlg = prefixFlg;
	}

	public String getQxqgzt() {
		return qxqgzt;
	}

	public void setQxqgzt(String qxqgzt) {
		this.qxqgzt = qxqgzt;
	}

	public String getQwrq() {
		return qwrq;
	}

	public void setQwrq(String qwrq) {
		this.qwrq = qwrq;
	}

	public String getKqxsl() {
		return kqxsl;
	}

	public void setKqxsl(String kqxsl) {
		this.kqxsl = kqxsl;
	}

	public String getQgqxry() {
		return qgqxry;
	}

	public void setQgqxry(String qgqxry) {
		this.qgqxry = qgqxry;
	}

	public String getQgqxsj() {
		return qgqxsj;
	}

	public void setQgqxsj(String qgqxsj) {
		this.qgqxsj = qgqxsj;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getZqxsl() {
		return zqxsl;
	}

	public void setZqxsl(String zqxsl) {
		this.zqxsl = zqxsl;
	}

	public String getJg() {
		return jg;
	}

	public void setJg(String jg) {
		this.jg = jg;
	}

	public String[] getWlzlbtcs() {
		return wlzlbtcs;
	}

	public void setWlzlbtcs(String[] wlzlbtcs) {
		this.wlzlbtcs = wlzlbtcs;
	}

	public String getQgmxsl() {
		return qgmxsl;
	}

	public void setQgmxsl(String qgmxsl) {
		this.qgmxsl = qgmxsl;
	}

	public String getYdsl() {
		return ydsl;
	}

	public void setYdsl(String ydsl) {
		this.ydsl = ydsl;
	}

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}

	public String getWllb() {
		return wllb;
	}

	public void setWllb(String wllb) {
		this.wllb = wllb;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String getWllbmc() {
		return wllbmc;
	}

	public void setWllbmc(String wllbmc) {
		this.wllbmc = wllbmc;
	}

	public String getWlzlb() {
		return wlzlb;
	}

	public void setWlzlb(String wlzlb) {
		this.wlzlb = wlzlb;
	}

	public String getWlzlbmc() {
		return wlzlbmc;
	}

	public void setWlzlbmc(String wlzlbmc) {
		this.wlzlbmc = wlzlbmc;
	}

	public String getWlzlbtc() {
		return wlzlbtc;
	}

	public void setWlzlbtc(String wlzlbtc) {
		this.wlzlbtc = wlzlbtc;
	}

	public String getWlzlbtcmc() {
		return wlzlbtcmc;
	}

	public void setWlzlbtcmc(String wlzlbtcmc) {
		this.wlzlbtcmc = wlzlbtcmc;
	}

	public String getScs() {
		return scs;
	}

	public void setScs(String scs) {
		this.scs = scs;
	}

	public String getGg() {
		return gg;
	}

	public void setGg(String gg) {
		this.gg = gg;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public String getYchh() {
		return ychh;
	}

	public void setYchh(String ychh) {
		this.ychh = ychh;
	}

	public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
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

	public String getSysl() {
		return sysl;
	}

	public void setSysl(String sysl) {
		this.sysl = sysl;
	}

	public String getYssl() {
		return yssl;
	}

	public void setYssl(String yssl) {
		this.yssl = yssl;
	}

	public String getSjsl() {
		return sjsl;
	}

	public void setSjsl(String sjsl) {
		this.sjsl = sjsl;
	}

	
}
