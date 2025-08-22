package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;
import java.util.List;

@Alias(value="XsmxDto")
public class XsmxDto extends XsmxModel{
	//物料编码
	private String wlbm;
	//物料名称
	private String wlmc;
	//规格
	private String gg;
	//库存量
	private String kcl;
	//发货id
	private String fhid;
	//客户简称
	private String khjcmc;
	//客户简称id
	private String khjc;
	//计量单位
	private String jldw;
	//客户代码
	private String khdm;
	private String ckhwid;
	private String yds;//预定数
	private String fhmxid;
	private String oaxsdh;
	private String yfhsl;
	private String fhbj;
	private String czbs;
	private String kysl;//可用数量
	//xsmxids
	private List<String> xsmxids;
	//产品类型
	private String cplx;
	//主键id
	private String zjid;
	//客户类型
	private String khlxmc;
	//销售类型
	private String 	xslxmc;
	//订单号
	private String ddh;	//订单号
	private String khmc;	//客户名称
	private String zd;	//终端
	private String zdqy;	//终端区域
	private String scph;	//生产批号
	private String dj;	//单价
	private String ddrq;	//订单日期
	private String cplxmc;	//产品类型
	private String htbh;	//合同编号
	private String ywy;	//业务员
	private String bm;	//部门
	private String fzdq;	//负责大区
	private String zk;	//折扣
	private String zsyy;	//赠送原因
	private String bz;	//备注
	private String fhr;	//发货日
	private String scrq;	//生产批号
	private String yxq;	//有效期
	private String fph;	//发票号
	private String dkje;	//到款金额
	private String khlxr;	//客户联系人
	private String shdz;//收货地址
	private String khlxfs;//收货联系方式
	private String kdxx;///快递信息
	private String djlxmc;//单据类型
	private String ddrqstart;
	private String ddrqend;
	private String[] khlbs;
	private String[] xslxs;
	private String[] djlxs;
	private String[] cplxs;
	private String yxqstart;
	private String yxqend;
	private String entire;
	private String shlxfs;//收货联系方式
	private String khid;
	private String jcghid;
	private String getKhid;
	private String sqlParam;
	//借用信息id
	private String jyxxid;
	//借出借用id
	private String jcjyid;
	//退货id
	private String thid;
	//原订单号
	private String yddh;
	//u8单号
	private String u8dh;
	private String yu8dh;
	private String viewid;
	private String yviewid;
	//发货数量
	private String fhsl;
	//应收款
	private String ysk;
	private String zsl;//总数量
	private String dky;//到款月
	private String ychh;//货号
	private String ysfsmc;//运输方式名称
	private String wlzllbcskz1;//物料质量类别参数扩展1
	private String fhrmc;//发货人名称
	private List<XsmxDto> xsmxDtos;//发货数据
	private String dysyje;//对应剩余金额
	private String yfje;//已付金额
	private String hkzq;//回款周期
	private String fhdh;//发货单号
	private String djrq;//单据日期
	private String zje;//总金额

	public String getFhdh() {
		return fhdh;
	}

	public void setFhdh(String fhdh) {
		this.fhdh = fhdh;
	}

	public String getDjrq() {
		return djrq;
	}

	public void setDjrq(String djrq) {
		this.djrq = djrq;
	}

	public String getZje() {
		return zje;
	}

	public void setZje(String zje) {
		this.zje = zje;
	}

	public String getHkzq() {
		return hkzq;
	}

	public void setHkzq(String hkzq) {
		this.hkzq = hkzq;
	}

	public String getDysyje() {
		return dysyje;
	}

	public void setDysyje(String dysyje) {
		this.dysyje = dysyje;
	}

	public String getYfje() {
		return yfje;
	}

	public void setYfje(String yfje) {
		this.yfje = yfje;
	}

	public List<XsmxDto> getXsmxDtos() {
		return xsmxDtos;
	}

	public void setXsmxDtos(List<XsmxDto> xsmxDtos) {
		this.xsmxDtos = xsmxDtos;
	}

	public String getFhrmc() {
		return fhrmc;
	}

	public void setFhrmc(String fhrmc) {
		this.fhrmc = fhrmc;
	}

	public String getYsfsmc() {
		return ysfsmc;
	}

	public void setYsfsmc(String ysfsmc) {
		this.ysfsmc = ysfsmc;
	}
	public String getWlzllbcskz1() {
		return wlzllbcskz1;
	}

