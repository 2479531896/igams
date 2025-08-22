package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="HtmxModel")
public class HtmxModel extends BaseModel{
	//合同明细ID
	private String htmxid;
	//合同ID
	private String htid;
	//请购ID
	private String qgid;
	//请购明细ID
	private String qgmxid;
	//序号
	private String xh;
	//物料ID
	private String wlid;
	//货物名称
	private String hwmc;
	//数量
	private String sl;
	//含税单价
	private String hsdj;
	//无税单价
	private String wsdj;
	//税率  从系统设置表里获取
	private String suil;
	//合计金额  含税
	private String hjje;
	//计划到货日期
	private String jhdhrq;
	//备注
	private String bz;
	//状态
	private String zt;
	//U8明细ID PO_Podetails  ID
	private String u8mxid;
	//快递类型
	private String kdlx;
	//快递类型名称
	private String kdlxmc;
	//快递单号
	private String kddh;
	//到货单号关联
	private String dhdhgl;
	//入库数量
	private String rksl;
	//原计划到货日期
	private String yjhdhrq;
	//是否发票维护
	private String sffpwh;
	//是否固定资产
	private String sfgdzc;
	//行状态 1 打开 0 关闭
	private String hzt;
	//打开人员
	private String dkry;
	//关闭人员
	private String gbry;
	//初次订购
	private String ccdg;
	//物料分类
	private String wlfl;
	//产品注册号
	private String cpzch;
	//到货周期
	private String dhzq;
	//框架合同明细id
	private String kjhtmxid;
	//是否停用    1 启用  0 停用
	private String sfty;
	//补充合同明细id
	private String bchtmxid;

	public String getBchtmxid() {
		return bchtmxid;
	}

	public void setBchtmxid(String bchtmxid) {
		this.bchtmxid = bchtmxid;
	}

	public String getSfty() {
		return sfty;
	}

	public void setSfty(String sfty) {
		this.sfty = sfty;
	}

	public String getKjhtmxid() {
		return kjhtmxid;
	}

	public void setKjhtmxid(String kjhtmxid) {
		this.kjhtmxid = kjhtmxid;
	}

	public String getDhzq() {
		return dhzq;
	}

	public void setDhzq(String dhzq) {
		this.dhzq = dhzq;
	}

	public String getCpzch() {
		return cpzch;
	}

	public void setCpzch(String cpzch) {
		this.cpzch = cpzch;
	}
	public String getCcdg() {
		return ccdg;
	}

	public void setCcdg(String ccdg) {
		this.ccdg = ccdg;
	}

	public String getWlfl() {
		return wlfl;
	}

	public void setWlfl(String wlfl) {
		this.wlfl = wlfl;
	}

	public String getHzt() {
		return hzt;
	}

	public void setHzt(String hzt) {
		this.hzt = hzt;
	}

	public String getDkry() {
		return dkry;
	}

	public void setDkry(String dkry) {
		this.dkry = dkry;
	}

	public String getGbry() {
		return gbry;
	}

	public void setGbry(String gbry) {
		this.gbry = gbry;
	}

    public String getSfgdzc() {
        return sfgdzc;
    }

    public void setSfgdzc(String sfgdzc) {
		this.sfgdzc = sfgdzc;
    }
    public String getSffpwh() {
		return sffpwh;
	}

	public void setSffpwh(String sffpwh) {
		this.sffpwh = sffpwh;
	}

	public String getYjhdhrq() {
		return yjhdhrq;
	}

	public void setYjhdhrq(String yjhdhrq) {
		this.yjhdhrq = yjhdhrq;
	}

	public String getRksl() {
		return rksl;
	}
	public void setRksl(String rksl) {
		this.rksl = rksl;
	}
	public String getDhdhgl() {
		return dhdhgl;
	}
	public void setDhdhgl(String dhdhgl) {
		this.dhdhgl = dhdhgl;
	}
	public String getKdlx() {
		return kdlx;
	}
	public void setKdlx(String kdlx) {
		this.kdlx = kdlx;
	}
	public String getKdlxmc() {
		return kdlxmc;
	}
	public void setKdlxmc(String kdlxmc) {
		this.kdlxmc = kdlxmc;
	}
	public String getKddh() {
		return kddh;
	}
	public void setKddh(String kddh) {
		this.kddh = kddh;
	}
	public String getHtmxid() {
		return htmxid;
	}
	public void setHtmxid(String htmxid){
		this.htmxid = htmxid;
	}
	//合同ID
	public String getHtid() {
		return htid;
	}
	public void setHtid(String htid){
		this.htid = htid;
	}
	//请购ID
	public String getQgid() {
		return qgid;
	}
	public void setQgid(String qgid){
		this.qgid = qgid;
	}
	//请购明细ID
	public String getQgmxid() {
		return qgmxid;
	}
	public void setQgmxid(String qgmxid){
		this.qgmxid = qgmxid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//物料ID
	public String getWlid() {
		return wlid;
	}
	public void setWlid(String wlid){
		this.wlid = wlid;
	}
	//货物名称
	public String getHwmc() {
		return hwmc;
	}
	public void setHwmc(String hwmc){
		this.hwmc = hwmc;
	}
	//数量
	public String getSl() {
		return sl;
	}
	public void setSl(String sl){
		this.sl = sl;
	}
	//含税单价
	public String getHsdj() {
		return hsdj;
	}
	public void setHsdj(String hsdj){
		this.hsdj = hsdj;
	}
	//无税单价
	public String getWsdj() {
		return wsdj;
	}
	public void setWsdj(String wsdj){
		this.wsdj = wsdj;
	}
	//税率  从系统设置表里获取
	public String getSuil() {
		return suil;
	}
	public void setSuil(String suil){
		this.suil = suil;
	}
	//合计金额  含税
	public String getHjje() {
		return hjje;
	}
	public void setHjje(String hjje){
		this.hjje = hjje;
	}
	//计划到货日期
	public String getJhdhrq() {
		return jhdhrq;
	}
	public void setJhdhrq(String jhdhrq){
		this.jhdhrq = jhdhrq;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//状态
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}
	//U8明细ID PO_Podetails  ID
	public String getU8mxid() {
		return u8mxid;
	}
	public void setU8mxid(String u8mxid){
		this.u8mxid = u8mxid;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
