package com.matridx.igams.warehouse.dao.entities;

import org.apache.ibatis.type.Alias;



@Alias(value="GdzcglDto")
public class GdzcglDto extends GdzcglModel{
	//U8入库单号
	private String u8rkdh;
	//发票号
	private String fph;
	//物料编码
	private String wlbm;
	//物料名称
	private String wlmc;
	//规格
	private String gg;
	//计量单位
	private String jldw;
	//类别名称
	private String lbmc;
	//使用地点
	private String sydd;
	//固定资产编号
	private String gdzcbh;
	//设备出厂编号
	private String sbccbh;
	//部门名称
	private String bmmc;
	//录入人员名称
	private String lrrymc;
	//entire
	private String entire;
	//合同单号
	private String htnbbh;
	//单据号
	private String djh;
	//到货单号
	private String dhdh;
	//生产批号
	private String scph;
	//追溯号
	private String zsh;
	//类别(高级筛选用)
	private String[] lbs;
	//使用状况(高级筛选用)
	private String[] syzks;
	//增加方式(高级筛选用)
	private String[] zjfss;
	//折旧方法(高级筛选用)
	private String[] zjffs;
	//录入时间
	private String lrsjstart;
	//录入时间
	private String lrsjend;
	//开始使用时间
	private String kssyrqstart;
	//开始使用时间
	private String kssyrqend;
	//类别代码
	private String lbdm;
	//资产组名称
	private String zczmc;
	//增加方式名称
	private String zjfsmc;
	//使用状况名称
	private String syzkmc;
	//折旧方法名称
	private String zjffmc;
	//币种名称
	private String bizmc;
	//对应折旧科目名称
	private String dyzjkmmc;
	//物料类别名称
	private String wllbmc;
	//物料子类别名称
	private String wlzlbmc;
	//物料子类别统称
	private String wlzlbtcmc;
	//旧物料编码
	private String jwlbm;
	//类别名称（物料）
	private String wl_lbmc;
	//生产商
	private String scs;
	//原厂货号
	private String ychh;
	//保存条件
	private String bctj;
	//保质期
	private String bzq;
	//是否危险品
	private String sfwxp;
	//起订量
	private String qdl;
	//货期
	private String hq;
	//安全库存
	private String aqkc;
	//所属产品类别名称
	private String sscplbmc;
	//备注（物料）
	private String wlbz;
	//发票种类名称
	private String fpzlmc;
	//开票日期
	private String kprq;
	//交给财务时间
	private String fplrsj;
	//发票供应商名称
	private String fpgysmc;
	//业务员名称
	private String ywymc;
	//税率
	private String sl;
	//发票币种名称
	private String fpbizmc;
	//发票数量
	private String fpsl;
	//开票金额
	private String kpje;
	//发票备注
	private String fpbz;
	//入库类别名称
	private String rklbmc;
	//到货日期
	private String dhrq;
	//到货供应商
	private String dhgysmc;
	//库位名称
	private String kwmc;
	//仓库名称
	private String ckmc;
	//到货数量
	private String dhsl;
	//初验退回数量
	private String cythsl;
	//生产日期
	private String scrq;
	//有效期
	private String yxq;
	//到货备注
	private String dhbz;
	//设备验收单号
	private String sbysdh;
	//验收数量
	private String yssl;
	//所属研发项目名称
	private String ssyfxmmc;
	//验收日期
	private String ysrq;
	//验收结果
	private String ysjg;
	//入库单号
	private String rkdh;
	//入库日期
	private String rkrq;
	//入库供应商
	private String rkgysmc;
	//入库仓库名称
	private String rkckmc;
	//入库库位名称
	private String rkkwmc;
	//入库数量
	private String rksl;
	//入库备注
	private String rkbz;
	//申请部门
	private String sqbm;
	//申请部门名称
	private String sqbmmc;
	//申请部门代码
	private String sqbmdm;
	//部门代码
	private String bmdm;
	//合同明细ID
	private String htmxid;
	private String SqlParam; 	//导出关联标记位//所选择的字段
	//是否危险品名称
	private String sfwxpmc;
	private String zjfsdm;
	private String zjfscskz1;
	private String syzkdm;
	private String zjffdm;
	private String zczdm;
	private String dyzjkmdm;
	private String xmdm;
	private String xmmc;

	public String getZjfsdm() {
		return zjfsdm;
	}

	public void setZjfsdm(String zjfsdm) {
		this.zjfsdm = zjfsdm;
	}

	public String getZjfscskz1() {
		return zjfscskz1;
	}

	public void setZjfscskz1(String zjfscskz1) {
		this.zjfscskz1 = zjfscskz1;
	}

	public String getSyzkdm() {
		return syzkdm;
	}

	public void setSyzkdm(String syzkdm) {
		this.syzkdm = syzkdm;
	}

	public String getZjffdm() {
		return zjffdm;
	}

