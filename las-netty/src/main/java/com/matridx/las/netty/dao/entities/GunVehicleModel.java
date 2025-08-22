package com.matridx.las.netty.dao.entities;

import com.alibaba.fastjson.JSONArray;
import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 枪头载具
 */
@Alias(value = "GunVehicleModel")
public class GunVehicleModel extends BaseModel {
	// 枪头管载具编号
	private String gunNm;
	// 托盘里的卡盒list
	private List<String> gunhead;

	private String deviceid;//载具的deviceidid
	private String gunType;
	private String gunheadSize;//枪头数量，前端用
	private String zt;//载具状态 0，在  3，已拿走（目前前端用）
	private String name;
	private  String wz;
	private  String qy;
    private String commanddeviceid;
	private String countGunhead;
	private String bz;
	private String csdm;
	private String startTime="";
	private Map<String,String> sendSxType= new HashMap<String,String>();
	private JSONArray sxList;

	public JSONArray getSxList() {
		return sxList;
	}

	public void setSxList(JSONArray sxList) {
		this.sxList = sxList;
	}
	public Map<String, String> getSendSxType() {
		return sendSxType;
	}

	public void setSendSxType(Map<String, String> sendSxType) {
		this.sendSxType = sendSxType;
	}
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getCsdm() {
		return csdm;
	}

	public void setCsdm(String csdm) {
		this.csdm = csdm;
	}
	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getCommanddeviceid() {
		return commanddeviceid;
	}

	public void setCommanddeviceid(String commanddeviceid) {
		this.commanddeviceid = commanddeviceid;
	}

	public String getWz() {
		return wz;
	}
	private  String pzwz;

	public String getPzwz() {
		return pzwz;
	}

	public void setPzwz(String pzwz) {
		this.pzwz = pzwz;
	}
	public void setWz(String wz) {
		this.wz = wz;
	}

	public String getQy() {
		return qy;
	}

	public void setQy(String qy) {
		this.qy = qy;
	}

	public String getGunNm() {
		return gunNm;
	}

	public void setGunNm(String gunNm) {
		this.gunNm = gunNm;
	}

	public List<String> getGunhead() {
		return gunhead;
	}

	public void setGunhead(List<String> gunhead) {
		this.gunhead = gunhead;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getGunType() {
		return gunType;
	}

	public void setGunType(String gunType) {
		this.gunType = gunType;
	}

	public String getGunheadSize() {
		return gunheadSize;
	}

	public void setGunheadSize(String gunheadSize) {
		this.gunheadSize = gunheadSize;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountGunhead() {
		return countGunhead;
	}

	public void setCountGunhead(String countGunhead) {
		this.countGunhead = countGunhead;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
