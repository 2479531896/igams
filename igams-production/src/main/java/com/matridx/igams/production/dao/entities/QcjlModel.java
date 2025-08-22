package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="QcjlModel")
public class QcjlModel extends BaseBasicModel {

	//清场记录ID
	private String qcjlid;
	//房间名称
	private String fjmc;
	//所属工序
	private String ssgx;
	//记录编号
	private String jlbh;
	//物料ID
	private String wlid;
	//批号
	private String ph;
	//操作人
	private String czr;
	//清场日期
	private String qcrq;
	//检查总评
	private String jczp;
	//检查人
	private String jcr;
	//备注
	private String bz;
	//有效期
	private String yxq;
	//状态
	private String zt;

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getQcjlid() {
		return qcjlid;
	}

	public void setQcjlid(String qcjlid) {
		this.qcjlid = qcjlid;
	}

	public String getFjmc() {
		return fjmc;
	}

	public void setFjmc(String fjmc) {
		this.fjmc = fjmc;
	}

	public String getSsgx() {
		return ssgx;
	}

	public void setSsgx(String ssgx) {
		this.ssgx = ssgx;
	}

	public String getJlbh() {
		return jlbh;
	}

	public void setJlbh(String jlbh) {
		this.jlbh = jlbh;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getPh() {
		return ph;
	}

	public void setPh(String ph) {
		this.ph = ph;
	}

	public String getCzr() {
		return czr;
	}

	public void setCzr(String czr) {
		this.czr = czr;
	}

	public String getQcrq() {
		return qcrq;
	}

	public void setQcrq(String qcrq) {
		this.qcrq = qcrq;
	}

	public String getJczp() {
		return jczp;
	}

	public void setJczp(String jczp) {
		this.jczp = jczp;
	}

	public String getJcr() {
		return jcr;
	}

	public void setJcr(String jcr) {
		this.jcr = jcr;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getYxq() {
		return yxq;
	}

	public void setYxq(String yxq) {
		this.yxq = yxq;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
