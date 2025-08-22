package com.matridx.igams.storehouse.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="HwllxxModel")
public class HwllxxModel extends BaseModel{
	//货物领料ID
	private String hwllid;
	//领料ID
	private String llid;
	//物料ID
	private String wlid;
	//请领数量
	private String qlsl;
	//实领数量
	private String slsl;
	//领料人员
	private String llry;
	//发料人员
	private String flry;
	//发料日期
	private String flrq;
	//备注
	private String bz;
	//状态 00:未提交(审核状态)
	private String zt;
	//项目大类
	private String xmdl;
	//项目编码
	private String xmbm;
	//货物领料明细
	private List<HwxxDto> hwxxDtos;
	//允许请领数量
	private String yxsl;
	//序号
	private String xh;
	//产品类型
	private String cplx;

	public String getCplx() {
		return cplx;
	}

	public void setCplx(String cplx) {
		this.cplx = cplx;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getYxsl() {
		return yxsl;
	}


	public void setYxsl(String yxsl) {
		this.yxsl = yxsl;
	}


	public List<HwxxDto> getHwxxDtos() {
		return hwxxDtos;
	}


	public void setHwxxDtos(List<HwxxDto> hwxxDtos) {
		this.hwxxDtos = hwxxDtos;
	}


	public String getXmdl() {
		return xmdl;
	}


	public void setXmdl(String xmdl) {
		this.xmdl = xmdl;
	}


	public String getXmbm() {
		return xmbm;
	}


	public String getHwllid() {
		return hwllid;
	}


	public void setHwllid(String hwllid) {
		this.hwllid = hwllid;
	}


	public String getLlid() {
		return llid;
	}


	public void setLlid(String llid) {
		this.llid = llid;
	}


	public String getWlid() {
		return wlid;
	}


	public void setWlid(String wlid) {
		this.wlid = wlid;
	}


	public String getQlsl() {
		return qlsl;
	}


	public void setQlsl(String qlsl) {
		this.qlsl = qlsl;
	}


	public String getSlsl() {
		return slsl;
	}


	public void setSlsl(String slsl) {
		this.slsl = slsl;
	}


	public String getLlry() {
		return llry;
	}


	public void setLlry(String llry) {
		this.llry = llry;
	}


	public String getFlry() {
		return flry;
	}


	public void setFlry(String flry) {
		this.flry = flry;
	}


	public String getFlrq() {
		return flrq;
	}


	public void setFlrq(String flrq) {
		this.flrq = flrq;
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





	public void setXmbm(String xmbm) {
		this.xmbm = xmbm;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
