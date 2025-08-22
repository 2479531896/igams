package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.Model;

@Alias(value="LsbModel")
public class LsbModel extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2673208880152988010L;
	
	private String cxid;
	private String nr;
	
	public String getCxid() {
		return cxid;
	}
	public void setCxid(String cxid) {
		this.cxid = cxid;
	}
	public String getNr() {
		return nr;
	}
	public void setNr(String nr) {
		this.nr = nr;
	}
}
