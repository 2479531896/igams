package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value = "WkmxModel")
public class WkmxModel extends BaseModel {
	// 文库明细ID
	private String wkmxid;
	// 原始样本id
	private String ysybid;
	// 文库板X坐标
	private String wkxzb;
	// 文库版Y坐标
	private String wkyzb;
	// 八连管X坐标
	private String blgxzb;
	// 八连管Y坐标
	private String blgyzb;
	// pcr孔位
	private String pcrkw;
	//文库id
	private String wkid;
	//文库名称
	private String wkmc;
	//样本到稀释管时间
	private String ybdxsysj;
	//样本到荧光定量管时间
	private String ybdygdlsj;
	//样本混合时间
	private String ybhhsj;
	//quantity 数量
	private String quantity;
	//第一次pcr结果id
	private String dycpcrid;
	//第二次pcr结果id
	private String decpcrid;
	//核酸X坐标
	private String hsxzb;
	//核酸Y坐标
	private String hsyzb;
	//删除标记
	private String scbj;
	//删除时间
	private String scsj;
	//录入时间
	private String lrsj;
	//原液
	private String yy;
	//稀释液
	private String xsy;
	//样本编号
	private String nbbh;
	//文库原液加样体积
	private String wkyyjytj;
	//文库稀释液加样体积
	private String wkxsyjytj;
	
	
	
	public String getWkyyjytj() {
		return wkyyjytj;
	}



	public void setWkyyjytj(String wkyyjytj) {
		this.wkyyjytj = wkyyjytj;
	}



	public String getWkxsyjytj() {
		return wkxsyjytj;
	}



	public void setWkxsyjytj(String wkxsyjytj) {
		this.wkxsyjytj = wkxsyjytj;
	}



	public String getWkmxid() {
		return wkmxid;
	}



	public void setWkmxid(String wkmxid) {
		this.wkmxid = wkmxid;
	}



	public String getYsybid() {
		return ysybid;
	}



	public void setYsybid(String ysybid) {
		this.ysybid = ysybid;
	}



	public String getWkxzb() {
		return wkxzb;
	}



	public void setWkxzb(String wkxzb) {
		this.wkxzb = wkxzb;
	}



	public String getWkyzb() {
		return wkyzb;
	}



	public void setWkyzb(String wkyzb) {
		this.wkyzb = wkyzb;
	}



	public String getBlgxzb() {
		return blgxzb;
	}



	public void setBlgxzb(String blgxzb) {
		this.blgxzb = blgxzb;
	}



	public String getBlgyzb() {
		return blgyzb;
	}



	public void setBlgyzb(String blgyzb) {
		this.blgyzb = blgyzb;
	}



	public String getPcrkw() {
		return pcrkw;
	}



	public void setPcrkw(String pcrkw) {
		this.pcrkw = pcrkw;
	}



	public String getWkid() {
		return wkid;
	}



	public void setWkid(String wkid) {
		this.wkid = wkid;
	}



	public String getWkmc() {
		return wkmc;
	}



	public void setWkmc(String wkmc) {
		this.wkmc = wkmc;
	}



	public String getYbdxsysj() {
		return ybdxsysj;
	}



	public void setYbdxsysj(String ybdxsysj) {
		this.ybdxsysj = ybdxsysj;
	}



	public String getYbdygdlsj() {
		return ybdygdlsj;
	}



	public void setYbdygdlsj(String ybdygdlsj) {
		this.ybdygdlsj = ybdygdlsj;
	}



	public String getYbhhsj() {
		return ybhhsj;
	}



	public void setYbhhsj(String ybhhsj) {
		this.ybhhsj = ybhhsj;
	}



	public String getQuantity() {
		return quantity;
	}



	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}



	public String getDycpcrid() {
		return dycpcrid;
	}



	public void setDycpcrid(String dycpcrid) {
		this.dycpcrid = dycpcrid;
	}



	public String getDecpcrid() {
		return decpcrid;
	}



	public void setDecpcrid(String decpcrid) {
		this.decpcrid = decpcrid;
	}



	public String getHsxzb() {
		return hsxzb;
	}



	public void setHsxzb(String hsxzb) {
		this.hsxzb = hsxzb;
	}



	public String getHsyzb() {
		return hsyzb;
	}



	public void setHsyzb(String hsyzb) {
		this.hsyzb = hsyzb;
	}



	public String getScbj() {
		return scbj;
	}



	public void setScbj(String scbj) {
		this.scbj = scbj;
	}



	public String getScsj() {
		return scsj;
	}



	public void setScsj(String scsj) {
		this.scsj = scsj;
	}



	public String getLrsj() {
		return lrsj;
	}



	public void setLrsj(String lrsj) {
		this.lrsj = lrsj;
	}

	public String getYy() {
		return yy;
	}

	public void setYy(String yy) {
		this.yy = yy;
	}

	public String getXsy() {
		return xsy;
	}

	public void setXsy(String xsy) {
		this.xsy = xsy;
	}

	public String getNbbh() {
		return nbbh;
	}

	public void setNbbh(String nbbh) {
		this.nbbh = nbbh;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
