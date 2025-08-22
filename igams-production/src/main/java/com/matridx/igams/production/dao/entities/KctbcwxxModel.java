package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="KctbcwxxModel")
public class KctbcwxxModel extends BaseBasicModel{
	//库存同步错误id
	private String kctbcwid;
	//物料编码
	private String wlbm;
	//提示信息
	private String tsxx;
	//生产批号
	private String scph;
	//仓库代码
	private String ckdm;
	//库位代码
	private String kwdm;
	//数量
	private String sl;
	//同步类型
	private String tblx;

	public String getKctbcwid() {
		return kctbcwid;
	}

	public void setKctbcwid(String kctbcwid) {
		this.kctbcwid = kctbcwid;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String getTsxx() {
		return tsxx;
	}

	public void setTsxx(String tsxx) {
		this.tsxx = tsxx;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getCkdm() {
		return ckdm;
	}

	public void setCkdm(String ckdm) {
		this.ckdm = ckdm;
	}

	public String getKwdm() {
		return kwdm;
	}

	public void setKwdm(String kwdm) {
		this.kwdm = kwdm;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getTblx() {
		return tblx;
	}

	public void setTblx(String tblx) {
		this.tblx = tblx;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
