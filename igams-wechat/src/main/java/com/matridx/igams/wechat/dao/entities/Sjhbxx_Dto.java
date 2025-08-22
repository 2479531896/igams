package com.matridx.igams.wechat.dao.entities;

import java.util.List;

public class Sjhbxx_Dto extends SjhbxxModel{
	//伙伴收费标准
	private List<HbsfbzDto> hbsfbzs;
	//送检项目
	private List<SjjcxmDto> sjxms;
	//收费标准
	private String sfbz;
	//省份名称
	private String sfmc;
	//城市名称
	private String csmc;
	//用户名
	private String yhm;
	//系统用户id
	private String xtyhid;
	//系统用户名
	private String xtyhm;
	//省份
	private String province;
	//城市
	private String city;
	//钉钉ID
	private String ddid;
	//报告模板名称
	private String bgmbmc;
	//代表
	private String db;
	//参数扩展1名称
	private String cskz1mc;
	//参数扩展2名称
	private String cskz2mc;
	//分类
	private String fl;
	//分类代码
	private String fldm;
	//子分类
	private String zfl;
	//子分类代码
	private String zfldm;
	//分类名称
	private String flmc;
	//子分类名称
	private String zflmc;
	//基础类别
	private String jclb;
	//盖章类型名称
	private String gzlxmc;
	//参数扩展1
	private String cskz1;
	//盖章扩展1
	private String gzcskz1;
	
	public String getGzcskz1() {
		return gzcskz1;
	}
	public void setGzcskz1(String gzcskz1) {
		this.gzcskz1 = gzcskz1;
	}
	public String getCskz1()
	{
		return cskz1;
	}
	public void setCskz1(String cskz1)
	{
		this.cskz1 = cskz1;
	}
	public String getGzlxmc()
	{
		return gzlxmc;
	}
	public void setGzlxmc(String gzlxmc)
	{
		this.gzlxmc = gzlxmc;
	}
	public String getJclb() {
		return jclb;
	}
	public void setJclb(String jclb) {
		this.jclb = jclb;
	}
	public String getFlmc()
	{
		return flmc;
	}
	public void setFlmc(String flmc)
	{
		this.flmc = flmc;
	}
	public String getZflmc()
	{
		return zflmc;
	}
	public void setZflmc(String zflmc)
	{
		this.zflmc = zflmc;
	}
	public String getFl()
	{
		return fl;
	}
	public void setFl(String fl)
	{
		this.fl = fl;
	}
	public String getZfl()
	{
		return zfl;
	}
	public void setZfl(String zfl)
	{
		this.zfl = zfl;
	}
	public String getCskz1mc()
	{
		return cskz1mc;
	}
	public void setCskz1mc(String cskz1mc)
	{
		this.cskz1mc = cskz1mc;
	}
	public String getCskz2mc()
	{
		return cskz2mc;
	}
	public void setCskz2mc(String cskz2mc)
	{
		this.cskz2mc = cskz2mc;
	}
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	public String getProvince()
	{
		return province;
	}
	public void setProvince(String province)
	{
		this.province = province;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city = city;
	}
	public List<HbsfbzDto> getHbsfbzs(){
		return hbsfbzs;
	}
	public void setHbsfbzs(List<HbsfbzDto> hbsfbzs){
		this.hbsfbzs = hbsfbzs;
	}

	public String getYhm(){
		return yhm;
	}
	public void setYhm(String yhm){
		this.yhm = yhm;
	}
	public String getXtyhid()
	{
		return xtyhid;
	}
	public void setXtyhid(String xtyhid)
	{
		this.xtyhid = xtyhid;
	}
	public String getXtyhm()
	{
		return xtyhm;
	}
	public void setXtyhm(String xtyhm)
	{
		this.xtyhm = xtyhm;
	}

	public List<SjjcxmDto> getSjxms() {
		return sjxms;
	}
	public void setSjxms(List<SjjcxmDto> sjxms) {
		this.sjxms = sjxms;
	}
	public String getSfbz() {
		return sfbz;
	}
	public void setSfbz(String sfbz) {
		this.sfbz = sfbz;
	}
	public String getSfmc() {
		return sfmc;
	}
	public void setSfmc(String sfmc) {
		this.sfmc = sfmc;
	}
	public String getCsmc() {
		return csmc;
	}
	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}
	public String getDdid() {
		return ddid;
	}
	public void setDdid(String ddid) {
		this.ddid = ddid;
	}
	public String getBgmbmc() {
		return bgmbmc;
	}
	public void setBgmbmc(String bgmbmc) {
		this.bgmbmc = bgmbmc;
	}
	public String getFldm() {
		return fldm;
	}
	public void setFldm(String fldm) {
		this.fldm = fldm;
	}
	public String getZfldm() {
		return zfldm;
	}
	public void setZfldm(String zfldm) {
		this.zfldm = zfldm;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
