package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="PayinfoModel")
public class PayinfoModel extends BaseModel{
	
	private static final long serialVersionUID = 1L;
	
	//支付ID
	private String zfid;
	
	//业务ID
	private String ywid;
	
	//业务类型
	private String ywlx;
	
	//订单编号
	private String ddbh;
	
	//平台订单号
	private String ptddh;
	
	//关联订单号
	private String glddh;
	
	//调用类型
	private String dylx;
	
	//调用接口
	private String dyjk;
	
	//业务域信息 区分退款和支付
	private String ywyxx;
	
	//支付结果  0：未完成  1：成功  2：关闭 3：失效 9：异常情况
	private String jg;
	
	//付款金额
	private String fkje;
	
	//交易金额
	private String jyje;
	
	//交易时间
	private String jysj;
	
	//交易创建时间
	private String cjsj;
	
	//错误信息
	private String cwxx;
	
	//支付方式
	private String zffs;
	//外部程序id
	private String wbcxid;

	public String getWbcxid() {
		return wbcxid;
	}

	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}

	// 支付检测项目
	private String zfjcxm;

	public String getZfjcxm() {
		return zfjcxm;
	}

	public void setZfjcxm(String zfjcxm) {
		this.zfjcxm = zfjcxm;
	}
	public String getZffs() {
		return zffs;
	}

	public void setZffs(String zffs) {
		this.zffs = zffs;
	}

	public String getZfid() {
		return zfid;
	}

	public void setZfid(String zfid) {
		this.zfid = zfid;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getDdbh() {
		return ddbh;
	}

	public void setDdbh(String ddbh) {
		this.ddbh = ddbh;
	}

	public String getPtddh() {
		return ptddh;
	}

	public void setPtddh(String ptddh) {
		this.ptddh = ptddh;
	}

	public String getGlddh() {
		return glddh;
	}

	public void setGlddh(String glddh) {
		this.glddh = glddh;
	}

	public String getDylx() {
		return dylx;
	}

	public void setDylx(String dylx) {
		this.dylx = dylx;
	}

	public String getDyjk() {
		return dyjk;
	}

	public void setDyjk(String dyjk) {
		this.dyjk = dyjk;
	}

	public String getYwyxx() {
		return ywyxx;
	}

	public void setYwyxx(String ywyxx) {
		this.ywyxx = ywyxx;
	}

	public String getJg() {
		return jg;
	}

	public void setJg(String jg) {
		this.jg = jg;
	}

	public String getFkje() {
		return fkje;
	}

	public void setFkje(String fkje) {
		this.fkje = fkje;
	}

	public String getJyje() {
		return jyje;
	}

	public void setJyje(String jyje) {
		this.jyje = jyje;
	}

	public String getJysj() {
		return jysj;
	}

	public void setJysj(String jysj) {
		this.jysj = jysj;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public String getCwxx() {
		return cwxx;
	}

	public void setCwxx(String cwxx) {
		this.cwxx = cwxx;
	}
}
