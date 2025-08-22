package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YhqtxxModel")
public class YhqtxxModel extends BaseModel{
	//用户id
	private String yhid;
	//上级ID
	private String sjid;
	//用户区分 基础数据
	private String yhqf;
	//用户子区分 基础数据 跟用户区分关联
	private String yhzqf;
	//收费权限
	private String sfqx;
	//验证码
	private String yzm;
	//验证时间
	private String yzsj;
	//外部程序id
	private String wbcxid;
	//职级
	private String zj;

	public String getZj() {
		return zj;
	}

	public void setZj(String zj) {
		this.zj = zj;
	}

	public String getWbcxid() {
		return wbcxid;
	}

	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}

	public String getYzm() {
		return yzm;
	}
	public void setYzm(String yzm) {
		this.yzm = yzm;
	}
	public String getYzsj() {
		return yzsj;
	}
	public void setYzsj(String yzsj) {
		this.yzsj = yzsj;
	}
	public String getSfqx() {
		return sfqx;
	}
	public void setSfqx(String sfqx) {
		this.sfqx = sfqx;
	}
	//用户id
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid){
		this.yhid = yhid;
	}
	//上级ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//用户区分 基础数据
	public String getYhqf() {
		return yhqf;
	}
	public void setYhqf(String yhqf){
		this.yhqf = yhqf;
	}
	//用户子区分 基础数据 跟用户区分关联
	public String getYhzqf() {
		return yhzqf;
	}
	public void setYhzqf(String yhzqf){
		this.yhzqf = yhzqf;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
