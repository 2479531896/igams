package com.matridx.las.netty.dao.entities;

import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.type.Alias;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ep管载具
 */
@Alias(value = "EpVehicleModel")
public class EpVehicleModel {
	// Ep管载具类型
	private String epType;
	//ep管载具位置编号
	private String  epNm;
	private String csdm;

	// 托盘里的卡盒list
	private List<AgvEpModel> epList ;

	private String deviceid;//载具的deviceidid
	private String zt;//载具状态 0，在  3，已拿走（目前前端用）

	private String qy;
	private String wz;
	private boolean isnull;
	private String pzwz;
	private String countEpList;
	private String commanddeviceid;
	private String bz;
	private String name;
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
	public String getEpType() {
		return epType;
	}

	public void setEpType(String epType) {
		this.epType = epType;
	}

	public List<AgvEpModel> getEpList() {
		return epList;
	}

	public void setEpList(List<AgvEpModel> epList) {
		this.epList = epList;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getEpNm() {
		return epNm;
	}

	public void setEpNm(String epNm) {
		this.epNm = epNm;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getCsdm() {
		return csdm;
	}

	public void setCsdm(String csdm) {
		this.csdm = csdm;
	}

	public String getQy() {
		return qy;
	}

	public void setQy(String qy) {
		this.qy = qy;
	}

	public String getWz() {
		return wz;
	}

	public void setWz(String wz) {
		this.wz = wz;
	}

	public boolean isIsnull() {
		return isnull;
	}

	public void setIsnull(boolean isnull) {
		this.isnull = isnull;
	}

	public String getPzwz() {
		return pzwz;
	}

	public void setPzwz(String pzwz) {
		this.pzwz = pzwz;
	}

	public String getCountEpList() {
		return countEpList;
	}

	public void setCountEpList(String countEpList) {
		this.countEpList = countEpList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
