package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="NyypxxModel")
public class NyypxxModel extends BaseModel{
	//耐药药品id
	private String nyypid;
	//耐药中文名 关联yp字段 如大环内酯类
	private String nyzwm;
	//耐药英文名
	private String nyywm;
	//抗生素简称
	private String kssjc;
	//抗生素全称
	private String kssqc;
	//相关药品
	private String xgyp;
	//序号
	private String xh;
	//药品等级
	private String ypdj;
	//耐药药品id
	public String getNyypid() {
		return nyypid;
	}
	public void setNyypid(String nyypid){
		this.nyypid = nyypid;
	}
	//耐药中文名 关联yp字段 如大环内酯类
	public String getNyzwm() {
		return nyzwm;
	}
	public void setNyzwm(String nyzwm){
		this.nyzwm = nyzwm;
	}
	//耐药英文名
	public String getNyywm() {
		return nyywm;
	}
	public void setNyywm(String nyywm){
		this.nyywm = nyywm;
	}
	//抗生素简称
	public String getKssjc() {
		return kssjc;
	}
	public void setKssjc(String kssjc){
		this.kssjc = kssjc;
	}
	//抗生素全称
	public String getKssqc() {
		return kssqc;
	}
	public void setKssqc(String kssqc){
		this.kssqc = kssqc;
	}
	//相关药品
	public String getXgyp() {
		return xgyp;
	}
	public void setXgyp(String xgyp){
		this.xgyp = xgyp;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public String getYpdj() {
        return ypdj;
    }

    public void setYpdj(String ypdj) {
        this.ypdj = ypdj;
    }
}
