package com.matridx.las.netty.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "FrontMaterialModel")
public class FrontMaterialModel extends BaseModel {
	private String tcsjph;
	private String zbf;
	private String jkysjph;//
	private String traynum;//托盘编号  ybtpwzxh
	private String nbbh;//样本内部编号
	private String cassettePosition;//样本卡盒所在托盘里的编号 tpnwzxh
	private String ysybid;//原始样本id
	private String jth;
	//执行时，将托盘号传给后端
	private String trays;
	private String userId;

	public String getTcsjph() {
		return tcsjph;
	}

	public void setTcsjph(String tcsjph) {
		this.tcsjph = tcsjph;
	}

	public String getZbf() {
		return zbf;
	}

	public void setZbf(String zbf) {
		this.zbf = zbf;
	}

	public String getJkysjph() {
		return jkysjph;
	}

	public void setJkysjph(String jkysjph) {
		this.jkysjph = jkysjph;
	}

	public String getTraynum() {
		return traynum;
	}

	public void setTraynum(String traynum) {
		this.traynum = traynum;
	}

	public String getNbbh() {
		return nbbh;
	}

	public void setNbbh(String nbbh) {
		this.nbbh = nbbh;
	}

	public String getCassettePosition() {
		return cassettePosition;
	}

	public void setCassettePosition(String cassettePosition) {
		this.cassettePosition = cassettePosition;
	}

	public String getTrays() {
		return trays;
	}

	public void setTrays(String trays) {
		this.trays = trays;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getYsybid() {
		return ysybid;
	}

	public void setYsybid(String ysybid) {
		this.ysybid = ysybid;
	}

	public String getJth() {
		return jth;
	}

	public void setJth(String jth) {
		this.jth = jth;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
