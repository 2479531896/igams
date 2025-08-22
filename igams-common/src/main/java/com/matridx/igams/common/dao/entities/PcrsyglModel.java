package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="PcrsyglModel")
public class PcrsyglModel extends BaseModel{
	//pcrsyglid
	private String pcrsyglid;
	//检测单位
	private String jcdw;
	//实验时间
	private String sysj;
	//验证/测试
	private String isyz;

	public String getSysj() {
		return sysj;
	}

	public void setSysj(String sysj) {
		this.sysj = sysj;
	}

	public String getPcrsyglid() {
		return pcrsyglid;
	}

	public void setPcrsyglid(String pcrsyglid) {
		this.pcrsyglid = pcrsyglid;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getIsyz() {
		return isyz;
	}

	public void setIsyz(String isyz) {
		this.isyz = isyz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
