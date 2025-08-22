package com.matridx.igams.storehouse.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value = "HwllxxDto")
public class HwllxxDto extends HwllxxModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 货物领料明细
	private List<HwllmxDto> hwllmxDtos;
	// 物料编码(wlgl.wlbm)
	private String wlbm;
	// 物料名称(wlgl.wlmc)
	private String wlmc;
	// 规格(wlgl.gg)
	private String gg;
	// 计量单位(wlgl.jldw)
	private String jldw;
	// 申请人员(llgl.sqry)
	private String sqry;
	// 申请日期(llgl.sqrq)
	private String sqrq;
	// 申请部门(llgl.sqbm)
	private String sqbm;
	// 领料单号(llgl.lldh)
	private String lldh;
	// 追溯号(hwxx.zsh)
	private String zsh;
	// 报关单号(hwxx.bgdh)
	private String bgdh;
	// 质检人员(hwxx.zjry)
	private String zjry;
	// 分组(wlgl.wllb)
	private String fz;
	private String hwllxxbz;
	// 类别(根据wlgl.lb的基础数据扩展参数1,jcsj.csid)
	private String lb;
	//申请日期开始
	private String sqrqstart;
	//申请日期结束
	private String sqrqend;
	//领料日期开始（发料日期）
	private String flrqstart;
	//领料日期结束
	private String flrqend;
	//关联表标记
	private String hwllxx_flag;
	//导出关联标记为所选择的字段
	private String sqlParam;
	// 到货日期(dhxx.dhrq)
	private String dhrq;
	// 供货商(gysxx.gysmc)
	private String gysmc;
	// 全部(模糊搜索用)
	private String entire;
	//可请领数
	private String kqls;
	//庫存數
	private String kcl;
	// 分组 数组
	private String[] fzs;
	// 类别 数组
	private String[] lbs;
	// 审核状态 数组
	private String[] zts;
	//预定数
	private String qlyds;
	//仓库预定数
	private String yds;
	//仓库货物信息id
	private String ckhwid;
	//总请领数量
	private String zqlsl;
	//货物mx关联u8id
	private String llmxglid;
	//货物id
	private String hwid;
	//总实领数量
	private String zslsl;
	// 生产批号
	private String scph;
	// 领料明细id
	private String llmxid;
	// 项目编码
	private String xmbh;
	// 货位
	private String hw;
	//货物领料明细
	private String hwllmx_json;
	//生产商
	private String scs;
	//货号
	private String hh;
	//请领总数
	private String qlzs;
	//实领总数
	private String slzs;
	//仓库分类名称
	private String ckqxmc;
	//仓库分类
	private String ckqxlx;
	//状态
	private String zt;
	//出库状态
	private String ckzt;
	//取样标记
	private String qybj;
	//项目名称
	private String xmmc;
	//有效期
	private String yxq;
	//判断是否是复制操作
	private String copyflg;
	//需求计划id
	private String xqjhid;
	//需求计划明细id
	private String xqjhmxid;
	//项目大类名称
	private String xmdlmc;
	//项目编码名称
	private String xmbmmc;
	//领料人员名称
	private String llrymc;
	//发料人员名称
	private String flrymc;
	//物料质量类别名称
	private String lbmc;
	//物料类别名称
	private String wllbmc;
	//物料子类别名称
	private String wlzlbmc;
	//原厂货号
	private String ychh;
	//产品类型名称
	private String cplxmc;
	//去污方式
	private String qwfs;
	//物料质量类别参数扩展1
	private String lbcskz1;
	//仓库名称
	private String ckmc;
	//仓库id
	private String ckid;

	public String getCkid() {
		return ckid;
	}

	public void setCkid(String ckid) {
		this.ckid = ckid;
	}

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	public String getLbcskz1() {
		return lbcskz1;
	}

	public void setLbcskz1(String lbcskz1) {
		this.lbcskz1 = lbcskz1;
	}

	public String getQwfs() {
		return qwfs;
	}

	public void setQwfs(String qwfs) {
		this.qwfs = qwfs;
	}

	public String getCplxmc() {
		return cplxmc;
	}

	public void setCplxmc(String cplxmc) {
		this.cplxmc = cplxmc;
	}

	public String getYchh() {
		return ychh;
	}

	public void setYchh(String ychh) {
		this.ychh = ychh;
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

	public String getLbmc() {
		return lbmc;
	}

	public void setLbmc(String lbmc) {
		this.lbmc = lbmc;
	}

	public String getFlrymc() {
		return flrymc;
	}

	public String getLlrymc() {
		return llrymc;
	}

	public void setLlrymc(String llrymc) {
		this.llrymc = llrymc;
	}

	public void setFlrymc(String flrymc) {
		this.flrymc = flrymc;
	}

	public String getXmbmmc() {
		return xmbmmc;
	}

	public void setXmbmmc(String xmbmmc) {
		this.xmbmmc = xmbmmc;
	}

	public String getXmdlmc() {
		return xmdlmc;
	}

	public void setXmdlmc(String xmdlmc) {
		this.xmdlmc = xmdlmc;
	}

	public String getXqjhid() {
		return xqjhid;
	}

	public void setXqjhid(String xqjhid) {
		this.xqjhid = xqjhid;
	}

	public String getXqjhmxid() {
		return xqjhmxid;
	}

	public void setXqjhmxid(String xqjhmxid) {
		this.xqjhmxid = xqjhmxid;
	}

	public String getCopyflg() {
		return copyflg;
	}

	public void setCopyflg(String copyflg) {
		this.copyflg = copyflg;
	}

	public String getYxq() {
		return yxq;
	}

	public void setYxq(String yxq) {
		this.yxq = yxq;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public String getQybj() {
		return qybj;
	}

	public void setQybj(String qybj) {
		this.qybj = qybj;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getCkzt() {
		return ckzt;
	}

	public void setCkzt(String ckzt) {
		this.ckzt = ckzt;
	}

	public String getCkqxlx() {
		return ckqxlx;
	}

	public void setCkqxlx(String ckqxlx) {
		this.ckqxlx = ckqxlx;
	}

	public String getCkqxmc() {
		return ckqxmc;
	}

	public void setCkqxmc(String ckqxmc) {
		this.ckqxmc = ckqxmc;
	}

	public String getQlzs() {
		return qlzs;
	}

	public void setQlzs(String qlzs) {
		this.qlzs = qlzs;
	}

	public String getSlzs() {
		return slzs;
	}

	public void setSlzs(String slzs) {
		this.slzs = slzs;
	}

	public String getHwllxxbz() {
		return hwllxxbz;
	}

	public void setHwllxxbz(String hwllxxbz) {
		this.hwllxxbz = hwllxxbz;
	}

	public String getHwllmx_json() {
		return hwllmx_json;
	}
	public void setHwllmx_json(String hwllmx_json) {
		this.hwllmx_json = hwllmx_json;
	}
	public String getScs() {
		return scs;
	}
	public void setScs(String scs) {
		this.scs = scs;
	}
	public String getHh() {
		return hh;
	}
	public void setHh(String hh) {
		this.hh = hh;
	}

	public String getLlmxglid() {
		return llmxglid;
	}

	public void setLlmxglid(String llmxglid) {
		this.llmxglid = llmxglid;
	}

	public String getLlmxid() {
		return llmxid;
	}

	public void setLlmxid(String llmxid) {
		this.llmxid = llmxid;
	}

	public String getHw() {
		return hw;
	}
	public void setHw(String hw) {
		this.hw = hw;
	}
	public String getXmbh() {
		return xmbh;
	}
	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}
	public String getScph() {
		return scph;
	}
	public void setScph(String scph) {
		this.scph = scph;
	}
	public String getZslsl() {
		return zslsl;
	}
	public void setZslsl(String zslsl) {
		this.zslsl = zslsl;
	}
	public String getHwid() {
		return hwid;
	}
	public void setHwid(String hwid) {
		this.hwid = hwid;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getZqlsl() {
		return zqlsl;
	}
	public void setZqlsl(String zqlsl) {
		this.zqlsl = zqlsl;
	}
	public String getCkhwid() {
		return ckhwid;
	}
	public void setCkhwid(String ckhwid) {
		this.ckhwid = ckhwid;
	}
	public String getYds() {
		return yds;
	}
	public void setYds(String yds) {
		this.yds = yds;
	}
	public List<HwllmxDto> getHwllmxDtos() {
		return hwllmxDtos;
	}
	public void setHwllmxDtos(List<HwllmxDto> hwllmxDtos) {
		this.hwllmxDtos = hwllmxDtos;
	}
	public String getQlyds() {
		return qlyds;
	}
	public void setQlyds(String qlyds) {
		this.qlyds = qlyds;
	}
	public String getKcl() {
		return kcl;
	}
	public void setKcl(String kcl) {
		this.kcl = kcl;
	}
	public String getKqls() {
		return kqls;
	}
	public void setKqls(String kqls) {
		this.kqls = kqls;
	}

	public String[] getFzs() {
		return fzs;
	}
	public void setFzs(String[] fzs) {
		this.fzs = fzs;
		for (int i = 0; i < fzs.length; i++){
			this.fzs[i]=this.fzs[i].replace("'","");
		}
	}
	public String[] getLbs() {
		return lbs;
	}
	public void setLbs(String[] lbs) {
		this.lbs = lbs;
		for (int i = 0; i < lbs.length; i++){
			this.lbs[i]=this.lbs[i].replace("'","");
		}
	}
	public String[] getZts() {
		return zts;
	}
	public void setZts(String[] zts) {
		this.zts = zts;
		for (int i = 0; i < zts.length; i++){
			this.zts[i]=this.zts[i].replace("'","");
		}
	}
	public String getEntire() {
		return entire;
	}
	public void setEntire(String entire) {
		this.entire = entire;
	}
	public String getSqbm() {
		return sqbm;
	}
	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getGysmc() {
		return gysmc;
	}
	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}
	public String getDhrq() {
		return dhrq;
	}
	public void setDhrq(String dhrq) {
		this.dhrq = dhrq;
	}
	public String getSqlParam() {
		return sqlParam;
	}
	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}
	public String getHwllxx_flag() {
		return hwllxx_flag;
	}
	public void setHwllxx_flag(String hwllxx_flag) {
		this.hwllxx_flag = hwllxx_flag;
	}
	public String getSqrqstart() {
		return sqrqstart;
	}
	public void setSqrqstart(String sqrqstart) {
		this.sqrqstart = sqrqstart;
	}
	public String getSqrqend() {
		return sqrqend;
	}
	public void setSqrqend(String sqrqend) {
		this.sqrqend = sqrqend;
	}
	public String getFlrqstart() {
		return flrqstart;
	}
	public void setFlrqstart(String flrqstart) {
		this.flrqstart = flrqstart;
	}
	public String getFlrqend() {
		return flrqend;
	}
	public void setFlrqend(String flrqend) {
		this.flrqend = flrqend;
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
	public String getSqry() {
		return sqry;
	}
	public void setSqry(String sqry) {
		this.sqry = sqry;
	}
	public String getSqrq() {
		return sqrq;
	}
	public void setSqrq(String sqrq) {
		this.sqrq = sqrq;
	}
	public String getLldh() {
		return lldh;
	}
	public void setLldh(String lldh) {
		this.lldh = lldh;
	}
	public String getZsh() {
		return zsh;
	}
	public void setZsh(String zsh) {
		this.zsh = zsh;
	}
	public String getBgdh() {
		return bgdh;
	}
	public void setBgdh(String bgdh) {
		this.bgdh = bgdh;
	}
	public String getZjry() {
		return zjry;
	}
	public void setZjry(String zjry) {
		this.zjry = zjry;
	}
	public String getFz() {
		return fz;
	}
	public void setFz(String fz) {
		this.fz = fz;
	}
	public String getLb() {
		return lb;
	}
	public void setLb(String lb) {
		this.lb = lb;
	}
	

	
}
