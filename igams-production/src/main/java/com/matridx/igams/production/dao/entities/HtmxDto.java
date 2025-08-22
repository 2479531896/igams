package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="HtmxDto")
public class HtmxDto extends HtmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//单据号
	private String djh;
	//物料编码
	private String wlbm;
	//物料名称
	private String wlmc;
	//规格
	private String gg;
	//计量单位
	private String jldw;
	//请购申请部门
	private String sqbm;
	//到货数量标记
	private String dhslbj;
	//备用到货数量
	private String bydhsl;
	//请购申请部门名称
	private String sqbmmc;
	//请购数量
	private String qgsl;
	//合同内部编号
	private String htnbbh;
	//物料类别名称
	private String wllbmc;
	//物料子类别名称
	private String wlzlbmc;
	//物料子类别统称名称
	private String wlzlbtcmc;
	//原厂货号
	private String ychh;
	//查询字段(全部)
	private String entire;
	//llid
	private String llid;
	//库存量
	private String kcl;
	//可领数量
	private String klsl;
	//合同外部编号
	private String htwbbh;
	private String SqlParam; 	//导出关联标记位//所选择的字段
	//剩余数量
	private String sysl;
	//预定数量
	private String ydsl;
	//请购期望日期（更新请购用）
	private String qgqwrq;
	//请购备注（更新请购用)
	private String qgbz;
	//请购取消请购(更新请购用)
	private String qgqxqg;
	//订单日期
	private String htddrq;
	//负责人
	private String fzr;
	//负责人名称
	private String fzrmc;
	//起订量
	private String qdl;
	//原数量(修改前数量)
	private String presl;
	//关联ID(请购明细) 
	private String glid;
	//快递类型名称（基础数据)
	private String kdmc;
	//请购申请人
	private String sqr;
	//ddid
	private String ddid;
	//到货日期
	private String dhrq;
	//到货数量
	private String dhsl;
	//货物入库单号
	private String hwrkdh;
	//货物ID
	private String hwid;
	//申请日期
	private String sqrq;
	//订单日期
	private String ddrq;
	//项目编码参数代码
	private String xmcsdm;
	//项目参数名称
	private String xmcsmc;
	//项目父类参数名称
	private String fcsdm;
	//rabit同步标记
	private String prefixFlg;
	//合同明细标记
	private String htmxidbj;

	//合同状态
	private String htzt;
	//创建日期
	private String cjrq;
	//期望到货日期
	private String qwrq;
	//计划到货日期开始时间
	private String jhdhrqstart;
	//计划到货日期结束时间
	private String jhdhrqend;
	//到货日期开始时间
	private String dhrqstart;
	//到货日期结束时间
	private String dhrqend;
	//基础数据参数扩展2
	private String cskz2;
	//入库数量
	private String rksl;
	//入库日期
	private String rkrq;
	//到货单号
	private String dhdh;
	//到货id
	private String dhid;
	//入库数量增加标记
	private String rksladd;
	//入库数量删除标记
	private String rksldel;
	//入库ID
	private String rkid;
	//请购类别名称
	private String qglbmc;
	//请购类别代码
	private String qglbdm;
	//请购类别
	private String qglb;
	//货物标准
	private String hwbz;
	//货物计量单位
	private String hwjldw;
	//货物用途
	private String hwyt;
	//发票号码
	private String fphms;
	//項目大類
	private String xmdl;
	//項目編碼
	private String xmbm;
	//仓库货物id
	private String ckhwid;
	//仓库货物名称
	private String ckqxmc;
	//仓库权限
	private String ckqxlx;
	//发票ID
	private String fpid;
	//供应商
	private String gys;
	//供应商名称
	private String gysmc;
	//汇率
	private String hl;
	//合同ID  s
	private List<String> htids;
	//明细数量
	private String mxsl;
	//币种
	private String biz;
	//币种名称
	private String bizmc;
	//发票方式
	private String fpfs;
	//双章标记
	private String szbj;
	//物料ids
	private List<String> wlids;
	//子件物料编码
	private String zjwlbm;
	//u8cpjgmxid
	private String u8cpjgmxid;
	//应领数量
	private String ylsl;
	//产品结构明细ID
	private String cpjgmxid;
	//到货状态
	private String dhzt;
	//关闭人员名称
	private String gbrymc;
	//打开人员名称
	private String dkrymc;
	//合同类型
	private String htlx;
	//u8omid
	private String u8omid;
	//u8poid
	private String u8poid;
	//u8行关闭状态字段
	private String cState;
	//物料分类名称
	private String wlflmc;
	//采购红字标记
	private String cghzbj;
	//可红冲数
	private String khcsl;
	private String sqryhm;//申请人用户名
	private String gdzcbh;//固定资产编号
	private String sbysid;//设备验收id
	private String sbysdh;
	private String gysid;//供应商id
	private String sfty;//是否停用
	//创建日期开始时间
	private String cjrqstart;
	//创建日期结束时间
	private String cjrqend;
	//框架类型
	private String kjlx;
	private List<String> glids;
	//原合同明细总金额
	private String yhtmxhjje;
	//原合同明细含税单价
	private String yhtmxhsdj;
	//原合同明细数量
	private String yhtmxsl;
	//仓库id
	private String ckid;
	private String bchthsdj;
	private String bchthjje;
	private String bchtsl;

	public String getBchthsdj() {
		return bchthsdj;
	}

	public void setBchthsdj(String bchthsdj) {
		this.bchthsdj = bchthsdj;
	}

	public String getBchthjje() {
		return bchthjje;
	}

	public void setBchthjje(String bchthjje) {
		this.bchthjje = bchthjje;
	}

	public String getBchtsl() {
		return bchtsl;
	}

	public void setBchtsl(String bchtsl) {
		this.bchtsl = bchtsl;
	}

	public String getCkid() {
		return ckid;
	}

	public void setCkid(String ckid) {
		this.ckid = ckid;
	}

	public String getYhtmxhjje() {
		return yhtmxhjje;
	}

	public void setYhtmxhjje(String yhtmxhjje) {
		this.yhtmxhjje = yhtmxhjje;
	}

	public String getYhtmxhsdj() {
		return yhtmxhsdj;
	}

	public void setYhtmxhsdj(String yhtmxhsdj) {
		this.yhtmxhsdj = yhtmxhsdj;
	}

	public String getYhtmxsl() {
		return yhtmxsl;
	}

	public void setYhtmxsl(String yhtmxsl) {
		this.yhtmxsl = yhtmxsl;
	}

	public List<String> getGlids() {
		return glids;
	}

	public void setGlids(List<String> glids) {
		this.glids = glids;
	}

	public String getKjlx() {
		return kjlx;
	}

	public void setKjlx(String kjlx) {
		this.kjlx = kjlx;
	}

	public String getCjrqstart() {
		return cjrqstart;
	}

	public void setCjrqstart(String cjrqstart) {
		this.cjrqstart = cjrqstart;
	}

	public String getCjrqend() {
		return cjrqend;
	}

	public void setCjrqend(String cjrqend) {
		this.cjrqend = cjrqend;
	}

	public String getSfty() {
		return sfty;
	}

	public void setSfty(String sfty) {
		this.sfty = sfty;
	}

	public String getGysid() {
		return gysid;
	}

	public void setGysid(String gysid) {
		this.gysid = gysid;
	}

	public String getSbysdh() {
		return sbysdh;
	}

	public void setSbysdh(String sbysdh) {
		this.sbysdh = sbysdh;
	}

	public String getGdzcbh() {
		return gdzcbh;
	}

	public void setGdzcbh(String gdzcbh) {
		this.gdzcbh = gdzcbh;
	}

	public String getSbysid() {
		return sbysid;
	}

	public void setSbysid(String sbysid) {
		this.sbysid = sbysid;
	}

	public String getSqryhm() {
		return sqryhm;
	}

	public void setSqryhm(String sqryhm) {
		this.sqryhm = sqryhm;
	}
	public String getKhcsl() {
		return khcsl;
	}

	public void setKhcsl(String khcsl) {
		this.khcsl = khcsl;
	}

	public String getCghzbj() {
		return cghzbj;
	}

	public void setCghzbj(String cghzbj) {
		this.cghzbj = cghzbj;
	}

	public String getWlflmc() {
		return wlflmc;
	}

	public void setWlflmc(String wlflmc) {
		this.wlflmc = wlflmc;
	}

	public List<String> getWlids() {
		return wlids;
	}
	public void setWlids(List<String> wlids) {
		this.wlids = wlids;
	}
	public String getcState() {
		return cState;
	}

	public void setcState(String cState) {
		this.cState = cState;
	}

	public String getGbrymc() {
		return gbrymc;
	}

	public void setGbrymc(String gbrymc) {
		this.gbrymc = gbrymc;
	}

	public String getDkrymc() {
		return dkrymc;
	}

	public void setDkrymc(String dkrymc) {
		this.dkrymc = dkrymc;
	}

	public String getHtlx() {
		return htlx;
	}

	public void setHtlx(String htlx) {
		this.htlx = htlx;
	}

	public String getU8omid() {
		return u8omid;
	}

	public void setU8omid(String u8omid) {
		this.u8omid = u8omid;
	}

	public String getU8poid() {
		return u8poid;
	}

	public void setU8poid(String u8poid) {
		this.u8poid = u8poid;
	}

	public String getDhzt() {
		return dhzt;
	}

	public void setDhzt(String dhzt) {
		this.dhzt = dhzt;
	}
	public String getU8cpjgmxid() {
		return u8cpjgmxid;
	}
	public void setU8cpjgmxid(String u8cpjgmxid) {
		this.u8cpjgmxid = u8cpjgmxid;
	}
	
	public String getZjwlbm() {
		return zjwlbm;
	}
	public void setZjwlbm(String zjwlbm) {
		this.zjwlbm = zjwlbm;
	}


	public String getCpjgmxid() {
		return cpjgmxid;
	}

	public void setCpjgmxid(String cpjgmxid) {
		this.cpjgmxid = cpjgmxid;
	}

	public String getYlsl() {
		return ylsl;
	}

	public void setYlsl(String ylsl) {
		this.ylsl = ylsl;
	}

    public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
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

	public String getQgsl() {
		return qgsl;
	}

	public void setQgsl(String qgsl) {
		this.qgsl = qgsl;
	}

	public String getHtnbbh() {
		return htnbbh;
	}

	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
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

	public String getYchh() {
		return ychh;
	}

	public void setYchh(String ychh) {
		this.ychh = ychh;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getLlid() {
		return llid;
	}

	public void setLlid(String llid) {
		this.llid = llid;
	}

	public String getKcl() {
		return kcl;
	}

	public void setKcl(String kcl) {
		this.kcl = kcl;
	}

	public String getKlsl() {
		return klsl;
	}

	public void setKlsl(String klsl) {
		this.klsl = klsl;
	}

	public String getHtwbbh() {
		return htwbbh;
	}

	public void setHtwbbh(String htwbbh) {
		this.htwbbh = htwbbh;
	}

	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public String getSysl() {
		return sysl;
	}

	public void setSysl(String sysl) {
		this.sysl = sysl;
	}

	public String getYdsl() {
		return ydsl;
	}

	public void setYdsl(String ydsl) {
		this.ydsl = ydsl;
	}

	public String getQgqwrq() {
		return qgqwrq;
	}

	public void setQgqwrq(String qgqwrq) {
		this.qgqwrq = qgqwrq;
	}

	public String getQgbz() {
		return qgbz;
	}

	public void setQgbz(String qgbz) {
		this.qgbz = qgbz;
	}

	public String getQgqxqg() {
		return qgqxqg;
	}

	public void setQgqxqg(String qgqxqg) {
		this.qgqxqg = qgqxqg;
	}

	public String getHtddrq() {
		return htddrq;
	}

	public void setHtddrq(String htddrq) {
		this.htddrq = htddrq;
	}

	public String getFzr() {
		return fzr;
	}

	public void setFzr(String fzr) {
		this.fzr = fzr;
	}

	public String getFzrmc() {
		return fzrmc;
	}

	public void setFzrmc(String fzrmc) {
		this.fzrmc = fzrmc;
	}

	public String getQdl() {
		return qdl;
	}

	public void setQdl(String qdl) {
		this.qdl = qdl;
	}

	public String getPresl() {
		return presl;
	}

	public void setPresl(String presl) {
		this.presl = presl;
	}

	public String getGlid() {
		return glid;
	}

	public void setGlid(String glid) {
		this.glid = glid;
	}

	public String getKdmc() {
		return kdmc;
	}

	public void setKdmc(String kdmc) {
		this.kdmc = kdmc;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getDhrq() {
		return dhrq;
	}

	public void setDhrq(String dhrq) {
		this.dhrq = dhrq;
	}

	public String getDhsl() {
		return dhsl;
	}

	public void setDhsl(String dhsl) {
		this.dhsl = dhsl;
	}

	public String getHwrkdh() {
		return hwrkdh;
	}

	public void setHwrkdh(String hwrkdh) {
		this.hwrkdh = hwrkdh;
	}

	public String getHwid() {
		return hwid;
	}

	public void setHwid(String hwid) {
		this.hwid = hwid;
	}

	public String getSqrq() {
		return sqrq;
	}

	public void setSqrq(String sqrq) {
		this.sqrq = sqrq;
	}

	public String getDdrq() {
		return ddrq;
	}

	public void setDdrq(String ddrq) {
		this.ddrq = ddrq;
	}

	public String getXmcsdm() {
		return xmcsdm;
	}

	public void setXmcsdm(String xmcsdm) {
		this.xmcsdm = xmcsdm;
	}

	public String getXmcsmc() {
		return xmcsmc;
	}

	public void setXmcsmc(String xmcsmc) {
		this.xmcsmc = xmcsmc;
	}

	public String getFcsdm() {
		return fcsdm;
	}

	public void setFcsdm(String fcsdm) {
		this.fcsdm = fcsdm;
	}

	public String getPrefixFlg() {
		return prefixFlg;
	}

	public void setPrefixFlg(String prefixFlg) {
		this.prefixFlg = prefixFlg;
	}

	public String getHtmxidbj() {
		return htmxidbj;
	}

	public void setHtmxidbj(String htmxidbj) {
		this.htmxidbj = htmxidbj;
	}

	public String getHtzt() {
		return htzt;
	}

	public void setHtzt(String htzt) {
		this.htzt = htzt;
	}

	public String getCjrq() {
		return cjrq;
	}

	public void setCjrq(String cjrq) {
		this.cjrq = cjrq;
	}

	public String getQwrq() {
		return qwrq;
	}

	public void setQwrq(String qwrq) {
		this.qwrq = qwrq;
	}

	public String getJhdhrqstart() {
		return jhdhrqstart;
	}

	public void setJhdhrqstart(String jhdhrqstart) {
		this.jhdhrqstart = jhdhrqstart;
	}

	public String getJhdhrqend() {
		return jhdhrqend;
	}

	public void setJhdhrqend(String jhdhrqend) {
		this.jhdhrqend = jhdhrqend;
	}

	public String getDhrqstart() {
		return dhrqstart;
	}

	public void setDhrqstart(String dhrqstart) {
		this.dhrqstart = dhrqstart;
	}

	public String getDhrqend() {
		return dhrqend;
	}

	public void setDhrqend(String dhrqend) {
		this.dhrqend = dhrqend;
	}

	public String getCskz2() {
		return cskz2;
	}

	public void setCskz2(String cskz2) {
		this.cskz2 = cskz2;
	}

	@Override
	public String getRksl() {
		return rksl;
	}

	@Override
	public void setRksl(String rksl) {
		this.rksl = rksl;
	}

	public String getRkrq() {
		return rkrq;
	}

	public void setRkrq(String rkrq) {
		this.rkrq = rkrq;
	}

	public String getDhdh() {
		return dhdh;
	}

	public void setDhdh(String dhdh) {
		this.dhdh = dhdh;
	}

	public String getDhid() {
		return dhid;
	}

	public void setDhid(String dhid) {
		this.dhid = dhid;
	}

	public String getRksladd() {
		return rksladd;
	}

	public void setRksladd(String rksladd) {
		this.rksladd = rksladd;
	}

	public String getRksldel() {
		return rksldel;
	}

	public void setRksldel(String rksldel) {
		this.rksldel = rksldel;
	}

	public String getRkid() {
		return rkid;
	}

	public void setRkid(String rkid) {
		this.rkid = rkid;
	}

	public String getQglbmc() {
		return qglbmc;
	}

	public void setQglbmc(String qglbmc) {
		this.qglbmc = qglbmc;
	}

	public String getQglbdm() {
		return qglbdm;
	}

	public void setQglbdm(String qglbdm) {
		this.qglbdm = qglbdm;
	}

	public String getQglb() {
		return qglb;
	}

	public void setQglb(String qglb) {
		this.qglb = qglb;
	}

	public String getHwbz() {
		return hwbz;
	}

	public void setHwbz(String hwbz) {
		this.hwbz = hwbz;
	}

	public String getHwjldw() {
		return hwjldw;
	}

	public void setHwjldw(String hwjldw) {
		this.hwjldw = hwjldw;
	}

	public String getHwyt() {
		return hwyt;
	}

	public void setHwyt(String hwyt) {
		this.hwyt = hwyt;
	}

	public String getFphms() {
		return fphms;
	}

	public void setFphms(String fphms) {
		this.fphms = fphms;
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

	public String getCkhwid() {
		return ckhwid;
	}

	public void setCkhwid(String ckhwid) {
		this.ckhwid = ckhwid;
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

	public String getFpid() {
		return fpid;
	}

	public void setFpid(String fpid) {
		this.fpid = fpid;
	}

	public String getGys() {
		return gys;
	}

	public void setGys(String gys) {
		this.gys = gys;
	}

	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}

	public String getHl() {
		return hl;
	}

	public void setHl(String hl) {
		this.hl = hl;
	}

	public List<String> getHtids() {
		return htids;
	}

	public void setHtids(List<String> htids) {
		this.htids = htids;
	}

	public String getMxsl() {
		return mxsl;
	}

	public void setMxsl(String mxsl) {
		this.mxsl = mxsl;
	}

	public String getBiz() {
		return biz;
	}

	public void setBiz(String biz) {
		this.biz = biz;
	}

	public String getU8rkdh() {
		return u8rkdh;
	}

	public void setU8rkdh(String u8rkdh) {
		this.u8rkdh = u8rkdh;
	}

	public String getLbcskz1() {
		return lbcskz1;
	}

	public void setLbcskz1(String lbcskz1) {
		this.lbcskz1 = lbcskz1;
	}

	public List<FpmxDto> getFpmxDtos() {
		return fpmxDtos;
	}

	public void setFpmxDtos(List<FpmxDto> fpmxDtos) {
		this.fpmxDtos = fpmxDtos;
	}

	public String getRkzsl() {
		return rkzsl;
	}

	public void setRkzsl(String rkzsl) {
		this.rkzsl = rkzsl;
	}

	public String getFph() {
		return fph;
	}

	public void setFph(String fph) {
		this.fph = fph;
	}

	public String getFpzje() {
		return fpzje;
	}

	public void setFpzje(String fpzje) {
		this.fpzje = fpzje;
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

	public String getQgshtgsjstart() {
		return qgshtgsjstart;
	}

	public void setQgshtgsjstart(String qgshtgsjstart) {
		this.qgshtgsjstart = qgshtgsjstart;
	}

	public String getQgshtgsjend() {
		return qgshtgsjend;
	}

	public void setQgshtgsjend(String qgshtgsjend) {
		this.qgshtgsjend = qgshtgsjend;
	}

	public String getHtshtgsjstart() {
		return htshtgsjstart;
	}

	public void setHtshtgsjstart(String htshtgsjstart) {
		this.htshtgsjstart = htshtgsjstart;
	}

	public String getHtshtgsjend() {
		return htshtgsjend;
	}

	public void setHtshtgsjend(String htshtgsjend) {
		this.htshtgsjend = htshtgsjend;
	}

	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public String getFkfsmc() {
		return fkfsmc;
	}

	public void setFkfsmc(String fkfsmc) {
		this.fkfsmc = fkfsmc;
	}

	public String getFplrsj() {
		return fplrsj;
	}

	public void setFplrsj(String fplrsj) {
		this.fplrsj = fplrsj;
	}

	public String getFkrq() {
		return fkrq;
	}

	public void setFkrq(String fkrq) {
		this.fkrq = fkrq;
	}

	public String getYfje() {
		return yfje;
	}

	public void setYfje(String yfje) {
		this.yfje = yfje;
	}

	public String getWfkje() {
		return wfkje;
	}

	public void setWfkje(String wfkje) {
		this.wfkje = wfkje;
	}

	public String getFkje() {
		return fkje;
	}

	public void setFkje(String fkje) {
		this.fkje = fkje;
	}

	public String getZje() {
		return zje;
	}

	public void setZje(String zje) {
		this.zje = zje;
	}

	public String getHwbj() {
		return hwbj;
	}

	public void setHwbj(String hwbj) {
		this.hwbj = hwbj;
	}

	public String getFpsl() {
		return fpsl;
	}

	public void setFpsl(String fpsl) {
		this.fpsl = fpsl;
	}

	public String getBhgsl() {
		return bhgsl;
	}

	public void setBhgsl(String bhgsl) {
		this.bhgsl = bhgsl;
	}

	public String getWwhsl() {
		return wwhsl;
	}

	public void setWwhsl(String wwhsl) {
		this.wwhsl = wwhsl;
	}

	public String getHtfxmc() {
		return htfxmc;
	}

	public void setHtfxmc(String htfxmc) {
		this.htfxmc = htfxmc;
	}
	public String getYwlxmc() {
		return ywlxmc;
	}

	public void setYwlxmc(String ywlxmc) {
		this.ywlxmc = ywlxmc;
	}

	public String getSzrq() {
		return szrq;
	}

	public void setSzrq(String szrq) {
		this.szrq = szrq;
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



	public String getQglx() {
		return qglx;
	}

	public void setQglx(String qglx) {
		this.qglx = qglx;
	}

	public String getQglxmc() {
		return qglxmc;
	}

	public void setQglxmc(String qglxmc) {
		this.qglxmc = qglxmc;
	}

	public String getJlbh() {
		return jlbh;
	}

	public void setJlbh(String jlbh) {
		this.jlbh = jlbh;
	}

	public void setSqly(String sqly) {
		this.sqly = sqly;
	}

	public String getSzbj() {
		return szbj;
	}

	public void setSzbj(String szbj) {
		this.szbj = szbj;
	}

	public String getFpfs() {
		return fpfs;
	}

	public void setFpfs(String fpfs) {
		this.fpfs = fpfs;
	}

	public String getBizmc() {
		return bizmc;
	}

	public void setBizmc(String bizmc) {
		this.bizmc = bizmc;
	}

	//u8入库单号
	private String u8rkdh;
	//类别参数扩展1
	private String lbcskz1;
	private List<FpmxDto> fpmxDtos;
	//入库总数量
	private String rkzsl;
	//发票号
	private String fph;
	//发票总金额
	private String fpzje;
	//交给财务时间开始时间
	private String lrsjstart;
	//交给财务时间结束时间Z
	private String lrsjend;
	private String qgshtgsjstart;
	private String qgshtgsjend;
	private String htshtgsjstart;
	private String htshtgsjend;
	//申请人
	private String sqrmc;
	//供应商
	private String fkfsmc;
	//交给财务时间
	private String fplrsj;
	//付款日期
	private String fkrq;
	//已付金额
	private String yfje;
	//未付款金额
	private String wfkje;
	//付款金额
	private String fkje;
	//总金额
	private String zje;
	private String hwbj;
	//发票数量
	private String fpsl;
	//不合格数量
	private String bhgsl;
	//未维护数量
	private String wwhsl;
	//合同风险程度
	private String htfxmc;
	//请购类型
	private String qglx;
	//请购类型名称
	private String qglxmc;
	//记录编号
	private String jlbh;
	//申请理由
	private String sqly;
	//审核时间
	private String shsj;
	//支付方法
	private String zffs;
	//产品结构id
	private String cpjgid;
	//母件损耗率
	private String mjshl;
	//bom类别名称
	private String bomlbmc;
	//u8cpjgid
	private String u8cpjgid;
	//基本用量
	private String jbyl;
	//基础数量
	private String jcsl;
	//子件损耗率
	private String zjshl;
	//仓库代码
	private String ckdm;
	//产品使用数量
	private String cpsysl;
	//u8关联id(委外)
	private String u8ommxid;
	//bom类别代码
	private String bomlbdm;
	public String getBomlbdm() {
		return bomlbdm;
	}

	public void setBomlbdm(String bomlbdm) {
		this.bomlbdm = bomlbdm;
	}
	public String getU8ommxid() {
		return u8ommxid;
	}

	public void setU8ommxid(String u8ommxid) {
		this.u8ommxid = u8ommxid;
	}

	public String getU8cpjgid() {
		return u8cpjgid;
	}

	public void setU8cpjgid(String u8cpjgid) {
		this.u8cpjgid = u8cpjgid;
	}

	public String getJbyl() {
		return jbyl;
	}

	public void setJbyl(String jbyl) {
		this.jbyl = jbyl;
	}

	public String getJcsl() {
		return jcsl;
	}

	public void setJcsl(String jcsl) {
		this.jcsl = jcsl;
	}

	public String getZjshl() {
		return zjshl;
	}

	public void setZjshl(String zjshl) {
		this.zjshl = zjshl;
	}

	public String getCkdm() {
		return ckdm;
	}

	public void setCkdm(String ckdm) {
		this.ckdm = ckdm;
	}

	public String getCpsysl() {
		return cpsysl;
	}

	public void setCpsysl(String cpsysl) {
		this.cpsysl = cpsysl;
	}

	public String getBomlbmc() {
		return bomlbmc;
	}

	public void setBomlbmc(String bomlbmc) {
		this.bomlbmc = bomlbmc;
	}

	public String getCpjgid() {
		return cpjgid;
	}

	public void setCpjgid(String cpjgid) {
		this.cpjgid = cpjgid;
	}

	public String getMjshl() {
		return mjshl;
	}

	public void setMjshl(String mjshl) {
		this.mjshl = mjshl;
	}
	//付款方
	private String fkfmc;
	//质量要求
	private String zlyq;
	//维保要求
	private String wbyq;
	//配置要求
	private String pzyq;
	//入库单号
	private String rkdh;
	//生产批号
	private String scph;
	//入库类别
	private String rklbmc;
	//仓库名称
	private String ckmc;
	//追溯好
	private String zsh;
	//保修时间
	private String bxsj;
	//可入库数量
	private String krksl;
	//生产商
	private String scs;
	//已入库数量
	private String yrksl;
	//业务类型
	private String ywlxmc;
	//双章日期
	private String szrq;
	//提交日期
	private String tjrq;
	//合同外发日期
	private String htwfrq;
	//是否过期
	private String sfgq;
	//服务标记
	private String fwbj;
	private String ksrq;//开始日期
	private String dqrq;//到期日期

	public String getKsrq() {
		return ksrq;
	}

	public void setKsrq(String ksrq) {
		this.ksrq = ksrq;
	}

	public String getDqrq() {
		return dqrq;
	}

	public void setDqrq(String dqrq) {
		this.dqrq = dqrq;
	}

	public String getFwbj() {
		return fwbj;
	}

	public void setFwbj(String fwbj) {
		this.fwbj = fwbj;
	}

	public String getSfgq() {
		return sfgq;
	}

	public void setSfgq(String sfgq) {
		this.sfgq = sfgq;
	}

	public String getYrksl() {
		return yrksl;
	}

	public void setYrksl(String yrksl) {
		this.yrksl = yrksl;
	}

	public String getScs() {
		return scs;
	}



	public void setKrksl(String krksl) {
		this.krksl = krksl;
	}

	public String getDhslbj() {
		return dhslbj;
	}

	public void setDhslbj(String dhslbj) {
		this.dhslbj = dhslbj;
	}

	public String getBydhsl() {
		return bydhsl;
	}

	public void setBydhsl(String bydhsl) {
		this.bydhsl = bydhsl;
	}

	public String getBxsj() {
		return bxsj;
	}

	public void setBxsj(String bxsj) {
		this.bxsj = bxsj;
	}
	
	public String getRkdh() {
		return rkdh;
	}

	public void setRkdh(String rkdh) {
		this.rkdh = rkdh;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getRklbmc() {
		return rklbmc;
	}

	public void setRklbmc(String rklbmc) {
		this.rklbmc = rklbmc;
	}

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	public String getZsh() {
		return zsh;
	}

	public void setZsh(String zsh) {
		this.zsh = zsh;
	}

	public String getZlyq() {
		return zlyq;
	}

	public void setZlyq(String zlyq) {
		this.zlyq = zlyq;
	}

	public String getWbyq() {
		return wbyq;
	}

	public void setWbyq(String wbyq) {
		this.wbyq = wbyq;
	}

	public String getPzyq() {
		return pzyq;
	}

	public void setPzyq(String pzyq) {
		this.pzyq = pzyq;
	}

	@Override
	public String getHwmc() {
		return hwmc;
	}

	@Override
	public void setHwmc(String hwmc) {
		this.hwmc = hwmc;
	}

	//货物名称
	private String hwmc;

	
	public String getFkfmc() {
		return fkfmc;
	}

	

	


	public void setScs(String scs) {
		this.scs = scs;
	}

	public String getKrksl() {
		return krksl;
	}




	public void setFkfmc(String fkfmc) {
		this.fkfmc = fkfmc;
	}

	public String getZffs() {
		return zffs;
	}

	public void setZffs(String zffs) {
		this.zffs = zffs;
	}

	public String getShsj() {
		return shsj;
	}

	public void setShsj(String shsj) {
		this.shsj = shsj;
	}

	public String getSqly() {
		return sqly;
	}

	

	

	

	



	

	

	

	




	

	

	

	
	


	

	



	

	

	
	
	
	
	
}
