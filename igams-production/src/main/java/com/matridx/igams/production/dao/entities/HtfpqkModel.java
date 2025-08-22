package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="HtfpqkModel")
public class HtfpqkModel extends BaseModel{
	//合同发票ID
	private String htfpid;
	//合同ID
	private String htid;
	//发票号码
	private String fphm;
	//发票接收日期
	private String fpjsrq;
	//税率
	private String sl;
	//发票种类
	private String fpzl;
	//不含税金额
	private String bhsje;
	//发票金额
	private String fpje;
	//转交财务日期
	private String zjcwrq;
	//开票日期
	private String kprq;
	//发票代码
	private String fpdm;
	//合同发票ID
	public String getHtfpid() {
		return htfpid;
	}
	public void setHtfpid(String htfpid){
		this.htfpid = htfpid;
	}
	//合同ID
	public String getHtid() {
		return htid;
	}
	public void setHtid(String htid){
		this.htid = htid;
	}
	//发票号码
	public String getFphm() {
		return fphm;
	}
	public void setFphm(String fphm){
		this.fphm = fphm;
	}
	//发票接收日期
	public String getFpjsrq() {
		return fpjsrq;
	}
	public void setFpjsrq(String fpjsrq){
		this.fpjsrq = fpjsrq;
	}
	//税率
	public String getSl() {
		return sl;
	}
	public void setSl(String sl){
		this.sl = sl;
	}
	//发票种类
	public String getFpzl() {
		return fpzl;
	}
	public void setFpzl(String fpzl){
		this.fpzl = fpzl;
	}
	//不含税金额
	public String getBhsje() {
		return bhsje;
	}
	public void setBhsje(String bhsje){
		this.bhsje = bhsje;
	}
	//发票金额
	public String getFpje() {
		return fpje;
	}
	public void setFpje(String fpje){
		this.fpje = fpje;
	}
	//转交财务日期
	public String getZjcwrq() {
		return zjcwrq;
	}
	public void setZjcwrq(String zjcwrq){
		this.zjcwrq = zjcwrq;
	}
	//开票日期
	public String getKprq() {
		return kprq;
	}
	public void setKprq(String kprq){
		this.kprq = kprq;
	}
	//发票代码
	public String getFpdm() {
		return fpdm;
	}
	public void setFpdm(String fpdm){
		this.fpdm = fpdm;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
