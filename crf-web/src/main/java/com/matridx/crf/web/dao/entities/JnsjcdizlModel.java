package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JnsjcdizlModel")
public class JnsjcdizlModel extends BaseModel{
	//治疗id
	private String zlid;
	//治疗药物
	private String zlyw;
	//单次剂量
	private String dcjl;
	//使用方法
	private String syff;
	//开始日期
	private String ksrq;
	//结束日期
	private String jsrq;
	//艰难梭菌报告记录
	private String jnsjbgid;
	//治疗id
	public String getZlid() {
		return zlid;
	}
	public void setZlid(String zlid){
		this.zlid = zlid;
	}
	//治疗药物
	public String getZlyw() {
		return zlyw;
	}
	public void setZlyw(String zlyw){
		this.zlyw = zlyw;
	}
	//单次剂量
	public String getDcjl() {
		return dcjl;
	}
	public void setDcjl(String dcjl){
		this.dcjl = dcjl;
	}
	//使用方法
	public String getSyff() {
		return syff;
	}
	public void setSyff(String syff){
		this.syff = syff;
	}
	//开始日期
	public String getKsrq() {
		return ksrq;
	}
	public void setKsrq(String ksrq){
		this.ksrq = ksrq;
	}
	//结束日期
	public String getJsrq() {
		return jsrq;
	}
	public void setJsrq(String jsrq){
		this.jsrq = jsrq;
	}
	//艰难梭菌报告记录
	public String getJnsjbgid() {
		return jnsjbgid;
	}
	public void setJnsjbgid(String jnsjbgid){
		this.jnsjbgid = jnsjbgid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
