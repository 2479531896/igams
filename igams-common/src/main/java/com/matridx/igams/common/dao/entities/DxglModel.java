package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="DxglModel")
public class DxglModel extends BaseModel{
	//发送账号
	private String fszh;
	//手机号
	private String sjh;
	//验证码
	private String yzm;
	//发送时间
	private String fssj;
	//发送次数
	private String fscs;


	public String getFszh() {
		return fszh;
	}

	public void setFszh(String fazh) {
		this.fszh = fazh;
	}

	public String getSjh() {
		return sjh;
	}

	public void setSjh(String sjh) {
		this.sjh = sjh;
	}

	public String getYzm() {
		return yzm;
	}

	public void setYzm(String yzm) {
		this.yzm = yzm;
	}

	public String getFssj() {
		return fssj;
	}

	public void setFssj(String fssj) {
		this.fssj = fssj;
	}

	public String getFscs() {
		return fscs;
	}

	public void setFscs(String fscs) {
		this.fscs = fscs;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
