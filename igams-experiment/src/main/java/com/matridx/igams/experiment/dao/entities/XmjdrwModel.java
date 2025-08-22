package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XmjdrwModel")
public class XmjdrwModel extends BaseModel{
	//任务ID
	private String rwid;
	//父任务ID
	private String frwid;
	//序号
	private String xh;
	//项目阶段ID
	private String xmjdid;
	//项目ID
	private String xmid;
	//任务名称
	private String rwmc;
	//负责人
	private String fzr;
	//开始日期
	private String ksrq;
	//结束日期
	private String jsrq;
	//任务级别  基础数据
	private String rwjb;
	//任务标签  基础数据
	private String rwbq;
	//任务描述
	private String rwms;
	//状态
	private String zt;
	//预计工作量
	private String yjgzl;
	//实际工作量
	private String sjgzl;
	//任务模板ID
	private String rwmbid;
	//阶段时间
	private String jdtime;

	//节省时间
	private String jssj;
	//分数
	private String fs;

	public String getJssj() {
		return jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getFs() {
		return fs;
	}

	public void setFs(String fs) {
		this.fs = fs;
	}


	public String getJdtime() {
		return jdtime;
	}

	public void setJdtime(String jdtime) {
		this.jdtime = jdtime;
	}
	
	public String getXh()
	{
		return xh;
	}
	public void setXh(String xh)
	{
		this.xh = xh;
	}
	public String getYjgzl()
	{
		return yjgzl;
	}
	public void setYjgzl(String yjgzl)
	{
		this.yjgzl = yjgzl;
	}
	public String getSjgzl()
	{
		return sjgzl;
	}
	public void setSjgzl(String sjgzl)
	{
		this.sjgzl = sjgzl;
	}
	//任务ID
	public String getRwid() {
		return rwid;
	}
	public void setRwid(String rwid){
		this.rwid = rwid;
	}
	//父任务ID
	public String getFrwid() {
		return frwid;
	}
	public void setFrwid(String frwid){
		this.frwid = frwid;
	}
	//项目阶段ID
	public String getXmjdid() {
		return xmjdid;
	}
	public void setXmjdid(String xmjdid){
		this.xmjdid = xmjdid;
	}
	//项目ID
	public String getXmid() {
		return xmid;
	}
	public void setXmid(String xmid){
		this.xmid = xmid;
	}
	//任务名称
	public String getRwmc() {
		return rwmc;
	}
	public void setRwmc(String rwmc){
		this.rwmc = rwmc;
	}
	//负责人
	public String getFzr() {
		return fzr;
	}
	public void setFzr(String fzr){
		this.fzr = fzr;
	}
	//开始日期
	public String getKsrq() {
		return ksrq;
	}
	public void setKsrq(String ksrq){
		this.ksrq = ksrq;
	}
	//结束日期
	public String getJsrq() {
		return jsrq;
	}
	public void setJsrq(String jsrq){
		this.jsrq = jsrq;
	}
	//任务级别  基础数据
	public String getRwjb() {
		return rwjb;
	}
	public void setRwjb(String rwjb){
		this.rwjb = rwjb;
	}
	//任务标签  基础数据
	public String getRwbq() {
		return rwbq;
	}
	public void setRwbq(String rwbq){
		this.rwbq = rwbq;
	}
	//任务描述
	public String getRwms() {
		return rwms;
	}
	public void setRwms(String rwms){
		this.rwms = rwms;
	}
	//状态
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}
	//任务模板ID
	public String getRwmbid() {
		return rwmbid;
	}
	public void setRwmbid(String rwmbid) {
		this.rwmbid = rwmbid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
