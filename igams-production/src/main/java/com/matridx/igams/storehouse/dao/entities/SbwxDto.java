package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SbwxDto")
public class SbwxDto extends SbwxModel {
	private String sqrmc;//申请人名称
	private String sqbmmc;//申请部门名称
	private String sbmc;//设备名称
	private String sbbh;//设备编号
	private String ggxh;//规格
	private String xlh;//序列号
	private String sydd;//使用地点
	//审批人角色id
	private String sprjsid;
	//审批人角色名称
	private String sprjsmc;
	private String entire;//查询
	private String gdzcbh;//固定资产编号
	private String sbccbh;//设备出厂编号
	private String glrymc;//设备管理员
	private String syrymc;//使用人员
	private String bmsbfzrmc;//部门设备负责人
	private String gzsj;//购置时间
	private String sccj;//生产厂家
	private String sccjlxfs;//生产厂家联系方式
	private String xsybmmc;//使用部门
	private String sblxmc;//设备类型名称
	private String[] sblxs;//设备类型多
	private String[] xsybms;//现使用部门多
	private String[] glrys;//管理人员
	private String sqrqstart;//申请日期
	private String sqrqend;//申请日期
	private String wxsjstart;//维修时间
	private String wxsjend;//维修时间
	private String yzrqstart;//验证日期
	private String yzrqend;//验证日期
	private String hfsyrqstart;//恢复使用日期
	private String hfsyrqend;//恢复使用日期
	private String yzrmc;//
	private String wxqqwbj;//判断是否为其他
	private String wxhqwbj;//判断是否为其他
	private String wxqqwfsmc;//维修前去污方式名称
	private String wxhqwfsmc;//维修后去污方式名称
	private String xsybm;//使用部门
	private String glry;//管理人员
	private String[] wxqqwfss;//维修前去污方式s
	private String[] wxhqwfss;//维修后去污方式s
	private String[] zts;//状态
	private String sbzt;//设备状态
	private String ysbzt;//原设备状态
	private String sfbmxz;//是否部门限制

	public String getSfbmxz() {
		return sfbmxz;
	}

	public void setSfbmxz(String sfbmxz) {
		this.sfbmxz = sfbmxz;
	}
	public String getSbzt() {
		return sbzt;
	}

	public void setSbzt(String sbzt) {
		this.sbzt = sbzt;
	}

	public String getYsbzt() {
		return ysbzt;
	}

	public void setYsbzt(String ysbzt) {
		this.ysbzt = ysbzt;
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
	}

	public String[] getWxqqwfss() {
		return wxqqwfss;
	}

	public void setWxqqwfss(String[] wxqqwfss) {
		this.wxqqwfss = wxqqwfss;
	}

	public String[] getWxhqwfss() {
		return wxhqwfss;
	}

	public void setWxhqwfss(String[] wxhqwfss) {
		this.wxhqwfss = wxhqwfss;
	}

	public String getGlry() {
		return glry;
	}

	public void setGlry(String glry) {
		this.glry = glry;
	}

	public String getXsybm() {
		return xsybm;
	}

	public void setXsybm(String xsybm) {
		this.xsybm = xsybm;
	}

	public String getWxqqwfsmc() {
		return wxqqwfsmc;
	}

	public void setWxqqwfsmc(String wxqqwfsmc) {
		this.wxqqwfsmc = wxqqwfsmc;
	}

	public String getWxhqwfsmc() {
		return wxhqwfsmc;
	}

	public void setWxhqwfsmc(String wxhqwfsmc) {
		this.wxhqwfsmc = wxhqwfsmc;
	}

	public String getYzrmc() {
		return yzrmc;
	}

	public void setYzrmc(String yzrmc) {
		this.yzrmc = yzrmc;
	}

	public String getWxqqwbj() {
		return wxqqwbj;
	}

	public void setWxqqwbj(String wxqqwbj) {
		this.wxqqwbj = wxqqwbj;
	}

	public String getWxhqwbj() {
		return wxhqwbj;
	}

	public void setWxhqwbj(String wxhqwbj) {
		this.wxhqwbj = wxhqwbj;
	}

	public String[] getSblxs() {
		return sblxs;
	}

	public void setSblxs(String[] sblxs) {
		this.sblxs = sblxs;
	}

