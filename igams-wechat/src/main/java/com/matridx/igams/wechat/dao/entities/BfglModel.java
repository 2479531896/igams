package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="BfglModel")
public class BfglModel extends BaseModel{
	//拜访ID
	private String bfid;
	//对象ID
	private String dxid;
	//单位ID
	private String dwid;
	//单位 采用基础数据维护，允许新增
	private String dw;
	//客户姓名
	private String khxm;
	//科室
	private String ks;
	//职称
	private String zc;
	//联系方式
	private String lxfs;
	//学术方向
	private String xsfx;
	//拜访时间起始
	private String bfsjqs;
	//拜访时间结束
	private String bfsjjs;
	//沟通内容
	private String gtnr;
	//预估业务量  例/月
	private String ygywl;
	//合作意向
	private String hzyx;
	//测序仪器标记
	private String cxyqbj;
	//床位数
	private String cws;
	//医生数
	private String yss;
	//分组数
	private String fzs;
	//科室合作公司
	private String kshzgs;
	//拜访人
	private String bfr;
	//拜访目的
	private String bfmd;
	//需求反馈
	private String xqfk;
	//下次跟进计划
	private String xcgjjh;
	//联系人id
	private String lxrid;
	//拜访类型
	private String bflx;
	//区分
	private String qf;
	//位置信息
	private String wzxx;

	public String getWzxx() {
		return wzxx;
	}

	public void setWzxx(String wzxx) {
		this.wzxx = wzxx;
	}

	public String getLxrid() {
		return lxrid;
	}

	public void setLxrid(String lxrid) {
		this.lxrid = lxrid;
	}

	public String getBflx() {
		return bflx;
	}

	public void setBflx(String bflx) {
		this.bflx = bflx;
	}

	public String getQf() {
		return qf;
	}

	public void setQf(String qf) {
		this.qf = qf;
	}

	public String getBfmd() {
		return bfmd;
	}

	public void setBfmd(String bfmd) {
		this.bfmd = bfmd;
	}

	public String getXqfk() {
		return xqfk;
	}

	public void setXqfk(String xqfk) {
		this.xqfk = xqfk;
	}

	public String getXcgjjh() {
		return xcgjjh;
	}

	public void setXcgjjh(String xcgjjh) {
		this.xcgjjh = xcgjjh;
	}

	public String getBfr() {
		return bfr;
	}
	public void setBfr(String bfr) {
		this.bfr = bfr;
	}
	//床位数
	public String getCws() {
		return cws;
	}
	public void setCws(String cws) {
		this.cws = cws;
	}
	//医生数
	public String getYss() {
		return yss;
	}
	public void setYss(String yss) {
		this.yss = yss;
	}
	//分组数
	public String getFzs() {
		return fzs;
	}
	public void setFzs(String fzs) {
		this.fzs = fzs;
	}
	//科室合作公司
	public String getKshzgs() {
		return kshzgs;
	}
	public void setKshzgs(String kshzgs) {
		this.kshzgs = kshzgs;
	}
	//拜访ID
	public String getBfid() {
		return bfid;
	}
	public void setBfid(String bfid){
		this.bfid = bfid;
	}
	//单位ID
	public String getDwid() {
		return dwid;
	}
	public void setDwid(String dwid){
		this.dwid = dwid;
	}
	//单位 采用基础数据维护，允许新增
	public String getDw() {
		return dw;
	}
	public void setDw(String dw){
		this.dw = dw;
	}
	//客户姓名
	public String getKhxm() {
		return khxm;
	}
	public void setKhxm(String khxm){
		this.khxm = khxm;
	}
	//科室
	public String getKs() {
		return ks;
	}
	public void setKs(String ks){
		this.ks = ks;
	}
	//职称
	public String getZc() {
		return zc;
	}
	public void setZc(String zc){
		this.zc = zc;
	}
	//联系方式
	public String getLxfs() {
		return lxfs;
	}
	public void setLxfs(String lxfs){
		this.lxfs = lxfs;
	}
	//学术方向
	public String getXsfx() {
		return xsfx;
	}
	public void setXsfx(String xsfx){
		this.xsfx = xsfx;
	}
	//拜访时间起始
	public String getBfsjqs() {
		return bfsjqs;
	}
	public void setBfsjqs(String bfsjqs){
		this.bfsjqs = bfsjqs;
	}
	//拜访时间结束
	public String getBfsjjs() {
		return bfsjjs;
	}
	public void setBfsjjs(String bfsjjs){
		this.bfsjjs = bfsjjs;
	}
	//沟通内容
	public String getGtnr() {
		return gtnr;
	}
	public void setGtnr(String gtnr){
		this.gtnr = gtnr;
	}
	//预估业务量  例/月
	public String getYgywl() {
		return ygywl;
	}
	public void setYgywl(String ygywl){
		this.ygywl = ygywl;
	}
	//合作意向
	public String getHzyx() {
		return hzyx;
	}
	public void setHzyx(String hzyx){
		this.hzyx = hzyx;
	}
	//测序仪器标记
	public String getCxyqbj() {
		return cxyqbj;
	}
	public void setCxyqbj(String cxyqbj){
		this.cxyqbj = cxyqbj;
	}

	public String getDxid() {
		return dxid;
	}
	public void setDxid(String dxid) {
		this.dxid = dxid;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
