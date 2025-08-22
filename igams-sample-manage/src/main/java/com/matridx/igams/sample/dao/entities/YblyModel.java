package com.matridx.igams.sample.dao.entities;


import org.apache.ibatis.type.Alias;
import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YblyModel")
public class YblyModel extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6033692625351132938L;
	//来源ID
	private String lyid;
	//入库类型
	private String rklx;
	//入库类型名称
	private String rklxmc;
	//标本类型
	private String yblx;
	//外部编号
	private String wbbh;
	//检验结果
	private String jyjg;
	//来源地
	private String lyd;
	//采样时间
	private String cysj;
	//性别
	private String xb;
	//性别名称
	private String xbmc;
	//孕周
	private String yz;
	//体重
	private String tz;
	//姓名
	private String xm;
	//备注
	private String bz;
	//来源编号
	private String lybh;
	//状态
	private String zt;
	
	public String getLyid() {
		return lyid;
	}
	public void setLyid(String lyid) {
		this.lyid = lyid;
	}
	public String getRklx() {
		return rklx;
	}
	public void setRklx(String rklx) {
		this.rklx = rklx;
		if("00".equals(rklx))
			rklxmc = "正式";
		else {
			rklxmc = "临时";
		}
	}
	public String getRklxmc() {
		return rklxmc;
	}
	public void setRklxmc(String rklxmc) {
		this.rklxmc = rklxmc;
	}
	public String getYblx() {
		return yblx;
	}
	public void setYblx(String yblx) {
		this.yblx = yblx;
	}
	public String getWbbh() {
		return wbbh;
	}
	public void setWbbh(String wbbh) {
		this.wbbh = wbbh;
	}
	public String getJyjg() {
		return jyjg;
	}
	public void setJyjg(String jyjg) {
		this.jyjg = jyjg;
	}
	public String getLyd() {
		return lyd;
	}
	public void setLyd(String lyd) {
		this.lyd = lyd;
	}
	public String getCysj() {
		return cysj;
	}
	public void setCysj(String cysj) {
		this.cysj = cysj;
	}
	public String getXb() {
		return xb;
	}
	public void setXb(String xb) {
		this.xb = xb;
		if("1".equals(xb))
			xbmc = "男";
		else if("2".equals(xb)) {
			xbmc = "女";
		}else{
			xbmc = "未知";
		}
	}
	public String getXbmc(){
		return xbmc;
	}
	public String getYz() {
		return yz;
	}
	public void setYz(String yz) {
		this.yz = yz;
	}
	public String getTz() {
		return tz;
	}
	public void setTz(String tz) {
		this.tz = tz;
	}
	public String getXm() {
		return xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getLybh() {
		return lybh;
	}
	public void setLybh(String lybh) {
		this.lybh = lybh;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
}