	public void setZjffdm(String zjffdm) {
		this.zjffdm = zjffdm;
	}

	public String getZczdm() {
		return zczdm;
	}

	public void setZczdm(String zczdm) {
		this.zczdm = zczdm;
	}

	public String getDyzjkmdm() {
		return dyzjkmdm;
	}

	public void setDyzjkmdm(String dyzjkmdm) {
		this.dyzjkmdm = dyzjkmdm;
	}

	public String getXmdm() {
		return xmdm;
	}

	public void setXmdm(String xmdm) {
		this.xmdm = xmdm;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public String getSfwxpmc() {
		return sfwxpmc;
	}

	public void setSfwxpmc(String sfwxpmc) {
		this.sfwxpmc = sfwxpmc;
	}

	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public String getHtmxid() {
		return htmxid;
	}

	public void setHtmxid(String htmxid) {
		this.htmxid = htmxid;
	}

	public String getBmdm() {
		return bmdm;
	}

	public void setBmdm(String bmdm) {
		this.bmdm = bmdm;
	}

	public String getSqbmdm() {
		return sqbmdm;
	}

	public void setSqbmdm(String sqbmdm) {
		this.sqbmdm = sqbmdm;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String getLbdm() {
		return lbdm;
	}

	public void setLbdm(String lbdm) {
		this.lbdm = lbdm;
	}

	public String getZczmc() {
		return zczmc;
	}

	public void setZczmc(String zczmc) {
		this.zczmc = zczmc;
	}

	public String getZjfsmc() {
		return zjfsmc;
	}

	public void setZjfsmc(String zjfsmc) {
		this.zjfsmc = zjfsmc;
	}

	public String getSyzkmc() {
		return syzkmc;
	}

	public void setSyzkmc(String syzkmc) {
		this.syzkmc = syzkmc;
	}

	public String getZjffmc() {
		return zjffmc;
	}

	public void setZjffmc(String zjffmc) {
		this.zjffmc = zjffmc;
	}

	public String getBizmc() {
		return bizmc;
	}

	public void setBizmc(String bizmc) {
		this.bizmc = bizmc;
	}

	public String getDyzjkmmc() {
		return dyzjkmmc;
	}

	public void setDyzjkmmc(String dyzjkmmc) {
		this.dyzjkmmc = dyzjkmmc;
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

	public String getJwlbm() {
		return jwlbm;
	}

	public void setJwlbm(String jwlbm) {
		this.jwlbm = jwlbm;
	}

	public String getWl_lbmc() {
		return wl_lbmc;
	}

	public void setWl_lbmc(String wl_lbmc) {
		this.wl_lbmc = wl_lbmc;
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

	public String getSfwxp() {
		return sfwxp;
	}

	public void setSfwxp(String sfwxp) {
		this.sfwxp = sfwxp;
	}

	public String getQdl() {
		return qdl;
	}

	public void setQdl(String qdl) {
		this.qdl = qdl;
	}

	public String getHq() {
		return hq;
	}

	public void setHq(String hq) {
		this.hq = hq;
	}

	public String getAqkc() {
		return aqkc;
	}

	public void setAqkc(String aqkc) {
		this.aqkc = aqkc;
	}

	public String getSscplbmc() {
		return sscplbmc;
	}

	public void setSscplbmc(String sscplbmc) {
		this.sscplbmc = sscplbmc;
	}

	public String getWlbz() {
		return wlbz;
	}

	public void setWlbz(String wlbz) {
		this.wlbz = wlbz;
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

	public String getFplrsj() {
		return fplrsj;
	}

	public void setFplrsj(String fplrsj) {
		this.fplrsj = fplrsj;
	}

	public String getFpgysmc() {
		return fpgysmc;
	}

	public void setFpgysmc(String fpgysmc) {
		this.fpgysmc = fpgysmc;
	}

	public String getYwymc() {
		return ywymc;
	}

	public void setYwymc(String ywymc) {
		this.ywymc = ywymc;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getFpbizmc() {
		return fpbizmc;
	}

	public void setFpbizmc(String fpbizmc) {
		this.fpbizmc = fpbizmc;
	}

	public String getFpsl() {
		return fpsl;
	}

	public void setFpsl(String fpsl) {
		this.fpsl = fpsl;
	}

	public String getKpje() {
		return kpje;
	}

	public void setKpje(String kpje) {
		this.kpje = kpje;
	}

	public String getFpbz() {
		return fpbz;
	}

	public void setFpbz(String fpbz) {
		this.fpbz = fpbz;
	}

	public String getRklbmc() {
		return rklbmc;
	}

	public void setRklbmc(String rklbmc) {
		this.rklbmc = rklbmc;
	}

	public String getDhrq() {
		return dhrq;
	}

	public void setDhrq(String dhrq) {
		this.dhrq = dhrq;
	}

	public String getDhgysmc() {
		return dhgysmc;
	}

	public void setDhgysmc(String dhgysmc) {
		this.dhgysmc = dhgysmc;
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

	public String getDhsl() {
		return dhsl;
	}

	public void setDhsl(String dhsl) {
		this.dhsl = dhsl;
	}

	public String getCythsl() {
		return cythsl;
	}

	public void setCythsl(String cythsl) {
		this.cythsl = cythsl;
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

	public String getDhbz() {
		return dhbz;
	}

	public void setDhbz(String dhbz) {
		this.dhbz = dhbz;
	}

	public String getSbysdh() {
		return sbysdh;
	}

	public void setSbysdh(String sbysdh) {
		this.sbysdh = sbysdh;
	}

	public String getYssl() {
		return yssl;
	}

	public void setYssl(String yssl) {
		this.yssl = yssl;
	}

	public String getSsyfxmmc() {
		return ssyfxmmc;
	}

	public void setSsyfxmmc(String ssyfxmmc) {
		this.ssyfxmmc = ssyfxmmc;
	}

	public String getYsrq() {
		return ysrq;
	}

	public void setYsrq(String ysrq) {
		this.ysrq = ysrq;
	}

	public String getYsjg() {
		return ysjg;
	}

	public void setYsjg(String ysjg) {
		this.ysjg = ysjg;
	}

	public String getRkdh() {
		return rkdh;
	}

	public void setRkdh(String rkdh) {
		this.rkdh = rkdh;
	}

	public String getRkrq() {
		return rkrq;
	}

	public void setRkrq(String rkrq) {
		this.rkrq = rkrq;
	}

	public String getRkgysmc() {
		return rkgysmc;
	}

	public void setRkgysmc(String rkgysmc) {
		this.rkgysmc = rkgysmc;
	}

	public String getRkckmc() {
		return rkckmc;
	}

	public void setRkckmc(String rkckmc) {
		this.rkckmc = rkckmc;
	}

	public String getRkkwmc() {
		return rkkwmc;
	}

	public void setRkkwmc(String rkkwmc) {
		this.rkkwmc = rkkwmc;
	}

	public String getRksl() {
		return rksl;
	}

	public void setRksl(String rksl) {
		this.rksl = rksl;
	}

	public String getRkbz() {
		return rkbz;
	}

	public void setRkbz(String rkbz) {
		this.rkbz = rkbz;
	}

	public String[] getLbs() {
		return lbs;
	}

	public void setLbs(String[] lbs) {
		this.lbs = lbs;
	}

	public String[] getSyzks() {
		return syzks;
	}

	public void setSyzks(String[] syzks) {
		this.syzks = syzks;
	}

	public String[] getZjfss() {
		return zjfss;
	}

	public void setZjfss(String[] zjfss) {
		this.zjfss = zjfss;
	}

	public String[] getZjffs() {
		return zjffs;
	}

	public void setZjffs(String[] zjffs) {
		this.zjffs = zjffs;
	}

	public String getLrsjstart() {
		return lrsjstart;
	}

	public void setLrsjstart(String lrsjstart) {
		this.lrsjstart = lrsjstart;
	}

	public String getLrsjend() {
		return lrsjend;
	}

	public void setLrsjend(String lrsjend) {
		this.lrsjend = lrsjend;
	}

	public String getKssyrqstart() {
		return kssyrqstart;
	}

	public void setKssyrqstart(String kssyrqstart) {
		this.kssyrqstart = kssyrqstart;
	}

	public String getKssyrqend() {
		return kssyrqend;
	}

	public void setKssyrqend(String kssyrqend) {
		this.kssyrqend = kssyrqend;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getHtnbbh() {
		return htnbbh;
	}

	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}

	public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
	}

	public String getDhdh() {
		return dhdh;
	}

	public void setDhdh(String dhdh) {
		this.dhdh = dhdh;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getZsh() {
		return zsh;
	}

	public void setZsh(String zsh) {
		this.zsh = zsh;
	}

	public String getU8rkdh() {
		return u8rkdh;
	}

	public void setU8rkdh(String u8rkdh) {
		this.u8rkdh = u8rkdh;
	}

	public String getFph() {
		return fph;
	}

	public void setFph(String fph) {
		this.fph = fph;
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

	public String getLbmc() {
		return lbmc;
	}

	public void setLbmc(String lbmc) {
		this.lbmc = lbmc;
	}

	public String getSydd() {
		return sydd;
	}

	public void setSydd(String sydd) {
		this.sydd = sydd;
	}

	public String getGdzcbh() {
		return gdzcbh;
	}

	public void setGdzcbh(String gdzcbh) {
		this.gdzcbh = gdzcbh;
	}

	public String getSbccbh() {
		return sbccbh;
	}

	public void setSbccbh(String sbccbh) {
		this.sbccbh = sbccbh;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
