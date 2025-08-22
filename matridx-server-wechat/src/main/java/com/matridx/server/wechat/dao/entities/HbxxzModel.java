package com.matridx.server.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="HbxxzModel")
public class HbxxzModel extends BaseModel{
	//伙伴x限制ID
	private String hbxxzid;
	//伙伴ID
	private String hbid;
	//实验室ID
	private String sysid;
	//检测项目kscz3
	private String jcxmcskz3;
	//限制类型
	private String xzlx;

	public String getHbxxzid() {
		return hbxxzid;
	}

	public void setHbxxzid(String hbxxzid) {
		this.hbxxzid = hbxxzid;
	}

	public String getJcxmcskz3() {
		return jcxmcskz3;
	}

	public void setJcxmcskz3(String jcxmcskz3) {
		this.jcxmcskz3 = jcxmcskz3;
	}

	public String getXzlx() {
		return xzlx;
	}

	public void setXzlx(String xzlx) {
		this.xzlx = xzlx;
	}

	//伙伴ID
	public String getHbid() {
		return hbid;
	}
	public void setHbid(String hbid){
		this.hbid = hbid;
	}
	//实验室ID
	public String getSysid() {
		return sysid;
	}
	public void setSysid(String sysid){
		this.sysid = sysid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
