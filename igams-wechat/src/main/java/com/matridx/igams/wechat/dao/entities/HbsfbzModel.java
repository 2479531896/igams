package com.matridx.igams.wechat.dao.entities;


import org.apache.ibatis.type.Alias;
import com.matridx.igams.common.dao.Model;

@Alias(value="HbsfbzModel")
public class HbsfbzModel extends Model{
	private String hbid;
	//项目
	private String xm;
	//收费标准
	private String  sfbz;
	//提取金额
	private String 	tqje;
	//子项目
	private String 	zxm;
	//开始日期
	private String 	ksrq;
	//结束日期
	private String 	jsrq;
	//合同明细ID
	private String htmxid;
	//伙伴收费标准ID
	private String hbsfbzid;

	public String getHbsfbzid() {
		return hbsfbzid;
	}

	public void setHbsfbzid(String hbsfbzid) {
		this.hbsfbzid = hbsfbzid;
	}

	public String getHtmxid() {
		return htmxid;
	}

	public void setHtmxid(String htmxid) {
		this.htmxid = htmxid;
	}

	public String getZxm() {
		return zxm;
	}

	public void setZxm(String zxm) {
		this.zxm = zxm;
	}

	public String getKsrq() {
		return ksrq;
	}

	public void setKsrq(String ksrq) {
		this.ksrq = ksrq;
	}

	public String getJsrq() {
		return jsrq;
	}

	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}

	public String getHbid(){
		return hbid;
	}

	public void setHbid(String hbid){
		this.hbid = hbid;
	}

	public String getXm(){
		return xm;
	}

	public void setXm(String xm){
		this.xm = xm;
	}

	public String getSfbz(){
		return sfbz;
	}

	public void setSfbz(String sfbz){
		this.sfbz = sfbz;
	}

	public String getTqje(){
		return tqje;
	}

	public void setTqje(String tqje){
		this.tqje = tqje;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
