package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="FpmxDto")
public class FpmxDto extends FpmxModel{

	//物料编码
	private String wlbm;
	//物料名称
	private String wlmc;
	//规格
	private String gg;
	//合同单号
	private String htdh;
	//入库单号
	private String rkdh;
	//到货数量
	private String dhsl;
	//到货ID
	private String dhid;
	//到货单号
	private String dhdh;
	//税率
	private String suil;
	//数量
	private String sl;
	//合计金额
	private String hjje;
	//合同ID
	private String htid;
	//合同明细ID
	private String htmxid;
	private String htnbbh;
	//含税单价
	private String hsdj;
	//无税单价
	private String wsdj;
	//明细数量
	private String mxsl;
	//物料ID
	private String wlid;
	//请购ID
	private String qgid;
	//请购单号
	private String qgdh;
	//入库ID
	private String rkid;
	//U8入库单号
	private String u8rkdh;
	//计量单位
	private String jldw;
	//生产批号
	private String scph;
	//生产日期
	private String scrq;
	//有效期
	private String yxq;
	private String lbcskz1;
	//开票金额
	private String kpje;
	//发票号
	private String fph;
	//发票种类名称
	private String fpzlmc;
	//开票日期
	private String kprq;
	//合同付款ID
	private String htfkid;
	private List<FpmxDto> fpmxDtos;
	private String sqlParam;
	//供应商名称
	private String gysmc;
	//业务员名称
	private String ywymc;
	//发票数量
	private String fpsl;
	//发票金额
	private String fpje;
	//币种名称
	private String bizmc;
	//汇率
	private String hl;
	//单据号
	private String djh;
	//状态多
	private String[] zts;
	//发票种类多
	private String[] fpzls;
	//开票日期
	private String kprqstart;
	//开票日期
	private String kprqend;
	//计划到货日期
	private String jhdhrq;
	//明细备注
	private String bz   ;
	//订单日期
	private String ddrq;
	//合同总金额
	private String zje;
	//已付金额
	private String yfje;
	//合同发票金额
	private String htfpje;
	//创建日期;
	private String cjrq;
	//提交日期
	private String tjrq;
	//合同外发日期
	private String htwfrq;
	//合同税率
	private String htsl;
	//合同备注
	private String htbz;
	//付款方式
	private String fkfsmc;
	//发票方式
	private String fpfsmc;
	//付款方
	private String fkfmc;
	//交货日期
	private String jhrq;
	//付款中金额
	private String fkzje;
	//总额
	private String sum;
	//合同外部编号
	private String htwbbh;
	//未维护数量
	private String wwhsl;
	//请购类别代码
	private String qglbdm;
	private String hwmc;
	private String hwbz;
	private String qgmxid;
	//发票类型名称
	private String fplxmc;
	private String bmmc;
	private String sbysid;
	private String gdzcbh;
	private String sbysdh;

	public String getSbysdh() {
		return sbysdh;
	}

	public void setSbysdh(String sbysdh) {
		this.sbysdh = sbysdh;
	}

	public String getSbysid() {
		return sbysid;
	}

	public void setSbysid(String sbysid) {
		this.sbysid = sbysid;
	}

	public String getGdzcbh() {
		return gdzcbh;
	}

