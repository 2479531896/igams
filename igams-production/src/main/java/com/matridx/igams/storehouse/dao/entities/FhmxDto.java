package com.matridx.igams.storehouse.dao.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.type.Alias;

@Alias(value="FhmxDto")
public class FhmxDto extends FhmxModel{
	//物料编码
	private String wlbm;
	//物料类别
	private String wllb;
	//销售明细关联id
	private String mxglid;
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
	//物料id
	private String wlid;
	//可退货数量
	private String kthsl;
	//复数明细ID
	private List<String> mxids;
	//u8销售订单
	private String u8xsdh;
	//物料单位
	private String jldw;
	//发货单
	private String fhdh;
	//仓库名称
	private String ckmc;
	//货位名称
	private String hwmc;
	//有效期
	private String yxq;
	//生产日期
	private String scrq;
	//客户代码
	private String khdm;
	//生产批号
	private String scph;
	//质量类别名称
	private String zllbmc;
	//仓库代码
	private String ckdm;
	//库位仓库代码
	private String kwckdm;
	//库位名称
	private String kwmc;
	//保质期标记
	private String bzqflg;
	//保质期
	private String bzq;
	//库存关联id
	private String kcglid;
	//出库id
	private String ckid;
	//出库明细关联id
	private String ckmxglid;
	//销售订单
	private String xsid;
	//销售明细id
	private String xsmxid;
	//物料编码
	private String cInvCode;
	//客户id
	private String kh;
	//客户名称
	private String khmc;
	//客户简称
	private String khjc;
	//仓库权限类型
	private String ckqxlx;
	//仓库权限类型
	private String hl;
	//币种
	private String biz;
	//主表税率
	private String zsuil;
	//总发货数量
	private String zfhsl;
	//销售关联id
	private String glid;
	//销售部门
	private String xsbm;
	//销售部门名称
	private String xsbmmc;
	//退货数量标记
	private String thslbj;
	//经手人
	private String jsr;
	//订单号
	private String ddh;
	//收货地址
	private String shdz;
	//库位
	private String kw;
	//追溯号
	private String zsh;
	//发货数量标记
	private String fhslbj;
//状态
	private String zt;
	private String djrq;
	//发货id
	private String fhid;
	//到款金额
	private String dkje;
	//总金额
	private String zje;
	//是否新增到款记录
	private String dkjlbj;

	public String getDkjlbj() {
		return dkjlbj;
	}

	public void setDkjlbj(String dkjlbj) {
		this.dkjlbj = dkjlbj;
	}

	public String getZje() {
		return zje;
	}

	public void setZje(String zje) {
		this.zje = zje;
	}

	@Override
	public String getFhid() {
		return fhid;
	}

	@Override
	public void setFhid(String fhid) {
		this.fhid = fhid;
	}

	public String getDkje() {
		return dkje;
	}

	public void setDkje(String dkje) {
		this.dkje = dkje;
	}

	public String getDjrq() {
		return djrq;
	}

	public void setDjrq(String djrq) {
		this.djrq = djrq;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}
	public String getFhslbj() {
		return fhslbj;
	}

	public void setFhslbj(String fhslbj) {
		this.fhslbj = fhslbj;
	}

	public String getZsh() {
		return zsh;
	}

	public void setZsh(String zsh) {
		this.zsh = zsh;
	}

	public String getKw() {
		return kw;
	}

	public void setKw(String kw) {
		this.kw = kw;
	}

	public String getJsr() {
		return jsr;
	}

	public void setJsr(String jsr) {
		this.jsr = jsr;
	}

	public String getDdh() {
		return ddh;
	}

	public void setDdh(String ddh) {
		this.ddh = ddh;
	}

	public String getShdz() {
		return shdz;
	}

	public void setShdz(String shdz) {
		this.shdz = shdz;
	}

	public String getThslbj() {
		return thslbj;
	}

	public void setThslbj(String thslbj) {
		this.thslbj = thslbj;
	}

	public String getXsbm() {
		return xsbm;
	}

	public void setXsbm(String xsbm) {
		this.xsbm = xsbm;
	}

	public String getXsbmmc() {
		return xsbmmc;
	}

