package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="ShxxModel")
public class ShxxModel extends BaseBasicModel{
	//审核信息ID
	private String shxxid;
	//过程ID
	private String gcid;
	//序号
	private String xh;
	//业务ID
	private String ywid;
	//审核ID
	private String shid;
	//流程序号
	private String lcxh;
	//申请时间
	private String sqsj;
	//申请人
	private String sqr;
	//审核时间
	private String shsj;
	//是否通过
	private String sftg;
	//审核意见
	private String shyj;
	//申诉意见
	private String ssyj;
	//岗位ID
	private String gwid;
	//角色ID
	private String jsid;
	//受托审核人员
	private String stshry;
	//委托审核人员
	private String wtshry;
	//评论标记，0或null不是评论 1是评论
	private String plbj;

	public String getPlbj() {
		return plbj;
	}

	public void setPlbj(String plbj) {
		this.plbj = plbj;
	}

	//审核信息ID
	public String getShxxid() {
		return shxxid;
	}
	public void setShxxid(String shxxid){
		this.shxxid = shxxid;
	}
	//过程ID
	public String getGcid() {
		return gcid;
	}
	public void setGcid(String gcid){
		this.gcid = gcid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//业务ID
	public String getYwid() {
		return ywid;
	}
	public void setYwid(String ywid){
		this.ywid = ywid;
	}
	//审核ID
	public String getShid() {
		return shid;
	}
	public void setShid(String shid){
		this.shid = shid;
	}
	//流程序号
	public String getLcxh() {
		return lcxh;
	}
	public void setLcxh(String lcxh){
		this.lcxh = lcxh;
	}
	//申请时间
	public String getSqsj() {
		return sqsj;
	}
	public void setSqsj(String sqsj){
		this.sqsj = sqsj;
	}
	//申请人
	public String getSqr() {
		return sqr;
	}
	public void setSqr(String sqr){
		this.sqr = sqr;
	}
	//审核时间
	public String getShsj() {
		return shsj;
	}
	public void setShsj(String shsj){
		this.shsj = shsj;
	}
	//是否通过
	public String getSftg() {
		return sftg;
	}
	public void setSftg(String sftg){
		this.sftg = sftg;
	}
	//审核意见
	public String getShyj() {
		return shyj;
	}
	public void setShyj(String shyj){
		this.shyj = shyj;
	}
	//申诉意见
	public String getSsyj() {
		return ssyj;
	}
	public void setSsyj(String ssyj){
		this.ssyj = ssyj;
	}
	//岗位ID
	public String getGwid() {
		return gwid;
	}
	public void setGwid(String gwid){
		this.gwid = gwid;
	}
	//角色ID
	public String getJsid() {
		return jsid;
	}
	public void setJsid(String jsid){
		this.jsid = jsid;
	}
	//受托审核人员
	public String getStshry() {
		return stshry;
	}
	public void setStshry(String stshry){
		this.stshry = stshry;
	}
	//委托审核人员
	public String getWtshry() {
		return wtshry;
	}
	public void setWtshry(String wtshry){
		this.wtshry = wtshry;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
