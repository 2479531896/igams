package com.matridx.igams.production.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.FjcfbDto;

@Alias(value="QgmxDto")
public class QgmxDto extends QgmxModel{
	//领料状态
	private String llzt;
	//物料名称
	private String wlmc;
	//物料类别
	private String wllb;
	//llid
	private String llid;
	//库存量
	private String kcl;
	//可领数量
	private String klsl;
	//物料编码
	private String wlbm;
	//物料类别名称
	private String wllbmc;
	//物料子类别
	private String wlzlb;
	//物料子类别名称
	private String wlzlbmc;
	//附件IDS
	private List<String> fjids;
	//物料子类别统称
	private  String wlzlbtc;
	//物料子类别统称名称
	private String wlzlbtcmc;
	//筛选条件（下载明细附件）
	private String[] wlzlbtcs;
	//文件路径
	private String wjlj;
	//文件名
	private String wjm;
	//业务类型
	private String ywlx;
	//附件标记(0 表示有附件)
	private String fjbj;
    //生产商
	private String scs;
	//规格
	private String gg;
	//计量单位
	private String jldw;
	//请购ids
	private List<String> qgids;
	//原厂货号
	private String ychh;
	//单据号
	private String djh;
	//领料类别
	private String lllb;
	//申请部门名称
	private String sqbmmc;
	// 全部(查询条件)
	private String entire;
	private String SqlParam; 	//导出关联标记位//所选择的字段
	//查询参数[多个条件]
	private String searchParam;
	//申请日期
	private String sqrq;
	//合同内部编号
	private String htnbbh;
	//合同创建日期
	private String cjrq;
	//订单日期
	private String ddrq;
	//合同数量
	private String htsl;
	//到货日期
	private String dhrq;
	//到货数量
	private String dhsl;
	//货物入库单号
	private String hwrkdh;
	//合同id
	private String htid;
	//物料id
	private String wlid;
	//货物id
	private String hwid;
	//合同标记（是否采购筛选）
	private String htbj;
	//货物标记（是否入库筛选）
	private String hwbj;
	//权限标记(0:可以查看合同详细信息，1：不能查看合同详细信息）
	private String qxbj;
	//总取消数量(取消但没有审核通过的取消请购数量)
	private String zqxsl;
	//可取消数量
	private String kqxsl;
	//物料子类别参数扩展1
	private String cskz1;
	// 检索用取消请购
	private String[] qxqgs;
	//计划到货日期
	private String jhdhrq;
	//期望到货开始时间
	private String qwdhsjstart;
	//期望到货结束时间
	private String qwdhsjend;
	//计划到货日期开始时间
	private String jhdhsjstart;
	//计划到货日期结束时间
	private String jhdhsjend;
	//到货日期开始时间
	private String dhsjstart;
	//到货日期结束时间
	private String dhsjend;
	//快递类型名称
	private String kdlxmc;
	//快递名称（基础数据吧)
	private String kdmc;
	//快递单号
	private String kddh;
	//扩展参数2
	private String cskz2;
	//合同明细id(过程维护用)
	private String htmxid;
	//图纸附件IDS(专门存放通过压缩包上传的图纸)
	private List<FjcfbDto> tzlist;
	//请求名称
	private String requestName;
	//rabbit消息队列标记
	private String prefixFlg;
	//请购审核状态(筛选)
	private String[] qgshzts;
	//请购审核状态
	private String qgshzt;
	//到货单号
	private String dhdh;
	//到货ID
	private String dhid;
	//入库ID
	private String rkid;
	//请购备注
	private String qgbz;
	//是否提供发票名称
	private String sftgfpmc;
	//货物用途
	private String hwyt;
	//供应商名称
	private String gysmc;
	//入库数量增加标记
	private String rksladd;
	//入库数量删除标记
	private String rksldel;
	//请购类别代码
	private String qglbdm;
	//质量要求名称
	private String zlyqmc;
	//下标（对应附件用）
	private String index;
	//入库日期
	private String rkrq;
	//请购申请人
	private String qgsqrmc;
	//合同单价
	private String htdj;
	//合同合计金额
	private String hthjje;
	//请购类别名称
	private String qglbmc;
	//发票号码
	private String fphms;
	//请购类型查询条件
	private String[] qglbs;
	//物料分类查询条件
	private String[] wlfls;
	//仓库分类ids
	private List<String> ckqxids;
	//保存条件
	private String bctj;
	//金额
	private String je;
	//申请日期开始时间
	private String sqrqstart;
	//申请日期结束时间
	private String sqrqend;
	//货物规格
	private String hwgg;
	//人员ID
	private String ryid;
	//用于判断是否为行政未确认列表确认界面点击选择的标记
	private String qrc_flag;
	//请购单号
	private String qgdh;
	//行政入库mxid
	private String xzrkmxid;
	//入库总数量
	private String rkzsl;
	//发票号
	private String fph;
	//U8入库单号
	private String u8rkdh;
	//发票总金额
	private String fpzje;
	private String qgshtgsjstart;
	private String qgshtgsjend;
	//交给财务时间开始时间
	private String lrsjstart;
	//交给财务时间结束时间Z
	private String lrsjend;
	//交给财务时间
	private String fplrsj;
	//不合格数量
	private String bhgsl;
	//格式发票号(数量)
	private String gsfph;
	//已付金额
	private String yfkje;
	//未付金额
	private String wfkje;
	//申请理由
	private String sqly;
	//可入库数量
	private String krksl;
	//已入库数量
	private String yrksl;
	//行政库存ID
	private String xzkcid;
	//请领数量
	private String qlsl;
	//预定数
	private String yds;
	private List<String> kcids;
	
