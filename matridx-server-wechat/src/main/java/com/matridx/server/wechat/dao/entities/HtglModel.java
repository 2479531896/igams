package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="HtglModel")
public class HtglModel extends BaseBasicModel{
    private static final long serialVersionUID = 1L;
    //合同id
    private String htid;
    //请购id
    private String qgid;
    //合同内部编号
    private String htnbbh;
    //合同外部编号
    private String htwbbh;
    //业务类型
    private String ywlx;
    //采购类型
    private String cglx;
    //订单日期
    private String ddrq;
    //总金额
    private String zje;
    //已付金额
    private String yfje;
    //发票金额
    private String fpje;
    //供应商
    private String gys;
    //负责人
    private String fzr;
    //申请部门
    private String sqbm;
    //创建日期
    private String cjrq;
    //提交日期
    private String tjrq;
    //合同外发日期
    private String htwfrq;
    //税率
    private String sl;
    //币种
    private String biz;
    //汇率
    private String hl;
    //双章标记
    private String szbj;
    //备注
    private String bz;
    //状态
    private String zt;
    //U8采购ID
    private String u8cgid;
    //U8POID
    private String u8poid;
    //付款方式
    private String fkfs;
    //发票方式
    private String fpfs;
    //完成标记
    private String wcbj;
    //交货日期
    private String jhrq;
    //付款方
    private String fkf;
    
	public String getFkf() {
		return fkf;
	}
	public void setFkf(String fkf) {
		this.fkf = fkf;
	}
	public String getJhrq() {
		return jhrq;
	}
	public void setJhrq(String jhrq) {
		this.jhrq = jhrq;
	}
	public String getWcbj() {
		return wcbj;
	}
	public void setWcbj(String wcbj) {
		this.wcbj = wcbj;
	}
	public String getFkfs() {
		return fkfs;
	}
	public void setFkfs(String fkfs) {
		this.fkfs = fkfs;
	}
	public String getFpfs() {
		return fpfs;
	}
	public void setFpfs(String fpfs) {
		this.fpfs = fpfs;
	}
	public String getHtid() {
		return htid;
	}
	public void setHtid(String htid) {
		this.htid = htid;
	}
	public String getQgid() {
		return qgid;
	}
	public void setQgid(String qgid) {
		this.qgid = qgid;
	}
	public String getHtnbbh() {
		return htnbbh;
	}
	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}
	public String getHtwbbh() {
		return htwbbh;
	}
	public void setHtwbbh(String htwbbh) {
		this.htwbbh = htwbbh;
	}
	public String getYwlx() {
		return ywlx;
	}
	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}
	public String getCglx() {
		return cglx;
	}
	public void setCglx(String cglx) {
		this.cglx = cglx;
	}
	public String getDdrq() {
		return ddrq;
	}
	public void setDdrq(String ddrq) {
		this.ddrq = ddrq;
	}
	public String getZje() {
		return zje;
	}
	public void setZje(String zje) {
		this.zje = zje;
	}
	public String getYfje() {
		return yfje;
	}
	public void setYfje(String yfje) {
		this.yfje = yfje;
	}
	public String getFpje() {
		return fpje;
	}
	public void setFpje(String fpje) {
		this.fpje = fpje;
	}
	public String getGys() {
		return gys;
	}
	public void setGys(String gys) {
		this.gys = gys;
	}
	public String getFzr() {
		return fzr;
	}
	public void setFzr(String fzr) {
		this.fzr = fzr;
	}
	public String getSqbm() {
		return sqbm;
	}
	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}
	public String getCjrq() {
		return cjrq;
	}
	public void setCjrq(String cjrq) {
		this.cjrq = cjrq;
	}
	public String getTjrq() {
		return tjrq;
	}
	public void setTjrq(String tjrq) {
		this.tjrq = tjrq;
	}
	public String getHtwfrq() {
		return htwfrq;
	}
	public void setHtwfrq(String htwfrq) {
		this.htwfrq = htwfrq;
	}
	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
	}
	public String getBiz() {
		return biz;
	}
	public void setBiz(String biz) {
		this.biz = biz;
	}
	public String getHl() {
		return hl;
	}
	public void setHl(String hl) {
		this.hl = hl;
	}
	public String getSzbj() {
		return szbj;
	}
	public void setSzbj(String szbj) {
		this.szbj = szbj;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	public String getU8cgid() {
		return u8cgid;
	}
	public void setU8cgid(String u8cgid) {
		this.u8cgid = u8cgid;
	}
	public String getU8poid() {
		return u8poid;
	}
	public void setU8poid(String u8poid) {
		this.u8poid = u8poid;
	}
    
    
}

