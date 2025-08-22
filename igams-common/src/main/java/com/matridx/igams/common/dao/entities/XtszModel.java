package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XtszModel")
public class XtszModel extends BaseModel{
	//设置类别
	private String szlb;
	//设置名称
	private String szmc;
	//设置值
	private String szz;
	//序号
	private String xh;
	//备注
	private String bz;
	//显示类型  00：普通输入框  01：带单位的输入框  02：单选框
	private String xslx;
	//扩展参数
	private String kzcs1;
	//扩展参数
	private String kzcs2;
	//扩展参数
	private String kzcs3;
	//扩展参数
	private String kzcs4;
	
	//设置类别
	public String getSzlb() {
		return szlb;
	}
	public void setSzlb(String szlb){
		this.szlb = szlb;
	}
	//设置名称
	public String getSzmc() {
		return szmc;
	}
	public void setSzmc(String szmc){
		this.szmc = szmc;
	}
	//设置值
	public String getSzz() {
		return szz;
	}
	public void setSzz(String szz){
		this.szz = szz;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//显示类型  00：普通输入框  01：带单位的输入框  02：单选框
	public String getXslx() {
		return xslx;
	}
	public void setXslx(String xslx){
		this.xslx = xslx;
	}
	//扩展参数
	public String getKzcs1() {
		return kzcs1;
	}
	public void setKzcs1(String kzcs1){
		this.kzcs1 = kzcs1;
	}
	//扩展参数
	public String getKzcs2() {
		return kzcs2;
	}
	public void setKzcs2(String kzcs2){
		this.kzcs2 = kzcs2;
	}
	//扩展参数
	public String getKzcs3() {
		return kzcs3;
	}
	public void setKzcs3(String kzcs3){
		this.kzcs3 = kzcs3;
	}
	//扩展参数
	public String getKzcs4() {
		return kzcs4;
	}
	public void setKzcs4(String kzcs4){
		this.kzcs4 = kzcs4;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