		//已领数量
	private String ylsl;
	//仓库货物id
	private String ckhwid;
	//领料mx
	private String llmx_json;
	//物料备注
	private String wlbz;
	//库位名称
	private String kwmc;
	//初次订购
	private String ccdgmc;
	//提交时间
	private String tjsj;
	//部门经理审核时间
	private String bmjl;
	//采购管理员审核时间
	private String cggl;
	//公司领导审核时间
	private String gsld;
	//新增到提交用时
	private String xzdtj;
	//采购部经理审核时间
	private String cgjl;
	//提交到部门经理审核用时
	private String tjdbmjl;
	//部门经理到采购管理员审核用时
	private String jldcggl;
	//采购管理员到采购部经理审核用时
	private String cggldcgjl;
	//采购部经理到公司领导审核用时
	private String cgjldgsld;
	//审核通过时间
	private String tjdtg;
	//法务审核时间
	private String fwsh;
	//公司领导审核时间
	private String htgsld;
	//会计审核时间
	private String kjsh;
	private String htxzdtj;
	private String httjdcgjl;
	private String fwspdgsld;
	private String tjfshtg;
	private String httjfshtg;
	private String gslddhj;
	private String htcgjl;
	private String qgtjdhtshtg;
	private String cgjldfwsp;
	private String tjdshtg;
	private String htlrsj;
	private String httjsj;
	private String httjdshtg;
	private String wlflmc;
	private String fzrmc;
	//记录编号
	private String jlbh;
	//含税单价
	private String hsdj;
	//合计金额
	private String hjje;
	//付款方式
	private String fkfsmc;
	//付款金额
	private String fkje;
	//付款日期
	private String fkrq;
	//请购类型 0普通请购，同步U8，1OA请购，不同步U8
	private String qglx;
	//初次订购
	private String ccdg;
	//发票总数量
	private String fpzsl;
	//发票维护
	private String fpwh;
	//是否签订合同
	private String sfqdht;
	//是否及时到货
	private String sfjsdh;
	//合同付款方式
	private String htfkfsmc;
	//付款备注
	private String fkbz;
	private String wlmc_t;
	private String gg_t;
	private String jldw_t;
	//行政入库数量
	private String xzrksl;
	//行政库存量
	private String xzkcl;
	//物料库存量
	private String wlkcl;
	private String sbmc;
	private String gdzcflag;
	private String hq;//货期
	private String gdzcbh;//固定资产编号
	private String sbysid;//设备验收id
	private String kjhtbj;//框架标记
	private String xzbj;//限制标记
	private String jsid;//角色id
	private String ckid;//仓库id
	private String ckmc;
	private String lbcskz1;

	public String getLbcskz1() {
		return lbcskz1;
	}

	public void setLbcskz1(String lbcskz1) {
		this.lbcskz1 = lbcskz1;
	}

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	public String getCkid() {
		return ckid;
	}

