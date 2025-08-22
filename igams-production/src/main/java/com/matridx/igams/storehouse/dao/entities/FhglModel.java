package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="FhglModel")
public class FhglModel extends BaseBasicModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//发货id
	private String fhid;
	//发货单号
	private String fhdh;
	//经手人
	private String jsr;
	//销售类型
	private String xslx;
	//发货日期
	private String djrq;
	//销售部门
	private String xsbm;
	//订单号
	private String ddh;
	//客户
	private String kh;
	//u8
	private String u8fhdh;
	//收货地址
	private String shdz;
	//收发标记 0 收   1 发
	private String sfbj;
	//备注
	private String bz;
	//运送方式
	private String ysfs;
	//出库id
	private String ckid;
	//税率
	private String suil;
	//汇率
	private String hl;
	//状态
	private String zt;
	//关联id
	private String glid;
	//关联发货id
	private String glfhid;
	//币种
	private String biz;
	//快递信息
	private String kdxx;
	//是否确认
	private String sfqr;
	//审核人员
	private String shry;
	//审核时间
	private String shsj;

	public String getShry() {
		return shry;
	}

	public void setShry(String shry) {
		this.shry = shry;
	}

	public String getShsj() {
		return shsj;
	}

	public void setShsj(String shsj) {
		this.shsj = shsj;
	}

	public String getKdxx() {
		return kdxx;
	}

	public void setKdxx(String kdxx) {
		this.kdxx = kdxx;
	}

	public String getSfqr() {
		return sfqr;
	}

	public void setSfqr(String sfqr) {
		this.sfqr = sfqr;
	}

	public String getU8fhdh() {
		return u8fhdh;
	}

	public void setU8fhdh(String u8fhdh) {
		this.u8fhdh = u8fhdh;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getBiz() {
		return biz;
	}

	public void setBiz(String biz) {
		this.biz = biz;
	}

	public String getGlfhid() {
		return glfhid;
	}

	public void setGlfhid(String glfhid) {
		this.glfhid = glfhid;
	}

	public String getGlid() {
		return glid;
	}

	public void setGlid(String glid) {
		this.glid = glid;
	}

	public String getHl() {
		return hl;
	}

	public void setHl(String hl) {
		this.hl = hl;
	}

	public String getSuil() {
		return suil;
	}

	public void setSuil(String suil) {
		this.suil = suil;
	}

	public String getCkid() {
		return ckid;
	}

	public void setCkid(String ckid) {
		this.ckid = ckid;
	}

	public String getDjrq() {
        return djrq;
    }
	
    public void setDjrq(String djrq) {
        this.djrq = djrq;
    }
	public String getYsfs() {
		return ysfs;
	}

	public void setYsfs(String ysfs) {
		this.ysfs = ysfs;
	}

	public String getFhid() {
		return fhid;
	}
	public void setFhid(String fhid) {
		this.fhid = fhid;
	}
	public String getFhdh() {
		return fhdh;
	}
	public void setFhdh(String fhdh) {
		this.fhdh = fhdh;
	}

	public String getJsr() {
		return jsr;
	}
	public void setJsr(String jsr) {
		this.jsr = jsr;
	}
	public String getXslx() {
		return xslx;
	}
	public void setXslx(String xslx) {
		this.xslx = xslx;
	}
	public String getXsbm() {
		return xsbm;
	}
	public void setXsbm(String xsbm) {
		this.xsbm = xsbm;
	}
	public String getDdh() {
		return ddh;
	}
	public void setDdh(String ddh) {
		this.ddh = ddh;
	}
	public String getKh() {
		return kh;
	}
	public void setKh(String kh) {
		this.kh = kh;
	}
	public String getShdz() {
		return shdz;
	}
	public void setShdz(String shdz) {
		this.shdz = shdz;
	}
	public String getSfbj() {
		return sfbj;
	}
	public void setSfbj(String sfbj) {
		this.sfbj = sfbj;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	
	
}
