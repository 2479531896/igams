package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="HwxxModel")
public class HwxxModel extends BaseBasicModel{
	//货物ID
	private String hwid;
	//合同明细ID
	private String htmxid;
	//请购明细ID
	private String qgmxid;
	//物料ID
	private String wlid;
	//数量  到货检验后的实际数量，相当于减去退回数量
	private String sl;
	//有效期至
	private String yxq;
	//到货ID
	private String dhid;
	//不合格货物
	private String bhghw;
	//到货数量  到货时的数量
	private String dhsl;
	//初验退回数量
	private String cythsl;
	//到货备注
	private String dhbz;
	//质检退回数量
	private String zjthsl;
	//取样量
	private String qyl;
	//取样日期
	private String qyrq;
	//报告单号
	private String bgdh;
	//质检日期
	private String zjrq;
	//类别参数扩展1
	private String lbcskz;
	//质检人员
	private String zjry;
	//检验备注
	private String jybz;
	//到货检验ID
	private String dhjyid;
	//入库ID
	private String rkid;
	//仓库ID
	private String ckid;
	//库位编号
	private String kwbh;
	//入库人员
	private String rkry;
	//入库日期
	private String rkrq;
	//入库备注
	private String rkbz;
	//库存量
	private String kcl;
	//预定数
	private String yds;
	//关联ID：关联U8系统中的主键ID:autoid
	private String glid;
	//备注
	private String bz;
	//状态 10,15,80
	private String zt;
	//生产日期
	private String scrq;
	//追溯号
	private String zsh;
	//库存关联U8库存
	private String kcglid;
	//生产批号
	private String scph;
	//初始库位
	private String cskw;
	//调拨明细关联ID
	private String dbmxglid;
	//借用数量
	private String jysl;
	//借用标记 0,无需借用,1,待借用,2已借用，3，已归还
	private String jybj;
	//类别标记-0.不是adc类，1是ABC类--
	private String lbbj;
	//借用人员
	private String jyry;
	//借用时间
	private String jysj;
	//归还数量
	private String ghsl;
	//归还人员
	private String ghry;
	//归还时间
	private String ghsj;
	//验收单号
	private String ysdh;
	//其他入库明细ID(关联U8其他入库明细ID)
	private String qtrkmxid;
	//其他出库明细ID(关联U8其他出库明细ID)
	private String qtckmxid;
	//调拨单id
	private String dbglid;
	//U8入库单号
	private String U8rkdh;
	//生产指令明细id
	private String sczlmxid;
	//采购红冲关联字段
	private String chhwid;
	//产品注册号
	private String cpzch;
	//退库标记
	private String tkbj;
	//留样数量
	private String lysl;
	private String jyqxbj;

	public String getJyqxbj() {
		return jyqxbj;
	}

	public void setJyqxbj(String jyqxbj) {
		this.jyqxbj = jyqxbj;
	}

	public String getLysl() {
		return lysl;
	}

	public void setLysl(String lysl) {
		this.lysl = lysl;
	}

	public String getTkbj() {
		return tkbj;
	}

	public void setTkbj(String tkbj) {
		this.tkbj = tkbj;
	}

	public String getCpzch() {
		return cpzch;
	}

	public void setCpzch(String cpzch) {
		this.cpzch = cpzch;
	}
	public String getChhwid() {
		return chhwid;
	}

	public void setChhwid(String chhwid) {
		this.chhwid = chhwid;
	}

	public String getSczlmxid() {
		return sczlmxid;
	}

	public void setSczlmxid(String sczlmxid) {
		this.sczlmxid = sczlmxid;
	}

	public String getU8rkdh() {
		return U8rkdh;
	}

	public void setU8rkdh(String u8rkdh) {
		U8rkdh = u8rkdh;
	}

	public String getDbglid() {
		return dbglid;
	}

	public void setDbglid(String dbglid) {
		this.dbglid = dbglid;
	}

	public String getQtrkmxid() {
		return qtrkmxid;
	}

	public void setQtrkmxid(String qtrkmxid) {
		this.qtrkmxid = qtrkmxid;
	}

	public String getQtckmxid() {
		return qtckmxid;
	}

	public void setQtckmxid(String qtckmxid) {
		this.qtckmxid = qtckmxid;
	}

