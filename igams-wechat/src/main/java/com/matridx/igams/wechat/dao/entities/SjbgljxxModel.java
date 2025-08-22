package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjbgljxxModel")
public class SjbgljxxModel extends BaseModel{
	//拦截id
	private String ljid;
	//送检id
	private String sjid;
	//物种id
	private String wzid;
	//结果类型
	private String jglx;
	//物种父类id
	private String fid;
	//报告日期
	private String bgrq;
	//是否拦截
	private String sflj;
	//发送时间
	private String fssj;
	//调用人
	private String dyr;
	//拦截id
	public String getLjid() {
		return ljid;
	}
	public void setLjid(String ljid){
		this.ljid = ljid;
	}
	//送检id
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//物种id
	public String getWzid() {
		return wzid;
	}
	public void setWzid(String wzid){
		this.wzid = wzid;
	}
	//结果类型
	public String getJglx() {
		return jglx;
	}
	public void setJglx(String jglx){
		this.jglx = jglx;
	}
	//物种父类id
	public String getFid() {
		return fid;
	}
	public void setFid(String fid){
		this.fid = fid;
	}
	//报告日期
	public String getBgrq() {
		return bgrq;
	}
	public void setBgrq(String bgrq){
		this.bgrq = bgrq;
	}
	//是否拦截
	public String getSflj() {
		return sflj;
	}
	public void setSflj(String sflj){
		this.sflj = sflj;
	}
	//发送时间
	public String getFssj() {
		return fssj;
	}
	public void setFssj(String fssj){
		this.fssj = fssj;
	}
	//调用人
	public String getDyr() {
		return dyr;
	}
	public void setDyr(String dyr){
		this.dyr = dyr;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
