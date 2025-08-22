package com.matridx.igams.storehouse.dao.entities;


import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="DhxxDto")
public class DhxxDto extends DhxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//供应商名称
	private String gysmc;
	// 到货日期结束日期
	private String dhsjend;
	// 到货日期开始日期
	private String dhsjstart;
	// 全部(查询条件)
	private String entire;
	//供应商ID
	private String gys;
	//到货明细数据
	private String dhmxJson;
	//存放htids
	private String htnbbhs;
	private String SqlParam; 	//导出关联标记位//所选择的字段
	//查询参数[多个条件]
	private String searchParam;
	//货物信息list
	private List<HwxxDto> hwxxDtos;
	//货物id
	private String hwid;
	//请购明细id
	private String qgmxid;
	//合同明细id
	private String htmxid;
	//请购到货数量
	private String qgmxdhsl;
	//合同明细到货数量
	private String htmxdhsl;
	//合同明细到货单号关联
	private String htmxdhdhgl;
	//请购明细到货单号关联
	private String qgmxdhdhgl;
	//到货数量
	private String dhsl;
	//请购到货日期
	private String qgmxdhrq;
	//合同到货日期
	private String htmxdhrq;
	//质量类别参数扩展1（查询条件）
	private String lbcskz1;
	//是否限制类别参数扩展1(查询判断条件)
	private String sfxzlbcskz1;
	//仓库名称
	private String ckmc;
	//仓库ID
	private String ckid;
	//记录编号
	private String jlbh;
	//请购申请人
	private String qgsqr;
	//合同关联U8id
	private String u8poid;
	//仓库代码
	private String ckdm;
	//合同明细税率
	private String htmxsl;
	//合同币种
	private String htbz;
	//采购类型名称
	private String cglxmc;
	//供应商代码
	private String gysdm;
	//入库类型代码
	private String rklxdm;
	//机构代码
	private String jgdm;
	//申请部门名称
	private String sqbmmc;
	//申请部门
	private String sqbm;
	//采购类别
	private String cglb;
	//入库类型名称
	private String rklxmc;
	//合同内部编号
	private String htnbbh;
	//真实姓名
	private String zsxm;
	//物料名称
	private String wlmc;
	//借用标记
	private String jybj;
	//借用总量
	private String jyzl;
	//归还总量
	private String ghzl;

	//废弃标记（判断删除的数据是否为废弃）
	private String fqbj;
	//rabbit标记
	private String prefixFlg;

	//生产商
	private String scs;
	//计量单位
	private String jldw;
	//规格
	private String gg;
	//原厂货号
	private String ychh;
	//物料编码
	private String wlbm;
	//追朔号
	private String zsh;
	//生产批号
	private String scph;
	//有效期
	private String yxq;
	//验收单号
	private String ysdh;
	//到货测量温度
	private String bctj;
	//入库单号
	private String rkdh;
	//检验单号
	private String jydh;
	//参数扩展1
	private String cskz1;
	private String hwxx_json;
	//制单人
	private String lrrymc;
	//类别标记
	private String lbbj;
	//入库类别代码
	private String rklbdm;
	//U8入库单号
	private String u8rkdh;
	//到货类型代码
	private String dhlxdm;
	//到货类型名称
	private String dhlxmc;
	//到货类型[多]
	private String[] dhlxs;
	//入库类别[多]
	private String[] rklbs;
	//参数扩展3
	private String cskz3;
	//指令单号
	private String zldh;
	//部门扩展参数1
	private String bmcskz;
	//合同类型
	private String htlx;
	//委外u8关联id
	private String u8omid;
	//汇率
	private String hl;
	//出库单数
	private String ckds;
	//入库单数
	private String rkds;
	//开始日期
	private String dhrqstart;
	//结束日期
	private String dhrqend;
	//部门名称
	private String bmmc;
	//入库总数
	private String rkzs;
	//出库总数
	private String ckzs;
	private String rklbmc;
	private String ztmc;
	private String chhwid;
	private String ckqxlx;
	private String wlid;
	//附件IDS
	private List<String> fjids;

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getCkqxlx() {
		return ckqxlx;
	}

	public void setCkqxlx(String ckqxlx) {
		this.ckqxlx = ckqxlx;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getChhwid() {
		return chhwid;
	}

	public void setChhwid(String chhwid) {
		this.chhwid = chhwid;
	}

	public String getZtmc() {
		return ztmc;
	}

	public void setZtmc(String ztmc) {
		this.ztmc = ztmc;
	}

	public String getRklbmc() {
		return rklbmc;
	}

	public void setRklbmc(String rklbmc) {
		this.rklbmc = rklbmc;
	}


	public String getRkzs() {
		return rkzs;
	}

	public void setRkzs(String rkzs) {
		this.rkzs = rkzs;
	}

	public String getCkzs() {
		return ckzs;
	}

	public void setCkzs(String ckzs) {
		this.ckzs = ckzs;
	}

	public String getCkds() {
		return ckds;
	}

	public void setCkds(String ckds) {
		this.ckds = ckds;
	}

	public String getRkds() {
		return rkds;
	}

	public void setRkds(String rkds) {
		this.rkds = rkds;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
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
	

	public String getHl() {
		return hl;
	}

	public void setHl(String hl) {
		this.hl = hl;
	}

	public String getU8omid() {
		return u8omid;
	}

	public void setU8omid(String u8omid) {
		this.u8omid = u8omid;
	}

	public String getHtlx() {
		return htlx;
	}
	public void setHtlx(String htlx) {
		this.htlx = htlx;
	}
	public String getBmcskz() {
		return bmcskz;
	}

	public void setBmcskz(String bmcskz) {
		this.bmcskz = bmcskz;
	}
	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getCglb() {
		return cglb;
	}

	public void setCglb(String cglb) {
		this.cglb = cglb;
	}

	public String getZldh() {
		return zldh;
	}

	public void setZldh(String zldh) {
		this.zldh = zldh;
	}
	public String getCskz3() {
		return cskz3;
	}

	public void setCskz3(String cskz3) {
		this.cskz3 = cskz3;
	}

	public String getDhlxmc() {
		return dhlxmc;
	}

	public void setDhlxmc(String dhlxmc) {
		this.dhlxmc = dhlxmc;
	}

	public String[] getDhlxs() {
		return dhlxs;
	}

	public void setDhlxs(String[] dhlxs) {
		this.dhlxs = dhlxs;
		for(int i=0;i<this.dhlxs.length;i++)
		{
			this.dhlxs[i] = this.dhlxs[i].replace("'", "");
		}
	}
	public String[] getRklbs() {
		return rklbs;
	}

	public void setRklbs(String[] rklbs) {
		this.rklbs = rklbs;
		for(int i=0;i<this.rklbs.length;i++)
		{
			this.rklbs[i] = this.rklbs[i].replace("'", "");
		}
	}
	public String getDhlxdm() {
		return dhlxdm;
	}

	public void setDhlxdm(String dhlxdm) {
		this.dhlxdm = dhlxdm;
	}

	public String getU8rkdh() {
		return u8rkdh;
	}

	public void setU8rkdh(String u8rkdh) {
		this.u8rkdh = u8rkdh;
	}

	public String getRklbdm() {
		return rklbdm;
	}

	public void setRklbdm(String rklbdm) {
		this.rklbdm = rklbdm;
	}

	public String getLbbj() {
		return lbbj;
	}

	public void setLbbj(String lbbj) {
		this.lbbj = lbbj;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getHwxx_json() {
		return hwxx_json;
	}

	public void setHwxx_json(String hwxx_json) {
		this.hwxx_json = hwxx_json;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}

	public String getRkdh() {
		return rkdh;
	}

	public void setRkdh(String rkdh) {
		this.rkdh = rkdh;
	}

	public String getJydh() {
		return jydh;
	}

	public void setJydh(String jydh) {
		this.jydh = jydh;
	}

	public String getScs() {
		return scs;
	}

	public void setScs(String scs) {
		this.scs = scs;
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

	public String getYchh() {
		return ychh;
	}

	public void setYchh(String ychh) {
		this.ychh = ychh;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String getZsh() {
		return zsh;
	}

	public void setZsh(String zsh) {
		this.zsh = zsh;
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

	public String getYsdh() {
		return ysdh;
	}

	public void setYsdh(String ysdh) {
		this.ysdh = ysdh;
	}

	public String getBctj() {
		return bctj;
	}

	public void setBctj(String bctj) {
		this.bctj = bctj;
	}

	public String getFqbj() {
		return fqbj;
	}

	public void setFqbj(String fqbj) {
		this.fqbj = fqbj;
	}

	public String getPrefixFlg() {
		return prefixFlg;
	}

	public void setPrefixFlg(String prefixFlg) {
		this.prefixFlg = prefixFlg;
	}

	public String getJyzl() {
		return jyzl;
	}
	public void setJyzl(String jyzl) {
		this.jyzl = jyzl;
	}
	public String getGhzl() {
		return ghzl;
	}
	public void setGhzl(String ghzl) {
		this.ghzl = ghzl;
	}
	public String getJybj() {
		return jybj;
	}
	public void setJybj(String jybj) {
		this.jybj = jybj;
	}
	public String getWlmc() {
		return wlmc;
	}
	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}
	public String getZsxm() {
		return zsxm;
	}
	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}
	public String getHtnbbh() {
		return htnbbh;
	}
	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}
	//	//dhids 数组
//	private String[] dhids;
//	
//	public String[] getDhids() {
//		return dhids;
//	}
//	public void setDhids(String[] dhids) {
//		this.dhids = dhids;
//		for (int i = 0; i < dhids.length; i++){
//			this.dhids[i]=this.dhids[i].replace("'","");
//		}
//	}
//	
	public String getRklxmc() {
		return rklxmc;
	}
	public void setRklxmc(String rklxmc) {
		this.rklxmc = rklxmc;
	}
	public String getSqbmmc() {
		return sqbmmc;
	}
	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}
	public String getJgdm() {
		return jgdm;
	}
	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}
	public String getRklxdm() {
		return rklxdm;
	}
	public void setRklxdm(String rklxdm) {
		this.rklxdm = rklxdm;
	}
	public String getU8poid() {
		return u8poid;
	}
	public void setU8poid(String u8poid) {
		this.u8poid = u8poid;
	}
	public String getCkdm() {
		return ckdm;
	}
	public void setCkdm(String ckdm) {
		this.ckdm = ckdm;
	}
	public String getHtmxsl() {
		return htmxsl;
	}
	public void setHtmxsl(String htmxsl) {
		this.htmxsl = htmxsl;
	}
	public String getHtbz() {
		return htbz;
	}
	public void setHtbz(String htbz) {
		this.htbz = htbz;
	}
	public String getCglxmc() {
		return cglxmc;
	}
	public void setCglxmc(String cglxmc) {
		this.cglxmc = cglxmc;
	}
	public String getGysdm() {
		return gysdm;
	}
	public void setGysdm(String gysdm) {
		this.gysdm = gysdm;
	}
	public String getJlbh() {
		return jlbh;
	}
	public void setJlbh(String jlbh) {
		this.jlbh = jlbh;
	}
	public String getQgsqr() {
		return qgsqr;
	}
	public void setQgsqr(String qgsqr) {
		this.qgsqr = qgsqr;
	}
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
	public String getSfxzlbcskz1() {
		return sfxzlbcskz1;
	}
	public void setSfxzlbcskz1(String sfxzlbcskz1) {
		this.sfxzlbcskz1 = sfxzlbcskz1;
	}
	public String getLbcskz1() {
		return lbcskz1;
	}
	public void setLbcskz1(String lbcskz1) {
		this.lbcskz1 = lbcskz1;
	}
	public String getQgmxdhrq() {
		return qgmxdhrq;
	}
	public void setQgmxdhrq(String qgmxdhrq) {
		this.qgmxdhrq = qgmxdhrq;
	}
	public String getHtmxdhrq() {
		return htmxdhrq;
	}
	public void setHtmxdhrq(String htmxdhrq) {
		this.htmxdhrq = htmxdhrq;
	}
	public String getDhsl() {
		return dhsl;
	}
	public void setDhsl(String dhsl) {
		this.dhsl = dhsl;
	}
	public String getHwid() {
		return hwid;
	}
	public void setHwid(String hwid) {
		this.hwid = hwid;
	}
	public String getQgmxid() {
		return qgmxid;
	}
	public void setQgmxid(String qgmxid) {
		this.qgmxid = qgmxid;
	}
	public String getHtmxid() {
		return htmxid;
	}
	public void setHtmxid(String htmxid) {
		this.htmxid = htmxid;
	}
	public String getQgmxdhsl() {
		return qgmxdhsl;
	}
	public void setQgmxdhsl(String qgmxdhsl) {
		this.qgmxdhsl = qgmxdhsl;
	}
	public String getHtmxdhsl() {
		return htmxdhsl;
	}
	public void setHtmxdhsl(String htmxdhsl) {
		this.htmxdhsl = htmxdhsl;
	}
	public String getHtmxdhdhgl() {
		return htmxdhdhgl;
	}
	public void setHtmxdhdhgl(String htmxdhdhgl) {
		this.htmxdhdhgl = htmxdhdhgl;
	}
	public String getQgmxdhdhgl() {
		return qgmxdhdhgl;
	}
	public void setQgmxdhdhgl(String qgmxdhdhgl) {
		this.qgmxdhdhgl = qgmxdhdhgl;
	}
	public List<HwxxDto> getHwxxDtos() {
		return hwxxDtos;
	}
	public void setHwxxDtos(List<HwxxDto> hwxxDtos) {
		this.hwxxDtos = hwxxDtos;
	}
	public String getSqlParam() {
		return SqlParam;
	}
	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}
	public String getSearchParam() {
		return searchParam;
	}
	public void setSearchParam(String searchParam) {
		this.searchParam = searchParam;
	}
	public String getHtnbbhs() {
		return htnbbhs;
	}
	public void setHtnbbhs(String htnbbhs) {
		this.htnbbhs = htnbbhs;
	}
	public String getDhmxJson() {
		return dhmxJson;
	}
	public void setDhmxJson(String dhmxJson) {
		this.dhmxJson = dhmxJson;
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
	public String getDhsjend() {
		return dhsjend;
	}
	public void setDhsjend(String dhsjend) {
		this.dhsjend = dhsjend;
	}
	public String getDhsjstart() {
		return dhsjstart;
	}
	public void setDhsjstart(String dhsjstart) {
		this.dhsjstart = dhsjstart;
	}
	public String getEntire() {
		return entire;
	}
	public void setEntire(String entire) {
		this.entire = entire;
	}
}