	public String getYsdh() {
		return ysdh;
	}
	public void setYsdh(String ysdh) {
		this.ysdh = ysdh;
	}
	public String getJyry() {
		return jyry;
	}
	public void setJyry(String jyry) {
		this.jyry = jyry;
	}
	public String getJysj() {
		return jysj;
	}
	public void setJysj(String jysj) {
		this.jysj = jysj;
	}
	public String getGhsl() {
		return ghsl;
	}
	public void setGhsl(String ghsl) {
		this.ghsl = ghsl;
	}
	public String getGhry() {
		return ghry;
	}
	public void setGhry(String ghry) {
		this.ghry = ghry;
	}
	public String getGhsj() {
		return ghsj;
	}
	public void setGhsj(String ghsj) {
		this.ghsj = ghsj;
	}
	public String getLbbj() {
		return lbbj;
	}
	public void setLbbj(String lbbj) {
		this.lbbj = lbbj;
	}
	public String getJysl() {
		return jysl;
	}
	public void setJysl(String jysl) {
		this.jysl = jysl;
	}
	public String getJybj() {
		return jybj;
	}
	public void setJybj(String jybj) {
		this.jybj = jybj;
	}
	public String getDbmxglid() {
		return dbmxglid;
	}
	public void setDbmxglid(String dbmxglid) {
		this.dbmxglid = dbmxglid;
	}
	public String getCskw() {
		return cskw;
	}
	public void setCskw(String cskw) {
		this.cskw = cskw;
	}
	public String getScph() {
		return scph;
	}
	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getKcglid() {
		return kcglid;
	}
	public void setKcglid(String kcglid) {
		this.kcglid = kcglid;
	}
	//追溯号
	public String getZsh() {
		return zsh;
	}
	public void setZsh(String zsh) {
		this.zsh = zsh;
	}
	//生产日期
	public String getScrq() {
		return scrq;
	}
	public void setScrq(String scrq) {
		this.scrq = scrq;
	}
	//货物ID
	public String getHwid() {
		return hwid;
	}
	public void setHwid(String hwid){
		this.hwid = hwid;
	}
	//合同明细ID
	public String getHtmxid() {
		return htmxid;
	}
	public void setHtmxid(String htmxid){
		this.htmxid = htmxid;
	}
	//请购明细ID
	public String getQgmxid() {
		return qgmxid;
	}
	public void setQgmxid(String qgmxid){
		this.qgmxid = qgmxid;
	}
	//物料ID
	public String getWlid() {
		return wlid;
	}
	public void setWlid(String wlid){
		this.wlid = wlid;
	}
	//数量  到货检验后的实际数量，相当于减去退回数量
	public String getSl() {
		return sl;
	}
	public void setSl(String sl){
		this.sl = sl;
	}
	//有效期至
	public String getYxq() {
		return yxq;
	}
	public void setYxq(String yxq){
		this.yxq = yxq;
	}
	//到货ID
	public String getDhid() {
		return dhid;
	}
	public void setDhid(String dhid){
		this.dhid = dhid;
	}
	//到货数量  到货时的数量
	public String getDhsl() {
		return dhsl;
	}
	public void setDhsl(String dhsl){
		this.dhsl = dhsl;
	}
	//初验退回数量
	public String getCythsl() {
		return cythsl;
	}
	public void setCythsl(String cythsl){
		this.cythsl = cythsl;
	}
	//到货备注
	public String getDhbz() {
		return dhbz;
	}
	public void setDhbz(String dhbz){
		this.dhbz = dhbz;
	}
	//质检退回数量
	public String getZjthsl() {
		return zjthsl;
	}
	public void setZjthsl(String zjthsl){
		this.zjthsl = zjthsl;
	}
	//取样量
	public String getQyl() {
		return qyl;
	}
	public void setQyl(String qyl){
		this.qyl = qyl;
	}

	public String getLbcskz() {
		return lbcskz;
	}

	public void setLbcskz(String lbcskz) {
		this.lbcskz = lbcskz;
	}

	//取样日期
	public String getQyrq() {
		return qyrq;
	}
	public void setQyrq(String qyrq){
		this.qyrq = qyrq;
	}
	//报告单号
	public String getBgdh() {
		return bgdh;
	}
	public void setBgdh(String bgdh){
		this.bgdh = bgdh;
	}
	//质检日期
	public String getZjrq() {
		return zjrq;
	}
	public void setZjrq(String zjrq){
		this.zjrq = zjrq;
	}
	//质检人员
	public String getZjry() {
		return zjry;
	}
	public void setZjry(String zjry){
		this.zjry = zjry;
	}
	//检验备注
	public String getJybz() {
		return jybz;
	}
	public void setJybz(String jybz){
		this.jybz = jybz;
	}
	//到货检验ID
	public String getDhjyid() {
		return dhjyid;
	}
	public void setDhjyid(String dhjyid){
		this.dhjyid = dhjyid;
	}
	//入库ID
	public String getRkid() {
		return rkid;
	}
	public void setRkid(String rkid){
		this.rkid = rkid;
	}
	//仓库ID
	public String getCkid() {
		return ckid;
	}
	public void setCkid(String ckid){
		this.ckid = ckid;
	}
	//库位编号
	public String getKwbh() {
		return kwbh;
	}
	public void setKwbh(String kwbh){
		this.kwbh = kwbh;
	}
	//入库人员
	public String getRkry() {
		return rkry;
	}
	public void setRkry(String rkry){
		this.rkry = rkry;
	}
	//入库日期
	public String getRkrq() {
		return rkrq;
	}
	public void setRkrq(String rkrq){
		this.rkrq = rkrq;
	}
	//入库备注
	public String getRkbz() {
		return rkbz;
	}
	public void setRkbz(String rkbz){
		this.rkbz = rkbz;
	}
	//库存量
	public String getKcl() {
		return kcl;
	}
	public void setKcl(String kcl){
		this.kcl = kcl;
	}
	//预定数
	public String getYds() {
		return yds;
	}
	public void setYds(String yds){
		this.yds = yds;
	}
	//关联ID：关联U8系统中的主键ID:autoid
	public String getGlid() {
		return glid;
	}
	public void setGlid(String glid){
		this.glid = glid;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//状态 10,15,80
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}

	public String getBhghw() {
		return bhghw;
	}

	public void setBhghw(String bhghw) {
		this.bhghw = bhghw;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
