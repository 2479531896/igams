package com.matridx.las.netty.dao.entities;

import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.type.Alias;

import java.util.HashMap;
import java.util.Map;

/**
 * 测序仪
 */
@Alias(value = "SeqMaterialModel")
public class SeqMaterialModel {
	private String commanddeviceid;
	private String deviceid;

	//八连管中的样本
	private OctalianpipeListModel octalianpipeList;
	//仪器状态
	private String state;

	private String name;
	private  String wz;
	private  String qy;
	private  String pzwz;
	private String octalianpipeListCount="";
	private String startTime="";
	private String bz;
	private String csdm;
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

	public String getCommanddeviceid() {
		return commanddeviceid;
	}

	public void setCommanddeviceid(String commanddeviceid) {
		this.commanddeviceid = commanddeviceid;
	}

	public OctalianpipeListModel getOctalianpipeList() {
		return octalianpipeList;
	}

	public void setOctalianpipeList(OctalianpipeListModel octalianpipeList) {
		this.octalianpipeList = octalianpipeList;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getOctalianpipeListCount() {
		return octalianpipeListCount;
	}

	public void setOctalianpipeListCount(String octalianpipeListCount) {
		this.octalianpipeListCount = octalianpipeListCount;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
