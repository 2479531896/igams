package com.matridx.igams.storehouse.dao.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.type.Alias;

@Alias(value="HwxxDto")
public class HwxxDto extends HwxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//物料编码
	private String wlbm;
	//物料名称
	private String wlmc;
	//到货单号
	private String dhdh;
	//红冲数量
	private String hcsl;
	//销售id
	private String xsid;
	//可引用数量
	private String kyysl;
	//销售明细id
	private String xsmxid;
	//可发货总数
	private String kfhzs;
	//发货数量
	private String fhsl;
	//到货日期
	private String dhrq;
	//供应商名称
	private String gysmc;
	//类别参数名称
	private String lbcsmc;
	//类别参数名称
	private String fhid;
	//计划到货日期
	private String jhdhrq;

	private String lbbj;

	public String getJhdhrq() {
		return jhdhrq;
	}

	public void setJhdhrq(String jhdhrq) {
		this.jhdhrq = jhdhrq;
	}

	//物料类别
	private String lb;
	private String zlb;
	private String kczt;
	//类别参数代码
	private String lbcsdm;
	//类别参数扩展1
	private String lbcskz1;
	//扩展参数
	private String kzcs;
	//物料单位
	private String jldw;
	//物料规格
	private String gg;
	//合同内部编号
	private String htnbbh;
	//参数扩展1
	private String cskz1;
	// 全部(查询条件)
	private String entire;
	//检验状态
	private String jyzt;
	//u8质检退回数量
	private String U8zjrksl;
	//到货数量-初验数量
	private String reduce;
	//到货ID
	private String dhid;
	//合同ID
	private String htid;
	//合同数量
	private String htsl;
	//原厂货号
	private String ychh;
	//无税单价
	private String wsdj;
	//合同明细合计金额
	private String hjje;
	//合同税率
	private String suil;
	//含税单价
	private String hsdj;
	//关联U8id
	private String u8cgid;
	//入库数量：到货数量-初验退回数量-质检退回数量
	private String rksl;
    //入库人员名称
	private String rkrymc;
	//退回数量
	private String thsl;
	//检验单号
	private String jydh;
	//入库单号
	private String rkdh;
	private String SqlParam; 	//导出关联标记位//所选择的字段
	//查询参数[多个条件]
	private String searchParam;
	// 到货日期结束日期
	private String dhsjend;
	// 到货日期开始日期
	private String dhsjstart;
	// 检索用类别（多）
	private String[] lbs;
	// 检索用到货类型（多）
	private String[] dhlxs;
	// 检索用入库类别（多）
	private String[] rklbs;
	//剩余数量(数量-入库数量)
	private String sysl;
	//实际数量
	private String sjsl;
	//申请部门名称
	private String sqbmmc;
	//申请部门
	private String sqbm;
	//备用已处理数
	private String byycls;
	//已处理数标记
	private String yclsbj;
	//供应商id
	private String gysid;
	//合同明细备注
	private String htmxbz;
	//申请日期
	private String sqrq;
	//申请理由
	private String sqly;
	//请购名称
	private String qggysmc;
	//质量要求
	private String zlyq;
	//货物备注
	private String hwbz;
	//货物用途
	private String hwyt;
	//配置要求
	private String pzyq;
	//维保要求
	private String wbyq;
	//请购明细备注
	private String qgmxbz;
	//条数
	private String ts;
	//行状态
	private String hzt;
