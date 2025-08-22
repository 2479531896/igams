package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WkglModel")
public class WkglModel extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//文库ID
	private String wkid;
	//文库名称
	private String wkmc;
	//稀释液批号
	private String xsyph;
	//扩增液批号
	private String kzyph;
	//标准液批号1
	private String bzyph1;
	//标准液批号2
	private String bzyph2;
	//标准液批号3
	private String bzyph3;
	//标准液批号4
	private String bzyph4;
	//标准液批号5
	private String bzyph5;
	//变性液
	private String bxy;
	//平衡混合液（平衡液Ⅰ：平衡液Ⅱ=1：9）加入量（ul）
	private String phhhy;
	//杂交缓冲液体积（ul）
	private String zjhcytj;
	//是否已导入
	private String sfdr;
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
	public String getXsyph() {
		return xsyph;
	}
	public void setXsyph(String xsyph) {
		this.xsyph = xsyph;
	}
	public String getKzyph() {
		return kzyph;
	}
	public void setKzyph(String kzyph) {
		this.kzyph = kzyph;
	}
	public String getBzyph1() {
		return bzyph1;
	}
	public void setBzyph1(String bzyph1) {
		this.bzyph1 = bzyph1;
	}
	public String getBzyph2() {
		return bzyph2;
	}
	public void setBzyph2(String bzyph2) {
		this.bzyph2 = bzyph2;
	}
	public String getBzyph3() {
		return bzyph3;
	}
	public void setBzyph3(String bzyph3) {
		this.bzyph3 = bzyph3;
	}
	public String getBzyph4() {
		return bzyph4;
	}
	public void setBzyph4(String bzyph4) {
		this.bzyph4 = bzyph4;
	}
	public String getBzyph5() {
		return bzyph5;
	}
	public void setBzyph5(String bzyph5) {
		this.bzyph5 = bzyph5;
	}
	public String getBxy() {
		return bxy;
	}
	public void setBxy(String bxy) {
		this.bxy = bxy;
	}
	public String getPhhhy() {
		return phhhy;
	}
	public void setPhhhy(String phhhy) {
		this.phhhy = phhhy;
	}
	public String getZjhcytj() {
		return zjhcytj;
	}
	public void setZjhcytj(String zjhcytj) {
		this.zjhcytj = zjhcytj;
	}

	public String getSfdr() {
		return sfdr;
	}

	public void setSfdr(String sfdr) {
		this.sfdr = sfdr;
	}
}
