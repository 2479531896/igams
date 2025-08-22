package com.matridx.igams.production.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Alias(value="QgmxModel")
public class QgmxModel extends BaseBasicModel{
	//采购明细ID
	private String qgmxid;
	//采购ID
	private String qgid;
	//序号
	private String xh;
	//物料ID
	private String wlid;
	//数量
	private String sl;
	//价格
	private String jg;
	//期望到货日期
	private String qwrq;
	//备注  审批备注，采购备注是否需要分开
	private String bz;
	//取消采购 审核通过时点击  0：正常  1：取消采购
	private String qxqg;
	//状态   审批状态 如果物料不通过  00 15,80
	private String zt;
	//关联id，关联U8中的主键ID
	private String glid;
	//拒绝审批人员
	private String jjspry;
	//拒绝审批时间
	private String jjspsj;
	//剩余数量
	private String sysl;
	//预定数量
	private String ydsl;
	//货物标准
	private String hwbz;
	//用途
	private String yt;
	//要求(设备质量要求或服务要求)
	private String yq;
	//审计结论
	private String sjjl;
	//原始数量
	private String yssl;
	//合同编号关联
	private String htbhgl;
	//计划到货日期
	private String jhdhrq;
	//合同日期
	private String htrq;
	//到货单号关联
	private String dhdhgl;
	//计量单位
	private String hwjldw;
	//货物名称
	private String hwmc;
	//入库数量
	private String rksl;
	//供应商
	private String gys;
	//维保要求
	private String wbyq;
	//配置要求
	private String pzyq;
	//所属研发项目
	private String ssyfxm;
	//是否入库
	private String sfrk;
	//是否确认
	private String sfqr;
	//产品注册号
	private String cpzch;
	private String xqjhmxid;

	public String getXqjhmxid() {
		return xqjhmxid;
	}

	public void setXqjhmxid(String xqjhmxid) {
		this.xqjhmxid = xqjhmxid;
	}

	public String getCpzch() {
		return cpzch;
	}

	public void setCpzch(String cpzch) {
		this.cpzch = cpzch;
	}
	private List<String> qgmxids;
	public List<String> getQgmxids() {
		return qgmxids;
	}
	public void setQgmxids(String qgmxids) {
		List<String> list = new ArrayList<>();
		if(StringUtil.isNotBlank(qgmxids)) {
			String[] str = qgmxids.split(",");
			list = Arrays.asList(str);
		}
		this.qgmxids = list;
	}
	public String getSfqr() {
		return sfqr;
	}

	public void setSfqr(String sfqr) {
		this.sfqr = sfqr;
	}

	public String getSfrk() {
		return sfrk;
	}
	public void setSfrk(String sfrk) {
		this.sfrk = sfrk;
	}
	public String getSsyfxm() {
		return ssyfxm;
	}
	public void setSsyfxm(String ssyfxm) {
		this.ssyfxm = ssyfxm;
	}
	public String getWbyq() {
		return wbyq;
	}
	public void setWbyq(String wbyq) {
		this.wbyq = wbyq;
	}
	public String getPzyq() {
		return pzyq;
	}
	public void setPzyq(String pzyq) {
		this.pzyq = pzyq;
	}
	public String getGys() {
		return gys;
	}
	public void setGys(String gys) {
		this.gys = gys;
	}
	public String getRksl() {
		return rksl;
	}
	public void setRksl(String rksl) {
		this.rksl = rksl;
	}
	public String getHwjldw() {
		return hwjldw;
	}
	public void setHwjldw(String hwjldw) {
		this.hwjldw = hwjldw;
	}
	public String getHwmc() {
		return hwmc;
	}
	public void setHwmc(String hwmc) {
		this.hwmc = hwmc;
	}

	public String getHtbhgl() {
		return htbhgl;
	}
	public void setHtbhgl(String htbhgl) {
		this.htbhgl = htbhgl;
	}
	public String getJhdhrq() {
		return jhdhrq;
	}
	public void setJhdhrq(String jhdhrq) {
		this.jhdhrq = jhdhrq;
	}
	public String getHtrq() {
		return htrq;
	}
	public void setHtrq(String htrq) {
		this.htrq = htrq;
	}
	public String getDhdhgl() {
		return dhdhgl;
	}
	public void setDhdhgl(String dhdhgl) {
		this.dhdhgl = dhdhgl;
	}
	public String getYssl() {
		return yssl;
	}
	public void setYssl(String yssl) {
		this.yssl = yssl;
	}
	public String getYdsl() {
		return ydsl;
	}
	public void setYdsl(String ydsl) {
		this.ydsl = ydsl;
	}
	public String getHwbz() {
		return hwbz;
	}
	public void setHwbz(String hwbz) {
		this.hwbz = hwbz;
	}
	public String getYt() {
		return yt;
	}
	public void setYt(String yt) {
		this.yt = yt;
	}
	public String getYq() {
		return yq;
	}
	public void setYq(String yq) {
		this.yq = yq;
	}
	public String getSjjl() {
		return sjjl;
	}
	public void setSjjl(String sjjl) {
		this.sjjl = sjjl;
	}
	public String getSysl() {
		return sysl;
	}
	public void setSysl(String sysl) {
		this.sysl = sysl;
	}
	public String getJjspry() {
		return jjspry;
	}
	public void setJjspry(String jjspry) {
		this.jjspry = jjspry;
	}
	public String getJjspsj() {
		return jjspsj;
	}
	public void setJjspsj(String jjspsj) {
		this.jjspsj = jjspsj;
	}
	public String getGlid() {
		return glid;
	}
	public void setGlid(String glid) {
		this.glid = glid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public String getQgmxid() {
		return qgmxid;
	}
	public void setQgmxid(String qgmxid) {
		this.qgmxid = qgmxid;
	}
	public String getQgid() {
		return qgid;
	}
	public void setQgid(String qgid) {
		this.qgid = qgid;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//物料ID
	public String getWlid() {
		return wlid;
	}
	public void setWlid(String wlid){
		this.wlid = wlid;
	}
	//数量
	public String getSl() {
		return sl;
	}
	public void setSl(String sl){
		this.sl = sl;
	}
	//价格
	public String getJg() {
		return jg;
	}
	public void setJg(String jg){
		this.jg = jg;
	}
	//期望到货日期
	public String getQwrq() {
		return qwrq;
	}
	public void setQwrq(String qwrq){
		this.qwrq = qwrq;
	}
	//备注  审批备注，采购备注是否需要分开
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//取消采购 审核通过时点击  0：正常  1：取消采购
	public String getQxqg() {
		return qxqg;
	}
	public void setQxqg(String qxqg){
		this.qxqg = qxqg;
	}
	//状态   审批状态 如果物料不通过  00 15,80
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