//钉钉查询字段
	private String ddentire;
	//合同主表备注
	private String htzbt;
	//到货主表备注
	private String dhzbz;
	//退货单号
	private String thdh;
	//出库单号
	private String ckdh;
	//采购红字标记
	private String cghzbj;
	//可红冲数
	private String khcsl;
	//处理标记
	private String clbj;
	private String sqryhm;//申请人用户名
	private String mxsl;//明细数量
	private String sl_t;
	private String hq;//货期
	private String xlh;//设备序列号
	private String sbccbh;//出厂编号
	private String gdzcbh;//固定资产编号
	private String sbbh;//设备编号
	private String qwfs;//去污方式
	private String sbysid;//设备验收id
	private String yhwkcl;//原货物库存量
	private String yhwbhgsl;//原货物不合格数量
	private List<String> jcghids;//借出归还ids
	private String hgbj;//合格标记
	private String jsid;//角色id
	private String xzbj;//限制标记
	private String iquantity;
	private String hwkcl;
	private String zlysl;
	private String lykcid;
	private String copyflg;
	private String zsxm;

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getZlb() {
		return zlb;
	}

	public void setZlb(String zlb) {
		this.zlb = zlb;
	}

	public String getKczt() {
		return kczt;
	}

	public void setKczt(String kczt) {
		this.kczt = kczt;
	}

	public String getCopyflg() {
		return copyflg;
	}

	public void setCopyflg(String copyflg) {
		this.copyflg = copyflg;
	}

	public String getLykcid() {
		return lykcid;
	}

	public void setLykcid(String lykcid) {
		this.lykcid = lykcid;
	}

	public String getZlysl() {
		return zlysl;
	}

	public void setZlysl(String zlysl) {
		this.zlysl = zlysl;
	}

	public String getHwkcl() {
		return hwkcl;
	}

	public void setHwkcl(String hwkcl) {
		this.hwkcl = hwkcl;
	}

	public String getIquantity() {
		return iquantity;
	}

	public void setIquantity(String iquantity) {
		this.iquantity = iquantity;
	}

	public String getXzbj() {
		return xzbj;
	}

	public void setXzbj(String xzbj) {
		this.xzbj = xzbj;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public String getHgbj() {
		return hgbj;
	}

	public void setHgbj(String hgbj) {
		this.hgbj = hgbj;
	}

	public List<String> getJcghids() {
		return jcghids;
	}

	public void setJcghids(List<String> jcghids) {
		this.jcghids = jcghids;
	}

	public String getYhwkcl() {
		return yhwkcl;
	}

	public void setYhwkcl(String yhwkcl) {
		this.yhwkcl = yhwkcl;
	}

	public String getYhwbhgsl() {
		return yhwbhgsl;
	}

	public void setYhwbhgsl(String yhwbhgsl) {
		this.yhwbhgsl = yhwbhgsl;
	}

	public String getQwfs() {
		return qwfs;
	}

	public void setQwfs(String qwfs) {
		this.qwfs = qwfs;
	}

	public String getSbysid() {
		return sbysid;
	}

	public void setSbysid(String sbysid) {
		this.sbysid = sbysid;
	}

	public String getXlh() {
		return xlh;
	}

	public void setXlh(String xlh) {
		this.xlh = xlh;
	}

	public String getSbccbh() {
		return sbccbh;
	}

	public void setSbccbh(String sbccbh) {
		this.sbccbh = sbccbh;
	}

	public String getGdzcbh() {
		return gdzcbh;
	}

	public void setGdzcbh(String gdzcbh) {
		this.gdzcbh = gdzcbh;
	}

	public String getSbbh() {
		return sbbh;
	}

	public void setSbbh(String sbbh) {
		this.sbbh = sbbh;
	}

	public String getHq() {
		return hq;
	}

	public void setHq(String hq) {
		this.hq = hq;
	}
	public String getSl_t() {
		return sl_t;
	}

	public void setSl_t(String sl_t) {
		this.sl_t = sl_t;
	}

	public String getMxsl() {
		return mxsl;
	}

	public void setMxsl(String mxsl) {
		this.mxsl = mxsl;
	}

	public String getSqryhm() {
		return sqryhm;
	}

	public void setSqryhm(String sqryhm) {
		this.sqryhm = sqryhm;
	}
	public String getClbj() {
		return clbj;
	}

	public void setClbj(String clbj) {
		this.clbj = clbj;
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

	public String getCkdh() {
		return ckdh;
	}

	public void setCkdh(String ckdh) {
		this.ckdh = ckdh;
	}

	public String getThdh() {
		return thdh;
	}

	public void setThdh(String thdh) {
		this.thdh = thdh;
	}

	public String getDhzbz() {
		return dhzbz;
	}

	public void setDhzbz(String dhzbz) {
		this.dhzbz = dhzbz;
	}

	public String getHtzbt() {
		return htzbt;
	}

	public void setHtzbt(String htzbt) {
		this.htzbt = htzbt;
	}

	public String getDdentire() {
		return ddentire;
	}

	public void setDdentire(String ddentire) {
		this.ddentire = ddentire;
	}
	public String getHzt() {
		return hzt;
	}

	public void setHzt(String hzt) {
		this.hzt = hzt;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getQgmxbz() {
		return qgmxbz;
	}

	public void setQgmxbz(String qgmxbz) {
		this.qgmxbz = qgmxbz;
	}

	public String getQggysmc() {
		return qggysmc;
	}

	public void setQggysmc(String qggysmc) {
		this.qggysmc = qggysmc;
	}

	public String getSqly() {
		return sqly;
	}

	public void setSqly(String sqly) {
		this.sqly = sqly;
	}

	public String getSqrq() {
		return sqrq;
	}

	public void setSqrq(String sqrq) {
		this.sqrq = sqrq;
	}

	public String getHtmxbz() {
		return htmxbz;
	}

	public void setHtmxbz(String htmxbz) {
		this.htmxbz = htmxbz;
	}

	//到货检验状态
	private String dhjyzt;
	//已用出库明细单
	private String yymxckd;
	//货物ids
	private List<String> hwids;
	//到货检验ids
	private List<String> dhjyids;
	//总库存量
	private String zkcl;
	//请购ID
	private String qgid;
	//请购申请人
	private String qgsqr;
	//请购人钉钉ID
	private String qgrddid;
	//请购单号
	private String qgdh;
	//已到货数量	
	private String ydhsl;
	//总到货数量
	private String zdhsl;
	//到货单号关联请购
	private String dhdhglqg;
	//到货单号关联合同
	private String dhdhglht;
	//请购项目大类代码
	private String xmdldm;
	//请购项目编码代码
	private String xmbmdm;
	//请购项目编码名称
	private String xmbmmc;
	//合同明细关联U8id
	private String u8mxid;
	//供应商代码
	private String gysdm;
	//物料保质期
	private String bzq;
	//物料保质期标记
	private String bzqflg;
	//货物信息关联附件id
	private List<String> fjids;
	//是否存在附件标记
	private String fjbj;
	//是否限制类别参数扩展1(查询判断条件)
	private String sfxzlbcskz1;
	//请购明细关联U8
	private String qgglid;
	//合同明细关联id
	private String htglid;
	//到货明细关联id
	private String dhglid;
	//项目大类名称
	private String xmdlmc;
	//可领数量
	private String klsl;
	//领料id
	private String llid;
	//实领数量
	private String slsl;
	//货物领料id
	private String hwllid;
	//请领数量
	private String qlsl;
	//mx请领数量
	private String mxqlsl;
	//xx请领数量
	private String xxqlsl;

	//预定数标记
	private String ydsbj;
	//货物状态
	private String hwzt;
	//允许数量
	private String yxsl;
	//前允许数量
	private String qyxsl;
	//要更新的库存量
	private String modkcl;
	//到货 ids
	private List<String> dhids;
	//库位名称
	private String kwmc;
	//初始库位名称
	private String cskwmc;
	//库位编号代码
	private String kwbhdm;
	//初始库位代码
	private String cskwdm;
	//领料单号
	private String lldh;
	//新增的取样量
	private String addqyl;
	//仓库代码
	private String ckdm;
	//出库数量
	private String cksl;
	//库位编号名称
	private String kwbhmc;
	//不合格关联id
	private String bhgglid;
	//U8入库数量
	private String U8rksl;
	//合同备注
	private String htbz;
	//增加库存量
	private String addkcl;
	//借用人员名称
	private String jyrymc;
	//归还人员名称
	private String ghrymc;
	//仓库名称
	private String ckmc;
	//借用总量
	private String jyzl;
	//归还总量
	private String ghzl;
	//归还总量
	private String llmxid;
	//归还总量
	private String rkzt;
	//请购到货数量
	private String qgdhsl;
	//请购到货数量
	private String htdhsl;
	//请购记录编号
	private String qgjlbh;
	//仓库库存量
	private String ckkcl;
	//rkgl.dbglid
	private String rkdbglid;
	//llgl。zt
	private String llzt;
	//hwllmx.ckglid
	private String mxckglid;
	//请购明细关联U8
	private String hwmxglid;
	//出库状态
	private String ckzt;
	//qtckglid
	private String qtckglid;
	//转出仓库
	private String zrck;
	//转出仓库
	private String zcck;
	//调拨id
	private String dbid;
	//调拨数量
	private String dbsl;
	//U8调拨数量
	private String U8dbsl;
	//转出仓库代码
	private String zcckdm;
	//转入仓库代码
	private String zrckdm;
	//调拨明细id
	private String dbmxid;
	//调出货位代码
	private String dchwdm;
	//调入货位代码
	private String drhwdm;
	//生产商
	private String scs;
	//htgl.htnbbh(合同单号)
	private String htdh;
	//供应商
	private String gys;
	//仓库分类
	private List<String> ckqxlxs;
	//仓库分类
	private String ckqxlx;
	//仓库分类
	private String ckqxmc;
	//调入货物id
	private String drhwid;
	//入库类型参数扩展1
	private String rklxcskz1;
	//入库类别
	private String rklb;
	//采购类别
	private String cglb;
	//采购类别代码
	private String cglbdm;
	//采购类别名称
	private String cglbmc;
	//入库类别
	private String rklbmc;
	//出库明细id
	private String ckmxid;
	//销售订单
	private String xsdd;
	//订单明细id
	private String ddmxid;
	//待处理数量
	private String dclsl;
	//已处理数量
	private String yclsl;
	//到货仓库名称
	private String dhckmc;
	//到货仓库id
	private String dhckid;
	//仓库货物id
	private String ckhwid;
	//是否废弃领料单
	private String sffqlld;
	//到货仓库代码
	private String dhckdm;
	//领料出库关联id
	private String llckglid;
	//领料出库id
	private String llckid;
	//到货日期开始时间
	private String dhrqstart;
	//到货日期结束时间
	private String dhrqend;
	//入库日期开始时间
	private String rkrqstart;
	//入库日期结束时间
	private String rkrqend;
	//质检日期开始时间
	private String zjrqstart;
	//质检日期结束时间
	private String zjrqend;
	//设备验收单号
	private String sbysdh;
	//入库类别代码
	private String rklbdm;
	//到货类型名称
	private String dhlxmc;
	//到货类型代码
	private String dhlxdm;
	//领料类型
	private String lllx;
	private String sfgdzc;
	//保修时间
	private String bxsj;
	//索引
	private String index;
	//可出数量
	private String kcsl;
	//成品机构明细ID
	private String cpjgmxid;
	//合同u8ommxid
	private String u8ommxid;
	//合同类型
	private String htlx;
    //指令单号
	private String zldh;
	//质检备注
	private String zjbz;
	//请购数量
	private String qgsl;
	//请购备注
	private String qgbz;
	//质检人员
	private String zjrymc;
	//入库仓库
	private String rkckmc;
	//物料备注
	private String wlbz;
	//入库供应商名称
	private String rkgysmc;
	//入库状态
	private String rkztmc;
	//检验结果
	private String jyjgmc;
	//检验日期
	private String jyrq;
	//检验负责人名称
	private String jyfzrmc;
	//付款方式名称
	private String fkfmc;
	//支付方式
	private String zffs;
	//审核时间
	private String shsj;
	//请购类别代码
	private String qglbdm;
	//请购类别
	private String qglb;
	//请购类型名称
	private String qglxmc;
	//请验数量
	private String qysl;
	//年份
	private String year;
	//入库ids
	private List<String> rkids;
	private String[] wllbs;	//检索用物料类别（多）
	private String[] wlzlbs;	//检索用物料子类别（多）
	private String[] sfwxps;	//检索用是否危险品（多）
	//申请人名称
	private String sqrmc;
	//请购类别名称
	private String qglbmc;
	//百分比
	private double bfb;
	//
	private int count;
	private String lcsl;//领出数量
	private String ztsl;//在途数量
	private String yf;//月份
	private String sbmc;//设备名称
	private String ggxh;//规格型号
	private String xqjhid;//需求计划id
	private String bhgyy;//不合格原因
	private String[] cks;
	public String[] getCks() {
		return cks;
	}

	public void setCks(String[] cks) {
		this.cks = cks;
	}
	public String getBhgyy() {
		return bhgyy;
	}

	public void setBhgyy(String bhgyy) {
		this.bhgyy = bhgyy;
	}

	public String getXqjhid() {
		return xqjhid;
	}

	public void setXqjhid(String xqjhid) {
		this.xqjhid = xqjhid;
	}

	public String getSbmc() {
		return sbmc;
	}

	public void setSbmc(String sbmc) {
		this.sbmc = sbmc;
	}

	public String getGgxh() {
		return ggxh;
	}

	public void setGgxh(String ggxh) {
		this.ggxh = ggxh;
	}

	public String getYf() {
		return yf;
	}

	public void setYf(String yf) {
		this.yf = yf;
	}

	public String getZtsl() {
		return ztsl;
	}

	public void setZtsl(String ztsl) {
		this.ztsl = ztsl;
	}

	public String getLcsl() {
		return lcsl;
	}

	public void setLcsl(String lcsl) {
		this.lcsl = lcsl;
	}


	public double getBfb() {
		return bfb;
	}

	public void setBfb(double bfb) {
		this.bfb = bfb;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getQglbmc() {
		return qglbmc;
	}

	public void setQglbmc(String qglbmc) {
		this.qglbmc = qglbmc;
	}

	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public String[] getWllbs() {
		return wllbs;
	}

	public void setWllbs(String[] wllbs) {
		this.wllbs = wllbs;
		for(int i=0;i<this.wllbs.length;i++)
		{
			this.wllbs[i] = this.wllbs[i].replace("'", "");
		}
	}

	public String[] getSfwxps() {
		return sfwxps;
	}

	public void setSfwxps(String[] sfwxps) {
		this.sfwxps = sfwxps;
		for(int i=0;i<this.sfwxps.length;i++)
		{
			this.sfwxps[i] = this.sfwxps[i].replace("'", "");
		}
	}
	public String[] getWlzlbs() {
		return wlzlbs;
	}

	public void setWlzlbs(String[] wlzlbs) {
		this.wlzlbs = wlzlbs;
		for(int i=0;i<this.wlzlbs.length;i++)
		{
			this.wlzlbs[i] = this.wlzlbs[i].replace("'", "");
		}
	}
	public List<String> getRkids() {
		return rkids;
	}

	public void setRkids(List<String> rkids) {
		this.rkids = rkids;
	}
	public String getZjbz() {
		return zjbz;
	}

	public void setZjbz(String zjbz) {
		this.zjbz = zjbz;
	}


	

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getQysl() {
		return qysl;
	}

	public void setQysl(String qysl) {
		this.qysl = qysl;
	}

	public String getCpjgmxid() {
		return cpjgmxid;
	}


	public String getJyfzrmc() {
		return jyfzrmc;
	}

	public void setJyfzrmc(String jyfzrmc) {
		this.jyfzrmc = jyfzrmc;
	}

	public String getJyrq() {
		return jyrq;
	}

	public void setJyrq(String jyrq) {
		this.jyrq = jyrq;
	}

	public String getJyjgmc() {
		return jyjgmc;
	}

	public void setJyjgmc(String jyjgmc) {
		this.jyjgmc = jyjgmc;
	}

	public String getRkgysmc() {
		return rkgysmc;
	}

	public void setRkgysmc(String rkgysmc) {
		this.rkgysmc = rkgysmc;
	}

	public String getRkztmc() {
		return rkztmc;
	}

	public void setRkztmc(String rkztmc) {
		this.rkztmc = rkztmc;
	}

	public String getWlbz() {
		return wlbz;
	}

	public void setWlbz(String wlbz) {
		this.wlbz = wlbz;
	}

	public String getRkckmc() {
		return rkckmc;
	}

	public void setRkckmc(String rkckmc) {
		this.rkckmc = rkckmc;
	}

	public String getZjrymc() {
		return zjrymc;
	}

	public void setZjrymc(String zjrymc) {
		this.zjrymc = zjrymc;
	}

	public String getQgbz() {
		return qgbz;
	}

	public void setQgbz(String qgbz) {
		this.qgbz = qgbz;
	}

	public String getQgsl() {
		return qgsl;
	}

	public void setQgsl(String qgsl) {
		this.qgsl = qgsl;
	}

	public void setCpjgmxid(String cpjgmxid) {
		this.cpjgmxid = cpjgmxid;
	}

	public String getKcsl() {
		return kcsl;
	}
	
	public void setKcsl(String kcsl) {
		this.kcsl = kcsl;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
	public String[] getRklbs() {
		return rklbs;
	}

	public void setRklbs(String[] rklbs) {
		this.rklbs = rklbs;
		if(rklbs!=null && rklbs.length>0) {
			for(int i=0;i<this.rklbs.length;i++)
			{
				this.rklbs[i] = this.rklbs[i].replace("'", "");
			}
		}
	}

	public String getHcsl() {
		return hcsl;
	}

	public void setHcsl(String hcsl) {
		this.hcsl = hcsl;
	}

	public String getByycls() {
		return byycls;
	}

	public void setByycls(String byycls) {
		this.byycls = byycls;
	}

	public String getYclsbj() {
		return yclsbj;
	}

	public void setYclsbj(String yclsbj) {
		this.yclsbj = yclsbj;
	}

	public String getCglb() {
		return cglb;
	}

	public void setCglb(String cglb) {
		this.cglb = cglb;
	}

	public String getCglbdm() {
		return cglbdm;
	}

	public void setCglbdm(String cglbdm) {
		this.cglbdm = cglbdm;
	}

	public String getCglbmc() {
		return cglbmc;
	}

	public void setCglbmc(String cglbmc) {
		this.cglbmc = cglbmc;
	}
	public String getHtlx() {
		return htlx;
	}

	public void setHtlx(String htlx) {
		this.htlx = htlx;
	}

	public String getU8ommxid() {
		return u8ommxid;
	}

	public void setU8ommxid(String u8ommxid) {
		this.u8ommxid = u8ommxid;
	}
	public String getZldh() {
		return zldh;
	}

	public void setZldh(String zldh) {
		this.zldh = zldh;
	}
	
	public void setBxsj(String bxsj) {
		this.bxsj = bxsj;
	}
	public String getBxsj() {
		return bxsj;
	}

	public String getKyysl() {
		return kyysl;
	}

	public void setKyysl(String kyysl) {
		this.kyysl = kyysl;
	}

	//增加的数量
	private String addsl;
	public String getAddsl() {
		return addsl;
	}

	public void setAddsl(String addsl) {
		this.addsl = addsl;
	}
	
	public String getSfgdzc() {
		return sfgdzc;
	}

	public void setSfgdzc(String sfgdzc) {
		this.sfgdzc = sfgdzc;
	}
	public String getYymxckd() {
		return yymxckd;
	}

	public void setYymxckd(String yymxckd) {
		this.yymxckd = yymxckd;
	}

	public String getLllx() {
		return lllx;
	}

	public void setLllx(String lllx) {
		this.lllx = lllx;
	}

	public String getDhlxdm() {
		return dhlxdm;
	}

	public void setDhlxdm(String dhlxdm) {
		this.dhlxdm = dhlxdm;
	}

	public String[] getDhlxs() {
		return dhlxs;
	}

	public void setDhlxs(String[] dhlxs) {
		this.dhlxs = dhlxs;
		if(dhlxs!=null && dhlxs.length>0) {
			for(int i=0;i<this.dhlxs.length;i++)
			{
				this.dhlxs[i] = this.dhlxs[i].replace("'", "");
			}
		}		
	}

	public String getDhlxmc() {
		return dhlxmc;
	}

	public void setDhlxmc(String dhlxmc) {
		this.dhlxmc = dhlxmc;
	}

	public String getFhsl() {
		return fhsl;
	}

	public void setFhsl(String fhsl) {
		this.fhsl = fhsl;
	}

	public String getFhid() {
		return fhid;
	}

	public void setFhid(String fhid) {
		this.fhid = fhid;
	}

	public String getKfhzs() {
		return kfhzs;
	}

	public void setKfhzs(String kfhzs) {
		this.kfhzs = kfhzs;
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

	public String getQyxsl() {
		return qyxsl;
	}

	public void setQyxsl(String qyxsl) {
		this.qyxsl = qyxsl;
	}

	public String getRklbdm() {
		return rklbdm;
	}

	public void setRklbdm(String rklbdm) {
		this.rklbdm = rklbdm;
	}

	public String getSbysdh() {
		return sbysdh;
	}

	public void setSbysdh(String sbysdh) {
		this.sbysdh = sbysdh;
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

	public String getRkrqstart() {
		return rkrqstart;
	}

	public void setRkrqstart(String rkrqstart) {
		this.rkrqstart = rkrqstart;
	}

	public String getRkrqend() {
		return rkrqend;
	}

	public void setRkrqend(String rkrqend) {
		this.rkrqend = rkrqend;
	}

	public String getZjrqstart() {
		return zjrqstart;
	}

	public void setZjrqstart(String zjrqstart) {
		this.zjrqstart = zjrqstart;
	}

	public String getZjrqend() {
		return zjrqend;
	}

	public void setZjrqend(String zjrqend) {
		this.zjrqend = zjrqend;
	}

	public String getLlckid() {
		return llckid;
	}

	public void setLlckid(String llckid) {
		this.llckid = llckid;
	}

	public String getU8zjrksl() {
		return U8zjrksl;
	}

	public void setU8zjrksl(String u8zjrksl) {
		U8zjrksl = u8zjrksl;
	}

	public String getLlckglid() {
		return llckglid;
	}

	public void setLlckglid(String llckglid) {
		this.llckglid = llckglid;
	}

	public String getDhckdm() {
		return dhckdm;
	}

	public void setDhckdm(String dhckdm) {
		this.dhckdm = dhckdm;
	}

	public String getSffqlld() {
		return sffqlld;
	}

	public void setSffqlld(String sffqlld) {
		this.sffqlld = sffqlld;
	}

	public String getCkhwid() {
		return ckhwid;
	}

	public void setCkhwid(String ckhwid) {
		this.ckhwid = ckhwid;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}
	public String getDhckmc() {
		return dhckmc;
	}

	public void setDhckmc(String dhckmc) {
		this.dhckmc = dhckmc;
	}

	public String getDhckid() {
		return dhckid;
	}

	public void setDhckid(String dhckid) {
		this.dhckid = dhckid;
	}
	public String getYclsl() {
		return yclsl;
	}

	public void setYclsl(String yclsl) {
		this.yclsl = yclsl;
	}

	public String getDclsl() {
		return dclsl;
	}

	public void setDclsl(String dclsl) {
		this.dclsl = dclsl;
	}

	public String getXsdd() {
		return xsdd;
	}

	public void setXsdd(String xsdd) {
		this.xsdd = xsdd;
	}
	
	public String getDdmxid() {
		return ddmxid;
	}

	public void setDdmxid(String ddmxid) {
		this.ddmxid = ddmxid;
	}

	public String getCkmxid() {
		return ckmxid;
	}

	public void setCkmxid(String ckmxid) {
		this.ckmxid = ckmxid;
	}


	public String getCksl() {
		return cksl;
	}

	public void setCksl(String cksl) {
		this.cksl = cksl;
	}

	public String getCkqxmc() {
		return ckqxmc;
	}

	public void setCkqxmc(String ckqxmc) {
		this.ckqxmc = ckqxmc;
	}

	public String getRklbmc() {
		return rklbmc;
	}

	public void setRklbmc(String rklbmc) {
		this.rklbmc = rklbmc;
	}

	public String getRklb() {
		return rklb;
	}

	public void setRklb(String rklb) {
		this.rklb = rklb;
	}

	public String getRklxcskz1() {
		return rklxcskz1;
	}

	public void setRklxcskz1(String rklxcskz1) {
		this.rklxcskz1 = rklxcskz1;
	}

	public String getDrhwid() {
		return drhwid;
	}

	public void setDrhwid(String drhwid) {
		this.drhwid = drhwid;
	}

	public List<String> getCkqxlxs() {
		return ckqxlxs;
	}

	public void setCkqxlxs(List<String> ckqxlxs) {
		this.ckqxlxs = ckqxlxs;
	}

	public String getCkqxlx() {
		return ckqxlx;
	}

	public void setCkqxlx(String ckqxlx) {
		this.ckqxlx = ckqxlx;
	}

	//wlids
	private List<String> wlids;
	//ckids
	private List<String> ckids;

	public List<String> getCkids() {
		return ckids;
	}

	public void setCkids(List<String> ckids) {
		this.ckids = ckids;
	}

	public List<String> getWlids() {
		return wlids;
	}

	public void setWlids(List<String> wlids) {
		this.wlids = wlids;
	}

	public String getGys() {
		return gys;
	}

	public void setGys(String gys) {
		this.gys = gys;
	}

	public String getHtdh() {
		return htdh;
	}

	public void setHtdh(String htdh) {
		this.htdh = htdh;
	}

	public String getDchwdm() {
		return dchwdm;
	}

	public void setDchwdm(String dchwdm) {
		this.dchwdm = dchwdm;
	}

	public String getDrhwdm() {
		return drhwdm;
	}

	public void setDrhwdm(String drhwdm) {
		this.drhwdm = drhwdm;
	}

	public String getDbmxid() {
		return dbmxid;
	}

	public void setDbmxid(String dbmxid) {
		this.dbmxid = dbmxid;
	}

	public String getZrckdm() {
		return zrckdm;
	}

	public void setZrckdm(String zrckdm) {
		this.zrckdm = zrckdm;
	}

	public String getZcckdm() {
		return zcckdm;
	}

	public void setZcckdm(String zcckdm) {
		this.zcckdm = zcckdm;
	}

	public String getU8dbsl() {
		return U8dbsl;
	}

	public void setU8dbsl(String u8dbsl) {
		U8dbsl = u8dbsl;
	}

	public String getDbsl() {
		return dbsl;
	}

	public void setDbsl(String dbsl) {
		this.dbsl = dbsl;
	}
	
	public String getScs() {
		return scs;
	}

	public void setScs(String scs) {
		this.scs = scs;
	}

	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}

	public String getZrck() {
		return zrck;
	}

	public void setZrck(String zrck) {
		this.zrck = zrck;
	}

	public String getZcck() {
		return zcck;
	}

	public void setZcck(String zcck) {
		this.zcck = zcck;
	}

	public void setDbglid(String dbglid) {
		this.dbglid = dbglid;
	}

	public String getQtckglid() {
		return qtckglid;
	}

	public void setQtckglid(String qtckglid) {
		this.qtckglid = qtckglid;
	}

	public String getCkzt() {
		return ckzt;
	}

	public void setCkzt(String ckzt) {
		this.ckzt = ckzt;
	}

	public String getHwmxglid() {
		return hwmxglid;
	}
	private String dbglid;
	//机构信息扩展参数
	private String jg_cskz1;

	public String getJg_cskz1() {
		return jg_cskz1;
	}

	public void setJg_cskz1(String jg_cskz1) {
		this.jg_cskz1 = jg_cskz1;
	}

	public void setHwmxglid(String hwmxglid) {
		this.hwmxglid = hwmxglid;
	}

	public String getMxckglid() {
		return mxckglid;
	}

	public void setMxckglid(String mxckglid) {
		this.mxckglid = mxckglid;
	}

	public String getLlzt() {
		return llzt;
	}

	public void setLlzt(String llzt) {
		this.llzt = llzt;
	}

	public String getRkdbglid() {
		return rkdbglid;
	}
	public String getDbglid() {
		return dbglid;
	}

	public void setRkdbglid(String rkdbglid) {
		this.rkdbglid = rkdbglid;
	}

	public String getCkkcl() {
		return ckkcl;
	}

	public void setCkkcl(String ckkcl) {
		this.ckkcl = ckkcl;
	}

	public String getQgjlbh() {
		return qgjlbh;
	}

	public void setQgjlbh(String qgjlbh) {
		this.qgjlbh = qgjlbh;
	}

	public String getDhglid() {
		return dhglid;
	}

	public void setDhglid(String dhglid) {
		this.dhglid = dhglid;
	}

	public String getHtdhsl() {
		return htdhsl;
	}

	public void setHtdhsl(String htdhsl) {
		this.htdhsl = htdhsl;
	}

	public String getQgdhsl() {
		return qgdhsl;
	}

	public void setQgdhsl(String qgdhsl) {
		this.qgdhsl = qgdhsl;
	}

	public String getRkzt() {
		return rkzt;
	}

	public void setRkzt(String rkzt) {
		this.rkzt = rkzt;
	}

	public String getMxqlsl() {
		return mxqlsl;
	}

	public void setMxqlsl(String mxqlsl) {
		this.mxqlsl = mxqlsl;
	}

	public String getXxqlsl() {
		return xxqlsl;
	}

	public void setXxqlsl(String xxqlsl) {
		this.xxqlsl = xxqlsl;
	}

	//领料明细id
	public String getLlmxid() {
		return llmxid;
	}

	public void setLlmxid(String llmxid) {
		this.llmxid = llmxid;
	}

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
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
	public String getJyrymc() {
		return jyrymc;
	}
	public void setJyrymc(String jyrymc) {
		this.jyrymc = jyrymc;
	}
	public String getGhrymc() {
		return ghrymc;
	}
	public void setGhrymc(String ghrymc) {
		this.ghrymc = ghrymc;
	}
	public String getAddkcl() {
		return addkcl;
	}
	public void setAddkcl(String addkcl) {
		this.addkcl = addkcl;
	}
	public String getHtbz() {
		return htbz;
	}
	public void setHtbz(String htbz) {
		this.htbz = htbz;
	}
	public String getU8rksl() {
		return U8rksl;
	}
	public void setU8rksl(String u8rksl) {
		U8rksl = u8rksl;
	}
	public String getBhgglid() {
		return bhgglid;
	}
	public void setBhgglid(String bhgglid) {
		this.bhgglid = bhgglid;
	}
	public String getKwbhmc() {
		return kwbhmc;
	}
	public void setKwbhmc(String kwbhmc) {
		this.kwbhmc = kwbhmc;
	}
	public String getCkdm() {
		return ckdm;
	}
	public void setCkdm(String ckdm) {
		this.ckdm = ckdm;
	}
	public String getAddqyl() {
		return addqyl;
	}
	public void setAddqyl(String addqyl) {
		this.addqyl = addqyl;
	}
	public String getLldh() {
		return lldh;
	}
	public void setLldh(String lldh) {
		this.lldh = lldh;
	}
	public String getKwbhdm() {
		return kwbhdm;
	}
	public void setKwbhdm(String kwbhdm) {
		this.kwbhdm = kwbhdm;
	}
	public String getCskwdm() {
		return cskwdm;
	}
	public void setCskwdm(String cskwdm) {
		this.cskwdm = cskwdm;
	}
	public String getCskwmc() {
		return cskwmc;
	}
	public void setCskwmc(String cskwmc) {
		this.cskwmc = cskwmc;
	}

	public String getKwmc() {
		return kwmc;
	}
	public void setKwmc(String kwmc) {
		this.kwmc = kwmc;
	}
	public List<String> getDhids() {
		return dhids;
	}
	public void setDhids(List<String> dhids) {
		this.dhids = dhids;
	}
	public String getModkcl() {
		return modkcl;
	}
	public void setModkcl(String modkcl) {
		this.modkcl = modkcl;
	}
	public String getYxsl() {
		return yxsl;
	}
	public void setYxsl(String yxsl) {
		this.yxsl = yxsl;
	}
	public String getHwzt() {
		return hwzt;
	}
	public void setHwzt(String hwzt) {
		this.hwzt = hwzt;
	}
	public String getYdsbj() {
		return ydsbj;
	}
	public void setYdsbj(String ydsbj) {
		this.ydsbj = ydsbj;
	}
	public String getQlsl() {
		return qlsl;
	}
	public void setQlsl(String qlsl) {
		this.qlsl = qlsl;
	}
	public String getHwllid() {
		return hwllid;
	}
	public void setHwllid(String hwllid) {
		this.hwllid = hwllid;
	}
	public String getSlsl() {
		return slsl;
	}
	public void setSlsl(String slsl) {
		this.slsl = slsl;
	}
	public String getLlid() {
		return llid;
	}
	public void setLlid(String llid) {
		this.llid = llid;
	}
	public String getKlsl() {
		return klsl;
	}
	public void setKlsl(String klsl) {
		this.klsl = klsl;
	}
	public String getXmdlmc() {
		return xmdlmc;
	}
	public void setXmdlmc(String xmdlmc) {
		this.xmdlmc = xmdlmc;
	}
	public String getQgglid() {
		return qgglid;
	}
	public void setQgglid(String qgglid) {
		this.qgglid = qgglid;
	}
	public String getHtglid() {
		return htglid;
	}
	public void setHtglid(String htglid) {
		this.htglid = htglid;
	}
	public String getSfxzlbcskz1() {
		return sfxzlbcskz1;
	}
	public void setSfxzlbcskz1(String sfxzlbcskz1) {
		this.sfxzlbcskz1 = sfxzlbcskz1;
	}
	public String getDhdhglqg() {
		return dhdhglqg;
	}
	public void setDhdhglqg(String dhdhglqg) {
		this.dhdhglqg = dhdhglqg;
	}
	public String getDhdhglht() {
		return dhdhglht;
	}
	public void setDhdhglht(String dhdhglht) {
		this.dhdhglht = dhdhglht;
	}
	public String getZdhsl() {
		return zdhsl;
	}
	public void setZdhsl(String zdhsl) {
		this.zdhsl = zdhsl;
	}
	public String getYdhsl() {
		return ydhsl;
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
	public String getGysdm() {
		return gysdm;
	}
	public void setGysdm(String gysdm) {
		this.gysdm = gysdm;
	}
	public String getU8mxid() {
		return u8mxid;
	}
	public void setU8mxid(String u8mxid) {
		this.u8mxid = u8mxid;
	}
	public String getXmdldm() {
		return xmdldm;
	}
	public void setXmdldm(String xmdldm) {
		this.xmdldm = xmdldm;
	}
	public String getXmbmdm() {
		return xmbmdm;
	}
	public void setXmbmdm(String xmbmdm) {
		this.xmbmdm = xmbmdm;
	}
	public String getXmbmmc() {
		return xmbmmc;
	}
	public void setXmbmmc(String xmbmmc) {
		this.xmbmmc = xmbmmc;
	}
	public void setYdhsl(String ydhsl) {
		this.ydhsl = ydhsl;

	}
	public String getQgdh() {
		return qgdh;
	}
	public void setQgdh(String qgdh) {
		this.qgdh = qgdh;
	}
	public String getQgrddid() {
		return qgrddid;
	}
	public void setQgrddid(String qgrddid) {
		this.qgrddid = qgrddid;
	}
	public String getQgid() {
		return qgid;
	}
	public void setQgid(String qgid) {
		this.qgid = qgid;
	}
	public String getQgsqr() {
		return qgsqr;
	}
	public void setQgsqr(String qgsqr) {
		this.qgsqr = qgsqr;
	}
	public String getZkcl() {
		return zkcl;
	}
	public void setZkcl(String zkcl) {
		this.zkcl = zkcl;
	}
	public String getSjsl() {
		return sjsl;
	}
	public List<String> getHwids() {
		return hwids;
	}
	public void setHwids(String hwids) {
		List<String> list;
		String[] str = hwids.split(",");
		list = Arrays.asList(str);
		this.hwids = list;
	}
	public void setHwids(List<String> hwids) {
		if(!CollectionUtils.isEmpty(hwids)) {
            hwids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.hwids = hwids;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getGysid() {
		return gysid;
	}

	public void setGysid(String gysid) {
		this.gysid = gysid;
	}

	public String getSysl() {
		return sysl;
	}

	public void setSysl(String sysl) {
		this.sysl = sysl;
	}
	public void setSjsl(String sjsl) {
		this.sjsl = sjsl;
	}
	public String[] getLbs() {
		return lbs;
	}

	public void setLbs(String[] lbs) {
		this.lbs = lbs;
		if(lbs!=null && lbs.length>0) {
			for (int i = 0; i < lbs.length; i++) {
				this.lbs[i] = this.lbs[i].replace("'", "");
			}
		}
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
	public String getLb() {
		return lb;
	}
	public void setLb(String lb) {
		this.lb = lb;
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
	public String getJydh() {
		return jydh;
	}
	public void setJydh(String jydh) {
		this.jydh = jydh;
	}
	public String getRkdh() {
		return rkdh;
	}
	public void setRkdh(String rkdh) {
		this.rkdh = rkdh;
	}
	public String getThsl() {
		return thsl;
	}
	public void setThsl(String thsl) {
		this.thsl = thsl;
	}
	public String getWsdj() {
		return wsdj;
	}
	public void setWsdj(String wsdj) {
		this.wsdj = wsdj;
	}
	public String getSuil() {
		return suil;
	}
	public void setSuil(String suil) {
		this.suil = suil;
	}
	public String getHsdj() {
		return hsdj;
	}
	public void setHsdj(String hsdj) {
		this.hsdj = hsdj;
	}
	public String getHjje() {
		return hjje;
	}
	public void setHjje(String hjje) {
		this.hjje = hjje;
	}
	public String getRkrymc() {
		return rkrymc;
	}
	public void setRkrymc(String rkrymc) {
		this.rkrymc = rkrymc;
	}
	public String getU8cgid() {
		return u8cgid;
	}
	public void setU8cgid(String u8cgid) {
		this.u8cgid = u8cgid;
	}
	public String getRksl() {
		return rksl;
	}
	public void setRksl(String rksl) {
		this.rksl = rksl;
	}
	public String getYchh() {
		return ychh;
	}
	public void setYchh(String ychh) {
		this.ychh = ychh;
	}
	public String getHtsl() {
		return htsl;
	}
	public void setHtsl(String htsl) {
		this.htsl = htsl;
	}
	public String getHtid() {
		return htid;
	}
	public void setHtid(String htid) {
		this.htid = htid;
	}
	public String getDhid() {
		return dhid;
	}
	public void setDhid(String dhid) {
		this.dhid = dhid;
	}
	public String getReduce() {
		return reduce;
	}
	public void setReduce(String reduce) {
		this.reduce = reduce;
	}
	public String getJyzt() {
		return jyzt;
	}
	public void setJyzt(String jyzt) {
		this.jyzt = jyzt;
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
	public String getDhdh() {
		return dhdh;
	}
	public void setDhdh(String dhdh) {
		this.dhdh = dhdh;
	}
	public String getDhrq() {
		return dhrq;
	}
	public void setDhrq(String dhrq) {
		this.dhrq = dhrq;
	}
	public String getGysmc() {
		return gysmc;
	}
	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}
	public String getLbcsmc() {
		return lbcsmc;
	}
	public void setLbcsmc(String lbcsmc) {
		this.lbcsmc = lbcsmc;
	}
	public String getLbcsdm() {
		return lbcsdm;
	}
	public void setLbcsdm(String lbcsdm) {
		this.lbcsdm = lbcsdm;
	}
	public String getLbcskz1() {
		return lbcskz1;
	}
	public void setLbcskz1(String lbcskz1) {
		this.lbcskz1 = lbcskz1;
	}
	public String getKzcs() {
		return kzcs;
	}
	public void setKzcs(String kzcs) {
		this.kzcs = kzcs;
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
	public String getHtnbbh() {
		return htnbbh;
	}
	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}
	public String getEntire() {
		return entire;
	}
	public void setEntire(String entire) {
		this.entire = entire;
	}
	public List<String> getDhjyids() {
		return dhjyids;
	}
	public void setDhjyids(List<String> dhjyids) {
		this.dhjyids = dhjyids;
	}
	public String getDhjyzt() {
		return dhjyzt;
	}
	public void setDhjyzt(String dhjyzt) {
		this.dhjyzt = dhjyzt;
	}
	public List<String> getFjids() {
		return fjids;
	}
	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}
	public String getFjbj() {
		return fjbj;
	}
	public void setFjbj(String fjbj) {
		this.fjbj = fjbj;
	}

	public String getZlyq() {
		return zlyq;
	}

	public void setZlyq(String zlyq) {
		this.zlyq = zlyq;
	}

	public String getHwbz() {
		return hwbz;
	}

	public void setHwbz(String hwbz) {
		this.hwbz = hwbz;
	}

	public String getHwyt() {
		return hwyt;
	}

	public void setHwyt(String hwyt) {
		this.hwyt = hwyt;
	}

	public String getPzyq() {
		return pzyq;
	}

	public void setPzyq(String pzyq) {
		this.pzyq = pzyq;
	}

	public String getWbyq() {
		return wbyq;
	}

	public void setWbyq(String wbyq) {
		this.wbyq = wbyq;
	}

	public String getFkfmc() {
		return fkfmc;
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

	public String getQglxmc() {
		return qglxmc;
	}

	public void setQglxmc(String qglxmc) {
		this.qglxmc = qglxmc;
	}

	@Override
	public String getLbbj() {
		return lbbj;
	}

	@Override
	public void setLbbj(String lbbj) {
		this.lbbj = lbbj;
	}
}
