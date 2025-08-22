package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="BfdxglModel")
public class BfdxglModel extends BaseModel{
	//单位ID
	private String dwid;
	//单位名称
	private String dwmc;
	//单位简称
	private String dwjc;
	//单位分类 固定 医院  公司
	private String dwfl;
	//单位等级 基础数据
	private String dwdj;
	//单位类别  基础数据
	private String dwlb;
	//省份 基础数据
	private String sf;
	//测序仪器标记
	private String cxyqbj;
	//公司背景
	private String gsbj;
	//公司销售人数
	private String gsxsrs;
	//对象编码
	private String dxbm;
	//地址
	private String dz;
	//联系电话
	private String lxdh;
	//是否认可
	private String sfrk;
	//分配人数
	private String fprs;
	//业务员
	private String ywy;
	//备注
	private String bz;
	//合并主体ID
	private String hbztid;
	//被合并时间
	private String bhbsj;
	//来源
	private String ly;
	//客户分级
	private String khfj;
	//项目现状
	private String xmxz;
	//年收入
	private String nsr;
	//床位数
	private String cws;
	//扩展字段
	private String kzzd;

	public String getKzzd() {
		return kzzd;
	}

	public void setKzzd(String kzzd) {
		this.kzzd = kzzd;
	}

	public String getXmxz() {
		return xmxz;
	}

	public void setXmxz(String xmxz) {
		this.xmxz = xmxz;
	}

	public String getNsr() {
		return nsr;
	}

	public void setNsr(String nsr) {
		this.nsr = nsr;
	}

	public String getCws() {
		return cws;
	}

	public void setCws(String cws) {
		this.cws = cws;
	}

	public String getKhfj() {
		return khfj;
	}

	public void setKhfj(String khfj) {
		this.khfj = khfj;
	}

	public String getLy() {
		return ly;
	}

	public void setLy(String ly) {
		this.ly = ly;
	}

	public String getDxbm() {
		return dxbm;
	}

	public void setDxbm(String dxbm) {
		this.dxbm = dxbm;
	}

	public String getDz() {
		return dz;
	}

	public void setDz(String dz) {
		this.dz = dz;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getSfrk() {
		return sfrk;
	}

	public void setSfrk(String sfrk) {
		this.sfrk = sfrk;
	}

	public String getFprs() {
		return fprs;
	}

	public void setFprs(String fprs) {
		this.fprs = fprs;
	}

	public String getYwy() {
		return ywy;
	}

	public void setYwy(String ywy) {
		this.ywy = ywy;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getHbztid() {
		return hbztid;
	}

	public void setHbztid(String hbztid) {
		this.hbztid = hbztid;
	}

	public String getBhbsj() {
		return bhbsj;
	}

	public void setBhbsj(String bhbsj) {
		this.bhbsj = bhbsj;
	}

	public String getGsbj() {
		return gsbj;
	}
	public void setGsbj(String gsbj) {
		this.gsbj = gsbj;
	}
	//公司销售人数
	public String getGsxsrs() {
		return gsxsrs;
	}
	public void setGsxsrs(String gsxsrs) {
		this.gsxsrs = gsxsrs;
	}
	//单位ID
	public String getDwid() {
		return dwid;
	}
	public void setDwid(String dwid){
		this.dwid = dwid;
	}
	//单位名称
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc){
		this.dwmc = dwmc;
	}
	//单位简称
	public String getDwjc() {
		return dwjc;
	}
	public void setDwjc(String dwjc){
		this.dwjc = dwjc;
	}
	//单位分类 固定 医院  公司
	public String getDwfl() {
		return dwfl;
	}
	public void setDwfl(String dwfl){
		this.dwfl = dwfl;
	}
	//单位等级 基础数据
	public String getDwdj() {
		return dwdj;
	}
	public void setDwdj(String dwdj){
		this.dwdj = dwdj;
	}
	//单位类别  基础数据
	public String getDwlb() {
		return dwlb;
	}
	public void setDwlb(String dwlb){
		this.dwlb = dwlb;
	}
	//省份 基础数据
	public String getSf() {
		return sf;
	}
	public void setSf(String sf){
		this.sf = sf;
	}
	//测序仪器标记
	public String getCxyqbj() {
		return cxyqbj;
	}
	public void setCxyqbj(String cxyqbj){
		this.cxyqbj = cxyqbj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
