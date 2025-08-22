package com.matridx.server.wechat.dao.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="FjsqDto")
public class FjsqDto extends FjsqModel{
	
	//新增检测项目名称
	private String jcxmmc;
	//发送报告标记名称
	private String bgbjmc;
	//类型名称
	private String lxmc;
	//类型（多）[]
	private String[] lxs;
	//发送报告（多）[]
	private String[] bgbjs;
	//状态（多）[]
	private String[] zts;
	//录入人员名称
	private String lrrymc;
	//标本编号
	private String ybbh;
	//患者姓名
	private String hzxm;
	//年龄
	private String nl;
	//标本类型名称
	private String yblxmc;
	//合作伙伴
	private String db;
	//内部编码
	private String nbbm;
	//序号
	private String xh;
	//录入时间标记
	private String lrsjFlg;
	//开始录入时间
	private String lrsjstart;
	//结束录入时间
	private String lrsjend;
	//开始录入时间（月）
	private String lrsjMstart;
	//结束录入时间（月）
	private String lrsjMend;
	//开始录入时间（年）
	private String lrsjYstart;
	//结束录入时间（年）
	private String lrsjYend;
	//接收日期
	private String jsrq;
	//开始日期
	private String jsrqstart;
	//结束日期
	private String jsrqend;
	//开始日期(月查询)
	private String jsrqMstart;
	//结束日期(月查询)
	private String jsrqMend;
	//开始日期(年查询)
	private String jsrqYstart;
	//结束日期(年查询)
	private String jsrqYend;
	//RNA检测标记
	private String jcbj;
	//RNA实验日期
	private String syrq;
	//DNA检测标记
	private String djcbj;
	//DNA实验日期
	private String dsyrq;
	//报告日期
	private String bgrq;
	//全部字段
	private String entire;
	//外部复检审核
	private String auditTypeOut;
	//钉钉复检审核
	private String auditTypeDing;
	//签名
	private String sign;
	//用戶id
	private String yhid;
	//合作伙伴（多）
	private List<String> sjhbs;
	//标记
	private String flg;
	//录入人员（多）
	private List<String> lrrys;
	//订单编号
	private String ddbh;
	//业务类型(支付)
	private String ywlx;
	//是否选中,用于钉钉小程序
	private String sfxz;
	//原因
	private String[] yys;
	//参数扩展三
	private String cskz3;

	public String getCskz3() {
		return cskz3;
	}

	public void setCskz3(String cskz3) {
		this.cskz3 = cskz3;
	}

	public String[] getYys() {
		return yys;
	}

	public void setYys(String[] yys) {
		this.yys = yys;
	}

	public String getSfxz() {
		return sfxz;
	}

