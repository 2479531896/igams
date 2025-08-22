package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XxdyModel")
public class XxdyModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//对应id
	private String dyid;
	//对应类型(主要存了csid)
	private String dylx;
	//原信息
	private String yxx;
	//对应信息
	private String dyxx;
	//原信息id
	private String yxxid;
	//子信息id
	private String zid;
	//信息对应名称
	private String dyxxmc;
	//对应子信息
	private String dyzxx;
	//对应子信息名称
	private String dyzxxmc;
	//排序
	private String px;

	public String getPx() {
		return px;
	}

	public void setPx(String px) {
		this.px = px;
	}

	private String zxx;//子信息
	private String dydm;//对应代码
	private String kzcs1;//扩展参数1
	private String kzcs2;//扩展参数2
	private String kzcs3;//扩展参数3
	private String kzcs4;//扩展参数4
	private String kzcs5;//扩展参数5
	private String kzcs6;//扩展参数6
	private String kzcs7;//扩展参数7
	private String kzcs8;//扩展参数8
	private String kzcs9;//扩展参数9

	public String getKzcs7() {
		return kzcs7;
	}

	public void setKzcs7(String kzcs7) {
		this.kzcs7 = kzcs7;
	}

	public String getKzcs8() {
		return kzcs8;
	}

	public void setKzcs8(String kzcs8) {
		this.kzcs8 = kzcs8;
	}

	public String getKzcs9() {
		return kzcs9;
	}

	public void setKzcs9(String kzcs9) {
		this.kzcs9 = kzcs9;
	}

	public String getKzcs6() {
		return kzcs6;
	}

	public void setKzcs6(String kzcs6) {
		this.kzcs6 = kzcs6;
	}

	public String getKzcs4() {
		return kzcs4;
	}

	public void setKzcs4(String kzcs4) {
		this.kzcs4 = kzcs4;
	}

	public String getKzcs5() {
		return kzcs5;
	}

	public void setKzcs5(String kzcs5) {
		this.kzcs5 = kzcs5;
	}

	public String getKzcs1() {
		return kzcs1;
	}

	public void setKzcs1(String kzcs1) {
		this.kzcs1 = kzcs1;
	}

	public String getKzcs2() {
		return kzcs2;
	}

	public void setKzcs2(String kzcs2) {
		this.kzcs2 = kzcs2;
	}

	public String getKzcs3() {
		return kzcs3;
	}

	public void setKzcs3(String kzcs3) {
		this.kzcs3 = kzcs3;
	}

	public String getDydm() {
		return dydm;
	}

	public void setDydm(String dydm) {
		this.dydm = dydm;
	}

	public String getZxx() {
		return zxx;
	}

	public void setZxx(String zxx) {
		this.zxx = zxx;
	}
	public String getYxxid() {
		return yxxid;
	}

	public void setYxxid(String yxxid) {
		this.yxxid = yxxid;
	}

	public String getZid() {
		return zid;
	}

	public void setZid(String zid) {
		this.zid = zid;
	}

	public String getDyxxmc() {
		return dyxxmc;
	}
	public void setDyxxmc(String dyxxmc) {
		this.dyxxmc = dyxxmc;
	}
	public String getDyid() {
		return dyid;
	}
	public void setDyid(String dyid) {
		this.dyid = dyid;
	}
	public String getDylx() {
		return dylx;
	}
	public void setDylx(String dylx) {
		this.dylx = dylx;
	}
	public String getYxx() {
		return yxx;
	}
	public void setYxx(String yxx) {
		this.yxx = yxx;
	}
	public String getDyxx() {
		return dyxx;
	}
	public void setDyxx(String dyxx) {
		this.dyxx = dyxx;
	}

    public String getDyzxx() {
        return dyzxx;
    }

    public void setDyzxx(String dyzxx) {
        this.dyzxx = dyzxx;
    }

    public String getDyzxxmc() {
        return dyzxxmc;
    }

    public void setDyzxxmc(String dyzxxmc) {
        this.dyzxxmc = dyzxxmc;
    }
}
