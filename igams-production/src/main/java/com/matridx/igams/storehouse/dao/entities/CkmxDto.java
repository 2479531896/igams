package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="CkmxDto")
public class CkmxDto extends CkmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//库位编号参数代码
	private String kwbhcsdm;
	//物料编码
	private String wlbm;
	//生产批号
	private String scph;
	//失效日期
	private String yxq;
	//项目大类代码
	private String xmdlcsdm;
	//仓库权限类型
	private String ckqxlx;
	//项目编码参数代码
	private String xmbmcsdm;
	//项目编码名称
	private String xmbmcsmc;
	//项目大类参数名称
	private String xmdlcsmc;
	//生产日期
	private String scrq;
	//保质期标记
	private String bzqflg;
	//保质期
	private String bzq;
	//领料单关联id
	private String glid;
	//库存关联id
	private String kcglid;
	//物料ID
	private String wlid;
	//物料类别
	private String wllb;
	//物料子类别
	private String wlzlb;
	//物料类别
	private String wllbmc;
	//物料子类别
	private String wlzlbmc;
	//物料名称
	private String wlmc;
	//生产商
	private String scs;
	//原厂货号
	private String ychh;
	//规格
	private String gg;
	//货物领料备注
	private String hwllbz;
	//备注
	private String bz;
	//u8借出单号
	private String u8jcid;
	//u8借出明细单号
	private String u8jcmxid;
	//总出库数量
	private String zcksl;
	//仓库代码
	private String ckdm;
	//货位
	private String hw;
	//计量单位
	private String jldw;
	//引用单号
	private String yydh;
	//计量单位
	private String kcksl;
	//货位信息List
	private List<HwxxDto> hwxxDtoList;
	//仓库ids
	private List<String> ckids;
	//库位编号
	private String kwbh;
	//预定数标记
	private String ydsbj;
	//红冲数标记
	private String hcsbj;
