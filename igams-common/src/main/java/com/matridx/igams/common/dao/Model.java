package com.matridx.igams.common.dao;

import java.io.Serializable;
import com.matridx.igams.common.enums.DelFlagEnum;

public class Model implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2348471938043883394L;

	//录入人员
	private String lrry;
	//录入时间
	private String lrsj;
	//修改人员
	private String xgry;
	//修改时间
	private String xgsj;
	//删除人员
	private String scry;
	//删除时间
	private String scsj;
	//删除标记
	private String scbj;

	// 删除标记名称
	private String scbjmc;

	public String getLrry() {
		return lrry;
	}
	public void setLrry(String lrry) {
		this.lrry = lrry;
	}
	public String getLrsj() {
		return lrsj;
	}
	public void setLrsj(String lrsj) {
		this.lrsj = lrsj;
	}
	public String getXgry() {
		return xgry;
	}
	public void setXgry(String xgry) {
		this.xgry = xgry;
	}
	public String getXgsj() {
		return xgsj;
	}
	public void setXgsj(String xgsj) {
		this.xgsj = xgsj;
	}
	public String getScry() {
		return scry;
	}
	public void setScry(String scry) {
		this.scry = scry;
	}
	public String getScsj() {
		return scsj;
	}
	public void setScsj(String scsj) {
		this.scsj = scsj;
	}
	public String getScbj() {
		return scbj;
	}
	public void setScbj(String scbj) {
		this.scbj = scbj;
		this.setScbjmc(DelFlagEnum.getValueByCode(scbj));
	}
	public String getScbjmc() {
		return scbjmc;
	}
	public void setScbjmc(String scbjmc) {
		this.scbjmc = scbjmc;
	}
}
