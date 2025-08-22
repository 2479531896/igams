package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="SbpdmxModel")
public class SbpdmxModel extends BaseModel {

	//设备盘点ID
	private String sbpdid;
	//设备盘点明细ID
	private String pdmxid;
	//设备验收ID
	private String sbysid;
	//盘点状态
	private String pdzt;
	//备注
	private String bz;

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getSbpdid() {
		return sbpdid;
	}

	public void setSbpdid(String sbpdid) {
		this.sbpdid = sbpdid;
	}

	public String getPdmxid() {
		return pdmxid;
	}

	public void setPdmxid(String pdmxid) {
		this.pdmxid = pdmxid;
	}

	public String getSbysid() {
		return sbysid;
	}

	public void setSbysid(String sbysid) {
		this.sbysid = sbysid;
	}

	public String getPdzt() {
		return pdzt;
	}

	public void setPdzt(String pdzt) {
		this.pdzt = pdzt;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	
}
