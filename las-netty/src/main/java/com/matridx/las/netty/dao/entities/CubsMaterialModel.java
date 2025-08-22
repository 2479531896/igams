package com.matridx.las.netty.dao.entities;

import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.type.Alias;

import java.util.HashMap;
import java.util.Map;

/**
 * 建库仪严样本
 */
@Alias(value = "CubsMaterialModel")
public class CubsMaterialModel{
	private String passId; //通道号
	private String sample; //样本编号
	private String ysybid;// 原始样本id
	private String gunheadRight; //右枪头
	private String gunheadLeft; //左枪头
	private String epRight; //右EP管
	private String epLeft; //左EP管
	private String position; //建库仪位置编号
	private String commanddeviceid;
	private String wzszid;//建库仪位置id
	private  String wz;
	private  String qy;
	private  String pzwz;
	private String deviceid;
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

	private CubisUpParamModel cubisUpParamModel; //上报信息
	private String state; //状态  "99","未上线" ，"01","空闲" ，"02","工作中"， "03","错误"

	public String getPassId() {
		return passId;
	}

	public void setPassId(String passId) {
		this.passId = passId;
	}

	public String getSample() {
		return sample;
	}

	public void setSample(String sample) {
		this.sample = sample;
	}

	public String getGunheadRight() {
		return gunheadRight;
	}

	public void setGunheadRight(String gunheadRight) {
		this.gunheadRight = gunheadRight;
	}

	public String getGunheadLeft() {
		return gunheadLeft;
	}

	public void setGunheadLeft(String gunheadLeft) {
		this.gunheadLeft = gunheadLeft;
	}

	public String getEpRight() {
		return epRight;
	}

	public void setEpRight(String epRight) {
		this.epRight = epRight;
	}

	public String getEpLeft() {
		return epLeft;
	}

	public void setEpLeft(String epLeft) {
		this.epLeft = epLeft;
	}

	public CubisUpParamModel getCubisUpParamModel() {
		return cubisUpParamModel;
	}

	public void setCubisUpParamModel(CubisUpParamModel cubisUpParamModel) {
		this.cubisUpParamModel = cubisUpParamModel;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getYsybid() {
		return ysybid;
	}

	public void setYsybid(String ysybid) {
		this.ysybid = ysybid;
	}

	public String getCommanddeviceid() {
		return commanddeviceid;
	}

	public void setCommanddeviceid(String commanddeviceid) {
		this.commanddeviceid = commanddeviceid;
	}

	public String getWzszid() {
		return wzszid;
	}

	public void setWzszid(String wzszid) {
		this.wzszid = wzszid;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
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
