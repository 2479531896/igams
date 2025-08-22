package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="XzkcxxModel")
public class XzkcxxModel extends BaseBasicModel {
	//行政库存ID
	private String xzkcid;
	//请购明细ID
	private String qgmxid;
	//库存量
	private String kcl;
	//预定数
	private String yds;
	//库位ID
	private String kwid;
	//生产商
	private String scs;
	//安全库存
	private String aqkc;

	public String getAqkc() {
		return aqkc;
	}

	public void setAqkc(String aqkc) {
		this.aqkc = aqkc;
	}

	public String getXzkcid() {
		return xzkcid;
	}

	public void setXzkcid(String xzkcid) {
		this.xzkcid = xzkcid;
	}

	public String getQgmxid() {
		return qgmxid;
	}

	public void setQgmxid(String qgmxid) {
		this.qgmxid = qgmxid;
	}

	public String getKcl() {
		return kcl;
	}

	public void setKcl(String kcl) {
		this.kcl = kcl;
	}

	public String getYds() {
		return yds;
	}

	public void setYds(String yds) {
		this.yds = yds;
	}

	public String getKwid() {
		return kwid;
	}

	public void setKwid(String kwid) {
		this.kwid = kwid;
	}

	public String getScs() {
		return scs;
	}

	public void setScs(String scs) {
		this.scs = scs;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