	public void setWlzllbcskz1(String wlzllbcskz1) {
		this.wlzllbcskz1 = wlzllbcskz1;
	}
	public String getYchh() {
		return ychh;
	}

	public void setYchh(String ychh) {
		this.ychh = ychh;
	}

	@Override
	public String getDky() {
		return dky;
	}

	@Override
	public void setDky(String dky) {
		this.dky = dky;
	}

	public String getZsl() {
		return zsl;
	}

	public void setZsl(String zsl) {
		this.zsl = zsl;
	}
	

	public String getYsk() {
		return ysk;
	}

	public void setYsk(String ysk) {
		this.ysk = ysk;
	}

	public String getFhsl() {
		return fhsl;
	}

	public void setFhsl(String fhsl) {
		this.fhsl = fhsl;
	}

	public String getViewid() {
		return viewid;
	}

	public void setViewid(String viewid) {
		this.viewid = viewid;
	}

	public String getYviewid() {
		return yviewid;
	}

	public void setYviewid(String yviewid) {
		this.yviewid = yviewid;
	}

	public String getYu8dh() {
		return yu8dh;
	}

	public void setYu8dh(String yu8dh) {
		this.yu8dh = yu8dh;
	}

	public String getU8dh() {
		return u8dh;
	}

	public void setU8dh(String u8dh) {
		this.u8dh = u8dh;
	}

	public String getYddh() {
		return yddh;
	}

	public void setYddh(String yddh) {
		this.yddh = yddh;
	}

	public String getThid() {
		return thid;
	}

	public void setThid(String thid) {
		this.thid = thid;
	}

	public String getJcjyid() {
		return jcjyid;
	}

	public void setJcjyid(String jcjyid) {
		this.jcjyid = jcjyid;
	}

	public String getJyxxid() {
		return jyxxid;
	}

	public void setJyxxid(String jyxxid) {
		this.jyxxid = jyxxid;
	}


	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getKhid() {
		return khid;
	}

	public void setKhid(String khid) {
		this.khid = khid;
	}

	public String getJcghid() {
		return jcghid;
	}

	public void setJcghid(String jcghid) {
		this.jcghid = jcghid;
	}

	public String getGetKhid() {
		return getKhid;
	}

	public void setGetKhid(String getKhid) {
		this.getKhid = getKhid;
	}

	public String getShlxfs() {
		return shlxfs;
	}

