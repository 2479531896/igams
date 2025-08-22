package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="FjcfbModel")
public class FjcfbModel extends BaseModel{
	//附件ID
	private String fjid;
	//业务ID  如：项目ID
	private String ywid;
	//子业务ID
	private String zywid;
	//业务类型   
	private String ywlx;
	//序号
	private String xh;
	//分区  0：主要  1：预留  2：预留
	private String fq;
	//文件名 原始文件名 未加密
	private String wjm;
	//文件路径 包含文件名 已加密
	private String wjlj;
	//转换标记  1:转换成功
	private String zhbj;
	//加密后正式文件分路径 不包含文件名 已加密
	private String fwjlj;
	//加密后正式文件分名 已转换后的文件名称 已加密
	private String fwjm;
	//stampId，邮票id，用于关联手机扫描上传
	private String lyid;
	//转换次数 超过一定次数后不再转换
	private String zhcs;
	//条形码信息
	private String BarcodeStr;
	//最后文件信息
	private String zhwjxx;
	//审核人信息
	private String shrs;
	//审核时间信息
	private String shsjs;
	//审核用户名信息
	private String shyhms;
	//检测类型
	private String jclx;
	//原始文件名
	private String yswjm;
	//原始文件路径
	private String yswjlj;
	//新文件名
	private String xwjlj;
	//新分文件路径
	private String xfwjlj;
	
	public String getXwjlj() {
		return xwjlj;
	}
	public void setXwjlj(String xwjlj) {
		this.xwjlj = xwjlj;
	}
	public String getXfwjlj() {
		return xfwjlj;
	}
	public void setXfwjlj(String xfwjlj) {
		this.xfwjlj = xfwjlj;
	}
	public String getYswjm() {
		return yswjm;
	}
	public void setYswjm(String yswjm) {
		this.yswjm = yswjm;
	}
	public String getYswjlj() {
		return yswjlj;
	}
	public void setYswjlj(String yswjlj) {
		this.yswjlj = yswjlj;
	}
	public String getJclx() {
		return jclx;
	}
	public void setJclx(String jclx) {
		this.jclx = jclx;
	}
	public String getZhwjxx()
	{
		return zhwjxx;
	}
	public void setZhwjxx(String zhwjxx)
	{
		this.zhwjxx = zhwjxx;
	}
	//附件ID
	public String getFjid() {
		return fjid;
	}
	public void setFjid(String fjid){
		this.fjid = fjid;
	}
	//业务ID  如：项目ID
	public String getYwid() {
		return ywid;
	}
	public void setYwid(String ywid){
		this.ywid = ywid;
	}
	//子业务ID
	public String getZywid() {
		return zywid;
	}
	public void setZywid(String zywid){
		this.zywid = zywid;
	}
	//业务类型   
	public String getYwlx() {
		return ywlx;
	}
	public void setYwlx(String ywlx){
		this.ywlx = ywlx;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//分区  0：主要  1：预留  2：预留
	public String getFq() {
		return fq;
	}
	public void setFq(String fq){
		this.fq = fq;
	}
	//文件名
	public String getWjm() {
		return wjm;
	}
	public void setWjm(String wjm){
		this.wjm = wjm;
	}
	//文件路径
	public String getWjlj() {
		return wjlj;
	}
	public void setWjlj(String wjlj){
		this.wjlj = wjlj;
	}
	//转换标记  1:转换成功
	public String getZhbj() {
		return zhbj;
	}
	public void setZhbj(String zhbj){
		this.zhbj = zhbj;
	}
	//加密后正式文件分路径
	public String getFwjlj() {
		return fwjlj;
	}
	public void setFwjlj(String fwjlj){
		this.fwjlj = fwjlj;
	}
	//加密后正式文件分名
	public String getFwjm() {
		return fwjm;
	}
	public void setFwjm(String fwjm){
		this.fwjm = fwjm;
	}
	//stampId，邮票id，用于关联手机扫描上传
	public String getLyid() {
		return lyid;
	}
	public void setLyid(String lyid){
		this.lyid = lyid;
	}
	//转换次数 超过一定次数后不再转换
	public String getZhcs() {
		return zhcs;
	}
	public void setZhcs(String zhcs){
		this.zhcs = zhcs;
	}

	public String getBarcodeStr() {
		return BarcodeStr;
	}
	public void setBarcodeStr(String barcodeStr) {
		BarcodeStr = barcodeStr;
	}

	public String getShrs() {
		return shrs;
	}
	public void setShrs(String shrs) {
		this.shrs = shrs;
	}

	public String getShsjs() {
		return shsjs;
	}
	public void setShsjs(String shsjs) {
		this.shsjs = shsjs;
	}
	public String getShyhms() {
		return shyhms;
	}
	public void setShyhms(String shyhms) {
		this.shyhms = shyhms;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