	public String[] getXsybms() {
		return xsybms;
	}

	public void setXsybms(String[] xsybms) {
		this.xsybms = xsybms;
	}

	public String[] getGlrys() {
		return glrys;
	}

	public void setGlrys(String[] glrys) {
		this.glrys = glrys;
	}

	public String getSqrqstart() {
		return sqrqstart;
	}

	public void setSqrqstart(String sqrqstart) {
		this.sqrqstart = sqrqstart;
	}

	public String getSqrqend() {
		return sqrqend;
	}

	public void setSqrqend(String sqrqend) {
		this.sqrqend = sqrqend;
	}

	public String getWxsjstart() {
		return wxsjstart;
	}

	public void setWxsjstart(String wxsjstart) {
		this.wxsjstart = wxsjstart;
	}

	public String getWxsjend() {
		return wxsjend;
	}

	public void setWxsjend(String wxsjend) {
		this.wxsjend = wxsjend;
	}

	public String getYzrqstart() {
		return yzrqstart;
	}

	public void setYzrqstart(String yzrqstart) {
		this.yzrqstart = yzrqstart;
	}

	public String getYzrqend() {
		return yzrqend;
	}

	public void setYzrqend(String yzrqend) {
		this.yzrqend = yzrqend;
	}

	public String getHfsyrqstart() {
		return hfsyrqstart;
	}

	public void setHfsyrqstart(String hfsyrqstart) {
		this.hfsyrqstart = hfsyrqstart;
	}

	public String getHfsyrqend() {
		return hfsyrqend;
	}

	public void setHfsyrqend(String hfsyrqend) {
		this.hfsyrqend = hfsyrqend;
	}

	public String getSblxmc() {
		return sblxmc;
	}

	public void setSblxmc(String sblxmc) {
		this.sblxmc = sblxmc;
	}

	public String getSccj() {
		return sccj;
	}

	public void setSccj(String sccj) {
		this.sccj = sccj;
	}

	public String getSccjlxfs() {
		return sccjlxfs;
	}

	public void setSccjlxfs(String sccjlxfs) {
		this.sccjlxfs = sccjlxfs;
	}

	public String getXsybmmc() {
		return xsybmmc;
	}

	public void setXsybmmc(String xsybmmc) {
		this.xsybmmc = xsybmmc;
	}

	public String getGzsj() {
		return gzsj;
	}

	public void setGzsj(String gzsj) {
		this.gzsj = gzsj;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getGdzcbh() {
		return gdzcbh;
	}

	public void setGdzcbh(String gdzcbh) {
		this.gdzcbh = gdzcbh;
	}

	public String getSbccbh() {
		return sbccbh;
	}

	public void setSbccbh(String sbccbh) {
		this.sbccbh = sbccbh;
	}

	public String getGlrymc() {
		return glrymc;
	}

	public void setGlrymc(String glrymc) {
		this.glrymc = glrymc;
	}

	public String getSyrymc() {
		return syrymc;
	}

	public void setSyrymc(String syrymc) {
		this.syrymc = syrymc;
	}

	public String getBmsbfzrmc() {
		return bmsbfzrmc;
	}

	public void setBmsbfzrmc(String bmsbfzrmc) {
		this.bmsbfzrmc = bmsbfzrmc;
	}

	public String getSprjsid() {
		return sprjsid;
	}

	public void setSprjsid(String sprjsid) {
		this.sprjsid = sprjsid;
	}

	public String getSprjsmc() {
		return sprjsmc;
	}

	public void setSprjsmc(String sprjsmc) {
		this.sprjsmc = sprjsmc;
	}

	public String getSbbh() {
		return sbbh;
	}

	public void setSbbh(String sbbh) {
		this.sbbh = sbbh;
	}

	public String getGgxh() {
		return ggxh;
	}

	public void setGgxh(String ggxh) {
		this.ggxh = ggxh;
	}

	public String getXlh() {
		return xlh;
	}

	public void setXlh(String xlh) {
		this.xlh = xlh;
	}

	public String getSydd() {
		return sydd;
	}

	public void setSydd(String sydd) {
		this.sydd = sydd;
	}

	public String getSbmc() {
		return sbmc;
	}

	public void setSbmc(String sbmc) {
		this.sbmc = sbmc;
	}

	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
