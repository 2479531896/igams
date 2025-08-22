package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="SjnyxModel")
public class SjnyxModel extends BaseModel{
	//送检耐药性ID
	private String sjnyxid;
	//送检ID
	private String sjid;
	//序号
	private String xh;
	//基因
	private String jy;
	//基因类
	private String jyl;
	//特性
	private String tx;
	//机理
	private String jl;
	//读数
	private String ds;
	//药品
	private String yp;
	//相关物种
	private String xgwz;
	//检测类型
	private String jclx;
	//检测子类型
	private String jczlx;
	//耐药基因ID
	private String nyjyid;
	//基因分型
	private String jyfx;
	//序列数
	private String xls;
	//起源种
	private String qyz;

	//json
	private String json;
	//背景突变
	private String bjtb;

	private String report_taxids;

	private String sfhb;
	
	private String ysyp;
	
	public String getSfhb() {
		return sfhb;
	}

	public void setSfhb(String sfhb) {
		this.sfhb = sfhb;
	}


	public String getSjnyxid() {
		return sjnyxid;
	}

	public void setSjnyxid(String sjnyxid) {
		this.sjnyxid = sjnyxid;
	}

	public String getBjtb() {
		return bjtb;
	}

	public void setBjtb(String bjtb) {
		this.bjtb = bjtb;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getNyjyid() {
		return nyjyid;
	}
	public void setNyjyid(String nyjyid) {
		this.nyjyid = nyjyid;
	}
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//基因
	public String getJy() {
		return jy;
	}
	public void setJy(String jy){
		this.jy = jy;
	}
	//基因类
	public String getJyl() {
		return jyl;
	}
	public void setJyl(String jyl){
		this.jyl = jyl;
	}
	//特性
	public String getTx() {
		return tx;
	}
	public void setTx(String tx){
		this.tx = tx;
	}
	//机理
	public String getJl() {
		return jl;
	}
	public void setJl(String jl){
		this.jl = jl;
	}
	//读数
	public String getDs() {
		return ds;
	}
	public void setDs(String ds){
		this.ds = ds;
	}
	//药物
	public String getYp() {
		return yp;
	}
	public void setYp(String yp){
		this.yp = yp;
	}
	//相关物种
	public String getXgwz() {
		return xgwz;
	}
	public void setXgwz(String xgwz){
		this.xgwz = xgwz;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getJclx() {
		return jclx;
	}
	public void setJclx(String jclx) {
		this.jclx = jclx;
	}
	public String getJyfx() {
		return jyfx;
	}
	public void setJyfx(String jyfx) {
		this.jyfx = jyfx;
	}
	public String getXls() {
		return xls;
	}
	public void setXls(String xls) {
		this.xls = xls;
	}

	public String getQyz() {
		return qyz;
	}

	public void setQyz(String qyz) {
		this.qyz = qyz;
	}

	public String getReport_taxids() {
		return report_taxids;
	}

	public void setReport_taxids(String report_taxids) {
		this.report_taxids = report_taxids;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getYsyp() {
		return ysyp;
	}

	public void setYsyp(String ysyp) {
		this.ysyp = ysyp;
	}

    public String getJczlx() {
        return jczlx;
    }

    public void setJczlx(String jczlx) {
        this.jczlx = jczlx;
    }
}
