package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjhbxxModel")
public class SjhbxxModel extends BaseModel{
    
	//伙伴id
	private String hbid;
	//合作伙伴
	private String hbmc;
	//伙伴类型
	private String hblx;
	//省份
	private String sf;
	//城市
	private String cs;
    //用户id
	private String yhid;
	//微信id
	private String wxid;
	//微信名
	private String wxm;
	//伙伴类型名称
	private String hblxmc;
	//报告模板
	private String bgmb;
	//发送方式
	private String fsfs;
	//邮箱
	private String yx;
	//扩展参数1
	private String cskz1;
	//扩展参数2
	private String cskz2;
	//扩展参数3
	private String cskz3;
	//扩展参数4
	private String cskz4;
	//盖章类型
	private String gzlx;
	//统计名称
	private String tjmc;
	//报告方式
	private String bgfs;
	//商务名称名称
	private String swmc;
	//文件名规则
	private String wjmgz;
	//送检区分
	private String sjqf;
	//大区信息
	private String dqxx;
	//业务部门
	private String ptgs;
	//合作公司名称
	private String hzgsmc;
	private String yx2;//邮箱2

	private String bz;//备注

	//伙伴代码（2023.12.13：目前艾迪康使用）
	private String hbdm;

	//伙伴级别
	private String hbjb;
	//平台归属
	private String yptgs;

	public String getYptgs() {
		return yptgs;
	}

	public void setYptgs(String yptgs) {
		this.yptgs = yptgs;
	}
	public String getHbjb() {
		return hbjb;
	}

	public void setHbjb(String hbjb) {
		this.hbjb = hbjb;
	}

	public String getHbdm() {
		return hbdm;
	}

	public void setHbdm(String hbdm) {
		this.hbdm = hbdm;
	}

	public String getYx2() {
		return yx2;
	}

	public void setYx2(String yx2) {
		this.yx2 = yx2;
	}

	public String getHzgsmc() {
		return hzgsmc;
	}

	public void setHzgsmc(String hzgsmc) {
		this.hzgsmc = hzgsmc;
	}

	public String getCskz4() {
		return cskz4;
	}

	public void setCskz4(String cskz4) {
		this.cskz4 = cskz4;
	}

	public String getPtgs() {
		return ptgs;
	}

	public void setPtgs(String ptgs) {
		this.ptgs = ptgs;
	}

	public String getDqxx() {
		return dqxx;
	}

	public void setDqxx(String dqxx) {
		this.dqxx = dqxx;
	}

	public String getSjqf() {
		return sjqf;
	}

	public void setSjqf(String sjqf) {
		this.sjqf = sjqf;
	}

	public String getWjmgz() {
		return wjmgz;
	}
	public void setWjmgz(String wjmgz) {
		this.wjmgz = wjmgz;
	}
	public String getSwmc() {
		return swmc;
	}
	public void setSwmc(String swmc) {
		this.swmc = swmc;
	}
	public String getBgfs() {
		return bgfs;
	}
	public void setBgfs(String bgfs) {
		this.bgfs = bgfs;
	}
	public String getTjmc() {
		return tjmc;
	}
	public void setTjmc(String tjmc) {
		this.tjmc = tjmc;
	}
	public String getGzlx(){
		return gzlx;
	}
	public void setGzlx(String gzlx){
		this.gzlx = gzlx;
	}
	public String getFsfs() {
		return fsfs;
	}
	public void setFsfs(String fsfs) {
		this.fsfs = fsfs;
	}
	public String getYx() {
		return yx;
	}
	public void setYx(String yx) {
		this.yx = yx;
	}
	public String getBgmb()
	{
		return bgmb;
	}
	public void setBgmb(String bgmb)
	{
		this.bgmb = bgmb;
	}
	public String getHblxmc()
	{
		return hblxmc;
	}
	public void setHblxmc(String hblxmc)
	{
		this.hblxmc = hblxmc;
	}

	public String getWxm()
	{
		return wxm;
	}

	public void setWxm(String wxm)
	{
		this.wxm = wxm;
	}

	public String getWxid()
	{
		return wxid;
	}

	public void setWxid(String wxid)
	{
		this.wxid = wxid;
	}

	public String getHbid(){
		return hbid;
	}

	public void setHbid(String hbid)
	{
		this.hbid = hbid;
	}

	public String getHbmc(){
		return hbmc;
	}

	public void setHbmc(String hbmc){
		this.hbmc = hbmc;
	}

	public String getHblx()
	{
		return hblx;
	}

	public void setHblx(String hblx)
	{
		this.hblx = hblx;
	}

	public String getSf()
	{
		return sf;
	}

	public void setSf(String sf)
	{
		this.sf = sf;
	}

	public String getCs()
	{
		return cs;
	}

	public void setCs(String cs)
	{
		this.cs = cs;
	}

	public String getYhid()
	{
		return yhid;
	}

	public void setYhid(String yhid)
	{
		this.yhid = yhid;
	}
	public String getCskz1()
	{
		return cskz1;
	}
	public void setCskz1(String cskz1)
	{
		this.cskz1 = cskz1;
	}
	public String getCskz2()
	{
		return cskz2;
	}
	public void setCskz2(String cskz2)
	{
		this.cskz2 = cskz2;
	}
	public String getCskz3()
	{
		return cskz3;
	}
	public void setCskz3(String cskz3)
	{
		this.cskz3 = cskz3;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
}
