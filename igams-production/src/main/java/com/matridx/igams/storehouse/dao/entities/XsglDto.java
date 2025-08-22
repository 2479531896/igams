package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;


@Alias(value="XsglDto")
public class XsglDto extends XsglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//entire
	private String entire;
	//销售部门名称
	private String xsbmmc;
	//业务员名称
	private String ywymc;
	//客户id
	private String khid;
	//销售部门代码
	private String xsbmdm;
	//业务员代码
	private String ywydm;
	//客户代码
	private String khdm;
	//客户简称名称
	private String khmc;
	//客户名称
	private String khjcmc;
	//销售类型名称
	private String xslxmc;
	//业务类型名称
	private String ywlxmc;
	private String ddrqstart;
	private String ddrqend;

	//预计发货时间
	private String yfhrqstart;
	private String yfhrqend;
	//销售明细
	private String xsmx_json;
	//执行标记
	private String flag;
	//销售类型代码
	private String xslxdm;
	//业务类型代码
	private String ywlxdm;
	//真实姓名
	private String zsxm;
	//物料
	private String wlbm;
	//物料名称
	private String wlmc;
	//状态名称
	private String ztmc;
	//归属人
	private String gsr;
	//通过IDs
	private List<String> tgids;
	//税率
	private String suil;
	//客户终端
	private String khzd;
	//运输要求
	private String ysyq;
	//合同单号
	private String htdh;
	//导出关联标记为所选择的字段
	private String sqlParam;
	//规格
	private String gg;
	//单位
	private String jldw;
	//产品类型名称
	private String cplxmc;
	//数量
	private String sl;
	//含税单价
	private String hsdj;
	//原厂货号
	private String ychh;
	//条数
	private int ts;
	//销售明细id
	private String xsmxid;
	//钉钉查询字段
	private String ddentire;
	//审核人
	private String shrymc;
	//终端区域
	private String zdqymc;
	//是否确认多
	private String[] sfqss;
	//是否签收多
	private String[] sfqrs;
	private String[] khlbs;
	//客户类型名称
	private String khlbmc;
	//联系人
	private String khlxr;
	//客户联系方式
	private String khlxfs;
	//价税总额
	private String jsze;
	//制单人
	private String zdr;
	//预发货日期
	private String yfhrq;
	//到款金额小
	private String dkjemin;
	//到款金额大
	private String dkjemax;
	//借用单号
	private String jydh;
	//原销售单号
	private String yoaxsdh;
	//单据类型s
	private String[] djlxs;
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
	//业务员钉钉ID
	private String ywyddid;
	//U8code
	private String grouping;
	private String exportFlag;//区分是正常导出还是主表导出
	private String htid;//合同id
	private String fzrmc;//负责人
	private String yfzr;//原负责人
	private String ysfsmc;//运输方式名称

	public String getYsfsmc() {
		return ysfsmc;
	}

	public void setYsfsmc(String ysfsmc) {
		this.ysfsmc = ysfsmc;
	}

	public String getYfzr() {
		return yfzr;
	}

	public void setYfzr(String yfzr) {
		this.yfzr = yfzr;
	}

	public String getFzrmc() {
		return fzrmc;
	}

	public void setFzrmc(String fzrmc) {
		this.fzrmc = fzrmc;
	}

	public String getHtid() {
		return htid;
	}

	public void setHtid(String htid) {
		this.htid = htid;
	}

	public String getExportFlag() {
		return exportFlag;
	}

	public void setExportFlag(String exportFlag) {
		this.exportFlag = exportFlag;
	}

	public String getGrouping() {
		return grouping;
	}

	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}

	public String getYwyddid() {
		return ywyddid;
	}

	public void setYwyddid(String ywyddid) {
		this.ywyddid = ywyddid;
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

	public String[] getDjlxs() {
		return djlxs;
	}

	public void setDjlxs(String[] djlxs) {
		this.djlxs = djlxs;
	}
	public String getYoaxsdh() {
		return yoaxsdh;
	}

	public void setYoaxsdh(String yoaxsdh) {
		this.yoaxsdh = yoaxsdh;
	}

	public String getJydh() {
		return jydh;
	}

	public void setJydh(String jydh) {
		this.jydh = jydh;
	}
	public String getDkjemin() {
		return dkjemin;
	}

	public void setDkjemin(String dkjemin) {
		this.dkjemin = dkjemin;
	}

	public String getDkjemax() {
		return dkjemax;
	}

	public void setDkjemax(String dkjemax) {
		this.dkjemax = dkjemax;
	}

	public String getYfhrq() {
		return yfhrq;
	}

	public void setYfhrq(String yfhrq) {
		this.yfhrq = yfhrq;
	}

	public String getZdr() {
		return zdr;
	}

	public void setZdr(String zdr) {
		this.zdr = zdr;
	}

	public String getJsze() {
		return jsze;
	}

	public void setJsze(String jsze) {
		this.jsze = jsze;
	}

	public String getKhlxr() {
		return khlxr;
	}

	public void setKhlxr(String khlxr) {
		this.khlxr = khlxr;
	}

	public String getKhlxfs() {
		return khlxfs;
	}

	public void setKhlxfs(String khlxfs) {
		this.khlxfs = khlxfs;
	}

	public String getKhlbmc() {
		return khlbmc;
	}

	public void setKhlbmc(String khlbmc) {
		this.khlbmc = khlbmc;
	}

	public String[] getSfqss() {
		return sfqss;
	}

	public void setSfqss(String[] sfqss) {
		this.sfqss = sfqss;
	}

	public String[] getSfqrs() {
		return sfqrs;
	}

	public void setSfqrs(String[] sfqrs) {
		this.sfqrs = sfqrs;
	}

	public String[] getKhlbs() {
		return khlbs;
	}

	public void setKhlbs(String[] khlbs) {
		this.khlbs = khlbs;
	}

	public String getShrymc() {
		return shrymc;
	}

	public void setShrymc(String shrymc) {
		this.shrymc = shrymc;
	}

	public String getZdqymc() {
		return zdqymc;
	}

	public void setZdqymc(String zdqymc) {
		this.zdqymc = zdqymc;
	}

	public String getDdentire() {
		return ddentire;
	}

	public void setDdentire(String ddentire) {
		this.ddentire = ddentire;
	}

	public String getXsmxid() {
		return xsmxid;
	}

	public void setXsmxid(String xsmxid) {
		this.xsmxid = xsmxid;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	//物料id
	private String wlid;

	public int getTs() {
		return ts;
	}

	public void setTs(int ts) {
		this.ts = ts;
	}

	public String getYchh() {
		return ychh;
	}

	public void setYchh(String ychh) {
		this.ychh = ychh;
	}

	public String getHsdj() {
		return hsdj;
	}

	public void setHsdj(String hsdj) {
		this.hsdj = hsdj;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getCplxmc() {
		return cplxmc;
	}

	public void setCplxmc(String cplxmc) {
		this.cplxmc = cplxmc;
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

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getHtdh() {
		return htdh;
	}

	public void setHtdh(String htdh) {
		this.htdh = htdh;
	}

	public String getKhzd() {
		return khzd;
	}

	public void setKhzd(String khzd) {
		this.khzd = khzd;
	}

	public String getYsyq() {
		return ysyq;
	}

	public void setYsyq(String ysyq) {
		this.ysyq = ysyq;
	}

	public String getSuil() {
		return suil;
	}

	public void setSuil(String suil) {
		this.suil = suil;
	}

	public String getYfhrqstart() {
		return yfhrqstart;
	}

	public void setYfhrqstart(String yfhrqstart) {
		this.yfhrqstart = yfhrqstart;
	}

	public String getYfhrqend() {
		return yfhrqend;
	}

	public void setYfhrqend(String yfhrqend) {
		this.yfhrqend = yfhrqend;
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

	public List<String> getTgids() {
		return tgids;
	}

	public void setTgids(List<String> tgids) {
		this.tgids = tgids;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getXslxdm() {
		return xslxdm;
	}

	public void setXslxdm(String xslxdm) {
		this.xslxdm = xslxdm;
	}

	public String getYwlxdm() {
		return ywlxdm;
	}

	public void setYwlxdm(String ywlxdm) {
		this.ywlxdm = ywlxdm;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getXsmx_json() {
		return xsmx_json;
	}

	public void setXsmx_json(String xsmx_json) {
		this.xsmx_json = xsmx_json;
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

	public String getXslxmc() {
		return xslxmc;
	}

	public void setXslxmc(String xslxmc) {
		this.xslxmc = xslxmc;
	}

	public String getYwlxmc() {
		return ywlxmc;
	}

	public void setYwlxmc(String ywlxmc) {
		this.ywlxmc = ywlxmc;
	}

	public String getKhjcmc() {
		return khjcmc;
	}

	public void setKhjcmc(String khjcmc) {
		this.khjcmc = khjcmc;
	}

	public String getXsbmmc() {
		return xsbmmc;
	}

	public void setXsbmmc(String xsbmmc) {
		this.xsbmmc = xsbmmc;
	}

	public String getYwymc() {
		return ywymc;
	}

	public void setYwymc(String ywymc) {
		this.ywymc = ywymc;
	}

	public String getKhid() {
		return khid;
	}

	public void setKhid(String khid) {
		this.khid = khid;
	}

	public String getXsbmdm() {
		return xsbmdm;
	}

	public void setXsbmdm(String xsbmdm) {
		this.xsbmdm = xsbmdm;
	}

	public String getYwydm() {
		return ywydm;
	}

	public void setYwydm(String ywydm) {
		this.ywydm = ywydm;
	}

	public String getKhdm() {
		return khdm;
	}

	public void setKhdm(String khdm) {
		this.khdm = khdm;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getGsr() {
		return gsr;
	}

	public void setGsr(String gsr) {
		this.gsr = gsr;
	}

	public String getZtmc() {
		return ztmc;
	}

	public void setZtmc(String ztmc) {
		this.ztmc = ztmc;
	}

	public XsglDto() {
	}

	public XsglDto(String xsid,String ysk,String xgry) {
		super(xsid,ysk,xgry);
	}
}
