package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="QtckglModel")
public class QtckglModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//其他出库id
	private String qtckid;
	//出库单号
	private String ckdh;
	//物料编码
	private String wlbm;
	//数量
	private String sl;
	//生产批号
	private String scph;
	//仓库id
	private String ckid;
	//库位
	private String kw;
	//关联id
	private String glid;
	//关联明细id
	private String glmxid;

	public String getQtckid() {
		return qtckid;
	}

	public void setQtckid(String qtckid) {
		this.qtckid = qtckid;
	}

	public String getCkdh() {
		return ckdh;
	}

	public void setCkdh(String ckdh) {
		this.ckdh = ckdh;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getCkid() {
		return ckid;
	}

	public void setCkid(String ckid) {
		this.ckid = ckid;
	}

	public String getKw() {
		return kw;
	}

	public void setKw(String kw) {
		this.kw = kw;
	}

	public String getGlid() {
		return glid;
	}

	public void setGlid(String glid) {
		this.glid = glid;
	}

	public String getGlmxid() {
		return glmxid;
	}

	public void setGlmxid(String glmxid) {
		this.glmxid = glmxid;
	}
}