	public void setXsbmmc(String xsbmmc) {
		this.xsbmmc = xsbmmc;
	}



	public String getMxglid() {
		return mxglid;
	}

	public void setMxglid(String mxglid) {
		this.mxglid = mxglid;
	}

	public String getGlid() {
		return glid;
	}

	public void setGlid(String glid) {
		this.glid = glid;
	}

	public String getU8xsdh() {
		return u8xsdh;
	}

	public void setU8xsdh(String u8xsdh) {
		this.u8xsdh = u8xsdh;
	}

	public String getZfhsl() {
		return zfhsl;
	}

	public void setZfhsl(String zfhsl) {
		this.zfhsl = zfhsl;
	}

	public String getHl() {
		return hl;
	}

	public void setHl(String hl) {
		this.hl = hl;
	}

	public String getBiz() {
		return biz;
	}

	public void setBiz(String biz) {
		this.biz = biz;
	}

	public String getZsuil() {
		return zsuil;
	}

	public void setZsuil(String zsuil) {
		this.zsuil = zsuil;
	}

	public String getCkqxlx() {
		return ckqxlx;
	}

	public void setCkqxlx(String ckqxlx) {
		this.ckqxlx = ckqxlx;
	}

	public String getKhdm() {
		return khdm;
	}

	public void setKhdm(String khdm) {
		this.khdm = khdm;
	}

	public String getKhjc() {
		return khjc;
	}

	public void setKhjc(String khjc) {
		this.khjc = khjc;
	}

	public String getKh() {
		return kh;
	}

	public void setKh(String kh) {
		this.kh = kh;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}
	public String getcInvCode() {
		return cInvCode;
	}

	public void setcInvCode(String cInvCode) {
		this.cInvCode = cInvCode;
	}

	public String getXsid() {
		return xsid;
	}

	public void setXsid(String xsid) {
		this.xsid = xsid;
	}

	public String getXsmxid() {
		return xsmxid;
	}

	public void setXsmxid(String xsmxid) {
		this.xsmxid = xsmxid;
	}


	public String getCkmxglid() {
		return ckmxglid;
	}

	public void setCkmxglid(String ckmxglid) {
		this.ckmxglid = ckmxglid;
	}
	public String getCkid() {
		return ckid;
	}
	public void setCkid(String ckid) {
		this.ckid = ckid;
	}
	public String getKcglid() {
		return kcglid;
	}
	public void setKcglid(String kcglid) {
		this.kcglid = kcglid;
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
	public String getKwckdm() {
		return kwckdm;
	}
	public void setKwckdm(String kwckdm) {
		this.kwckdm = kwckdm;
	}
	public String getCkdm() {
		return ckdm;
	}
	public void setCkdm(String ckdm) {
		this.ckdm = ckdm;
	}

	public String getZllbmc() {
		return zllbmc;
	}
	public void setZllbmc(String zllbmc) {
		this.zllbmc = zllbmc;
	}
	public String getJldw() {
		return jldw;
	}
	public void setJldw(String jldw) {
		this.jldw = jldw;
	}
	public String getFhdh() {
		return fhdh;
	}
	public void setFhdh(String fhdh) {
		this.fhdh = fhdh;
	}
	public String getCkmc() {
		return ckmc;
	}
	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}
	public String getHwmc() {
		return hwmc;
	}
	public void setHwmc(String hwmc) {
		this.hwmc = hwmc;
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
	public String getScph() {
		return scph;
	}
	public void setScph(String scph) {
		this.scph = scph;
	}
	public List<String> getMxids() {
		return mxids;
	}
	public void setMxids(String mxids) {
		List<String> list;
		String[] str = mxids.split(",");
		list = Arrays.asList(str);
		this.mxids = list;
	}

	public String getKwmc() {
		return kwmc;
	}

	public void setKwmc(String kwmc) {
		this.kwmc = kwmc;
	}

	public void setMxids(List<String> mxids) {
		if(!CollectionUtils.isEmpty(mxids)){
            mxids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.mxids = mxids;
	}
	public String getKthsl() {
		return kthsl;
	}

	public void setKthsl(String kthsl) {
		this.kthsl = kthsl;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
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

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
