package com.matridx.las.netty.dao.entities;

import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.type.Alias;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加该机
 */
@Alias(value = "ChmMaterialModel")
public class ChmMaterialModel {
	//压盖机的八连管数量
	private Integer octNum = 0;

	//压盖机的八连管加盖数量
	private Integer octgNum = 0;
	//压盖机上的盖数量
	private Integer chmgNum = 0;
	private String commanddeviceid;
	private String deviceid;

	private String name;
	//仪器状态
	private String state;
	private String octalianpipeListCount="";
	private String startTime="";
	//八连管中的样本信息
	private Map<String,List<OctalYbxxxModel>> map;
	//推送消息时用到的，不需要修改赋值
	private JSONArray toSendMap;
	private  String wz;
	private  String qy;
	private  String pzwz;
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

	public Integer getOctgNum() {
		return octgNum;
	}

	public void setOctgNum(Integer octgNum) {
		this.octgNum = octgNum;
	}

	public Integer getOctNum() {
		return octNum;
	}

	public void setOctNum(Integer octNum) {
		this.octNum = octNum;
	}


	public Map<String, List<OctalYbxxxModel>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<OctalYbxxxModel>> map) {
		this.map = map;
	}

	public String getCommanddeviceid() {
		return commanddeviceid;
	}

	public void setCommanddeviceid(String commanddeviceid) {
		this.commanddeviceid = commanddeviceid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public JSONArray getToSendMap() {
		return toSendMap;
	}

	public void setToSendMap(JSONArray toSendMap) {
		this.toSendMap = toSendMap;
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

	public Integer getChmgNum() {
		return chmgNum;
	}

	public void setChmgNum(Integer chmgNum) {
		this.chmgNum = chmgNum;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
