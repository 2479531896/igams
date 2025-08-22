package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;
import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="CwglModel")
public class CwglModel extends BaseBasicModel{
	//错误ID
	private String cwid;
	//错误类型 保存消息queues名
	private String cwlx;
	//错误时间
	private String cwsj;
	//原始内容
	private String ysnr;
	//是否处理
	private String sfcl;
	//处理时间
	private String clsj;
	//状态
	private String zt;
	
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	//错误ID
	public String getCwid() {
		return cwid;
	}
	public void setCwid(String cwid){
		this.cwid = cwid;
	}
	//错误类型 保存消息queues名
	public String getCwlx() {
		return cwlx;
	}
	public void setCwlx(String cwlx){
		this.cwlx = cwlx;
	}
	//错误时间
	public String getCwsj() {
		return cwsj;
	}
	public void setCwsj(String cwsj){
		this.cwsj = cwsj;
	}
	//原始内容
	public String getYsnr() {
		return ysnr;
	}
	public void setYsnr(String ysnr){
		this.ysnr = ysnr;
	}
	//是否处理
	public String getSfcl() {
		return sfcl;
	}
	public void setSfcl(String sfcl){
		this.sfcl = sfcl;
	}
	//处理时间
	public String getClsj() {
		return clsj;
	}
	public void setClsj(String clsj){
		this.clsj = clsj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
