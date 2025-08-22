package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;


@Alias(value="CkglModel")
public class CkglModel extends BaseBasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//出库id
	private String ckid;
	//出库单号
	private String ckdh;
	//部门
	private String bm;
	//单据类型
	private String djlx;
	//领料人
	private String llr;
	//发料人
	private String flr;
	//出库日期
	private String ckrq;
	//仓库
	private String ck;
	//关联id
	private String glid;
	//出库类别
	private String cklb;
	//备注
	private String bz;
	//状态
	private String zt;
	//领料id
	private String llid;
	//借出借用id
	private String jcjyid;
	//红冲关联id
	private String hcglid;
	//U8红冲单号
	private String U8hcdh;
	//U8出库单号
	private String u8ckdh;
	//记录编号
	private String jlbh;
	//发货id
	private String fhid;

	public String getFhid() {
		return fhid;
	}

	public void setFhid(String fhid) {
		this.fhid = fhid;
	}

	public String getDjlx() {
		return djlx;
	}

	public void setDjlx(String djlx) {
		this.djlx = djlx;
	}

	public String getJlbh() {
		return jlbh;
	}

	public void setJlbh(String jlbh) {
		this.jlbh = jlbh;
	}

	public String getU8ckdh() {
		return u8ckdh;
	}

	public void setU8ckdh(String u8ckdh) {
		this.u8ckdh = u8ckdh;
	}

	public String getU8hcdh() {
		return U8hcdh;
	}

	public void setU8hcdh(String u8hcdh) {
		U8hcdh = u8hcdh;
	}

	public String getHcglid() {
		return hcglid;
	}

	public void setHcglid(String hcglid) {
		this.hcglid = hcglid;
	}

	public String getJcjyid() {
		return jcjyid;
	}

	public void setJcjyid(String jcjyid) {
		this.jcjyid = jcjyid;
	}

	public String getLlid() {
		return llid;
	}
	public void setLlid(String llid) {
		this.llid = llid;
	}
	public String getCkid() {
		return ckid;
	}
	public void setCkid(String ckid) {
		this.ckid = ckid;
	}
	public String getCkdh() {
		return ckdh;
	}
	public void setCkdh(String ckdh) {
		this.ckdh = ckdh;
	}
	public String getBm() {
		return bm;
	}
	public void setBm(String bm) {
		this.bm = bm;
	}
	public String getLlr() {
		return llr;
	}
	public void setLlr(String llr) {
		this.llr = llr;
	}
	public String getFlr() {
		return flr;
	}
	public void setFlr(String flr) {
		this.flr = flr;
	}
	public String getCkrq() {
		return ckrq;
	}
	public void setCkrq(String ckrq) {
		this.ckrq = ckrq;
	}
	public String getCk() {
		return ck;
	}
	public void setCk(String ck) {
		this.ck = ck;
	}
	public String getGlid() {
		return glid;
	}
	public void setGlid(String glid) {
		this.glid = glid;
	}
	public String getCklb() {
		return cklb;
	}
	public void setCklb(String cklb) {
		this.cklb = cklb;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
}
