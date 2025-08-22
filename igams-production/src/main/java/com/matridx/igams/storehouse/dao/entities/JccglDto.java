package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="JccglDto")
public class JccglDto extends JccglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//物料名称
	private String wlmc;
	//物料编码
	private String wlbm;
	//物料类别
	private String wllb;
	//物料子类别
	private String wlzlb;
	//代理供应商
	private String dlgys;
	//审核时间
	private String shsj;
	//生产商
	private String scs;
	//原厂货号
	private String ychh;
	//规格
	private String gg;
	//计量单位
	private String jldw;
	//保存条件
	private String bctj;
	//保质期
	private String bzq;
	//保质期标记
	private String bzqflg;
	//是否危险品
	private String sfwxp;
	//备注
	private String bz;
	//审核状态   00:未提交 10:审核中  15:审核未通过  80:审核通过
	private String zt;
	//旧物料编码
	private String jwlbm;
	//类别
	private String lb;
	//安全库存
	private String aqkc;
	//起订量
	private String qdl;
	//物料子类别统称
	private String wlzlbtc;
	//物料类别名称
	private String wllbmc;
	//物料子类别名称
	private String wlzlbmc;
	//物料子类别统称名称
	private String wlzlbtcmc;
	//物料编码s
	private List<String> wlbms;
	//物料子类别参数扩展1
	private String cskz1;
	//库存量
	private String kcl;
	//预定数
	private String yds;
	//可请领数
	private String kqls;
	//项目大类
	private String xmdl;
	//项目编码
	private String xmbm;
	//货物领料明细
	private String hwllmx_json;
	//仓库分类名称
	private String ckqxmc;
	//仓库分类
	private String ckqxlx;
	//物料id
	private String wlid;

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getCkqxmc() {
		return ckqxmc;
	}

	public void setCkqxmc(String ckqxmc) {
		this.ckqxmc = ckqxmc;
	}

	public String getCkqxlx() {
		return ckqxlx;
	}

	public void setCkqxlx(String ckqxlx) {
		this.ckqxlx = ckqxlx;
	}

	public String getHwllmx_json() {
		return hwllmx_json;
	}
	public void setHwllmx_json(String hwllmx_json) {
		this.hwllmx_json = hwllmx_json;
	}
	public String getXmdl() {
		return xmdl;
	}
	public void setXmdl(String xmdl) {
		this.xmdl = xmdl;
	}
	public String getXmbm() {
		return xmbm;
	}
	public void setXmbm(String xmbm) {
		this.xmbm = xmbm;
	}
	public String getKqls() {
		return kqls;
	}
	public void setKqls(String kqls) {
		this.kqls = kqls;
	}
	public String getYds() {
		return yds;
	}
	public void setYds(String yds) {
		this.yds = yds;
	}
	public String getKcl() {
		return kcl;
	}
	public void setKcl(String kcl) {
		this.kcl = kcl;
	}
	public String getWlmc() {
		return wlmc;
	}
	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}
	public String getWlbm() {
		return wlbm;
	}
	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}
	public String getWllb() {
		return wllb;
	}
	public void setWllb(String wllb) {
		this.wllb = wllb;
	}
	public String getWlzlb() {
		return wlzlb;
	}
	public void setWlzlb(String wlzlb) {
		this.wlzlb = wlzlb;
	}
	public String getDlgys() {
		return dlgys;
	}
	public void setDlgys(String dlgys) {
		this.dlgys = dlgys;
	}
	public String getShsj() {
		return shsj;
	}
	public void setShsj(String shsj) {
		this.shsj = shsj;
	}
	public String getScs() {
		return scs;
	}
	public void setScs(String scs) {
		this.scs = scs;
	}
	public String getYchh() {
		return ychh;
	}
	public void setYchh(String ychh) {
		this.ychh = ychh;
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
	public String getBctj() {
		return bctj;
	}
	public void setBctj(String bctj) {
		this.bctj = bctj;
	}
	public String getBzq() {
		return bzq;
	}
	public void setBzq(String bzq) {
		this.bzq = bzq;
	}
	public String getBzqflg() {
		return bzqflg;
	}
	public void setBzqflg(String bzqflg) {
		this.bzqflg = bzqflg;
	}
	public String getSfwxp() {
		return sfwxp;
	}
	public void setSfwxp(String sfwxp) {
		this.sfwxp = sfwxp;
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
	public String getJwlbm() {
		return jwlbm;
	}
	public void setJwlbm(String jwlbm) {
		this.jwlbm = jwlbm;
	}
	public String getLb() {
		return lb;
	}
	public void setLb(String lb) {
		this.lb = lb;
	}
	public String getAqkc() {
		return aqkc;
	}
	public void setAqkc(String aqkc) {
		this.aqkc = aqkc;
	}
	public String getQdl() {
		return qdl;
	}
	public void setQdl(String qdl) {
		this.qdl = qdl;
	}
	public String getWlzlbtc() {
		return wlzlbtc;
	}
	public void setWlzlbtc(String wlzlbtc) {
		this.wlzlbtc = wlzlbtc;
	}
	public String getWllbmc() {
		return wllbmc;
	}
	public void setWllbmc(String wllbmc) {
		this.wllbmc = wllbmc;
	}
	public String getWlzlbmc() {
		return wlzlbmc;
	}
	public void setWlzlbmc(String wlzlbmc) {
		this.wlzlbmc = wlzlbmc;
	}
	public String getWlzlbtcmc() {
		return wlzlbtcmc;
	}
	public void setWlzlbtcmc(String wlzlbtcmc) {
		this.wlzlbtcmc = wlzlbtcmc;
	}
	public List<String> getWlbms() {
		return wlbms;
	}
	public void setWlbms(List<String> wlbms) {
		this.wlbms = wlbms;
	}
	public String getCskz1() {
		return cskz1;
	}
	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}
}
