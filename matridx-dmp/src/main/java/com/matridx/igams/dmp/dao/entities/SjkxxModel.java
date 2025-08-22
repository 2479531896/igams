package com.matridx.igams.dmp.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjkxxModel")
public class SjkxxModel extends BaseModel{
	//数据库ID
	private String sjkid;
	//数据库名
	private String sjkm;
	//数据库用户名
	private String sjkyhm;
	//数据库密码
	private String sjkmm;
	//数据库类型  oracle mysql postgresql db2
	private String sjklx;
	//数据库连接串
	private String sjkljc;
	//数据库ID
	public String getSjkid() {
		return sjkid;
	}
	public void setSjkid(String sjkid){
		this.sjkid = sjkid;
	}
	//数据库名
	public String getSjkm() {
		return sjkm;
	}
	public void setSjkm(String sjkm){
		this.sjkm = sjkm;
	}
	//数据库用户名
	public String getSjkyhm() {
		return sjkyhm;
	}
	public void setSjkyhm(String sjkyhm){
		this.sjkyhm = sjkyhm;
	}
	//数据库密码
	public String getSjkmm() {
		return sjkmm;
	}
	public void setSjkmm(String sjkmm){
		this.sjkmm = sjkmm;
	}
	//数据库类型  oracle mysql postgresql db2
	public String getSjklx() {
		return sjklx;
	}
	public void setSjklx(String sjklx){
		this.sjklx = sjklx;
	}
	//数据库连接串
	public String getSjkljc() {
		return sjkljc;
	}
	public void setSjkljc(String sjkljc){
		this.sjkljc = sjkljc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
