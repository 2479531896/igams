package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="HwllmxDto")
public class HwllmxDto extends HwllmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//前实领数量
	private String qyxsl;
	//货物领料实领数量(货物领料明细实领数量总和)
	private String llyxsl;
	//货物总预定数
	private String zyds;
	//保质期
	private String bzq;
	//保质期标记
	private String bzqflg;
	//明细实领数量
	private String mxyxsl;
	//项目大类参数代码
	private String xmdlcsdm;
	//项目大类参数名称
	private String xmdlcsmc;
	//项目编码参数代码
	private String xmbmcsdm;
	//项目编码参数名称
	private String xmbmcsmc;
	//有效期
	private String yxq;
	//生产日期
	private String scrq;
	//仓库代码
	private String ckdm;
	//物料名称
	private String wlmc;
	//物料编码
	private String wlbm;
	//货物领料备注
	private String hwllbz;
	//追溯号
	private String zsh;
	//货物总允许领取数
	private String zyxsl;
	//货物总实领数量
	private String zslsl;
	//仓库货物id
	private String ckhwid;
	//物料id
	private String wlid;
	//货物领料信息请领数量
	private String hwllqlsl;
	//仓库预定数
	private String ckyds;
	//仓库库存量
	private String ckkcl;
	//出库状态
	private String ckzt;
	//取样领料标记
	private String qybj;
	//无税单价
	private String wsdj;
	//合计金额
	private String hjje;
	//税率
	private String suil;
	//库位编号
	private String kwbh;
	//申请关联id
	private String sqglid;
	//库存关联id
	private String kcglid;
	// 到货单号
	private String dhdh;
	// 规格
	private String gg;
	// 计量单位
	private String jldw;
	// 原厂货号
	private String ychh;
	// 实际数量
	private String sjsl;
	// 入库日期
	private String rkrq;
	// 数量
	private String sl;
	//库位仓库代码
	private String kwbhcsdm;
	//生产批号
	private String scph;
	//含税单价
	private String hsdj;
	//仓库名称
	private String ckmc;

	// 库位编号名称
	private String kwmc;
	//加减标记，标记为0，减，标记为1，加
	private String jjbj;
	//前请领数
	private String qqls;
	//货物id
	private String hwid;
	//出库id
	private String ckid;
	//仓库
	private String ck;
	//备注
	private String bz;
	//状态
	private String zt;
	//到货类型代码
	private String dhlxdm;
	//出库单号
	private String ckdh;
	//留样库存id
	private String lykcid;
	//库存量
	private String kcl;

	
	//出库日期
	private String ckrq;
	//u8出库单号
	private String u8ckdh;
	//出库数量
	private String cksl;
	//领料人名称
	private String llrmc;
	//发料人名称
	private String flrmc;
	//请购明细备注
	private String qgmxbz;
	//物料质量类别代码
	private String lbdm;
	//物料质量类别参数拓展1
	private String lbcskz1;
	//项目大类
	private String xmdl;
	//项目编码
	private String xmbm;
	//仓库类型
	private String ckqxmc;
	//领料申请人
	private String sqry;
	//设备验收id
	private String sbysid;
	private String khmc;
	private String khid;
	//设备状态
	private String sbzt;
	//退库标记
	private String tkbj;
	//领料明细ids
	List<String> llmxids;
	//物料质量类别名称
	private String lbcsmc;

	public String getLbcsmc() {
		return lbcsmc;
	}

	public void setLbcsmc(String lbcsmc) {
		this.lbcsmc = lbcsmc;
	}

	public List<String> getLlmxids() {
		return llmxids;
	}

	public void setLlmxids(List<String> llmxids) {
		this.llmxids = llmxids;
	}

	public String getTkbj() {
		return tkbj;
	}

	public void setTkbj(String tkbj) {
		this.tkbj = tkbj;
	}
	public String getSbzt() {
		return sbzt;
	}

	public void setSbzt(String sbzt) {
		this.sbzt = sbzt;
	}

	public String getKhid() {
		return khid;
	}

	public void setKhid(String khid) {
		this.khid = khid;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getSbysid() {
		return sbysid;
	}

	public void setSbysid(String sbysid) {
		this.sbysid = sbysid;
	}

	public String getSqry() {
		return sqry;
	}

	public void setSqry(String sqry) {
		this.sqry = sqry;
	}

	public String getCkqxmc() {
		return ckqxmc;
	}

	public void setCkqxmc(String ckqxmc) {
		this.ckqxmc = ckqxmc;
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

	public String getLbcskz1() {
		return lbcskz1;
	}

	public void setLbcskz1(String lbcskz1) {
		this.lbcskz1 = lbcskz1;
	}

	public String getLbdm() {
		return lbdm;
	}

	public void setLbdm(String lbdm) {
		this.lbdm = lbdm;
	}

	public String getQgmxbz() {
		return qgmxbz;
	}

	public void setQgmxbz(String qgmxbz) {
		this.qgmxbz = qgmxbz;
	}
	

	public String getKcl() {
		return kcl;
	}

	public void setKcl(String kcl) {
		this.kcl = kcl;
	}
	public String getCkdh() {
		return ckdh;
	}
	public String getLykcid() {
		return lykcid;
	}

	public void setLykcid(String lykcid) {
		this.lykcid = lykcid;
	}


	public String getLlrmc() {
		return llrmc;
	}

	public void setLlrmc(String llrmc) {
		this.llrmc = llrmc;
	}

	public String getFlrmc() {
		return flrmc;
	}

	public void setFlrmc(String flrmc) {
		this.flrmc = flrmc;
	}

	public String getCksl() {
		return cksl;
	}

	public void setCksl(String cksl) {
		this.cksl = cksl;
	}

	public String getU8ckdh() {
		return u8ckdh;
	}

	public void setU8ckdh(String u8ckdh) {
		this.u8ckdh = u8ckdh;
	}

	public String getCkrq() {
		return ckrq;
	}

	public void setCkrq(String ckrq) {
		this.ckrq = ckrq;
	}

	public void setCkdh(String ckdh) {
		this.ckdh = ckdh;
	}
	public String getDhlxdm() {
		return dhlxdm;
	}

	public void setDhlxdm(String dhlxdm) {
		this.dhlxdm = dhlxdm;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getCk() {
		return ck;
	}

	public void setCk(String ck) {
		this.ck = ck;
	}

	public String getCkid() {
		return ckid;
	}

	public void setCkid(String ckid) {
		this.ckid = ckid;
	}

	public String getHwid() {
		return hwid;
	}

	public void setHwid(String hwid) {
		this.hwid = hwid;
	}

	public String getQqls() {
		return qqls;
	}

	public void setQqls(String qqls) {
		this.qqls = qqls;
	}

	public String getJjbj() {
		return jjbj;
	}

	public void setJjbj(String jjbj) {
		this.jjbj = jjbj;
	}

	public String getKwmc() {
		return kwmc;
	}

	public void setKwmc(String kwmc) {
		this.kwmc = kwmc;
	}


	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	public String getHsdj() {
		return hsdj;
	}

	public void setHsdj(String hsdj) {
		this.hsdj = hsdj;
	}

	public String getScph() {
		return scph;
	}
	public void setScph(String scph) {
		this.scph = scph;
	}
	public String getKwbhcsdm() {
		return kwbhcsdm;
	}
	public void setKwbhcsdm(String kwbhcsdm) {
		this.kwbhcsdm = kwbhcsdm;
	}
	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
	}
	public String getSjsl() {
		return sjsl;
	}
	public void setSjsl(String sjsl) {
		this.sjsl = sjsl;
	}
	public String getRkrq() {
		return rkrq;
	}
	public void setRkrq(String rkrq) {
		this.rkrq = rkrq;
	}
	public String getYchh() {
		return ychh;
	}
	public void setYchh(String ychh) {
		this.ychh = ychh;
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
	public String getDhdh() {
		return dhdh;
	}
	public void setDhdh(String dhdh) {
		this.dhdh = dhdh;
	}
	public String getKcglid() {
		return kcglid;
	}
	public void setKcglid(String kcglid) {
		this.kcglid = kcglid;
	}
	public String getSqglid() {
		return sqglid;
	}
	public void setSqglid(String sqglid) {
		this.sqglid = sqglid;
	}
	public String getKwbh() {
		return kwbh;
	}
	public void setKwbh(String kwbh) {
		this.kwbh = kwbh;
	}
	public String getWsdj() {
		return wsdj;
	}
	public void setWsdj(String wsdj) {
		this.wsdj = wsdj;
	}
	public String getHjje() {
		return hjje;
	}
	public void setHjje(String hjje) {
		this.hjje = hjje;
	}
	public String getSuil() {
		return suil;
	}
	public void setSuil(String suil) {
		this.suil = suil;
	}
	public String getCkzt() {
		return ckzt;
	}
	public void setCkzt(String ckzt) {
		this.ckzt = ckzt;
	}
	public String getQybj() {
		return qybj;
	}
	public void setQybj(String qybj) {
		this.qybj = qybj;
	}
	public String getCkyds() {
		return ckyds;
	}
	public void setCkyds(String ckyds) {
		this.ckyds = ckyds;
	}
	public String getCkkcl() {
		return ckkcl;
	}
	public void setCkkcl(String ckkcl) {
		this.ckkcl = ckkcl;
	}
	public String getCkhwid() {
		return ckhwid;
	}
	public void setCkhwid(String ckhwid) {
		this.ckhwid = ckhwid;
	}
	public String getWlid() {
		return wlid;
	}
	public void setWlid(String wlid) {
		this.wlid = wlid;
	}
	public String getHwllqlsl() {
		return hwllqlsl;
	}
	public void setHwllqlsl(String hwllqlsl) {
		this.hwllqlsl = hwllqlsl;
	}
	public String getZslsl() {
		return zslsl;
	}
	public void setZslsl(String zslsl) {
		this.zslsl = zslsl;
	}
	public String getZyxsl() {
		return zyxsl;
	}
	public void setZyxsl(String zyxsl) {
		this.zyxsl = zyxsl;
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
	public String getXmdlcsdm() {
		return xmdlcsdm;
	}
	public void setXmdlcsdm(String xmdlcsdm) {
		this.xmdlcsdm = xmdlcsdm;
	}
	public String getXmdlcsmc() {
		return xmdlcsmc;
	}
	public void setXmdlcsmc(String xmdlcsmc) {
		this.xmdlcsmc = xmdlcsmc;
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
	public String getCkdm() {
		return ckdm;
	}
	public void setCkdm(String ckdm) {
		this.ckdm = ckdm;
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
	public String getHwllbz() {
		return hwllbz;
	}
	public void setHwllbz(String hwllbz) {
		this.hwllbz = hwllbz;
	}
	public String getZsh() {
		return zsh;
	}
	public void setZsh(String zsh) {
		this.zsh = zsh;
	}
	public String getZyds() {
		return zyds;
	}
	public void setZyds(String zyds) {
		this.zyds = zyds;
	}
	public String getQyxsl() {
		return qyxsl;
	}
	public void setQyxsl(String qyxsl) {
		this.qyxsl = qyxsl;
	}
	public String getLlyxsl() {
		return llyxsl;
	}
	public void setLlyxsl(String llyxsl) {
		this.llyxsl = llyxsl;
	}
	public String getMxyxsl() {
		return mxyxsl;
	}
	public void setMxyxsl(String mxyxsl) {
		this.mxyxsl = mxyxsl;
	}
	
	
}
