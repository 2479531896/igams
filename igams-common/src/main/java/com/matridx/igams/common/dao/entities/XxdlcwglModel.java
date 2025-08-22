package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XxdlcwglModel")
public class XxdlcwglModel extends BaseModel{
	//错误ID
	private String cwid;
	//错误类型 保存消息queues名
	private String cwlx;
	//错误时间
	private String cwsj;
	//原始内容
	private String ysnr;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
