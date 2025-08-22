package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;
import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="WlgllsbModel")
public class WlgllsbModel extends BaseBasicModel{
	//临时ID
	private String lsid;
	//物料ID
	private String wlid;
	//物料编码
	private String wlbm;
	//旧物料编码
	private String jwlbm;
	//物料类别
	private String wllb;
	//物料子类别
	private String wlzlb;
	//物料名称
	private String wlmc;
	//代理供应商
	private String dlgys;
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
	//保质期标记 如为1 ，则填写数字以外的文字
	private String bzqflg;
	//是否危险品
	private String sfwxp;
	//备注
	private String bz;
	//审核时间
	private String shsj;
	//审核状态   00:未提交 10:审核中  15:审核未通过  80:审核通过
	private String zt;
	//起订量
	private String qdl;
	//货期
	private String hq;
	//物料子类别统称
	private String wlzlbtc;
	//产品注册号
	private String cpzch;

	public String getCpzch() {
		return cpzch;
	}

	public void setCpzch(String cpzch) {
		this.cpzch = cpzch;
	}

	public String getWlzlbtc() {
		return wlzlbtc;
	}
	public void setWlzlbtc(String wlzlbtc) {
		this.wlzlbtc = wlzlbtc;
	}
	public String getQdl()
	{
		return qdl;
	}
	public void setQdl(String qdl)
	{
		this.qdl = qdl;
	}
	public String getHq()
	{
		return hq;
	}
	public void setHq(String hq)
	{
		this.hq = hq;
	}
	//临时ID
	public String getLsid() {
		return lsid;
	}
	public void setLsid(String lsid){
		this.lsid = lsid;
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
	//旧物料编码
	public String getJwlbm() {
		return jwlbm;
	}
	public void setJwlbm(String jwlbm){
		this.jwlbm = jwlbm;
	}
	//物料类别
	public String getWllb() {
		return wllb;
	}
	public void setWllb(String wllb){
		this.wllb = wllb;
	}
	//物料子类别
	public String getWlzlb() {
		return wlzlb;
	}
	public void setWlzlb(String wlzlb){
		this.wlzlb = wlzlb;
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
	//保质期标记 如为1 ，则填写数字以外的文字
	public String getBzqflg() {
		return bzqflg;
	}
	public void setBzqflg(String bzqflg){
		this.bzqflg = bzqflg;
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
	//审核时间
	public String getShsj() {
		return shsj;
	}
	public void setShsj(String shsj){
		this.shsj = shsj;
	}
	//审核状态   00:未提交 10:审核中  15:审核未通过  80:审核通过
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
