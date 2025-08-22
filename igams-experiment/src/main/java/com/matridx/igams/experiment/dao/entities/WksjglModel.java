package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WksjglModel")
public class WksjglModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//文库上机ID
	private String wksjid;
	//文库ID
	private String wkid;
	//测序仪
	private String cxy;
	//上机时间
	private String sjsj;
	//芯片ID
	private String xpid;
	//芯片名称
	private String xpmc;
	//下机时间
	private String xjsj;
	//预计下机时间
	private String yjxjsj;
	//服务器
	private String fwq;
	//所属实验室
	private String sssys;
	//芯片类型
	private String chiptype;

	public String getXpid() {
		return xpid;
	}

	public void setXpid(String xpid) {
		this.xpid = xpid;
	}

	public String getXpmc() {
		return xpmc;
	}

	public void setXpmc(String xpmc) {
		this.xpmc = xpmc;
	}

	public String getXjsj() {
		return xjsj;
	}

	public void setXjsj(String xjsj) {
		this.xjsj = xjsj;
	}

	public String getYjxjsj() {
		return yjxjsj;
	}

	public void setYjxjsj(String yjxjsj) {
		this.yjxjsj = yjxjsj;
	}

	public String getFwq() {
		return fwq;
	}

	public void setFwq(String fwq) {
		this.fwq = fwq;
	}

	public String getSssys() {
		return sssys;
	}

	public void setSssys(String sssys) {
		this.sssys = sssys;
	}

	public String getWksjid() {
		return wksjid;
	}
	public void setWksjid(String wksjid) {
		this.wksjid = wksjid;
	}
	public String getWkid() {
		return wkid;
	}
	public void setWkid(String wkid) {
		this.wkid = wkid;
	}
	public String getCxy() {
		return cxy;
	}
	public void setCxy(String cxy) {
		this.cxy = cxy;
	}
	public String getSjsj() {
		return sjsj;
	}
	public void setSjsj(String sjsj) {
		this.sjsj = sjsj;
	}

	public String getChiptype() {
		return chiptype;
	}

	public void setChiptype(String chiptype) {
		this.chiptype = chiptype;
	}
}
