package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="XzrkglModel")
public class XzrkglModel extends BaseBasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//行政入库ID
	private String xzrkid;
	//入库日期
	private String rkrq;
	//入库单号
	private String rkdh;
	//入库人员
	private String rkry;
	//状态 00：未提交  10：审核中
	private String zt;
	//备注
	private String bz;
	//库位ID
	private String kwid;
	//入库类别
	private String rklb;

	public String getRklb() {
		return rklb;
	}

	public void setRklb(String rklb) {
		this.rklb = rklb;
	}

	public String getKwid() {
		return kwid;
	}

	public void setKwid(String kwid) {
		this.kwid = kwid;
	}

	public String getXzrkid() {
		return xzrkid;
	}

	public void setXzrkid(String xzrkid) {
		this.xzrkid = xzrkid;
	}

	public String getRkrq() {
		return rkrq;
	}

	public void setRkrq(String rkrq) {
		this.rkrq = rkrq;
	}

	public String getRkdh() {
		return rkdh;
	}

	public void setRkdh(String rkdh) {
		this.rkdh = rkdh;
	}

	public String getRkry() {
		return rkry;
	}

	public void setRkry(String rkry) {
		this.rkry = rkry;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

}
