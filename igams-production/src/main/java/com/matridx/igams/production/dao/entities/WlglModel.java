package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;
import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="WlglModel")
public class WlglModel extends BaseBasicModel{
	//物料ID
	private String wlid;
	//物料编码
	private String wlbm;
	//物料类别
	private String wllb;
	//物料子类别
	private String wlzlb;
	//物料名称
	private String wlmc;
	//代理供应商
	private String dlgys;
	//审核时间
	private String shsj;
	//生产商
	private String scs;
	//原厂货号
	private String ychh;
	//规格
	private String gg;
	//计量单位
	private String jldw;
	//保存条件
	private String bctj;
	//保质期
	private String bzq;
	//保质期标记
	private String bzqflg;
	//是否危险品
	private String sfwxp;
	//备注
	private String bz;
	//审核状态   00:未提交 10:审核中  15:审核未通过  80:审核通过
	private String zt;
	//旧物料编码
	private String jwlbm;
	//类别
	private String lb;
	//安全库存
	private String aqkc;
	//起订量
	private String qdl;
	//货期
	private String hq;
	//最低采购量
	private String zdcgl;
	//物料子类别统称
	private String wlzlbtc;
	//物料子类别统称名称
	private String wlzlbtcmc;
	//所属产品类别
	private String sscplb;
	//产品注册号
	private String cpzch;
	//人份
	private String rf;
	//技术指标
	private String jszb;
	//质量要求
	private String zlyq;

	public String getJszb() {
		return jszb;
	}

	public void setJszb(String jszb) {
		this.jszb = jszb;
	}

	public String getZlyq() {
		return zlyq;
	}

	public void setZlyq(String zlyq) {
		this.zlyq = zlyq;
	}

	public String getRf() {
		return rf;
	}

	public void setRf(String rf) {
		this.rf = rf;
	}

	public String getCpzch() {
		return cpzch;
	}

	public void setCpzch(String cpzch) {
		this.cpzch = cpzch;
	}

	public String getSscplb() {
		return sscplb;
	}
	public void setSscplb(String sscplb) {
		this.sscplb = sscplb;
	}
	public String getWlzlbtcmc() {
		return wlzlbtcmc;
	}
	public void setWlzlbtcmc(String wlzlbtcmc) {
		this.wlzlbtcmc = wlzlbtcmc;
	}
	public String getWlzlbtc() {
		return wlzlbtc;
	}
	public void setWlzlbtc(String wlzlbtc) {
		this.wlzlbtc = wlzlbtc;
	}
	public String getZdcgl() {
		return zdcgl;
	}
	public void setZdcgl(String zdcgl) {
		this.zdcgl = zdcgl;
	}
	public String getHq()
	{
		return hq;
	}
	public void setHq(String hq)
	{
		this.hq = hq;
	}
	public String getQdl()
	{
		return qdl;
	}
	public void setQdl(String qdl)
	{
		this.qdl = qdl;
	}
	//安全库存
	public String getAqkc() {
		return aqkc;
	}
	public void setAqkc(String aqkc) {
		this.aqkc = aqkc;
	}
	//类别
	public String getLb() {
		return lb;
	}
	public void setLb(String lb) {
		this.lb = lb;
	}
	//物料ID
	public String getWlid() {
		return wlid;
	}
	public void setWlid(String wlid){
		this.wlid = wlid;
	}
	//物料编码
	public String getWlbm() {
		return wlbm;
	}
	public void setWlbm(String wlbm){
		this.wlbm = wlbm;
	}
	//物料名称
	public String getWlmc() {
		return wlmc;
	}
	public void setWlmc(String wlmc){
		this.wlmc = wlmc;
	}
	//代理供应商
	public String getDlgys() {
		return dlgys;
	}
	public void setDlgys(String dlgys){
		this.dlgys = dlgys;
	}
	//生产商
	public String getScs() {
		return scs;
	}
	public void setScs(String scs){
		this.scs = scs;
	}
	//原厂货号
	public String getYchh() {
		return ychh;
	}
	public void setYchh(String ychh){
		this.ychh = ychh;
	}
	//规格
	public String getGg() {
		return gg;
	}
	public void setGg(String gg){
		this.gg = gg;
	}
	//计量单位
	public String getJldw() {
		return jldw;
	}
	public void setJldw(String jldw){
		this.jldw = jldw;
	}
	//保存条件
	public String getBctj() {
		return bctj;
	}
	public void setBctj(String bctj){
		this.bctj = bctj;
	}
	//保质期
	public String getBzq() {
		return bzq;
	}
	public void setBzq(String bzq){
		this.bzq = bzq;
	}
	//是否危险品
	public String getSfwxp() {
		return sfwxp;
	}
	public void setSfwxp(String sfwxp){
		this.sfwxp = sfwxp;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//审核状态   00:未提交 10:审核中  15:审核未通过  80:审核通过
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}

	public String getWllb() {
		return wllb;
	}
	public void setWllb(String wllb) {
		this.wllb = wllb;
	}

	public String getWlzlb() {
		return wlzlb;
	}
	public void setWlzlb(String wlzlb) {
		this.wlzlb = wlzlb;
	}
	public String getShsj() {
		return shsj;
	}
	public void setShsj(String shsj) {
		this.shsj = shsj;
	}

	public String getBzqflg() {
		return bzqflg;
	}
	public void setBzqflg(String bzqflg) {
		this.bzqflg = bzqflg;
	}
	//旧物料编码
	public String getJwlbm() {
		return jwlbm;
	}
	public void setJwlbm(String jwlbm) {
		this.jwlbm = jwlbm;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
