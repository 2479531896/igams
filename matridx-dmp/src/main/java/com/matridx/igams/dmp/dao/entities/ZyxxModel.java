package com.matridx.igams.dmp.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="ZyxxModel")
public class ZyxxModel extends BaseModel{
	//资源ID
	private String zyid;
	//资源代码
	private String zydm;
	//资源名称
	private String zymc;
	//提供方ID
	private String tgfid;
	//对接方式  database，interface，register
	private String djfs;
	//关联ID  数据库ID或者连接ID
	private String glid;
	//接口内容
	private String jknr;
	//参数  &分割，再用等号分割获取后一个信息，处理掉#{}获取参数名称
	private String cs1;
	//参数2
	private String cs2;
	//参数3
	private String cs3;
	//参数4
	private String cs4;
	//资源ID
	public String getZyid() {
		return zyid;
	}
	public void setZyid(String zyid){
		this.zyid = zyid;
	}
	//资源代码
	public String getZydm() {
		return zydm;
	}
	public void setZydm(String zydm){
		this.zydm = zydm;
	}
	//资源名称
	public String getZymc() {
		return zymc;
	}
	public void setZymc(String zymc){
		this.zymc = zymc;
	}
	//提供方ID
	public String getTgfid() {
		return tgfid;
	}
	public void setTgfid(String tgfid){
		this.tgfid = tgfid;
	}
	//对接方式  database，interface，register
	public String getDjfs() {
		return djfs;
	}
	public void setDjfs(String djfs){
		this.djfs = djfs;
	}
	//关联ID  数据库ID或者连接ID
	public String getGlid() {
		return glid;
	}
	public void setGlid(String glid){
		this.glid = glid;
	}
	//接口内容
	public String getJknr() {
		return jknr;
	}
	public void setJknr(String jknr){
		this.jknr = jknr;
	}
	//参数  &分割，再用等号分割获取后一个信息，处理掉#{}获取参数名称
	public String getCs1() {
		return cs1;
	}
	public void setCs1(String cs1){
		this.cs1 = cs1;
	}
	//参数2
	public String getCs2() {
		return cs2;
	}
	public void setCs2(String cs2){
		this.cs2 = cs2;
	}
	//参数3
	public String getCs3() {
		return cs3;
	}
	public void setCs3(String cs3){
		this.cs3 = cs3;
	}
	//参数4
	public String getCs4() {
		return cs4;
	}
	public void setCs4(String cs4){
		this.cs4 = cs4;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
