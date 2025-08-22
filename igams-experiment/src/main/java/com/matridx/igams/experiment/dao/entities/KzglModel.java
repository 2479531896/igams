package com.matridx.igams.experiment.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="KzglModel")
public class KzglModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//扩增ID
	private String kzid;
	//审核状态   00:未提交 10:审核中  15:审核未通过  80:审核通过
	private String zt;
	//检测单位
	private String jcdw;
	//序号
	private String xh;
	//扩增名称
	private String kzmc;
	//扩增时间
	private String kzsj;
	//逆转录试剂盒
	private String sjph2;
	//宏基因组DNA建库试剂盒
	private String sjph1;

	public String getKzid() {
		return kzid;
	}

	public void setKzid(String kzid) {
		this.kzid = kzid;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getKzmc() {
		return kzmc;
	}

	public void setKzmc(String kzmc) {
		this.kzmc = kzmc;
	}

	public String getKzsj() {
		return kzsj;
	}

	public void setKzsj(String kzsj) {
		this.kzsj = kzsj;
	}

	public String getSjph2() {
		return sjph2;
	}

	public void setSjph2(String sjph2) {
		this.sjph2 = sjph2;
	}

	public String getSjph1() {
		return sjph1;
	}

	public void setSjph1(String sjph1) {
		this.sjph1 = sjph1;
	}
}