	public void setGdzcbh(String gdzcbh) {
		this.gdzcbh = gdzcbh;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getFplxmc() {
		return fplxmc;
	}

	public void setFplxmc(String fplxmc) {
		this.fplxmc = fplxmc;
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

	public String getQgmxid() {
		return qgmxid;
	}

	public void setQgmxid(String qgmxid) {
		this.qgmxid = qgmxid;
	}

	public String getQglbdm() {
		return qglbdm;
	}

	public void setQglbdm(String qglbdm) {
		this.qglbdm = qglbdm;
	}

	public String getWwhsl() {
		return wwhsl;
	}

	public void setWwhsl(String wwhsl) {
		this.wwhsl = wwhsl;
	}

	public String getHtwbbh() {
		return htwbbh;
	}

	public void setHtwbbh(String htwbbh) {
		this.htwbbh = htwbbh;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getJhdhrq() {
		return jhdhrq;
	}

	public void setJhdhrq(String jhdhrq) {
		this.jhdhrq = jhdhrq;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getDdrq() {
		return ddrq;
	}

	public void setDdrq(String ddrq) {
		this.ddrq = ddrq;
	}

	public String getZje() {
		return zje;
	}

	public void setZje(String zje) {
		this.zje = zje;
	}

	public String getYfje() {
		return yfje;
	}

	public void setYfje(String yfje) {
		this.yfje = yfje;
	}

	public String getHtfpje() {
		return htfpje;
	}

	public void setHtfpje(String htfpje) {
		this.htfpje = htfpje;
	}

	public String getCjrq() {
		return cjrq;
	}

	public void setCjrq(String cjrq) {
		this.cjrq = cjrq;
	}

	public String getTjrq() {
		return tjrq;
	}

	public void setTjrq(String tjrq) {
		this.tjrq = tjrq;
	}

	public String getHtwfrq() {
		return htwfrq;
	}

	public void setHtwfrq(String htwfrq) {
		this.htwfrq = htwfrq;
	}

	public String getHtsl() {
		return htsl;
	}

	public void setHtsl(String htsl) {
		this.htsl = htsl;
	}

	public String getHtbz() {
		return htbz;
	}

	public void setHtbz(String htbz) {
		this.htbz = htbz;
	}

	public String getFkfsmc() {
		return fkfsmc;
	}

	public void setFkfsmc(String fkfsmc) {
		this.fkfsmc = fkfsmc;
	}

	public String getFpfsmc() {
		return fpfsmc;
	}

	public void setFpfsmc(String fpfsmc) {
		this.fpfsmc = fpfsmc;
	}

	public String getFkfmc() {
		return fkfmc;
	}

	public void setFkfmc(String fkfmc) {
		this.fkfmc = fkfmc;
	}

	public String getJhrq() {
		return jhrq;
	}

	public void setJhrq(String jhrq) {
		this.jhrq = jhrq;
	}

	public String getFkzje() {
		return fkzje;
	}

	public void setFkzje(String fkzje) {
		this.fkzje = fkzje;
	}

	public String getKprqstart() {
		return kprqstart;
	}

	public void setKprqstart(String kprqstart) {
		this.kprqstart = kprqstart;
	}

	public String getKprqend() {
		return kprqend;
	}

	public void setKprqend(String kprqend) {
		this.kprqend = kprqend;
	}

	public String[] getFpzls() {
		return fpzls;
	}

	public void setFpzls(String[] fpzls) {
		this.fpzls = fpzls;
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
	}

	public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
	}

	public String getHl() {
		return hl;
	}

	public void setHl(String hl) {
		this.hl = hl;
	}

	public String getBizmc() {
		return bizmc;
	}

	public void setBizmc(String bizmc) {
		this.bizmc = bizmc;
	}

	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}

	public String getYwymc() {
		return ywymc;
	}

	public void setYwymc(String ywymc) {
		this.ywymc = ywymc;
	}

	@Override
	public String getFpsl() {
		return fpsl;
	}

	@Override
	public void setFpsl(String fpsl) {
		this.fpsl = fpsl;
	}

	@Override
	public String getFpje() {
		return fpje;
	}

	@Override
	public void setFpje(String fpje) {
		this.fpje = fpje;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public List<FpmxDto> getFpmxDtos() {
		return fpmxDtos;
	}

	public void setFpmxDtos(List<FpmxDto> fpmxDtos) {
		this.fpmxDtos = fpmxDtos;
	}

	public String getHtfkid() {
		return htfkid;
	}

	public void setHtfkid(String htfkid) {
		this.htfkid = htfkid;
	}

	public String getFph() {
		return fph;
	}

	public void setFph(String fph) {
		this.fph = fph;
	}

	public String getFpzlmc() {
		return fpzlmc;
	}

	public void setFpzlmc(String fpzlmc) {
		this.fpzlmc = fpzlmc;
	}

	public String getKprq() {
		return kprq;
	}

	public void setKprq(String kprq) {
		this.kprq = kprq;
	}

	public String getKpje() {
		return kpje;
	}

	public void setKpje(String kpje) {
		this.kpje = kpje;
	}

	public String getLbcskz1() {
		return lbcskz1;
	}

	public void setLbcskz1(String lbcskz1) {
		this.lbcskz1 = lbcskz1;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getScrq() {
		return scrq;
	}

	public void setScrq(String scrq) {
		this.scrq = scrq;
	}

	public String getYxq() {
		return yxq;
	}

	public void setYxq(String yxq) {
		this.yxq = yxq;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getQgid() {
		return qgid;
	}

	public void setQgid(String qgid) {
		this.qgid = qgid;
	}

	public String getQgdh() {
		return qgdh;
	}

	public void setQgdh(String qgdh) {
		this.qgdh = qgdh;
	}

	public String getRkid() {
		return rkid;
	}

	public void setRkid(String rkid) {
		this.rkid = rkid;
	}

	public String getU8rkdh() {
		return u8rkdh;
	}

	public void setU8rkdh(String u8rkdh) {
		this.u8rkdh = u8rkdh;
	}

	public String getMxsl() {
		return mxsl;
	}

	public void setMxsl(String mxsl) {
		this.mxsl = mxsl;
	}

	public String getHsdj() {
		return hsdj;
	}

	public void setHsdj(String hsdj) {
		this.hsdj = hsdj;
	}

	public String getWsdj() {
		return wsdj;
	}

	public void setWsdj(String wsdj) {
		this.wsdj = wsdj;
	}

	public String getHtnbbh() {
		return htnbbh;
	}

	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}

	public String getHtid() {
		return htid;
	}

	public void setHtid(String htid) {
		this.htid = htid;
	}

	public String getHtmxid() {
		return htmxid;
	}

	public void setHtmxid(String htmxid) {
		this.htmxid = htmxid;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getHjje() {
		return hjje;
	}

	public void setHjje(String hjje) {
		this.hjje = hjje;
	}

	public String getDhdh() {
		return dhdh;
	}

	public void setDhdh(String dhdh) {
		this.dhdh = dhdh;
	}

	public String getSuil() {
		return suil;
	}

	public void setSuil(String suil) {
		this.suil = suil;
	}

	public String getDhid() {
		return dhid;
	}

	public void setDhid(String dhid) {
		this.dhid = dhid;
	}

	public String getDhsl() {
		return dhsl;
	}

	public void setDhsl(String dhsl) {
		this.dhsl = dhsl;
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

	public String getHtdh() {
		return htdh;
	}

	public void setHtdh(String htdh) {
		this.htdh = htdh;
	}

	public String getRkdh() {
		return rkdh;
	}

	public void setRkdh(String rkdh) {
		this.rkdh = rkdh;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
