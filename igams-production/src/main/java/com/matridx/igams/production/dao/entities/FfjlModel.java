package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="FfjlModel")
public class FfjlModel extends BaseModel{
	private String ffjlid;	//发放记录id
	private String bm;		//部门
	private String fffs;	//发放份数
	private String sfff;	//是否发放
	private String ffrq;	//发放日期
	private String wjid;	//文件id

	public String getFfjlid() {
		return ffjlid;
	}

	public void setFfjlid(String ffjlid) {
		this.ffjlid = ffjlid;
	}

	public String getBm() {
		return bm;
	}

	public void setBm(String bm) {
		this.bm = bm;
	}

	public String getFffs() {
		return fffs;
	}

	public void setFffs(String fffs) {
		this.fffs = fffs;
	}

	public String getSfff() {
		return sfff;
	}

	public void setSfff(String sfff) {
		this.sfff = sfff;
	}

	public String getFfrq() {
		return ffrq;
	}

	public void setFfrq(String ffrq) {
		this.ffrq = ffrq;
	}

	public String getWjid() {
		return wjid;
	}

	public void setWjid(String wjid) {
		this.wjid = wjid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
