package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="SbpdModel")
public class SbpdModel extends BaseBasicModel {

	//设备盘点ID
	private String sbpdid;
	//发起时间
	private String fqsj;
	//盘点人员
	private String pdry;
	//发起人员
	private String fqry;
	//部门
	private String bm;
	//状态
	private String zt;

	public String getSbpdid() {
		return sbpdid;
	}

	public void setSbpdid(String sbpdid) {
		this.sbpdid = sbpdid;
	}

	public String getFqsj() {
		return fqsj;
	}

	public void setFqsj(String fqsj) {
		this.fqsj = fqsj;
	}

	public String getPdry() {
		return pdry;
	}

	public void setPdry(String pdry) {
		this.pdry = pdry;
	}

	public String getFqry() {
		return fqry;
	}

	public void setFqry(String fqry) {
		this.fqry = fqry;
	}

	public String getBm() {
		return bm;
	}

	public void setBm(String bm) {
		this.bm = bm;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	
}