	public void setCkid(String ckid) {
		this.ckid = ckid;
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

	public String getKjhtbj() {
		return kjhtbj;
	}

	public void setKjhtbj(String kjhtbj) {
		this.kjhtbj = kjhtbj;
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

	public String getHq() {
		return hq;
	}

	public void setHq(String hq) {
		this.hq = hq;
	}

	public String getGdzcflag() {
		return gdzcflag;
	}

	public void setGdzcflag(String gdzcflag) {
		this.gdzcflag = gdzcflag;
	}

	public String getSbmc() {
		return sbmc;
	}

	public void setSbmc(String sbmc) {
		this.sbmc = sbmc;
	}

	public String getXzkcl() {
		return xzkcl;
	}

	public void setXzkcl(String xzkcl) {
		this.xzkcl = xzkcl;
	}

	public String getXzrksl() {
		return xzrksl;
	}

	public void setXzrksl(String xzrksl) {
		this.xzrksl = xzrksl;
	}

    public String getWlkcl() {
		return wlkcl;
	}

	public void setWlkcl(String wlkcl) {
		this.wlkcl = wlkcl;
	}
	public String getWlmc_t() {
		return wlmc_t;
	}

	public void setWlmc_t(String wlmc_t) {
		this.wlmc_t = wlmc_t;
	}

	public String getGg_t() {
		return gg_t;
	}

	public void setGg_t(String gg_t) {
		this.gg_t = gg_t;
	}

	public String getJldw_t() {
		return jldw_t;
	}

	public void setJldw_t(String jldw_t) {
		this.jldw_t = jldw_t;
	}

	public String getHtfkfsmc() {
		return htfkfsmc;
	}

	public void setHtfkfsmc(String htfkfsmc) {
		this.htfkfsmc = htfkfsmc;
	}

	public String getFkbz() {
		return fkbz;
	}

	public void setFkbz(String fkbz) {
		this.fkbz = fkbz;
	}

	public String getSfqdht() {
		return sfqdht;
	}

	public void setSfqdht(String sfqdht) {
		this.sfqdht = sfqdht;
	}

	public String getSfjsdh() {
		return sfjsdh;
	}

	public void setSfjsdh(String sfjsdh) {
		this.sfjsdh = sfjsdh;
	}

	public String getFpwh() {
		return fpwh;
	}

	public void setFpwh(String fpwh) {
		this.fpwh = fpwh;
	}

	public String getFpzsl() {
		return fpzsl;
	}

	public void setFpzsl(String fpzsl) {
		this.fpzsl = fpzsl;
	}

	public String getCcdg() {
		return ccdg;
	}

	public void setCcdg(String ccdg) {
		this.ccdg = ccdg;
	}

	public String getQglx() {
		return qglx;
	}

	public void setQglx(String qglx) {
		this.qglx = qglx;
	}

	public String getFkrq() {
		return fkrq;
	}

	public void setFkrq(String fkrq) {
		this.fkrq = fkrq;
	}

	public String getFkfsmc() {
		return fkfsmc;
	}

	public void setFkfsmc(String fkfsmc) {
		this.fkfsmc = fkfsmc;
	}

	public String getFkje() {
		return fkje;
	}

	public void setFkje(String fkje) {
		this.fkje = fkje;
	}

	public String getJlbh() {
		return jlbh;
	}

	public void setJlbh(String jlbh) {
		this.jlbh = jlbh;
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


	public String getFzrmc() {
		return fzrmc;
	}

	public void setFzrmc(String fzrmc) {
		this.fzrmc = fzrmc;
	}

	public String getWlflmc() {
		return wlflmc;
	}

	public void setWlflmc(String wlflmc) {
		this.wlflmc = wlflmc;
	}

	public String getCcdgmc() {
		return ccdgmc;
	}

	public void setCcdgmc(String ccdgmc) {
		this.ccdgmc = ccdgmc;
	}

	public String getTjsj() {
		return tjsj;
	}

	public void setTjsj(String tjsj) {
		this.tjsj = tjsj;
	}

	public String getBmjl() {
		return bmjl;
	}

	public void setBmjl(String bmjl) {
		this.bmjl = bmjl;
	}

	public String getCggl() {
		return cggl;
	}

	public void setCggl(String cggl) {
		this.cggl = cggl;
	}

	public String getGsld() {
		return gsld;
	}

	public void setGsld(String gsld) {
		this.gsld = gsld;
	}

	public String getXzdtj() {
		return xzdtj;
	}

	public void setXzdtj(String xzdtj) {
		this.xzdtj = xzdtj;
	}

	public String getCgjl() {
		return cgjl;
	}

	public void setCgjl(String cgjl) {
		this.cgjl = cgjl;
	}

	public String getTjdbmjl() {
		return tjdbmjl;
	}

	public void setTjdbmjl(String tjdbmjl) {
		this.tjdbmjl = tjdbmjl;
	}

	public String getJldcggl() {
		return jldcggl;
	}

	public void setJldcggl(String jldcggl) {
		this.jldcggl = jldcggl;
	}

	public String getCggldcgjl() {
		return cggldcgjl;
	}

	public void setCggldcgjl(String cggldcgjl) {
		this.cggldcgjl = cggldcgjl;
	}

	public String getCgjldgsld() {
		return cgjldgsld;
	}

	public void setCgjldgsld(String cgjldgsld) {
		this.cgjldgsld = cgjldgsld;
	}

	public String getTjdtg() {
		return tjdtg;
	}

	public void setTjdtg(String tjdtg) {
		this.tjdtg = tjdtg;
	}

	public String getFwsh() {
		return fwsh;
	}

	public void setFwsh(String fwsh) {
		this.fwsh = fwsh;
	}

	public String getHtgsld() {
		return htgsld;
	}

	public void setHtgsld(String htgsld) {
		this.htgsld = htgsld;
	}

	public String getKjsh() {
		return kjsh;
	}

	public void setKjsh(String kjsh) {
		this.kjsh = kjsh;
	}

	public String getHtxzdtj() {
		return htxzdtj;
	}

	public void setHtxzdtj(String htxzdtj) {
		this.htxzdtj = htxzdtj;
	}

	public String getHttjdcgjl() {
		return httjdcgjl;
	}

	public void setHttjdcgjl(String httjdcgjl) {
		this.httjdcgjl = httjdcgjl;
	}

	public String getFwspdgsld() {
		return fwspdgsld;
	}

	public void setFwspdgsld(String fwspdgsld) {
		this.fwspdgsld = fwspdgsld;
	}

	public String getTjfshtg() {
		return tjfshtg;
	}

	public void setTjfshtg(String tjfshtg) {
		this.tjfshtg = tjfshtg;
	}

	public String getHttjfshtg() {
		return httjfshtg;
	}

	public void setHttjfshtg(String httjfshtg) {
		this.httjfshtg = httjfshtg;
	}

	public String getGslddhj() {
		return gslddhj;
	}

	public void setGslddhj(String gslddhj) {
		this.gslddhj = gslddhj;
	}

	public String getHtcgjl() {
		return htcgjl;
	}

	public void setHtcgjl(String htcgjl) {
		this.htcgjl = htcgjl;
	}

	public String getQgtjdhtshtg() {
		return qgtjdhtshtg;
	}

	public void setQgtjdhtshtg(String qgtjdhtshtg) {
		this.qgtjdhtshtg = qgtjdhtshtg;
	}

	public String getCgjldfwsp() {
		return cgjldfwsp;
	}

	public void setCgjldfwsp(String cgjldfwsp) {
		this.cgjldfwsp = cgjldfwsp;
	}

	public String getTjdshtg() {
		return tjdshtg;
	}

	public void setTjdshtg(String tjdshtg) {
		this.tjdshtg = tjdshtg;
	}

	public String getHtlrsj() {
		return htlrsj;
	}

	public void setHtlrsj(String htlrsj) {
		this.htlrsj = htlrsj;
	}

	public String getHttjsj() {
		return httjsj;
	}

	public void setHttjsj(String httjsj) {
		this.httjsj = httjsj;
	}

	public String getHttjdshtg() {
		return httjdshtg;
	}

	public void setHttjdshtg(String httjdshtg) {
		this.httjdshtg = httjdshtg;
	}

	public String getKwmc() {
		return kwmc;
	}

	public void setKwmc(String kwmc) {
		this.kwmc = kwmc;
	}
	public String getLlmx_json() {
		return llmx_json;
	}

	public void setLlmx_json(String llmx_json) {
		this.llmx_json = llmx_json;
	}
	
	public String getWlbz() {
		return wlbz;
	}

	public void setWlbz(String wlbz) {
		this.wlbz = wlbz;
	}

	public String getLlzt() {
		return llzt;
	}

	public void setLlzt(String llzt) {
		this.llzt = llzt;
	}

	public String getCkhwid() {
		return ckhwid;
	}

	public void setCkhwid(String ckhwid) {
		this.ckhwid = ckhwid;
	}

	public String getYlsl() {
		return ylsl;
	}

	public void setYlsl(String ylsl) {
		this.ylsl = ylsl;
	}

	public List<String> getKcids() {
		return kcids;
	}

	public void setKcids(List<String> kcids) {
		this.kcids = kcids;
	}

	public String getYds() {
		return yds;
	}

	public void setYds(String yds) {
		this.yds = yds;
	}

	public String getQlsl() {
		return qlsl;
	}

	public void setQlsl(String qlsl) {
		this.qlsl = qlsl;
	}

	public String getXzkcid() {
		return xzkcid;
	}

	public void setXzkcid(String xzkcid) {
		this.xzkcid = xzkcid;
	}

	public String getYrksl() {
		return yrksl;
	}

	public void setYrksl(String yrksl) {
		this.yrksl = yrksl;
	}

	public String getKrksl() {
		return krksl;
	}

	public void setKrksl(String krksl) {
		this.krksl = krksl;
	}

	public String getSqly() {
		return sqly;
	}

	public void setSqly(String sqly) {
		this.sqly = sqly;
	}
	
	public String getYfkje() {
		return yfkje;
	}

	public void setYfkje(String yfkje) {
		this.yfkje = yfkje;
	}

	public String getWfkje() {
		return wfkje;
	}

	public void setWfkje(String wfkje) {
		this.wfkje = wfkje;
	}

	public String getGsfph() {
		return gsfph;
	}

	public void setGsfph(String gsfph) {
		this.gsfph = gsfph;
	}


	public String getBhgsl() {
		return bhgsl;
	}

	public void setBhgsl(String bhgsl) {
		this.bhgsl = bhgsl;
	}

	public String getFplrsj() {
		return fplrsj;
	}

	public void setFplrsj(String fplrsj) {
		this.fplrsj = fplrsj;
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

	public String getFpzje() {
		return fpzje;
	}

	public void setFpzje(String fpzje) {
		this.fpzje = fpzje;
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

	public String getU8rkdh() {
		return u8rkdh;
	}

	public void setU8rkdh(String u8rkdh) {
		this.u8rkdh = u8rkdh;
	}

	public String getXzrkmxid() {
		return xzrkmxid;
	}

	public void setXzrkmxid(String xzrkmxid) {
		this.xzrkmxid = xzrkmxid;
	}

	public String getQgdh() {
		return qgdh;
	}

	public void setQgdh(String qgdh) {
		this.qgdh = qgdh;
	}

	public String getQrc_flag() {
		return qrc_flag;
	}

	public void setQrc_flag(String qrc_flag) {
		this.qrc_flag = qrc_flag;
	}

	public String getRyid() {
		return ryid;
	}

	public void setRyid(String ryid) {
		this.ryid = ryid;
	}

	public String getBctj() {
		return bctj;
	}

	public void setBctj(String bctj) {
		this.bctj = bctj;
	}

	public String getJe() {
		return je;
	}

	public void setJe(String je) {
		this.je = je;
	}

	public List<String> getCkqxids() {
		return ckqxids;
	}

	public void setCkqxids(List<String> ckqxids) {
		this.ckqxids = ckqxids;
	}

	public String[] getQglbs() {
		return qglbs;
	}

	public void setQglbs(String[] qglbs) {
		this.qglbs = qglbs;
		for (int i = 0; i < qglbs.length; i++) {
			this.qglbs[i] = this.qglbs[i].replace("'", "");
		}
	}
	public String[] getWlfls() {
		return wlfls;
	}

	public void setWlfls(String[] wlfls) {
		this.wlfls = wlfls;
		for (int i = 0; i < wlfls.length; i++) {
			this.wlfls[i] = this.wlfls[i].replace("'", "");
		}
	}
	public String getFphms() {
		return fphms;
	}

	public void setFphms(String fphms) {
		this.fphms = fphms;
	}

	public String getQglbmc() {
		return qglbmc;
	}

	public void setQglbmc(String qglbmc) {
		this.qglbmc = qglbmc;
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

	public String getLllb() {
		return lllb;
	}

	public void setLllb(String lllb) {
		this.lllb = lllb;
	}

	public String getKlsl() {
		return klsl;
	}

	public void setKlsl(String klsl) {
		this.klsl = klsl;
	}

	public String getHtdj() {
		return htdj;
	}

	public void setHtdj(String htdj) {
		this.htdj = htdj;
	}

	public String getHthjje() {
		return hthjje;
	}

	public void setHthjje(String hthjje) {
		this.hthjje = hthjje;
	}

	public String getQgsqrmc() {
		return qgsqrmc;
	}

	public void setQgsqrmc(String qgsqrmc) {
		this.qgsqrmc = qgsqrmc;
	}

	public String getRkrq() {
		return rkrq;
	}

	public void setRkrq(String rkrq) {
		this.rkrq = rkrq;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getZlyqmc() {
		return zlyqmc;
	}

	public void setZlyqmc(String zlyqmc) {
		this.zlyqmc = zlyqmc;
	}

	public String getQglbdm() {
		return qglbdm;
	}

	public void setQglbdm(String qglbdm) {
		this.qglbdm = qglbdm;
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
	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}

	public String getHwyt() {
		return hwyt;
	}

	public void setHwyt(String hwyt) {
		this.hwyt = hwyt;
	}

	public String getSftgfpmc() {
		return sftgfpmc;
	}

	public void setSftgfpmc(String sftgfpmc) {
		this.sftgfpmc = sftgfpmc;
	}

	public String getQgbz() {
		return qgbz;
	}

	public void setQgbz(String qgbz) {
		this.qgbz = qgbz;
	}

	public String getRkid() {
		return rkid;
	}

	public void setRkid(String rkid) {
		this.rkid = rkid;
	}

	public String getDhid() {
		return dhid;
	}

	public void setDhid(String dhid) {
		this.dhid = dhid;
	}

	public String getDhdh() {
		return dhdh;
	}

	public void setDhdh(String dhdh) {
		this.dhdh = dhdh;
	}

	public String getRequestName() {
		return requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public String getQgshzt() {
		return qgshzt;
	}

	public void setQgshzt(String qgshzt) {
		this.qgshzt = qgshzt;
	}

	public String[] getQgshzts() {
		return qgshzts;
	}

	public void setQgshzts(String[] qgshzts) {
		this.qgshzts = qgshzts;
		for (int i = 0; i < qgshzts.length; i++) {
			this.qgshzts[i] = this.qgshzts[i].replace("'", "");
		}
	}

	public String getHtmxid() {
		return htmxid;
	}

	public void setHtmxid(String htmxid) {
		this.htmxid = htmxid;
	}
	public List<FjcfbDto> getTzlist() {
		return tzlist;
	}

	public void setTzlist(List<FjcfbDto> tzlist) {
		this.tzlist = tzlist;
	}
	
	public String getPrefixFlg() {
		return prefixFlg;
	}

	public void setPrefixFlg(String prefixFlg) {
		this.prefixFlg = prefixFlg;
	}
	

	public String getCskz2() {
		return cskz2;
	}

	public void setCskz2(String cskz2) {
		this.cskz2 = cskz2;
	}

	public String getKdlxmc() {
		return kdlxmc;
	}

	public void setKdlxmc(String kdlxmc) {
		this.kdlxmc = kdlxmc;
	}

	public String getKdmc() {
		return kdmc;
	}

	public void setKdmc(String kdmc) {
		this.kdmc = kdmc;
	}

	public String getKddh() {
		return kddh;
	}

	public void setKddh(String kddh) {
		this.kddh = kddh;
	}

	public String getQwdhsjstart() {
		return qwdhsjstart;
	}

	public void setQwdhsjstart(String qwdhsjstart) {
		this.qwdhsjstart = qwdhsjstart;
	}

	public String getQwdhsjend() {
		return qwdhsjend;
	}

	public void setQwdhsjend(String qwdhsjend) {
		this.qwdhsjend = qwdhsjend;
	}

	public String getJhdhsjstart() {
		return jhdhsjstart;
	}

	public void setJhdhsjstart(String jhdhsjstart) {
		this.jhdhsjstart = jhdhsjstart;
	}

	public String getJhdhsjend() {
		return jhdhsjend;
	}

	public void setJhdhsjend(String jhdhsjend) {
		this.jhdhsjend = jhdhsjend;
	}

	public String getDhsjstart() {
		return dhsjstart;
	}

	public void setDhsjstart(String dhsjstart) {
		this.dhsjstart = dhsjstart;
	}

	public String getDhsjend() {
		return dhsjend;
	}

	public void setDhsjend(String dhsjend) {
		this.dhsjend = dhsjend;
	}

	public String getJhdhrq() {
		return jhdhrq;
	}

	public void setJhdhrq(String jhdhrq) {
		this.jhdhrq = jhdhrq;
	}

	public String[] getQxqgs() {
		return qxqgs;
	}

	public void setQxqgs(String[] qxqgs) {
		this.qxqgs = qxqgs;
		for (int i = 0; i < qxqgs.length; i++) {
			this.qxqgs[i] = this.qxqgs[i].replace("'", "");
		}
	}
	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}

	public String getKqxsl() {
		return kqxsl;
	}

	public void setKqxsl(String kqxsl) {
		this.kqxsl = kqxsl;
	}

	public String getZqxsl() {
		return zqxsl;
	}

	public void setZqxsl(String zqxsl) {
		this.zqxsl = zqxsl;
	}

	public String getQxbj() {
		return qxbj;
	}

	public void setQxbj(String qxbj) {
		this.qxbj = qxbj;
	}

	public String getHtbj() {
		return htbj;
	}

	public void setHtbj(String htbj) {
		this.htbj = htbj;
	}

	public String getHwbj() {
		return hwbj;
	}

	public void setHwbj(String hwbj) {
		this.hwbj = hwbj;
	}

	public String getHwid() {
		return hwid;
	}

	public void setHwid(String hwid) {
		this.hwid = hwid;
	}

	public String getHtid() {
		return htid;
	}

	public void setHtid(String htid) {
		this.htid = htid;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getHwrkdh() {
		return hwrkdh;
	}

	public void setHwrkdh(String hwrkdh) {
		this.hwrkdh = hwrkdh;
	}
		
	public String getSqrq() {
		return sqrq;
	}

	public void setSqrq(String sqrq) {
		this.sqrq = sqrq;
	}

	public String getHtnbbh() {
		return htnbbh;
	}

	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}

	public String getCjrq() {
		return cjrq;
	}

	public void setCjrq(String cjrq) {
		this.cjrq = cjrq;
	}

	public String getDdrq() {
		return ddrq;
	}

	public void setDdrq(String ddrq) {
		this.ddrq = ddrq;
	}

	public String getHtsl() {
		return htsl;
	}

	public void setHtsl(String htsl) {
		this.htsl = htsl;
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

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
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

	public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String getYchh() {
		return ychh;
	}

	public void setYchh(String ychh) {
		this.ychh = ychh;
	}

	public String getScs() {
		return scs;
	}

	public void setScs(String scs) {
		this.scs = scs;
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

	public String getWlzlbtcmc() {
		return wlzlbtcmc;
	}

	public void setWlzlbtcmc(String wlzlbtcmc) {
		this.wlzlbtcmc = wlzlbtcmc;
	}

	public String getFjbj() {
		return fjbj;
	}

	public void setFjbj(String fjbj) {
		this.fjbj = fjbj;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getWjlj() {
		return wjlj;
	}

	public void setWjlj(String wjlj) {
		this.wjlj = wjlj;
	}

	public String getWjm() {
		return wjm;
	}

	public void setWjm(String wjm) {
		this.wjm = wjm;
	}

	public String[] getWlzlbtcs() {
		return wlzlbtcs;
	}

	public void setWlzlbtcs(String[] wlzlbtcs) {
		this.wlzlbtcs = wlzlbtcs;
		for(int i=0;i<this.wlzlbtcs.length;i++)
		{
			this.wlzlbtcs[i] = this.wlzlbtcs[i].replace("'", "");
		}
		this.wlzlbtcs = wlzlbtcs;
	}

	public String getWlzlbtc() {
		return wlzlbtc;
	}

	public void setWlzlbtc(String wlzlbtc) {
		this.wlzlbtc = wlzlbtc;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}

	public String getWllb() {
		return wllb;
	}

	public void setWllb(String wllb) {
		this.wllb = wllb;
	}

	public String getWllbmc() {
		return wllbmc;
	}

	public void setWllbmc(String wllbmc) {
		this.wllbmc = wllbmc;
	}

	public String getWlzlb() {
		return wlzlb;
	}

	public void setWlzlb(String wlzlb) {
		this.wlzlb = wlzlb;
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
	
	public List<String> getQgids() {
		return qgids;
	}

	public void setQgids(List<String> qgids) {
		this.qgids = qgids;
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

	public String getHwgg() {
		return hwgg;
	}

	public void setHwgg(String hwgg) {
		this.hwgg = hwgg;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
