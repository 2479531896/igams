package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="FsgcmlModel")
public class FsgcmlModel extends BaseModel{
	//过程id
	private String gcid;
	//过程类型
	private String gclx;
	//协议类型
	private String xylx;
	//命令类型
	private String mllx;
	//命令代码
	private String mldm;
	//命令参数
	private String mlcs;
	//回调类型，就是回调实现类名
	private String hdlx;
	//是否异步
	private String sfyb;
	//是否最后一部
	private String sfzhyb;
	//线程名称
	private String xcmc;
	//第一次返回超时时间
	private String dycfhcssj;
	//第二次返回超时时间
	private String decfhcssj;
	//开始前是否检查仪器
	private String sfyqjc;
	//开始前是否检查机器人
	private String sfjqrjc;
	//结束时候是否归还仪器
	private String sfjsghyq;
	//结束时是否归还机器人
	private String sfjsghjqr;
	//序号
	private String xh;
	//修改后状体
	private String jszt;
	//仪器协议类型
	private String yqxylx;
	//需要归还的仪器类型
	private String ghyqlx;
	//是否需要传参
	private String iscs;
	//过程id
	public String getGcid() {
		return gcid;
	}
	public void setGcid(String gcid){
		this.gcid = gcid;
	}
	//过程类型
	public String getGclx() {
		return gclx;
	}
	public void setGclx(String gclx){
		this.gclx = gclx;
	}
	//协议类型
	public String getXylx() {
		return xylx;
	}
	public void setXylx(String xylx){
		this.xylx = xylx;
	}
	//命令类型
	public String getMllx() {
		return mllx;
	}
	public void setMllx(String mllx){
		this.mllx = mllx;
	}
	//命令代码
	public String getMldm() {
		return mldm;
	}
	public void setMldm(String mldm){
		this.mldm = mldm;
	}
	//命令参数
	public String getMlcs() {
		return mlcs;
	}
	public void setMlcs(String mlcs){
		this.mlcs = mlcs;
	}
	//回调类型，就是回调实现类名
	public String getHdlx() {
		return hdlx;
	}
	public void setHdlx(String hdlx){
		this.hdlx = hdlx;
	}
	//是否异步
	public String getSfyb() {
		return sfyb;
	}
	public void setSfyb(String sfyb){
		this.sfyb = sfyb;
	}
	//是否最后一部
	public String getSfzhyb() {
		return sfzhyb;
	}
	public void setSfzhyb(String sfzhyb){
		this.sfzhyb = sfzhyb;
	}
	//线程名称
	public String getXcmc() {
		return xcmc;
	}
	public void setXcmc(String xcmc){
		this.xcmc = xcmc;
	}
	//第一次返回超时时间
	public String getDycfhcssj() {
		return dycfhcssj;
	}
	public void setDycfhcssj(String dycfhcssj){
		this.dycfhcssj = dycfhcssj;
	}
	//第二次返回超时时间
	public String getDecfhcssj() {
		return decfhcssj;
	}
	public void setDecfhcssj(String decfhcssj){
		this.decfhcssj = decfhcssj;
	}
	//开始前是否检查仪器
	public String getSfyqjc() {
		return sfyqjc;
	}
	public void setSfyqjc(String sfyqjc){
		this.sfyqjc = sfyqjc;
	}
	//开始前是否检查机器人
	public String getSfjqrjc() {
		return sfjqrjc;
	}
	public void setSfjqrjc(String sfjqrjc){
		this.sfjqrjc = sfjqrjc;
	}
	//null
	public String getSfjsghyq() {
		return sfjsghyq;
	}
	public void setSfjsghyq(String sfjsghyq){
		this.sfjsghyq = sfjsghyq;
	}

	public String getSfjsghjqr() {
		return sfjsghjqr;
	}
	public void setSfjsghjqr(String sfjsghjqr) {
		this.sfjsghjqr = sfjsghjqr;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getJszt() {
		return jszt;
	}
	public void setJszt(String jszt) {
		this.jszt = jszt;
	}

	public String getYqxylx() {
		return yqxylx;
	}
	public void setYqxylx(String yqxylx) {
		this.yqxylx = yqxylx;
	}

	public String getGhyqlx() {
		return ghyqlx;
	}
	public void setGhyqlx(String ghyqlx) {
		this.ghyqlx = ghyqlx;
	}

	public String getIscs() {
		return iscs;
	}
	public void setIscs(String iscs) {
		this.iscs = iscs;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
