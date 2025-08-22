package com.matridx.las.netty.dao.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.type.Alias;

@Alias(value = "TrayModel")
public class TrayModel {
	// 托盘编号
	private String tpbh;
	// 托盘里的卡盒list
	private List<YsybxxDto> boxList = new ArrayList<YsybxxDto>();
	//状态 1:带运行  0:正常   2:正在处理  3:已放置机器人平台
	private String zt;
	private String deviceid;//托盘的deviceidid
	private String name;
	private  String wz;
	private  String qy;
	private  String pzwz;
	private  String commanddeviceid;
	private String countTray;
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

	public String getPzwz() {
		return pzwz;
	}

	public void setPzwz(String pzwz) {
		this.pzwz = pzwz;
	}
	public String getWz() {
		return wz;
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


	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getTpbh() {
		return tpbh;
	}

	public void setTpbh(String tpbh) {
		this.tpbh = tpbh;
	}

	public List<YsybxxDto> getBoxList() {
		return boxList;
	}

	public void setBoxList(List<YsybxxDto> boxList) {
		this.boxList = boxList;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCommanddeviceid() {
		return commanddeviceid;
	}

	public void setCommanddeviceid(String commanddeviceid) {
		this.commanddeviceid = commanddeviceid;
	}

	public String getCountTray() {
		return countTray;
	}

	public void setCountTray(String countTray) {
		this.countTray = countTray;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
