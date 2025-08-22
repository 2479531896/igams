package com.matridx.igams.production.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value = "HtglDto")
public class HtglDto extends HtglModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// 付款金额
	private String fkje;
	// 发票方式名称
	private String fpfsmc;
	// 付款方式名称
	private String fkfsmc;
	// 请求部门名称
	private String sqbmmc;
	//请求部分代码
	private String jqdm;
	// 币种名称
	private String bizmc;
	// 合同类型名称
	private String ywlxmc;
	// 内部编号前缀（合同类型扩展参数一）
	private String nbbhqz;
	//供应商名称
	private String gysmc;
	//供应商代码
	private String gysdm;
	// 检索用删除标记
	private String[] scbjs;
	// 检索用双章标记
	private String[] szbjs;
	// 检索用发票方式（多）
	private String[] fpfss;
	// 检索用付款方式（多）
	private String[] fkfss;
	// 金额最大数
	private Double zjemax;
	// 金额最小数
	private Double zjemin;
	// 创建日期结束日期
	private String shsjend;
	// 创建日期开始日期
	private String shsjstart;
	// 全部(查询条件)
	private String entire;
	private String SqlParam; 	//导出关联标记位//所选择的字段
	//查询参数[多个条件]
	private String searchParam;
	//查询条数
	private String count;
	//负责人名称
	private String fzrmc;
	//请购ID字符串
	private String djhs;
	//请购单据号
	private String djh;
	//合同明细json
	private String htmxJson;
	//访问标记(判断是否为外部接口)
	private String fwbj;
	//申请人姓名
	private String sqrmc;
	//申请日期
	private String sqrq;
	//付款百分比
	private String fkbfb;
	//附件IDS
	private List<String> fjids;
	//合同业务类型（防止与上传附件业务类型字段冲突）
	private String htywlx;
	//供应商负责人
	private String gysfzr;
	//供应商负责人电话
	private String gysfzrdh;
	//付款方名称
	private String fkfmc;
	//申请人(生成自定义记录编号)
	private String sqr;
	//请购数量
	private String qgsl;
	//审批人id
	private String sprid;
	//审批人姓名
	private String sprxm;
	//审批人用户名
	private String spryhm;
	//审批人钉钉id
	private String sprddid;
	//审批人角色id
	private String sprjsid;
	//审批人角色名称
	private String sprjsmc;
	//废弃标记（判断删除的数据是否为废弃）
	private String fqbj;
	// 校验起订量标记  '0' 表示不用校验
	private String checkqdl;
	//rabbit标记
	private String prefixFlg;
	//物料名称(查询用)
	private String wlmc;
	//物料编码(查询用)
	private String wlbm;
	//规格(查询用)
	private String gg;
	//付款方式代码
	private String fkfsdm;
	//未付金额
	private String wfje;
	//付款提醒JSON
	private String fktxJson;
	//支付方开户行
	private String zffkhh;
	//支付方银行账户
	private String zffyhzh;
	//合同风险程度多
	private String[] htfxcds;
	//合同风险程度
	private String htfxcd;
	//合同风险名称
	private String htfxmc;
	//双章标记名称
	private String szbjmc;
	//状态名称
	private String ztmc;
	//采购类型多
	private String[]cglxs;
	//提交时间
	private String tjsj;
	//采购部经理审核时间
	private String bmjl;
	//法务审核时间
	private String fwsh;
	//公司领导审核时间
	private String gsld;
	//会计审核时间
	private String kjsh;
	//新增到提交用时
	private String xzdtj;
	//提交到采购部经理审核用时
	private String tjdcgjl;
	//采购部经理到法务审核用时
	private String cgjldfw;
	//法务到公司领导审核用时
	private String fwdgsld;
	//公司领导到会计审核用时
	private String gslddkj;
	//提交到审核通过用时
	private String tjdtg;
	//完成标记多
	private String[]wcbjs;
	//状态多
	private String[]zts;
	//合同新增标记 0普通新增
	private String htaddflag;
	//请购类别代码
	private String qglbdm;
	//机构代码
	private String jgdm;
	//采购类型代码
	private String cglxdm;
	//类型
	private String lx;
	//统计开始时间
	private String tjsjstart;
	private String tjsjend;
	//统计时间(月)
	private String tjsjMstart;
	private String tjsjMend;
	//统计时间(年)
	private String tjsjYstart;
	private String tjsjYend;
	//采购管理审核用时
	private String cggl;
	//采购经理审核用时
	private String cgjl;
	//平均用时
	private String pjys;
	//合计总金额
	private String hjzye;
	//合同单数
	private String htds;
	//总数
	private String zs;
	//路径标记
	private String ljbj;
	//财务签一
	private String cwqy;
	//财务签二
	private String cwqe;
	//出纳
	private String cn;
	//合同业务类型多
	private String[] htywlxs;
	private String jsdhl;//及时到货率
	private String wjsdhl;//未及时到货率
	private String wlflmc;//物料分类名称
	private String htlxmc;//合同类型名称
	private String[] gysids;//供应商ids
	private String cghzbj;//采购红字标记
	private String kjlxmc;//框架类型名称
	private String sfgq;//是否过期
	private String sfddsp;//是否钉钉审批 0是否 null 是
	private String nhtlx;//不查询该合同类型
	private String yyhtnbbh;//引用合同内部编号
	private String kjhtbh;//框架合同内部编号
	private String kjhtidnull;//将框架合同id置为null
	//原合同钉钉实例ID
	private String yhtddslid;
	private String bchtnbbh;
	private String bchtzje;
	private String xzje;
	private String updateFlg;
	private String postStart;
	private String postEnd;
	private String timeLag;
	private String qgshsj;
	private String flg;
	private String ddid;
	private String postCg;
	private String sftx;
	private String overTime;
	private String overdueTime;
	private String qgfzrmc;
	private String unit;
	private String rule;
	private String qgzt;
	private String cszt;
	private String csbz;
	private String shys;
	private String zsxm;
	private String yhm;

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getShys() {
		return shys;
	}

	public void setShys(String shys) {
		this.shys = shys;
	}

	public String getCszt() {
		return cszt;
	}

	public void setCszt(String cszt) {
		this.cszt = cszt;
	}

	public String getCsbz() {
		return csbz;
	}

	public void setCsbz(String csbz) {
		this.csbz = csbz;
	}

	public String getQgzt() {
		return qgzt;
	}

	public void setQgzt(String qgzt) {
		this.qgzt = qgzt;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getQgfzrmc() {
		return qgfzrmc;
	}

	public void setQgfzrmc(String qgfzrmc) {
		this.qgfzrmc = qgfzrmc;
	}

	public String getOverTime() {
		return overTime;
	}

	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}

	public String getOverdueTime() {
		return overdueTime;
	}

	public void setOverdueTime(String overdueTime) {
		this.overdueTime = overdueTime;
	}

	public String getSftx() {
		return sftx;
	}

	public void setSftx(String sftx) {
		this.sftx = sftx;
	}

	public String getPostCg() {
		return postCg;
	}

	public void setPostCg(String postCg) {
		this.postCg = postCg;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getFlg() {
		return flg;
	}

	public void setFlg(String flg) {
		this.flg = flg;
	}

	public String getQgshsj() {
		return qgshsj;
	}

	public void setQgshsj(String qgshsj) {
		this.qgshsj = qgshsj;
	}

	public String getTimeLag() {
		return timeLag;
	}

	public void setTimeLag(String timeLag) {
		this.timeLag = timeLag;
	}

	public String getPostStart() {
		return postStart;
	}

	public void setPostStart(String postStart) {
		this.postStart = postStart;
	}

	public String getPostEnd() {
		return postEnd;
	}

	public void setPostEnd(String postEnd) {
		this.postEnd = postEnd;
	}

	public String getUpdateFlg() {
		return updateFlg;
	}

	public void setUpdateFlg(String updateFlg) {
		this.updateFlg = updateFlg;
	}

	public String getXzje() {
		return xzje;
	}

	public void setXzje(String xzje) {
		this.xzje = xzje;
	}

	public String getBchtnbbh() {
		return bchtnbbh;
	}

	public void setBchtnbbh(String bchtnbbh) {
		this.bchtnbbh = bchtnbbh;
	}

	public String getBchtzje() {
		return bchtzje;
	}

	public void setBchtzje(String bchtzje) {
		this.bchtzje = bchtzje;
	}

	public String getYhtddslid() {
		return yhtddslid;
	}

	public void setYhtddslid(String yhtddslid) {
		this.yhtddslid = yhtddslid;
	}

	public String getKjhtidnull() {
		return kjhtidnull;
	}

	public void setKjhtidnull(String kjhtidnull) {
		this.kjhtidnull = kjhtidnull;
	}

	public String getKjhtbh() {
		return kjhtbh;
	}

	public void setKjhtbh(String kjhtbh) {
		this.kjhtbh = kjhtbh;
	}

	public String getYyhtnbbh() {
		return yyhtnbbh;
	}

	public void setYyhtnbbh(String yyhtnbbh) {
		this.yyhtnbbh = yyhtnbbh;
	}

	public String getNhtlx() {
		return nhtlx;
	}

	public void setNhtlx(String nhtlx) {
		this.nhtlx = nhtlx;
	}

	public String getSfddsp() {
		return sfddsp;
	}

	public void setSfddsp(String sfddsp) {
		this.sfddsp = sfddsp;
	}

	public String getSfgq() {
		return sfgq;
	}

	public void setSfgq(String sfgq) {
		this.sfgq = sfgq;
	}

	public String getKjlxmc() {
		return kjlxmc;
	}

	public void setKjlxmc(String kjlxmc) {
		this.kjlxmc = kjlxmc;
	}

	public String getCghzbj() {
		return cghzbj;
	}

	public void setCghzbj(String cghzbj) {
		this.cghzbj = cghzbj;
	}

	public String[] getGysids() {
		return gysids;
	}

	public void setGysids(String[] gysids) {
		this.gysids = gysids;
	}

	public String getHtlxmc() {
		return htlxmc;
	}

	public void setHtlxmc(String htlxmc) {
		this.htlxmc = htlxmc;
	}

	public String getWlflmc() {
		return wlflmc;
	}

	public void setWlflmc(String wlflmc) {
		this.wlflmc = wlflmc;
	}

	public String getJsdhl() {
		return jsdhl;
	}

	public void setJsdhl(String jsdhl) {
		this.jsdhl = jsdhl;
	}

	public String getWjsdhl() {
		return wjsdhl;
	}

	public void setWjsdhl(String wjsdhl) {
		this.wjsdhl = wjsdhl;
	}

	public String[] getHtywlxs() {
		return htywlxs;
	}

	public void setHtywlxs(String[] htywlxs) {
		this.htywlxs = htywlxs;
	}

	public String getCwqy() {
		return cwqy;
	}

	public void setCwqy(String cwqy) {
		this.cwqy = cwqy;
	}

	public String getCwqe() {
		return cwqe;
	}

	public void setCwqe(String cwqe) {
		this.cwqe = cwqe;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getLjbj() {
		return ljbj;
	}

	public void setLjbj(String ljbj) {
		this.ljbj = ljbj;
	}

	public String getZs() {
		return zs;
	}

	public void setZs(String zs) {
		this.zs = zs;
	}

	public String getHtds() {
		return htds;
	}

	public void setHtds(String htds) {
		this.htds = htds;
	}

	public String getHjzye() {
		return hjzye;
	}

	public void setHjzye(String hjzye) {
		this.hjzye = hjzye;
	}

	public String getCgjl() {
		return cgjl;
	}

	public void setCgjl(String cgjl) {
		this.cgjl = cgjl;
	}

	public String getTjsjend() {
		return tjsjend;
	}

	public void setTjsjend(String tjsjend) {
		this.tjsjend = tjsjend;
	}

	public String getTjsjMend() {
		return tjsjMend;
	}

	public void setTjsjMend(String tjsjMend) {
		this.tjsjMend = tjsjMend;
	}

	public String getTjsjYend() {
		return tjsjYend;
	}

	public void setTjsjYend(String tjsjYend) {
		this.tjsjYend = tjsjYend;
	}

	public String getCggl() {
		return cggl;
	}

	public void setCggl(String cggl) {
		this.cggl = cggl;
	}

	public String getPjys() {
		return pjys;
	}

	public void setPjys(String pjys) {
		this.pjys = pjys;
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

	public String getFwsh() {
		return fwsh;
	}

	public void setFwsh(String fwsh) {
		this.fwsh = fwsh;
	}

	public String getGsld() {
		return gsld;
	}

	public void setGsld(String gsld) {
		this.gsld = gsld;
	}
	public String getKjsh() {
		return kjsh;
	}

	public void setKjsh(String kjsh) {
		this.kjsh = kjsh;
	}

	public String getXzdtj() {
		return xzdtj;
	}

	public void setXzdtj(String xzdtj) {
		this.xzdtj = xzdtj;
	}

	public String getTjdcgjl() {
		return tjdcgjl;
	}

	public void setTjdcgjl(String tjdcgjl) {
		this.tjdcgjl = tjdcgjl;
	}
	public String getCgjldfw() {
		return cgjldfw;
	}
	//双章标记名称
	public void setCgjldfw(String cgjldfw) {
		this.cgjldfw = cgjldfw;
	}
	public String getFwdgsld() {
		return fwdgsld;
	}
	//状态名称
	public void setFwdgsld(String fwdgsld) {
		this.fwdgsld = fwdgsld;
	}
	public String getGslddkj() {
		return gslddkj;
	}
	//采购类型多
	public void setGslddkj(String gslddkj) {
		this.gslddkj = gslddkj;
	}

	public String getTjdtg() {
		return tjdtg;
	}

	public void setTjdtg(String tjdtg) {
		this.tjdtg = tjdtg;
	}






	public String getTjsjMstart() {
		return tjsjMstart;
	}

	public void setTjsjMstart(String tjsjMstart) {
		this.tjsjMstart = tjsjMstart;
	}
	public String getTjsjYstart() {
		return tjsjYstart;
	}

	public void setTjsjYstart(String tjsjYstart) {
		this.tjsjYstart = tjsjYstart;
	}
	


	public String getTjsjstart() {
		return tjsjstart;
	}

	public void setTjsjstart(String tjsjstart) {
		this.tjsjstart = tjsjstart;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	//状态多
	public String getHtaddflag() {
		return htaddflag;
	}

	public void setHtaddflag(String htaddflag) {
		this.htaddflag = htaddflag;
	}

	public String[] getWcbjs() {
		return wcbjs;
	}

	public void setWcbjs(String[] wcbjs) {
		this.wcbjs = wcbjs;
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
	}

	public String[] getCglxs() {
		return cglxs;
	}

	public void setCglxs(String[] cglxs) {
		this.cglxs = cglxs;
	}
	public String getCglxdm() {
		return cglxdm;
	}
	public void setCglxdm(String cglxdm) {
		this.cglxdm = cglxdm;
	}

	public String getJgdm() {
		return jgdm;
	}

	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}
	public String getZtmc() {
		return ztmc;
	}

	public void setZtmc(String ztmc) {
		this.ztmc = ztmc;
	}

	public String getSzbjmc() {
		return szbjmc;
	}

	public void setSzbjmc(String szbjmc) {
		this.szbjmc = szbjmc;
	}

	public String getQglbdm() {
		return qglbdm;
	}

	public void setQglbdm(String qglbdm) {
		this.qglbdm = qglbdm;
	}


	public String getHtfxmc() {
		return htfxmc;
	}

	public void setHtfxmc(String htfxmc) {
		this.htfxmc = htfxmc;
	}

	public String[] getHtfxcds() {
		return htfxcds;
	}

	public void setHtfxcds(String[] htfxcds) {
		this.htfxcds = htfxcds;
		for (int i = 0; i < htfxcds.length; i++) {
			this.htfxcds[i] = this.htfxcds[i].replace("'", "");
		}
	}


	public String getHtfxcd() {
		return htfxcd;
	}

	public void setHtfxcd(String htfxcd) {
		this.htfxcd = htfxcd;
	}

	public String getZffkhh() {
		return zffkhh;
	}

	public void setZffkhh(String zffkhh) {
		this.zffkhh = zffkhh;
	}

	public String getZffyhzh() {
		return zffyhzh;
	}

	public void setZffyhzh(String zffyhzh) {
		this.zffyhzh = zffyhzh;
	}

	public String getFktxJson() {
		return fktxJson;
	}

	public void setFktxJson(String fktxJson) {
		this.fktxJson = fktxJson;
	}

	public String getWfje() {
		return wfje;
	}

	public void setWfje(String wfje) {
		this.wfje = wfje;
	}

	public String getFkfsdm() {
		return fkfsdm;
	}

	public void setFkfsdm(String fkfsdm) {
		this.fkfsdm = fkfsdm;
	}

	public String getGg() {
		return gg;
	}

	public void setGg(String gg) {
		this.gg = gg;
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

	public String getPrefixFlg() {
		return prefixFlg;
	}

	public void setPrefixFlg(String prefixFlg) {
		this.prefixFlg = prefixFlg;
	}

	public String getCheckqdl() {
		return checkqdl;
	}

	public void setCheckqdl(String checkqdl) {
		this.checkqdl = checkqdl;
	}

	public String[] getScbjs() {
		return scbjs;
	}

	public void setScbjs(String[] scbjs) {
		this.scbjs = scbjs;
		for (int i = 0; i < scbjs.length; i++) {
			this.scbjs[i] = this.scbjs[i].replace("'", "");
		}
	}

	public String getFqbj() {
		return fqbj;
	}

	public void setFqbj(String fqbj) {
		this.fqbj = fqbj;
	}

	public String getSprjsid() {
		return sprjsid;
	}

	public void setSprjsid(String sprjsid) {
		this.sprjsid = sprjsid;
	}

	public String getSprjsmc() {
		return sprjsmc;
	}

	public void setSprjsmc(String sprjsmc) {
		this.sprjsmc = sprjsmc;
	}

	public String getSprid() {
		return sprid;
	}

	public void setSprid(String sprid) {
		this.sprid = sprid;
	}

	public String getSprxm() {
		return sprxm;
	}

	public void setSprxm(String sprxm) {
		this.sprxm = sprxm;
	}

	public String getSpryhm() {
		return spryhm;
	}

	public void setSpryhm(String spryhm) {
		this.spryhm = spryhm;
	}

	public String getSprddid() {
		return sprddid;
	}

	public void setSprddid(String sprddid) {
		this.sprddid = sprddid;
	}

	public String getQgsl() {
		return qgsl;
	}

	public void setQgsl(String qgsl) {
		this.qgsl = qgsl;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}
	public String getFkfmc() {
		return fkfmc;
	}

	public void setFkfmc(String fkfmc) {
		this.fkfmc = fkfmc;
	}

	public String getGysfzr() {
		return gysfzr;
	}

	public void setGysfzr(String gysfzr) {
		this.gysfzr = gysfzr;
	}

	public String getGysfzrdh() {
		return gysfzrdh;
	}

	public void setGysfzrdh(String gysfzrdh) {
		this.gysfzrdh = gysfzrdh;
	}

	public String getHtywlx() {
		return htywlx;
	}

	public void setHtywlx(String htywlx) {
		this.htywlx = htywlx;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getFkbfb() {
		return fkbfb;
	}

	public void setFkbfb(String fkbfb) {
		this.fkbfb = fkbfb;
	}

	public String getJqdm() {
		return jqdm;
	}

	public void setJqdm(String jqdm) {
		this.jqdm = jqdm;
	}

	public String getGysdm() {
		return gysdm;
	}

	public void setGysdm(String gysdm) {
		this.gysdm = gysdm;
	}

	public String getSqrq() {
		return sqrq;
	}

	public void setSqrq(String sqrq) {
		this.sqrq = sqrq;
	}

	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
	}

	public String getFwbj() {
		return fwbj;
	}

	public void setFwbj(String fwbj) {
		this.fwbj = fwbj;
	}

	public String getHtmxJson() {
		return htmxJson;
	}

	public void setHtmxJson(String htmxJson) {
		this.htmxJson = htmxJson;
	}

	public String getDjhs() {
		return djhs;
	}

	public void setDjhs(String djhs) {
		this.djhs = djhs;
	}

	public String getFzrmc() {
		return fzrmc;
	}

	public void setFzrmc(String fzrmc) {
		this.fzrmc = fzrmc;
	}

	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
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

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getFkje() {
		return fkje;
	}

	public void setFkje(String fkje) {
		this.fkje = fkje;
	}

	public String getFpfsmc() {
		return fpfsmc;
	}

	public void setFpfsmc(String fpfsmc) {
		this.fpfsmc = fpfsmc;
	}

	public String getFkfsmc() {
		return fkfsmc;
	}

	public void setFkfsmc(String fkfsmc) {
		this.fkfsmc = fkfsmc;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String[] getFpfss() {
		return fpfss;
	}

	public void setFpfss(String[] fpfss) {
		this.fpfss = fpfss;
		for (int i = 0; i < fpfss.length; i++) {
			this.fpfss[i] = this.fpfss[i].replace("'", "");
		}
	}

	public String[] getFkfss() {
		return fkfss;
	}

	public void setFkfss(String[] fkfss) {
		this.fkfss = fkfss;
		for (int i = 0; i < fkfss.length; i++) {
			this.fkfss[i] = this.fkfss[i].replace("'", "");
		}
	}

	public String getBizmc() {
		return bizmc;
	}

	public void setBizmc(String bizmc) {
		this.bizmc = bizmc;
	}

	public String getYwlxmc() {
		return ywlxmc;
	}

	public void setYwlxmc(String ywlxmc) {
		this.ywlxmc = ywlxmc;
	}

	public String getNbbhqz() {
		return nbbhqz;
	}

	public void setNbbhqz(String nbbhqz) {
		this.nbbhqz = nbbhqz;
	}

	public Double getZjemax() {
		return zjemax;
	}

	public void setZjemax(Double zjemax) {
		this.zjemax = zjemax;
	}

	public Double getZjemin() {
		return zjemin;
	}

	public void setZjemin(Double zjemin) {
		this.zjemin = zjemin;
	}

	public String getShsjend() {
		return shsjend;
	}

	public void setShsjend(String shsjend) {
		this.shsjend = shsjend;
	}

	public String getShsjstart() {
		return shsjstart;
	}

	public void setShsjstart(String shsjstart) {
		this.shsjstart = shsjstart;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String[] getSzbjs() {
		return szbjs;
	}

	public void setSzbjs(String[] szbjs) {
		this.szbjs = szbjs;
		for (int i = 0; i < szbjs.length; i++) {
			this.szbjs[i] = this.szbjs[i].replace("'", "");
		}
	}
}
