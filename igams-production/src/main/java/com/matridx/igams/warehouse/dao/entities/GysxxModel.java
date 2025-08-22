package com.matridx.igams.warehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="GysxxModel")
public class GysxxModel extends BaseModel{
	//供应商ID
	private String gysid;
	//供应商名称
	private String gysmc;
	//供应商代码
	private String gysdm;
	//供应商简称
	private String gysjc;
	//地区
	private String dq;
	//发展日期
	private String fzrq;
	//联系人
	private String lxr;
	//电话
	private String dh;
	//供方管理类别 基础数据
	private String gfgllb;
	//状态 00:未提交
	private String zt;
	//备注
	private String bz;
	//供应商手机
	private String sj;
	//供应商QQ
	private String qq;
	//供应商微信
	private String wx;
	//省份
	private String sf;
	//开户行
	private String khh;
	//账户
	private String zh;
	//税率
	private String sl;
	//传真
	private String cz;
	//邮箱
	private String yx;
	private String fddbr;//法定代表人
	private String gfbh;//供方编号
	private String ywbm;//往来单位业务编码
	private String sfxs;//是否显示
	private String tyry;//停用人员
	private String tysj;//停用时间

	public String getTyry() {
		return tyry;
	}

	public void setTyry(String tyry) {
		this.tyry = tyry;
	}

	public String getTysj() {
		return tysj;
	}

	public void setTysj(String tysj) {
		this.tysj = tysj;
	}

	public String getSfxs() {
		return sfxs;
	}

	public void setSfxs(String sfxs) {
		this.sfxs = sfxs;
	}

	public String getYwbm() {
		return ywbm;
	}

	public void setYwbm(String ywbm) {
		this.ywbm = ywbm;
	}

	public String getGfbh() {
		return gfbh;
	}

	public void setGfbh(String gfbh) {
		this.gfbh = gfbh;
	}

	public String getFddbr() {
		return fddbr;
	}

	public void setFddbr(String fddbr) {
		this.fddbr = fddbr;
	}

	public String getCz() {
		return cz;
	}
	public void setCz(String cz) {
		this.cz = cz;
	}
	public String getYx() {
		return yx;
	}
	public void setYx(String yx) {
		this.yx = yx;
	}
	public String getSf() {
		return sf;
	}
	public void setSf(String sf) {
		this.sf = sf;
	}
	public String getKhh() {
		return khh;
	}
	public void setKhh(String khh) {
		this.khh = khh;
	}
	public String getZh() {
		return zh;
	}
	public void setZh(String zh) {
		this.zh = zh;
	}
	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
	}
	public String getSj() {
		return sj;
	}
	public void setSj(String sj) {
		this.sj = sj;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWx() {
		return wx;
	}
	public void setWx(String wx) {
		this.wx = wx;
	}
	//供应商ID
	public String getGysid() {
		return gysid;
	}
	public void setGysid(String gysid){
		this.gysid = gysid;
	}
	//供应商名称
	public String getGysmc() {
		return gysmc;
	}
	public void setGysmc(String gysmc){
		this.gysmc = gysmc;
	}
	//供应商代码
	public String getGysdm() {
		return gysdm;
	}
	public void setGysdm(String gysdm){
		this.gysdm = gysdm;
	}
	//供应商简称
	public String getGysjc() {
		return gysjc;
	}
	public void setGysjc(String gysjc){
		this.gysjc = gysjc;
	}
	//地区
	public String getDq() {
		return dq;
	}
	public void setDq(String dq){
		this.dq = dq;
	}
	//发展日期
	public String getFzrq() {
		return fzrq;
	}
	public void setFzrq(String fzrq){
		this.fzrq = fzrq;
	}
	//联系人
	public String getLxr() {
		return lxr;
	}
	public void setLxr(String lxr){
		this.lxr = lxr;
	}
	//电话
	public String getDh() {
		return dh;
	}
	public void setDh(String dh){
		this.dh = dh;
	}
	//供方管理类别 基础数据
	public String getGfgllb() {
		return gfgllb;
	}
	public void setGfgllb(String gfgllb){
		this.gfgllb = gfgllb;
	}
	//状态 00:未提交
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
