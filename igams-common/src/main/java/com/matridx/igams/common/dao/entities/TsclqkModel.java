package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="TsclqkModel")
public class TsclqkModel extends BaseModel{

	//投诉处理情况ID
	private String tsclqkid;
	//质量投诉ID
	private String zltsid;
	//类型
	private String lx;
	//部门ID
	private String bmid;
	//处理内容
	private String clnr;
	//是否处理
	private String sfcl;
	//处理时间
	private String clsj;
	//处理人员
	private String clry;
	//要求
	private String yq;

	public String getYq() {
		return yq;
	}

	public void setYq(String yq) {
		this.yq = yq;
	}

	public String getClry() {
		return clry;
	}

	public void setClry(String clry) {
		this.clry = clry;
	}

	public String getTsclqkid() {
		return tsclqkid;
	}

	public void setTsclqkid(String tsclqkid) {
		this.tsclqkid = tsclqkid;
	}

	public String getZltsid() {
		return zltsid;
	}

	public void setZltsid(String zltsid) {
		this.zltsid = zltsid;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getBmid() {
		return bmid;
	}

	public void setBmid(String bmid) {
		this.bmid = bmid;
	}

	public String getClnr() {
		return clnr;
	}

	public void setClnr(String clnr) {
		this.clnr = clnr;
	}

	public String getSfcl() {
		return sfcl;
	}

	public void setSfcl(String sfcl) {
		this.sfcl = sfcl;
	}

	public String getClsj() {
		return clsj;
	}

	public void setClsj(String clsj) {
		this.clsj = clsj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
