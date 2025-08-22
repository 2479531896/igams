package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="LlglModel")
public class LlglModel extends BaseBasicModel{
	//领料ID
	private String llid;
	//领料单号
	private String lldh;
	//申请部门
	private String sqbm;
	//申请人员
	private String sqry;
	//申请日期
	private String sqrq;
	//订单完成标记 0:未完成  1：完成 全部已处理
	private String wcbj;
	//备注
	private String bz;
	//状态 00:未提交
	private String zt;
	//出库类别
	private String cklb;
	//记录编码
	private String jlbh;
	//出库类别名称
	private String cklbmc;
	//关联id
	private String glid;
	//出库单号
	private String ckdh;
	//出库日期
	private String ckrq;
	//仓库id
	private String ckid;
	//领料人
	private String llr;
	//发料人员
	private String flry;
	//出库状态
	private String ckzt;
	//取样标记--1：取样领料  0或null:普通领料
	private String qybj;
	//出库关联id
	private String ckglid;
	//领料类型(0:普通领料，同步U8；1:OA领料，不同步U8 2：留样领料 3：销售领料 4：OA留样领料 5：生产领料
	private String lllx;
	//客户id
	private String khid;
	//终端
	private String zd;
	//终端区域
	private String zdqy;
	//合同单号
	private String htdh;
	//归属人
	private String gsr;
	//负责大区
	private String fzdq;
	//赠送原因
	private String zsyy;
	//收获地址
	private String shdz;
	//收获联系方式
	private String shlxfs;
	//快递信息
	private String kdxx;
	//是否确认
	private String sfqr;
	//销售类型
	private String xslx;
	
	//归属人部门
	private String gsrbm;
	//是否自用
	private String sfzy;
	//部门设备负责人
	private String bmsbfzr;
	//使用地点
	private String sydd;
//生产指令id
	private String sczlid;
	private String fzr;//负责人
	//运输方式
	private String ysfs;

	public String getYsfs() {
		return ysfs;
	}

	public void setYsfs(String ysfs) {
		this.ysfs = ysfs;
	}
	public String getFzr() {
		return fzr;
	}

	public void setFzr(String fzr) {
		this.fzr = fzr;
	}

	public String getSczlid() {
		return sczlid;
	}

	public void setSczlid(String sczlid) {
		this.sczlid = sczlid;
	}
	public String getBmsbfzr() {
		return bmsbfzr;
	}

	public void setBmsbfzr(String bmsbfzr) {
		this.bmsbfzr = bmsbfzr;
	}

	public String getSydd() {
		return sydd;
	}

	public void setSydd(String sydd) {
		this.sydd = sydd;
	}

	public String getSfzy() {
		return sfzy;
	}

	public void setSfzy(String sfzy) {
		this.sfzy = sfzy;
	}

	public String getGsrbm() {
		return gsrbm;
	}

	public void setGsrbm(String gsrbm) {
		this.gsrbm = gsrbm;
	}

	public String getXslx() {
		return xslx;
	}

	public void setXslx(String xslx) {
		this.xslx = xslx;
	}
	public String getKdxx() {
		return kdxx;
	}

	public void setKdxx(String kdxx) {
		this.kdxx = kdxx;
	}

	public String getSfqr() {
		return sfqr;
	}

	public void setSfqr(String sfqr) {
		this.sfqr = sfqr;
	}

	public String getKhid() {
		return khid;
	}

	public void setKhid(String khid) {
		this.khid = khid;
	}

	public String getZd() {
		return zd;
	}

	public void setZd(String zd) {
		this.zd = zd;
	}

	public String getZdqy() {
		return zdqy;
	}

	public void setZdqy(String zdqy) {
		this.zdqy = zdqy;
	}

	public String getHtdh() {
		return htdh;
	}

	public void setHtdh(String htdh) {
		this.htdh = htdh;
	}

	public String getGsr() {
		return gsr;
	}

	public void setGsr(String gsr) {
		this.gsr = gsr;
	}

	public String getFzdq() {
		return fzdq;
	}

	public void setFzdq(String fzdq) {
		this.fzdq = fzdq;
	}

	public String getZsyy() {
		return zsyy;
	}

	public void setZsyy(String zsyy) {
		this.zsyy = zsyy;
	}

	public String getShdz() {
		return shdz;
	}

	public void setShdz(String shdz) {
		this.shdz = shdz;
	}

	public String getShlxfs() {
		return shlxfs;
	}

	public void setShlxfs(String shlxfs) {
		this.shlxfs = shlxfs;
	}

	public String getLllx() {
		return lllx;
	}
	public void setLllx(String lllx) {
		this.lllx = lllx;
	}
	public String getCkglid() {
		return ckglid;
	}
	public void setCkglid(String ckglid) {
		this.ckglid = ckglid;
	}
	public String getQybj() {
		return qybj;
	}
	public void setQybj(String qybj) {
		this.qybj = qybj;
	}
	public String getCkzt() {
		return ckzt;
	}
	public void setCkzt(String ckzt) {
		this.ckzt = ckzt;
	}
	public String getLlr() {
		return llr;
	}
	public void setLlr(String llr) {
		this.llr = llr;
	}
	public String getFlry() {
		return flry;
	}
	public void setFlry(String flry) {
		this.flry = flry;
	}
	public String getCkdh() {
		return ckdh;
	}
	public void setCkdh(String ckdh) {
		this.ckdh = ckdh;
	}
	public String getCkrq() {
		return ckrq;
	}
	public void setCkrq(String ckrq) {
		this.ckrq = ckrq;
	}
	public String getCkid() {
		return ckid;
	}
	public void setCkid(String ckid) {
		this.ckid = ckid;
	}
	public String getGlid() {
		return glid;
	}
	public void setGlid(String glid) {
		this.glid = glid;
	}
	public String getCklbmc() {
		return cklbmc;
	}
	public void setCklbmc(String cklbmc) {
		this.cklbmc = cklbmc;
	}
	public String getCklb() {
		return cklb;
	}
	public void setCklb(String cklb) {
		this.cklb = cklb;
	}
	public String getJlbh() {
		return jlbh;
	}
	public void setJlbh(String jlbh) {
		this.jlbh = jlbh;
	}
	//领料ID
	public String getLlid() {
		return llid;
	}
	public void setLlid(String llid){
		this.llid = llid;
	}
	//领料单号
	public String getLldh() {
		return lldh;
	}
	public void setLldh(String lldh){
		this.lldh = lldh;
	}
	//申请部门
	public String getSqbm() {
		return sqbm;
	}
	public void setSqbm(String sqbm){
		this.sqbm = sqbm;
	}
	//申请人员
	public String getSqry() {
		return sqry;
	}
	public void setSqry(String sqry){
		this.sqry = sqry;
	}
	//申请日期
	public String getSqrq() {
		return sqrq;
	}
	public void setSqrq(String sqrq){
		this.sqrq = sqrq;
	}
	//订单完成标记 0:未完成  1：完成 全部已处理
	public String getWcbj() {
		return wcbj;
	}
	public void setWcbj(String wcbj){
		this.wcbj = wcbj;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//状态 00:未提交
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