	public void setSfxz(String sfxz) {
		this.sfxz = sfxz;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getDdbh() {
		return ddbh;
	}

	public void setDdbh(String ddbh) {
		this.ddbh = ddbh;
	}

	public List<String> getLrrys() {
		return lrrys;
	}

	public void setLrrys(String lrrys) {
		List<String> list = new ArrayList<String>();
		String str[] = lrrys.split(",");
		list = Arrays.asList(str);
		this.lrrys = list;
	}
	public void setLrrys(List<String> lrrys) {
		if(lrrys!=null && lrrys.size() > 0){
			for(int i=0;i<lrrys.size();i++){
				lrrys.set(i,lrrys.get(i).replace("[", "").replace("]", "").trim());
			}
		}
		this.lrrys = lrrys;
	}

	public String getFlg()
	{
		return flg;
	}

	public void setFlg(String flg)
	{
		this.flg = flg;
	}

	public List<String> getSjhbs()
	{
		return sjhbs;
	}

	public void setSjhbs(List<String> sjhbs)
	{
		this.sjhbs = sjhbs;
	}

	public String getYhid()
	{
		return yhid;
	}

	public void setYhid(String yhid)
	{
		this.yhid = yhid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAuditTypeDing() {
		return auditTypeDing;
	}

	public void setAuditTypeDing(String auditTypeDing) {
		this.auditTypeDing = auditTypeDing;
	}

	public String getAuditTypeOut() {
		return auditTypeOut;
	}

	public void setAuditTypeOut(String auditTypeOut) {
		this.auditTypeOut = auditTypeOut;
	}

	public String getEntire()
	{
		return entire;
	}

	public void setEntire(String entire)
	{
		this.entire = entire;
	}

	public String getJsrq()
	{
		return jsrq;
	}

	public void setJsrq(String jsrq)
	{
		this.jsrq = jsrq;
	}

	public String getJcbj()
	{
		return jcbj;
	}

	public void setJcbj(String jcbj)
	{
		this.jcbj = jcbj;
	}

	public String getSyrq()
	{
		return syrq;
	}

	public void setSyrq(String syrq)
	{
		this.syrq = syrq;
	}

	public String getDjcbj()
	{
		return djcbj;
	}

	public void setDjcbj(String djcbj)
	{
		this.djcbj = djcbj;
	}

	public String getDsyrq()
	{
		return dsyrq;
	}

	public void setDsyrq(String dsyrq)
	{
		this.dsyrq = dsyrq;
	}

	public String getLrsjMstart()
	{
		return lrsjMstart;
	}

	public String getJsrqstart()
	{
		return jsrqstart;
	}

	public void setJsrqstart(String jsrqstart)
	{
		this.jsrqstart = jsrqstart;
	}

	public String getJsrqend()
	{
		return jsrqend;
	}

	public void setJsrqend(String jsrqend)
	{
		this.jsrqend = jsrqend;
	}

	public String getJsrqMstart()
	{
		return jsrqMstart;
	}

	public void setJsrqMstart(String jsrqMstart)
	{
		this.jsrqMstart = jsrqMstart;
	}

	public String getJsrqMend()
	{
		return jsrqMend;
	}

	public void setJsrqMend(String jsrqMend)
	{
		this.jsrqMend = jsrqMend;
	}

	public String getJsrqYstart()
	{
		return jsrqYstart;
	}

	public void setJsrqYstart(String jsrqYstart)
	{
		this.jsrqYstart = jsrqYstart;
	}

	public String getJsrqYend()
	{
		return jsrqYend;
	}

	public void setJsrqYend(String jsrqYend)
	{
		this.jsrqYend = jsrqYend;
	}

	public void setLrsjMstart(String lrsjMstart){
		this.lrsjMstart = lrsjMstart;
		if("null".equals(this.lrsjMstart) || this.lrsjMstart=="") {
			this.lrsjMstart="";
		}
		
	}

	public String getLrsjMend()
	{
		return lrsjMend;
	}

	public void setLrsjMend(String lrsjMend){
		this.lrsjMend = lrsjMend;
		if("null".equals(this.lrsjMend) || this.lrsjMend=="") {
			this.lrsjMend="";
		}
	}

	public String getLrsjYstart()
	{
		return lrsjYstart;
	}

	public void setLrsjYstart(String lrsjYstart){
		this.lrsjYstart = lrsjYstart;
		if("null".equals(this.lrsjYstart) ||this.lrsjYstart=="") {
			this.lrsjYstart="";
		}
	}

	public String getLrsjYend()
	{
		return lrsjYend;
	}

	public void setLrsjYend(String lrsjYend){
		this.lrsjYend = lrsjYend;
		if("null".equals(this.lrsjYend) ||this.lrsjYend=="") {
			this.lrsjYend="";
		}
	}

	public String getLrsjFlg()
	{
		return lrsjFlg;
	}

	public void setLrsjFlg(String lrsjFlg)
	{
		this.lrsjFlg = lrsjFlg;
	}

	public String getLrsjstart()
	{
		return lrsjstart;
	}

	public void setLrsjstart(String lrsjstart){
		this.lrsjstart = lrsjstart;
		if("null".equals(this.lrsjstart) || this.lrsjstart=="") {
			this.lrsjstart="";
		}
	}

	public String getLrsjend(){
		return lrsjend;
	}

	public void setLrsjend(String lrsjend){
		this.lrsjend = lrsjend;
		if("null".equals(this.lrsjend) || this.lrsjend=="") {
			this.lrsjend="";
		}
	}

	public String getXh()
	{
		return xh;
	}

	public void setXh(String xh)
	{
		this.xh = xh;
	}

	public String getNbbm()
	{
		return nbbm;
	}

	public void setNbbm(String nbbm)
	{
		this.nbbm = nbbm;
	}

	public String getDb()
	{
		return db;
	}

	public void setDb(String db)
	{
		this.db = db;
	}

	public String getYbbh(){
		return ybbh;
	}

	public void setYbbh(String ybbh){
		this.ybbh = ybbh;
	}

	public String getHzxm(){
		return hzxm;
	}

	public void setHzxm(String hzxm){
		this.hzxm = hzxm;
	}

	public String getNl(){
		return nl;
	}

	public void setNl(String nl){
		this.nl = nl;
	}

	public String getYblxmc(){
		return yblxmc;
	}

	public void setYblxmc(String yblxmc){
		this.yblxmc = yblxmc;
	}

	public String getLrrymc(){
		return lrrymc;
	}

	public void setLrrymc(String lrrymc){
		this.lrrymc = lrrymc;
	}

	public String getLxmc(){
		return lxmc;
	}

	public void setLxmc(String lxmc){
		this.lxmc = lxmc;
	}


	public String getBgbjmc(){
		return bgbjmc;
	}


	public void setBgbjmc(String bgbjmc){
		this.bgbjmc = bgbjmc;
	}


	public String getJcxmmc(){
		return jcxmmc;
	}


	public void setJcxmmc(String jcxmmc){
		this.jcxmmc = jcxmmc;
	}

	public String[] getLxs()
	{
		return lxs;
	}

	public void setLxs(String[] lxs){
		this.lxs = lxs;
		for(int i=0;i<this.lxs.length;i++){
			this.lxs[i] = this.lxs[i].replace("'", "");
		}
	}

	public String[] getBgbjs()
	{
		return bgbjs;
	}

	public void setBgbjs(String[] bgbjs)
	{
		this.bgbjs = bgbjs;
		for(int i=0;i<this.bgbjs.length;i++){
			this.bgbjs[i] = this.bgbjs[i].replace("'", "");
		}
	}

	public String[] getZts()
	{
		return zts;
	}

	public void setZts(String[] zts)
	{
		this.zts = zts;
		for(int i=0;i<this.zts.length;i++){
			this.zts[i] = this.zts[i].replace("'", "");
		}
	}


	public String getBgrq() {
		return bgrq;
	}

	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
