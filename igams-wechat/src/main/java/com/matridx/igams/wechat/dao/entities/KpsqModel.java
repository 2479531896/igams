package com.matridx.igams.wechat.dao.entities;


import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="KpsqModel")
public class KpsqModel extends BaseBasicModel {
	private String fpsqid;//发票申请ID
	private String fpxz;//发票性质
	private String kpzt;//开票主体
	private String fphm;//发票号码
	private String sqbm;//申请部门
	private String kpdx;//开票对象
	private String khmc;//客户名称
	private String kjrq;//开具日期
	private String kplx;//开票类型
	private String kjje;//开具金额
	private String kpnr;//开票内容
	private String yx;//邮箱
	private String sh;//税号
	private String khyhjzh;//开户银行及账号
	private String dzdh;//地址电话
	private String bz;//备注
	private String ywlx;//业务类型
	private String ywid;//业务ID
	private String zt;//状态
	private String wbcxid;//外部程序ID
	private String spr;//审批人

	public String getSpr() {
		return spr;
	}

	public void setSpr(String spr) {
		this.spr = spr;
	}

	public String getKpzt() {
		return kpzt;
	}

	public void setKpzt(String kpzt) {
		this.kpzt = kpzt;
	}

	public String getFpsqid() {
		return fpsqid;
	}

	public void setFpsqid(String fpsqid) {
		this.fpsqid = fpsqid;
	}

	public String getFpxz() {
		return fpxz;
	}

	public void setFpxz(String fpxz) {
		this.fpxz = fpxz;
	}

	public String getFphm() {
		return fphm;
	}

	public void setFphm(String fphm) {
		this.fphm = fphm;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getKpdx() {
		return kpdx;
	}

	public void setKpdx(String kpdx) {
		this.kpdx = kpdx;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getKjrq() {
		return kjrq;
	}

	public void setKjrq(String kjrq) {
		this.kjrq = kjrq;
	}

	public String getKplx() {
		return kplx;
	}

	public void setKplx(String kplx) {
		this.kplx = kplx;
	}

	public String getKjje() {
		return kjje;
	}

	public void setKjje(String kjje) {
		this.kjje = kjje;
	}

	public String getKpnr() {
		return kpnr;
	}

	public void setKpnr(String kpnr) {
		this.kpnr = kpnr;
	}

	public String getYx() {
		return yx;
	}

	public void setYx(String yx) {
		this.yx = yx;
	}

	public String getSh() {
		return sh;
	}

	public void setSh(String sh) {
		this.sh = sh;
	}

	public String getKhyhjzh() {
		return khyhjzh;
	}

	public void setKhyhjzh(String khyhjzh) {
		this.khyhjzh = khyhjzh;
	}

	public String getDzdh() {
		return dzdh;
	}

	public void setDzdh(String dzdh) {
		this.dzdh = dzdh;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getWbcxid() {
		return wbcxid;
	}

	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
