package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;
import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="WjglModel")
public class WjglModel extends BaseBasicModel{
	//文件ID
	private String wjid;
	//文件分类  基础数据，外来文件，质量管理体系文件
	private String wjfl;
	//文件类别  基础数据，作为文件分类的下一级
	private String wjlb;
	//文件编号
	private String wjbh;
	//文件名称
	private String wjmc;
	//版本号
	private String bbh;
	//生效日期
	private String sxrq;
	//跟踪记录
	private String gzjl;
	//备注
	private String bz;
	//关联文件ID
	private String glwjid;
	//审核状态   00:未提交 10:审核中  15:审核未通过  80:审核通过
	private String zt;
	//编制人员
	private String bzry;
	//编制时间
	private String bzsj;
	//审核人员
	private String shry;
	//审核时间
	private String shsj;
	//批准人员
	private String pzry;
	//批准时间
	private String pzsj;
	//根文件ID
	private String gwjid;
	//原文件ID
	private String ywjid;
	//更改来源
	private String ggly;
	//受影响文件
	private String syxwj;
	//转换规则
	private String zhgz;
	//是否替换
	private String sfth;
	//参数扩展1
	private String cskz1;
	//参数扩展2
	private String cskz2;
	//参数扩展3
	private String cskz3;
	//发放状态  0未发放，1已发放，2部分发放
	private String ffzt;

	public String getFfzt() {
		return ffzt;
	}

	public void setFfzt(String ffzt) {
		this.ffzt = ffzt;
	}

	public String getCskz1() {
		return cskz1;
	}
	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}
	public String getCskz2() {
		return cskz2;
	}
	public void setCskz2(String cskz2) {
		this.cskz2 = cskz2;
	}
	public String getCskz3() {
		return cskz3;
	}
	public void setCskz3(String cskz3) {
		this.cskz3 = cskz3;
	}
	//转换规则
	public String getZhgz() {
		return zhgz;
	}
	public void setZhgz(String zhgz) {
		this.zhgz = zhgz;
	}
	//是否替换
	public String getSfth() {
		return sfth;
	}
	public void setSfth(String sfth) {
		this.sfth = sfth;
	}
	//更改来源
	public String getGgly() {
		return ggly;
	}
	public void setGgly(String ggly) {
		this.ggly = ggly;
	}
	//受影响文件
	public String getSyxwj() {
		return syxwj;
	}
	public void setSyxwj(String syxwj) {
		this.syxwj = syxwj;
	}
	//文件ID
	public String getWjid() {
		return wjid;
	}
	public void setWjid(String wjid){
		this.wjid = wjid;
	}
	//文件分类  基础数据，外来文件，质量管理体系文件
	public String getWjfl() {
		return wjfl;
	}
	public void setWjfl(String wjfl){
		this.wjfl = wjfl;
	}
	//文件类别  基础数据，作为文件分类的下一级
	public String getWjlb() {
		return wjlb;
	}
	public void setWjlb(String wjlb){
		this.wjlb = wjlb;
	}
	//文件编号
	public String getWjbh() {
		return wjbh;
	}
	public void setWjbh(String wjbh){
		this.wjbh = wjbh;
	}
	//文件名称
	public String getWjmc() {
		return wjmc;
	}
	public void setWjmc(String wjmc){
		this.wjmc = wjmc;
	}
	//版本号
	public String getBbh() {
		return bbh;
	}
	public void setBbh(String bbh){
		this.bbh = bbh;
	}
	//生效日期
	public String getSxrq() {
		return sxrq;
	}
	public void setSxrq(String sxrq){
		this.sxrq = sxrq;
	}
	//跟踪记录
	public String getGzjl() {
		return gzjl;
	}
	public void setGzjl(String gzjl){
		this.gzjl = gzjl;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//关联文件ID
	public String getGlwjid() {
		return glwjid;
	}
	public void setGlwjid(String glwjid){
		this.glwjid = glwjid;
	}
	//审核状态   00:未提交 10:审核中  15:审核未通过  80:审核通过
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}
	//编制人员
	public String getBzry() {
		return bzry;
	}
	public void setBzry(String bzry){
		this.bzry = bzry;
	}
	//编制时间
	public String getBzsj() {
		return bzsj;
	}
	public void setBzsj(String bzsj){
		this.bzsj = bzsj;
	}
	//审核人员
	public String getShry() {
		return shry;
	}
	public void setShry(String shry){
		this.shry = shry;
	}
	//审核时间
	public String getShsj() {
		return shsj;
	}
	public void setShsj(String shsj){
		this.shsj = shsj;
	}
	//批准人员
	public String getPzry() {
		return pzry;
	}
	public void setPzry(String pzry){
		this.pzry = pzry;
	}
	//批准时间
	public String getPzsj() {
		return pzsj;
	}
	public void setPzsj(String pzsj){
		this.pzsj = pzsj;
	}
	//根文件ID
	public String getGwjid() {
		return gwjid;
	}
	public void setGwjid(String gwjid) {
		this.gwjid = gwjid;
	}
	//原文件ID
	public String getYwjid() {
		return ywjid;
	}
	public void setYwjid(String ywjid) {
		this.ywjid = ywjid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
