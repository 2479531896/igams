package com.matridx.igams.wechat.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="SjybztDto")
public class SjybztDto extends SjybztModel{
	//基础数据参数名称
	private String csmc;
	
	private String jccsmc;
	//zt标记
	private String ztflg;
	//接收日期
	private String jsrq;
	//标本状态名称
	private String ybztmc;
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
	//接收日期格式化
	private String yhid;
	//标本状态参数扩展一
	private String ybztCskz1;
	//标本状态参数扩展二
	private String ybztCskz2;
	//标本状态参数扩展一
	private List<String> ybztCskz1s;
	//标本数量
	private String num;
	//不合格数量
	private String sampleGood;
	//合格数量
	private String sampleBad;
	//合格率
	private String samplePass;
	//不合格率
	private String sampleNopass;
	//比率
	private String percentage;

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getSampleGood() {
		return sampleGood;
	}

	public void setSampleGood(String sampleGood) {
		this.sampleGood = sampleGood;
	}

	public String getSampleBad() {
		return sampleBad;
	}

	public void setSampleBad(String sampleBad) {
		this.sampleBad = sampleBad;
	}

	public String getSamplePass() {
		return samplePass;
	}

	public void setSamplePass(String samplePass) {
		this.samplePass = samplePass;
	}

	public String getSampleNopass() {
		return sampleNopass;
	}

	public void setSampleNopass(String sampleNopass) {
		this.sampleNopass = sampleNopass;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public List<String> getYbztCskz1s() {
		return ybztCskz1s;
	}

	public void setYbztCskz1s(List<String> ybztCskz1s) {
		this.ybztCskz1s = ybztCskz1s;
	}

	public String getYbztCskz2() {
		return ybztCskz2;
	}

	public void setYbztCskz2(String ybztCskz2) {
		this.ybztCskz2 = ybztCskz2;
	}


	private List<String> sjhbs;
	
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

	public String getYbztmc()
	{
		return ybztmc;
	}

	public String getJsrqstart()
	{
		return jsrqstart;
	}

	public void setJsrqstart(String jsrqstart){
		this.jsrqstart = jsrqstart;
		if("null".equals(this.jsrqstart) || this.jsrqstart=="") {
			this.jsrqstart="";
		}
	}

	public String getJsrqend()
	{
		return jsrqend;
	}

	public void setJsrqend(String jsrqend){
		this.jsrqend = jsrqend;
		if("null".equals(this.jsrqend) || this.jsrqend=="") {
			this.jsrqend="";
		}
	}

	public String getJsrqMstart()
	{
		return jsrqMstart;
	}

	public void setJsrqMstart(String jsrqMstart){
		this.jsrqMstart = jsrqMstart;
		if("null".equals(this.jsrqMstart) || this.jsrqMstart=="") {
			this.jsrqMstart="";
		}
	}

	public String getJsrqMend()
	{
		return jsrqMend;
	}

	public void setJsrqMend(String jsrqMend){
		this.jsrqMend = jsrqMend;
		if("null".equals(this.jsrqMend) || this.jsrqMend=="") {
			this.jsrqMend="";
		}
	}

	public String getJsrqYstart()
	{
		return jsrqYstart;
	}

	public void setJsrqYstart(String jsrqYstart){
		this.jsrqYstart = jsrqYstart;
		if("null".equals(this.jsrqYstart) || this.jsrqYstart=="") {
			this.jsrqYstart="";
		}
	}

	public String getJsrqYend()
	{
		return jsrqYend;
	}

	public void setJsrqYend(String jsrqYend){
		this.jsrqYend = jsrqYend;
		if("null".equals(this.jsrqYend) || this.jsrqYend=="") {
			this.jsrqYend="";
		}
	}

	public void setYbztmc(String ybztmc)
	{
		this.ybztmc = ybztmc;
	}

	public String getZtflg()
	{
		return ztflg;
	}

	public void setZtflg(String ztflg)
	{
		this.ztflg = ztflg;
	}

	public String getJsrq()
	{
		return jsrq;
	}

	public void setJsrq(String jsrq){
		this.jsrq = jsrq;
		if("null".equals(this.jsrq ) || this.jsrq =="") {
			this.jsrq ="";
		}
	}

	public String getJccsmc()
	{
		return jccsmc;
	}

	public void setJccsmc(String jccsmc)
	{
		this.jccsmc = jccsmc;
	}

	public String getCsmc()
	{
		return csmc;
	}

	public void setCsmc(String csmc)
	{
		this.csmc = csmc;
	}

	public String getYbztCskz1() {
		return ybztCskz1;
	}

	public void setYbztCskz1(String ybztCskz1) {
		this.ybztCskz1 = ybztCskz1;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
