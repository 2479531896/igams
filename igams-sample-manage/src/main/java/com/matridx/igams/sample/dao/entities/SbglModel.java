package com.matridx.igams.sample.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.enums.DeviceTypeEnum;

@Alias(value="SbglModel")
public class SbglModel extends BaseModel{
	//设备ID
	private String sbid;
	//父设备ID
	private String fsbid;
	//设备类型
	private String sblx;
	//设备类型名称
	public String sblxmc;
	//设备号
	private String sbh;
	//存放数
	private String cfs;
	//标本类型
	private String yblx;
	//排序
	private String px;
	//备注
	private String bz;
	//已存放数
	private String ycfs;
	//存储单位
	private String jcdw;
	//位置
	private String wz;

	public void setSblxmc(String sblxmc) {
		this.sblxmc = sblxmc;
	}

	public String getWz() {
		return wz;
	}

	public void setWz(String wz) {
		this.wz = wz;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	//已存放数
	public String getYcfs() {
		return ycfs;
	}

	public void setYcfs(String ycfs) {
		this.ycfs = ycfs;
	}
	//设备ID
	public String getSbid() {
		return sbid;
	}
	public void setSbid(String sbid){
		this.sbid = sbid;
	}
	//父设备ID
	public String getFsbid() {
		return fsbid;
	}
	public void setFsbid(String fsbid){
		this.fsbid = fsbid;
	}
	//设备类型
	public String getSblx() {
		return sblx;
	}
	public void setSblx(String sblx) {
		this.sblx = sblx;
		this.sblxmc = DeviceTypeEnum.getValueByCode(sblx);
	}
	//设备类型名称
	public String getSblxmc() {
		return sblxmc;
	}
	//设备号
	public String getSbh() {
		return sbh;
	}
	public void setSbh(String sbh){
		this.sbh = sbh;
	}
	//存放数
	public String getCfs() {
		return cfs;
	}
	public void setCfs(String cfs){
		this.cfs = cfs;
	}
	//标本类型
	public String getYblx() {
		return yblx;
	}
	public void setYblx(String yblx){
		this.yblx = yblx;
	}
	//排序
	public String getPx() {
		return px;
	}
	public void setPx(String px){
		this.px = px;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	public SbglModel() {
	}
	public SbglModel(String sbid) {
		this.sbid = sbid;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