	public void setShlxfs(String shlxfs) {
		this.shlxfs = shlxfs;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String[] getKhlbs() {
		return khlbs;
	}

	public void setKhlbs(String[] khlbs) {
		this.khlbs = khlbs;
	}

	public String[] getXslxs() {
		return xslxs;
	}

	public void setXslxs(String[] xslxs) {
		this.xslxs = xslxs;
	}

	public String[] getDjlxs() {
		return djlxs;
	}

	public void setDjlxs(String[] djlxs) {
		this.djlxs = djlxs;
	}

	public String[] getCplxs() {
		return cplxs;
	}

	public void setCplxs(String[] cplxs) {
		this.cplxs = cplxs;
	}

	public String getYxqstart() {
		return yxqstart;
	}

	public void setYxqstart(String yxqstart) {
		this.yxqstart = yxqstart;
	}

	public String getYxqend() {
		return yxqend;
	}

	public void setYxqend(String yxqend) {
		this.yxqend = yxqend;
	}

	public String getDdrqstart() {
		return ddrqstart;
	}

	public void setDdrqstart(String ddrqstart) {
		this.ddrqstart = ddrqstart;
	}

	public String getDdrqend() {
		return ddrqend;
	}

	public void setDdrqend(String ddrqend) {
		this.ddrqend = ddrqend;
	}

	public String getDjlxmc() {
		return djlxmc;
	}

	public void setDjlxmc(String djlxmc) {
		this.djlxmc = djlxmc;
	}

	public String getZjid() {
		return zjid;
	}

	public void setZjid(String zjid) {
		this.zjid = zjid;
	}

	public String getKhlxmc() {
		return khlxmc;
	}

	public void setKhlxmc(String khlxmc) {
		this.khlxmc = khlxmc;
	}

	public String getXslxmc() {
		return xslxmc;
	}

	public void setXslxmc(String xslxmc) {
		this.xslxmc = xslxmc;
	}

	public String getDdh() {
		return ddh;
	}

	public void setDdh(String ddh) {
		this.ddh = ddh;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getZd() {
		return zd;
	}

	public void setZd(String zd) {
		this.zd = zd;
	}

	public String getZdqy() {
		return zdqy;
	}

	public void setZdqy(String zdqy) {
		this.zdqy = zdqy;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getDj() {
		return dj;
	}

	public void setDj(String dj) {
		this.dj = dj;
	}


	public String getDdrq() {
		return ddrq;
	}

	public void setDdrq(String ddrq) {
		this.ddrq = ddrq;
	}

	public String getCplxmc() {
		return cplxmc;
	}

	public void setCplxmc(String cplxmc) {
		this.cplxmc = cplxmc;
	}

	public String getHtbh() {
		return htbh;
	}

	public void setHtbh(String htbh) {
		this.htbh = htbh;
	}

	public String getYwy() {
		return ywy;
	}

	public void setYwy(String ywy) {
		this.ywy = ywy;
	}

	public String getBm() {
		return bm;
	}

	public void setBm(String bm) {
		this.bm = bm;
	}

	public String getFzdq() {
		return fzdq;
	}

	public void setFzdq(String fzdq) {
		this.fzdq = fzdq;
	}


	public String getZk() {
		return zk;
	}

	public void setZk(String zk) {
		this.zk = zk;
	}

	public String getZsyy() {
		return zsyy;
	}

	public void setZsyy(String zsyy) {
		this.zsyy = zsyy;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getFhr() {
		return fhr;
	}

	public void setFhr(String fhr) {
		this.fhr = fhr;
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

	public String getFph() {
		return fph;
	}

	public void setFph(String fph) {
		this.fph = fph;
	}

	public String getDkje() {
		return dkje;
	}

	public void setDkje(String dkje) {
		this.dkje = dkje;
	}

	public String getKhlxr() {
		return khlxr;
	}

	public void setKhlxr(String khlxr) {
		this.khlxr = khlxr;
	}

	public String getShdz() {
		return shdz;
	}

	public void setShdz(String shdz) {
		this.shdz = shdz;
	}

	public String getKhlxfs() {
		return khlxfs;
	}

	public void setKhlxfs(String khlxfs) {
		this.khlxfs = khlxfs;
	}

	public String getKdxx() {
		return kdxx;
	}

	public void setKdxx(String kdxx) {
		this.kdxx = kdxx;
	}

	public String getCplx() {
		return cplx;
	}

	public void setCplx(String cplx) {
		this.cplx = cplx;
	}

	public String getKysl() {
		return kysl;
	}

	public void setKysl(String kysl) {
		this.kysl = kysl;
	}

	public String getCzbs() {
		return czbs;
	}

	public void setCzbs(String czbs) {
		this.czbs = czbs;
	}

	public String getYfhsl() {
		return yfhsl;
	}

	public void setYfhsl(String yfhsl) {
		this.yfhsl = yfhsl;
	}

	public String getFhbj() {
		return fhbj;
	}

	public void setFhbj(String fhbj) {
		this.fhbj = fhbj;
	}

	public String getFhid() {
		return fhid;
	}

	public void setFhid(String fhid) {
		this.fhid = fhid;
	}

	public String getKhjc() {
		return khjc;
	}

	public void setKhjc(String khjc) {
		this.khjc = khjc;
	}

	public List<String> getXsmxids() {
		return xsmxids;
	}

	public void setXsmxids(List<String> xsmxids) {
		this.xsmxids = xsmxids;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public String getKhjcmc() {
		return khjcmc;
	}

	public void setKhjcmc(String khjcmc) {
		this.khjcmc = khjcmc;
	}

	public String getKhdm() {
		return khdm;
	}

	public void setKhdm(String khdm) {
		this.khdm = khdm;
	}

	public String getOaxsdh() {
		return oaxsdh;
	}

	public void setOaxsdh(String oaxsdh) {
		this.oaxsdh = oaxsdh;
	}

	public String getFhmxid() {
		return fhmxid;
	}

	public void setFhmxid(String fhmxid) {
		this.fhmxid = fhmxid;
	}

	public String getYds() {
		return yds;
	}

	public void setYds(String yds) {
		this.yds = yds;
	}

	public String getCkhwid() {
		return ckhwid;
	}

	public void setCkhwid(String ckhwid) {
		this.ckhwid = ckhwid;
	}

	public String getKcl() {
		return kcl;
	}

	public void setKcl(String kcl) {
		this.kcl = kcl;
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

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	
}
