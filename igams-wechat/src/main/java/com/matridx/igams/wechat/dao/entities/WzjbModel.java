package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WzjbModel")
public class WzjbModel extends BaseModel{
	//级别id
	private String jbid;
	//物种id
	private String wzid;
	//传染病级别
	private String crbjb;
	//是否拦截
	private String sflj;
	//新增时间
	private String xzsj;
	//新增人
	private String xzr;
	//修改人
	private String xgr;
	//级别id
	public String getJbid() {
		return jbid;
	}
	public void setJbid(String jbid){
		this.jbid = jbid;
	}
	//物种id
	public String getWzid() {
		return wzid;
	}
	public void setWzid(String wzid){
		this.wzid = wzid;
	}
	//传染病级别
	public String getCrbjb() {
		return crbjb;
	}
	public void setCrbjb(String crbjb){
		this.crbjb = crbjb;
	}
	//是否拦截
	public String getSflj() {
		return sflj;
	}
	public void setSflj(String sflj){
		this.sflj = sflj;
	}
	//新增时间
	public String getXzsj() {
		return xzsj;
	}
	public void setXzsj(String xzsj){
		this.xzsj = xzsj;
	}
	//新增人
	public String getXzr() {
		return xzr;
	}
	public void setXzr(String xzr){
		this.xzr = xzr;
	}
	//修改人
	public String getXgr() {
		return xgr;
	}
	public void setXgr(String xgr){
		this.xgr = xgr;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
