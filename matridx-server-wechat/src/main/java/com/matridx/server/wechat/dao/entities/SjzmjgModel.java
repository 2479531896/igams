package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjzmjgModel")
public class SjzmjgModel extends BaseModel{
	//自免id
	private String zmid;
	//送检id
	private String sjid;
	//序号
	private String xh;
	//项目
	private String xm;
	//结果
	private String jg;
	//参考范围
	private String ckz;
	//检测方法
	private String jcff;
	//类型
	private String lx;

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	//自免id
	public String getZmid() {
		return zmid;
	}
	public void setZmid(String zmid){
		this.zmid = zmid;
	}
	//送检id
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//项目
	public String getXm() {
		return xm;
	}
	public void setXm(String xm){
		this.xm = xm;
	}
	//结果
	public String getJg() {
		return jg;
	}
	public void setJg(String jg){
		this.jg = jg;
	}

	public String getCkz() {
		return ckz;
	}
	public void setCkz(String ckz) {
		this.ckz = ckz;
	}
	public String getJcff() {
		return jcff;
	}
	public void setJcff(String jcff) {
		this.jcff = jcff;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
