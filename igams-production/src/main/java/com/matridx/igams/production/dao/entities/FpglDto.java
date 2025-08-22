package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="FpglDto")
public class FpglDto extends FpglModel{
	//发票种类名称
	private String fpzlmc;
	//发票类型名称
	private String fplxmc;
	//供应商名称
	private String gysmc;
	//代垫单位名称
	private String dddwmc;
	//部门名称
	private String bmmc;
	//  数组
	private String[] fplxs;
	//  数组
	private String[] fpzls;
	// 审核状态 数组
	private String[] zts;
	//开票日期
	private String kprqstart;
	//开票日期
	private String kprqend;
	//业务员名称
	private String ywymc;
	//发票明细
	private String fpmx_json;
	//合同ID
	private String htid;
	//币种名称
	private String bizmc;
	//录入人员名称
	private String lrrymc;
	private String sqlParam;
	private String fpsl;
	private String fpje;
	//通过IDs
	private List<String> tgids;
	//U8入库单号
	private String u8rkdh;
	//合同单号
	private String htnbbh;
	//全部(查询用)
	private String entire;
	//路径标记
	private String ljbj;

	public String getLjbj() {
		return ljbj;
	}

	public void setLjbj(String ljbj) {
		this.ljbj = ljbj;
	}


	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getHtnbbh() {
		return htnbbh;
	}

	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}

	public String getU8rkdh() {
		return u8rkdh;
	}

	public void setU8rkdh(String u8rkdh) {
		this.u8rkdh = u8rkdh;
	}

	public List<String> getTgids() {
		return tgids;
	}

	public void setTgids(List<String> tgids) {
		this.tgids = tgids;
	}

	public String getFpsl() {
		return fpsl;
	}

	public void setFpsl(String fpsl) {
		this.fpsl = fpsl;
	}

	public String getFpje() {
		return fpje;
	}

	public void setFpje(String fpje) {
		this.fpje = fpje;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getBizmc() {
		return bizmc;
	}

	public void setBizmc(String bizmc) {
		this.bizmc = bizmc;
	}

	public String getHtid() {
		return htid;
	}

	public void setHtid(String htid) {
		this.htid = htid;
	}

	public String getFpmx_json() {
		return fpmx_json;
	}

	public void setFpmx_json(String fpmx_json) {
		this.fpmx_json = fpmx_json;
	}

	public String getYwymc() {
		return ywymc;
	}

	public void setYwymc(String ywymc) {
		this.ywymc = ywymc;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getFpzlmc() {
		return fpzlmc;
	}

	public void setFpzlmc(String fpzlmc) {
		this.fpzlmc = fpzlmc;
	}

	public String getFplxmc() {
		return fplxmc;
	}

	public void setFplxmc(String fplxmc) {
		this.fplxmc = fplxmc;
	}

	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}

	public String getDddwmc() {
		return dddwmc;
	}

	public void setDddwmc(String dddwmc) {
		this.dddwmc = dddwmc;
	}

	public String[] getFplxs() {
		return fplxs;
	}

	public void setFplxs(String[] fplxs) {
		this.fplxs = fplxs;
		for (int i = 0; i < fplxs.length; i++){
			this.fplxs[i]=this.fplxs[i].replace("'","");
		}
	}

	public String[] getFpzls() {
		return fpzls;
	}

	public void setFpzls(String[] fpzls) {
		this.fpzls = fpzls;
		for (int i = 0; i < fpzls.length; i++){
			this.fpzls[i]=this.fpzls[i].replace("'","");
		}
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
		for (int i = 0; i < zts.length; i++){
			this.zts[i]=this.zts[i].replace("'","");
		}
	}

	public String getKprqstart() {
		return kprqstart;
	}

	public void setKprqstart(String kprqstart) {
		this.kprqstart = kprqstart;
	}

	public String getKprqend() {
		return kprqend;
	}

	public void setKprqend(String kprqend) {
		this.kprqend = kprqend;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
