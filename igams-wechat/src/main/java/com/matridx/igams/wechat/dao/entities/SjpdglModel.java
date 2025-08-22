package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjpdglModel")
public class SjpdglModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//送检派单ID
	private String sjpdid;
	//派单号
	private String pdh;
	//寄送方式
	private String jsfs;
	//取样地址
	private String qydz;
	//预计时间
	private String yjsj;
	//取样联系人
	private String qylxr;
	//取样联系电话
	private String qylxdh;
	//标本类型
	private String bblx;
	//是否收费
	private String sfsf;
	//收费金额
	private String sfje;
	//接单标记
	private String jdbj;
	//检测单位
	private String jcdw;
	//派单备注
	private String pdbz;
	//取消派单原因
	private String qxpdyy;
	//派单标记(保存0  派单1)
	private String pdbj;
	//取消派单标记
	private String qxpdbj;
	//合作伙伴名称
	private String hbmc;
	//送检id
	private String sjid;
	//派单时间
	private String pdsj;
	//运输时长
	private String yssc;

	public String getYssc() {
		return yssc;
	}

	public void setYssc(String yssc) {
		this.yssc = yssc;
	}

	public String getPdsj() {
		return pdsj;
	}

	public void setPdsj(String pdsj) {
		this.pdsj = pdsj;
	}
	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getHbmc() {
		return hbmc;
	}

	public void setHbmc(String hbmc) {
		this.hbmc = hbmc;
	}

	public String getQxpdbj() {
		return qxpdbj;
	}

	public void setQxpdbj(String qxpdbj) {
		this.qxpdbj = qxpdbj;
	}

	public String getPdbj() {
		return pdbj;
	}

	public void setPdbj(String pdbj) {
		this.pdbj = pdbj;
	}


	//预计到达时间
	private String yjddsj;

	//标本编号
	private String bbbh;
	//状态
	private String zt;

	public String getPdbz() {
		return pdbz;
	}

	public void setPdbz(String pdbz) {
		this.pdbz = pdbz;
	}



	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getBbbh() {
		return bbbh;
	}

	public void setBbbh(String bbbh) {
		this.bbbh = bbbh;
	}

	public String getYjddsj() {
		return yjddsj;
	}

	public void setYjddsj(String yjddsj) {
		this.yjddsj = yjddsj;
	}

	public String getSjpdid() {
		return sjpdid;
	}

	public void setSjpdid(String sjpdid) {
		this.sjpdid = sjpdid;
	}

	public String getPdh() {
		return pdh;
	}

	public void setPdh(String pdh) {
		this.pdh = pdh;
	}

	public String getJsfs() {
		return jsfs;
	}

	public void setJsfs(String jsfs) {
		this.jsfs = jsfs;
	}

	public String getQydz() {
		return qydz;
	}

	public void setQydz(String qydz) {
		this.qydz = qydz;
	}

	public String getYjsj() {
		return yjsj;
	}

	public void setYjsj(String yjsj) {
		this.yjsj = yjsj;
	}

	public String getQylxr() {
		return qylxr;
	}

	public void setQylxr(String qylxr) {
		this.qylxr = qylxr;
	}

	public String getQylxdh() {
		return qylxdh;
	}

	public void setQylxdh(String qylxdh) {
		this.qylxdh = qylxdh;
	}

	public String getBblx() {
		return bblx;
	}

	public void setBblx(String bblx) {
		this.bblx = bblx;
	}

	public String getSfsf() {
		return sfsf;
	}

	public void setSfsf(String sfsf) {
		this.sfsf = sfsf;
	}

	public String getSfje() {
		return sfje;
	}

	public void setSfje(String sfje) {
		this.sfje = sfje;
	}

	public String getJdbj() {
		return jdbj;
	}

	public void setJdbj(String jdbj) {
		this.jdbj = jdbj;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getQxpdyy() {
		return qxpdyy;
	}

	public void setQxpdyy(String qxpdyy) {
		this.qxpdyy = qxpdyy;
	}
}
