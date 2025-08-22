package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="JcjyglDto")
public class JcjyglDto extends JcjyglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//部门名称
	private String bmmc;

	//部门代码
	private String bmdm;

	//单位名称
	private String dwmc;

	//单位代码
	private String dwdm;

	//jymx_json
	private String jymx_json;

	//单位类型名称
	private String dwlxmc;

	//状态
	private String zt;

	private String entire;

	//真实姓名
	private String zsxm;

	//钉钉标记
	private String ddbj;

	//区分不同的审核页面
	private String xsbj;

	//借用开始时间
	private String jysjstart;

	//借用结束时间
	private String jysjend;
	//审核状态
	private String ztmc;
	//借出数量
	private String jcsl;
	private String ckhwid;
	private String hwid;
	private String jyxxid;
	private String jymxid;
	//借用数量
	private String jysl;
	//录入人名称
	private String lrrymc;
	//条数
	private int ts;
	//客户简称
	private String khjc;
	//物料编码
	private String wlbm;
	//物料名称
	private String wlmc;
	//物料id
	private String wlid;
	//钉钉查询字段
	private String ddentire;
	//客户名称
	private String khmc;
	//是否确认多
	private String[] sfqss;
	//是否签收多
	private String[] sfqrs;
	private String[] khlbs;
	private String sqlParam;
	//制单人
	private String zdr;
	//审核人
	private String shrmc;
	//规格
	private String gg;
	//计量单位
	private String jldw;
	//仓库名称
	private String ckmc;
	//生产批号
	private String scph;
	//保存条件
	private String bctj;
	//预计归还日期
	private String yjghrq;
	//业务员
	private String ywymc;
	//终端区域名称
	private String zdqymc;
	private String shrymc;
	//原借用单号
	private String yjydh;
	//单据类型s
	private String[] djlxs;
	//换货标记
	private String hhbj;
	private String grouping;
	//销售类型名称
	private String xslxmc;
	//合同id
	private String htid;
	private String fzrmc;//负责人
	private String yfzr;//原负责人
	private String ysfsmc;//运输方式名称

	public String getYsfsmc() {
		return ysfsmc;
	}

	public void setYsfsmc(String ysfsmc) {
		this.ysfsmc = ysfsmc;
	}
	public String getYfzr() {
		return yfzr;
	}

	public void setYfzr(String yfzr) {
		this.yfzr = yfzr;
	}

	public String getFzrmc() {
		return fzrmc;
	}

	public void setFzrmc(String fzrmc) {
		this.fzrmc = fzrmc;
	}

	public String getHtid() {
		return htid;
	}

	public void setHtid(String htid) {
		this.htid = htid;
	}

	public String getXslxmc() {
		return xslxmc;
	}

	public void setXslxmc(String xslxmc) {
		this.xslxmc = xslxmc;
	}

	public String getGrouping() {
		return grouping;
	}

	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}

	public String getHhbj() {
		return hhbj;
	}

	public void setHhbj(String hhbj) {
		this.hhbj = hhbj;
	}

	public String[] getDjlxs() {
		return djlxs;
	}

	public void setDjlxs(String[] djlxs) {
		this.djlxs = djlxs;
	}

	public String getYjydh() {
		return yjydh;
	}

	public void setYjydh(String yjydh) {
		this.yjydh = yjydh;
	}

	public String getShrymc() {
		return shrymc;
	}

	public void setShrymc(String shrymc) {
		this.shrymc = shrymc;
	}

	public String getZdqymc() {
		return zdqymc;
	}

	public void setZdqymc(String zdqymc) {
		this.zdqymc = zdqymc;
	}

	public String getYwymc() {
		return ywymc;
	}

	public void setYwymc(String ywymc) {
		this.ywymc = ywymc;
	}

	public String getYjghrq() {
		return yjghrq;
	}

	public void setYjghrq(String yjghrq) {
		this.yjghrq = yjghrq;
	}

	public String getBctj() {
		return bctj;
	}

	public void setBctj(String bctj) {
		this.bctj = bctj;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
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

	public String getShrmc() {
		return shrmc;
	}

	public void setShrmc(String shrmc) {
		this.shrmc = shrmc;
	}

	public String getZdr() {
		return zdr;
	}

	public void setZdr(String zdr) {
		this.zdr = zdr;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String[] getSfqss() {
		return sfqss;
	}

	public void setSfqss(String[] sfqss) {
		this.sfqss = sfqss;
	}

	public String[] getSfqrs() {
		return sfqrs;
	}

	public void setSfqrs(String[] sfqrs) {
		this.sfqrs = sfqrs;
	}

	public String[] getKhlbs() {
		return khlbs;
	}

	public void setKhlbs(String[] khlbs) {
		this.khlbs = khlbs;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getDdentire() {
		return ddentire;
	}

	public void setDdentire(String ddentire) {
		this.ddentire = ddentire;
	}

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String getKhjc() {
		return khjc;
	}

	public void setKhjc(String khjc) {
		this.khjc = khjc;
	}

	public int getTs() {
		return ts;
	}

	public void setTs(int ts) {
		this.ts = ts;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}
public String getJyxxid() {
		return jyxxid;
	}

	public void setJyxxid(String jyxxid) {
		this.jyxxid = jyxxid;
	}

	public String getJymxid() {
		return jymxid;
	}

	public void setJymxid(String jymxid) {
		this.jymxid = jymxid;
	}

	public String getJcsl() {
		return jcsl;
	}

	public void setJcsl(String jcsl) {
		this.jcsl = jcsl;
	}

	public String getCkhwid() {
		return ckhwid;
	}

	public void setCkhwid(String ckhwid) {
		this.ckhwid = ckhwid;
	}

	public String getHwid() {
		return hwid;
	}

	public void setHwid(String hwid) {
		this.hwid = hwid;
	}

	public String getJysl() {
		return jysl;
	}

	public void setJysl(String jysl) {
		this.jysl = jysl;
	}

	public String getZtmc() {
		return ztmc;
	}

	public void setZtmc(String ztmc) {
		this.ztmc = ztmc;
	}

	public String getJysjstart() {
		return jysjstart;
	}

	public void setJysjstart(String jysjstart) {
		this.jysjstart = jysjstart;
	}

	public String getJysjend() {
		return jysjend;
	}

	public void setJysjend(String jysjend) {
		this.jysjend = jysjend;
	}

	public String getXsbj() {
		return xsbj;
	}

	public void setXsbj(String xsbj) {
		this.xsbj = xsbj;
	}

	public String getDdbj() {
		return ddbj;
	}

	public void setDdbj(String ddbj) {
		this.ddbj = ddbj;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getDwlxmc() {
		return dwlxmc;
	}

	public void setDwlxmc(String dwlxmc) {
		this.dwlxmc = dwlxmc;
	}

	public String getJymx_json() {
		return jymx_json;
	}

	public void setJymx_json(String jymx_json) {
		this.jymx_json = jymx_json;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getDwdm() {
		return dwdm;
	}

	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getBmdm() {
		return bmdm;
	}

	public void setBmdm(String bmdm) {
		this.bmdm = bmdm;
	}
}
