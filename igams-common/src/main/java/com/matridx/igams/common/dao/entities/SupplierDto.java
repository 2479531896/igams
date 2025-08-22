package com.matridx.igams.common.dao.entities;
import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SupplierDto")
public class SupplierDto extends BaseModel{
	private static final long serialVersionUID = 1L;
	//供应商ID
	private String gysid;
	//供应商名称
	private String gysmc;
	//供应商代码
	private String gysdm;
	//供应商简称
	private String gysjc;
	//地区
	private String dq;
	//发展日期
	private String fzrq;
	//联系人
	private String lxr;
	//电话
	private String dh;
	//供方管理类别 基础数据
	private String gfgllb;
	//状态 00:未提交
	private String zt;
	//备注
	private String bz;
	//分布式标识
	private String prefix;
	//全部查询
	private String entire;
	//供方管理类别名称
	private String gfgllbmc;
	//供应商手机
	private String sj;
	//供应商QQ
	private String qq;
	//供应商微信
	private String wx;
	//供应商税率
	private String sl;
	//供应商开户行
	private String khh;
	//供应商账号
	private String zh;
	//供方编号
	private String gfbh;
	//是否有附件
	private String sfyfj;
//是否存在框架合同
	private String sfczkjht;
	private String cz;
	private String jxflg;

	public String getJxflg() {
		return jxflg;
	}

	public void setJxflg(String jxflg) {
		this.jxflg = jxflg;
	}

	public String getCz() {
		return cz;
	}

	public void setCz(String cz) {
		this.cz = cz;
	}

	public String getSfczkjht() {
		return sfczkjht;
	}

	public void setSfczkjht(String sfczkjht) {
		this.sfczkjht = sfczkjht;
	}
	public String getSfyfj() {
		return sfyfj;
	}

	public void setSfyfj(String sfyfj) {
		this.sfyfj = sfyfj;
	}

	public String getGfbh() {
		return gfbh;
	}

	public void setGfbh(String gfbh) {
		this.gfbh = gfbh;
	}

	public String getKhh() {
		return khh;
	}

	public void setKhh(String khh) {
		this.khh = khh;
	}

	public String getZh() {
		return zh;
	}

	public void setZh(String zh) {
		this.zh = zh;
	}

	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
	}
	public String getSj() {
		return sj;
	}
	public void setSj(String sj) {
		this.sj = sj;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWx() {
		return wx;
	}
	public void setWx(String wx) {
		this.wx = wx;
	}
	//供应商ID
	public String getGysid() {
		return gysid;
	}
	public void setGysid(String gysid) {
		this.gysid = gysid;
	}
	public String getGysmc() {
		return gysmc;
	}
	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}
	public String getGysdm() {
		return gysdm;
	}
	public void setGysdm(String gysdm) {
		this.gysdm = gysdm;
	}
	public String getGysjc() {
		return gysjc;
	}
	public void setGysjc(String gysjc) {
		this.gysjc = gysjc;
	}
	public String getDq() {
		return dq;
	}
	public void setDq(String dq) {
		this.dq = dq;
	}
	public String getFzrq() {
		return fzrq;
	}
	public void setFzrq(String fzrq) {
		this.fzrq = fzrq;
	}
	public String getLxr() {
		return lxr;
	}
	public void setLxr(String lxr) {
		this.lxr = lxr;
	}
	public String getDh() {
		return dh;
	}
	public void setDh(String dh) {
		this.dh = dh;
	}
	public String getGfgllb() {
		return gfgllb;
	}
	public void setGfgllb(String gfgllb) {
		this.gfgllb = gfgllb;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getEntire() {
		return entire;
	}
	public void setEntire(String entire) {
		this.entire = entire;
	}
	public String getGfgllbmc() {
		return gfgllbmc;
	}
	public void setGfgllbmc(String gfgllbmc) {
		this.gfgllbmc = gfgllbmc;
	}
}