//仓库名称
	private String ckmc;
	//追溯号
	private String zsh;
	//保存条件
	private String bctj;
	//客户名称
	private String khmc;
	//出库日期
	private String ckrq;
	//货物的仓库id
	private String hwckid;

	public String getHwckid() {
		return hwckid;
	}

	public void setHwckid(String hwckid) {
		this.hwckid = hwckid;
	}

	public String getCkrq() {
		return ckrq;
	}

	public void setCkrq(String ckrq) {
		this.ckrq = ckrq;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getBctj() {
		return bctj;
	}

	public void setBctj(String bctj) {
		this.bctj = bctj;
	}

	public String getZsh() {
		return zsh;
	}

	public void setZsh(String zsh) {
		this.zsh = zsh;
	}

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	//u8出库单号
	private String u8ckdh;
	//供应商代码
	private String gysdm;
	//请购记录编号
	private String qgjlbh;
	//合同内部编号
	private String htnbbh;
	//数量
	private String sl;
	private String u8ommxid;
	private String u8omid;
	//合同备注
	private String htbz;
	private String ckzsl;
    //合同物料编码
	private String htwlbm;
	//出库审核状态
	private String ckzt;
	//出库类别参数代码
	private String cklbcsdm;
	//出库类别参数名称
	private String cklbcsmc;
	//出库单号
	private String zckdh;
	//出库明细出库数量
	private String ckmxcksl;
	//合同明细id
	private String htmxid;
	//请购明细id
	private String qgmxid;
	//产品注册号
	private String cpzch;

	public String getCpzch() {
		return cpzch;
	}

	public void setCpzch(String cpzch) {
		this.cpzch = cpzch;
	}

	public String getHtmxid() {
		return htmxid;
	}

	public void setHtmxid(String htmxid) {
		this.htmxid = htmxid;
	}

	public String getQgmxid() {
		return qgmxid;
	}

	public void setQgmxid(String qgmxid) {
		this.qgmxid = qgmxid;
	}

	public String getCkmxcksl() {
		return ckmxcksl;
	}

	public void setCkmxcksl(String ckmxcksl) {
		this.ckmxcksl = ckmxcksl;
	}

	public String getCklbcsmc() {
		return cklbcsmc;
	}

	public void setCklbcsmc(String cklbcsmc) {
		this.cklbcsmc = cklbcsmc;
	}

	public String getZckdh() {
		return zckdh;
	}

	public void setZckdh(String zckdh) {
		this.zckdh = zckdh;
	}

	public String getCklbcsdm() {
		return cklbcsdm;
	}

	public void setCklbcsdm(String cklbcsdm) {
		this.cklbcsdm = cklbcsdm;
	}

	public String getCkzt() {
		return ckzt;
	}

	public void setCkzt(String ckzt) {
		this.ckzt = ckzt;
	}

	public String getHtwlbm() {
		return htwlbm;
	}

	public void setHtwlbm(String htwlbm) {
		this.htwlbm = htwlbm;
	}

	public String getCkzsl() {
		return ckzsl;
	}

	public void setCkzsl(String ckzsl) {
		this.ckzsl = ckzsl;
	}


	public String getHtbz() {
		return htbz;
	}

	public void setHtbz(String htbz) {
		this.htbz = htbz;
	}

	public String getU8omid() {
		return u8omid;
	}

	public void setU8omid(String u8omid) {
		this.u8omid = u8omid;
	}

	public String getU8ommxid() {
		return u8ommxid;
	}

	public void setU8ommxid(String u8ommxid) {
		this.u8ommxid = u8ommxid;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getHtnbbh() {
		return htnbbh;
	}

	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}

	public String getQgjlbh() {
		return qgjlbh;
	}

	public void setQgjlbh(String qgjlbh) {
		this.qgjlbh = qgjlbh;
	}

	public String getGysdm() {
		return gysdm;
	}

	public void setGysdm(String gysdm) {
		this.gysdm = gysdm;
	}

	public String getU8ckdh() {
		return u8ckdh;
	}

	public void setU8ckdh(String u8ckdh) {
		this.u8ckdh = u8ckdh;
	}

	public String getYydh() {
		return yydh;
	}

	public void setYydh(String yydh) {
		this.yydh = yydh;
	}

	public String getYdsbj() {
		return ydsbj;
	}

	public void setYdsbj(String ydsbj) {
		this.ydsbj = ydsbj;
	}

	public String getHcsbj() {
		return hcsbj;
	}

	public void setHcsbj(String hcsbj) {
		this.hcsbj = hcsbj;
	}

	public List<String> getCkids() {
		return ckids;
	}

	public void setCkids(List<String> ckids) {
		this.ckids = ckids;
	}

	public String getCkqxlx() {
		return ckqxlx;
	}

	public void setCkqxlx(String ckqxlx) {
		this.ckqxlx = ckqxlx;
	}

	public String getKwbh() {
		return kwbh;
	}

	public void setKwbh(String kwbh) {
		this.kwbh = kwbh;
	}

	public List<HwxxDto> getHwxxDtoList() {
		return hwxxDtoList;
	}

	public void setHwxxDtoList(List<HwxxDto> hwxxDtoList) {
		this.hwxxDtoList = hwxxDtoList;
	}

	public String getKcksl() {
		return kcksl;
	}

	public void setKcksl(String kcksl) {
		this.kcksl = kcksl;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public String getHw() {
		return hw;
	}

	public void setHw(String hw) {
		this.hw = hw;
	}

	public String getCkdm() {
		return ckdm;
	}

	public void setCkdm(String ckdm) {
		this.ckdm = ckdm;
	}

	public String getU8jcid() {
		return u8jcid;
	}

	public void setU8jcid(String u8jcid) {
		this.u8jcid = u8jcid;
	}

	public String getU8jcmxid() {
		return u8jcmxid;
	}

	public void setU8jcmxid(String u8jcmxid) {
		this.u8jcmxid = u8jcmxid;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
	
	
	public String getZcksl() {
		return zcksl;
	}

	public void setZcksl(String zcksl) {
		this.zcksl = zcksl;
	}

	public String getHwllbz() {
		return hwllbz;
	}

	public void setHwllbz(String hwllbz) {
		this.hwllbz = hwllbz;
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

	public String getKcglid() {
		return kcglid;
	}
	public void setKcglid(String kcglid) {
		this.kcglid = kcglid;
	}
	public String getKwbhcsdm() {
		return kwbhcsdm;
	}
	public void setKwbhcsdm(String kwbhcsdm) {
		this.kwbhcsdm = kwbhcsdm;
	}
	public String getWlbm() {
		return wlbm;
	}
	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
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
	public String getXmdlcsdm() {
		return xmdlcsdm;
	}
	public void setXmdlcsdm(String xmdlcsdm) {
		this.xmdlcsdm = xmdlcsdm;
	}
	public String getXmbmcsdm() {
		return xmbmcsdm;
	}
	public void setXmbmcsdm(String xmbmcsdm) {
		this.xmbmcsdm = xmbmcsdm;
	}
	public String getXmbmcsmc() {
		return xmbmcsmc;
	}
	public void setXmbmcsmc(String xmbmcsmc) {
		this.xmbmcsmc = xmbmcsmc;
	}
	public String getXmdlcsmc() {
		return xmdlcsmc;
	}
	public void setXmdlcsmc(String xmdlcsmc) {
		this.xmdlcsmc = xmdlcsmc;
	}
	public String getScrq() {
		return scrq;
	}
	public void setScrq(String scrq) {
		this.scrq = scrq;
	}
	public String getBzqflg() {
		return bzqflg;
	}
	public void setBzqflg(String bzqflg) {
		this.bzqflg = bzqflg;
	}
	public String getBzq() {
		return bzq;
	}
	public void setBzq(String bzq) {
		this.bzq = bzq;
	}
	public String getGlid() {
		return glid;
	}
	public void setGlid(String glid) {
		this.glid = glid;
	}
	
	
}
