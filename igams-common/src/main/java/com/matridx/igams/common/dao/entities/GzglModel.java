package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="GzglModel")
public class GzglModel extends BaseModel{
	//工作ID
	private String gzid;
	//业务ID 主要是指调查ID，研发ID
	private String ywid;
	//业务名称
	private String ywmc;
	//业务地址 用于业务名称的链接使用
	private String ywdz;
	//任务ID  采用基础数据的任务ID
	private String rwid;
	//任务名称
	private String rwmc;
	//工作类型 用于确定是关联哪张表的
	private String gzlx;
	//任务期限
	private String rwqx;
	//期望完成日期
	private String qwwcsj;
	//完成日期
	private String wcsj;
	//当前进度
	private String dqjd;
	//机构ID
	private String jgid;
	//负责人
	private String fzr;
	//确认人员
	private String qrry;
	//状态
	private String zt;
	//备注
	private String bz;
	//紧急度
	private String jjd;
	//预计工作量
	private String yjgzl;
	//实际工作量
	private String sjgzl;
	//通过标记
	private String tgbj;
	//记录编号
	private String jlbh;
	//所属公司
	private String ssgs;
	//培训方式
	private String pxfs;
	//关联标记
	private String glbj;
	//课时
	private String ks;
	//是否签到
	private String sfqd;
	//工作结束时间
	private String gzjssj;
	//节省时间
	private String jssj;

	public String getGzjssj() {
		return gzjssj;
	}

	public void setGzjssj(String gzjssj) {
		this.gzjssj = gzjssj;
	}

	public String getSfqd() {
		return sfqd;
	}

	public void setSfqd(String sfqd) {
		this.sfqd = sfqd;
	}

	public String getGlbj() {
		return glbj;
	}

	public void setGlbj(String glbj) {
		this.glbj = glbj;
	}

	public String getKs() {
		return ks;
	}

	public void setKs(String ks) {
		this.ks = ks;
	}

	public String getSsgs() {
		return ssgs;
	}

	public void setSsgs(String ssgs) {
		this.ssgs = ssgs;
	}

	public String getPxfs() {
		return pxfs;
	}

	public void setPxfs(String pxfs) {
		this.pxfs = pxfs;
	}

	public String getJlbh() {
		return jlbh;
	}

	public void setJlbh(String jlbh) {
		this.jlbh = jlbh;
	}

	public String getTgbj() {
		return tgbj;
	}

	public void setTgbj(String tgbj) {
		this.tgbj = tgbj;
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
	public String getJjd() {
		return jjd;
	}
	public void setJjd(String jjd) {
		this.jjd = jjd;
	}
	//工作ID
	public String getGzid() {
		return gzid;
	}
	public void setGzid(String gzid){
		this.gzid = gzid;
	}
	//业务ID 主要是指调查ID，研发ID
	public String getYwid() {
		return ywid;
	}
	public void setYwid(String ywid){
		this.ywid = ywid;
	}
	//业务名称
	public String getYwmc() {
		return ywmc;
	}
	public void setYwmc(String ywmc){
		this.ywmc = ywmc;
	}
	//业务地址 用于业务名称的链接使用
	public String getYwdz() {
		return ywdz;
	}
	public void setYwdz(String ywdz){
		this.ywdz = ywdz;
	}
	//任务ID  采用基础数据的任务ID
	public String getRwid() {
		return rwid;
	}
	public void setRwid(String rwid){
		this.rwid = rwid;
	}
	//任务名称
	public String getRwmc() {
		return rwmc;
	}
	public void setRwmc(String rwmc){
		this.rwmc = rwmc;
	}
	//工作类型 用于确定是关联哪张表的
	public String getGzlx() {
		return gzlx;
	}
	public void setGzlx(String gzlx){
		this.gzlx = gzlx;
	}
	//任务期限
	public String getRwqx() {
		return rwqx;
	}
	public void setRwqx(String rwqx){
		this.rwqx = rwqx;
	}
	//期望完成日期
	public String getQwwcsj() {
		return qwwcsj;
	}
	public void setQwwcsj(String qwwcsj){
		this.qwwcsj = qwwcsj;
	}
	//完成日期
	public String getWcsj() {
		return wcsj;
	}
	public void setWcsj(String wcsj){
		this.wcsj = wcsj;
	}
	//当前进度
	public String getDqjd() {
		return dqjd;
	}
	public void setDqjd(String dqjd){
		this.dqjd = dqjd;
	}
	//机构ID
	public void setJgid(String jgid) {
		this.jgid = jgid;
	}
	public String getJgid() {
		return jgid;
	}
	//负责人
	public String getFzr() {
		return fzr;
	}
	public void setFzr(String fzr){
		this.fzr = fzr;
	}
	//状态
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//确认人员
	public String getQrry() {
		return qrry;
	}
	public void setQrry(String qrry) {
		this.qrry = qrry;
	}


	public String getJssj() {
		return jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
