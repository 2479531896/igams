package com.matridx.las.netty.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="ProcessconfigModel")
public class ProcessconfigModel extends BaseModel{

	private String lclxid;
	private String csid;
	private String xh;
	private String yqlx;
	private String tsbz ;
	private String csdm ;

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public String getLclxid() {
		return lclxid;
	}

	public void setLclxid(String lclxid) {
		this.lclxid = lclxid;
	}

	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getYqlx() {
		return yqlx;
	}

	public void setYqlx(String yqlx) {
		this.yqlx = yqlx;
	}

	public String getTsbz() {
		return tsbz;
	}

	public void setTsbz(String tsbz) {
		this.tsbz = tsbz;
	}

	public String getCsdm() {
		return csdm;
	}

	public void setCsdm(String csdm) {
		this.csdm = csdm;
	}
}
