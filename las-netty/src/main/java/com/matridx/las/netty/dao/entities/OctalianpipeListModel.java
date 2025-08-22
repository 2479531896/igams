package com.matridx.las.netty.dao.entities;

import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.type.Alias;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 载具上的八连管属性
 */
@Alias(value = "OctalianpipeListModel")
public class OctalianpipeListModel {
	//载具上的八连管数量
	private Integer octNum = 0;
	//载具上的加盖八连管数量
	private Integer octgNum = 0;
	//压盖机需要补充的八连管数量
	private Integer octbNum = 0;
	//干净的八连管载具数量
	private Integer num = 0;
	private String deviceid;
	private String ocType;
	private String zt;//载具状态 0，在  3，已拿走（目前前端用）
	private  String wz;
	private  String qy;
	private  String pzwz;
	private String commanddeviceid;
	//八连管中的样本信息
	private Map<String,List<OctalYbxxxModel>> map;
	//推送消息时用到的，不需要修改赋值
	private JSONArray toSendMap;
	private String name;
	private String countOctal;
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

	//	//八连管中的样本信息
//	private List<OctalYbxxxModel> octalList;
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getOctbNum() {
		return octbNum;
	}

	public void setOctbNum(Integer octbNum) {
		this.octbNum = octbNum;
	}

	public Integer getOctgNum() {
		return octgNum;
	}

	public void setOctgNum(Integer octgNum) {
		this.octgNum = octgNum;
	}


	public Map<String, List<OctalYbxxxModel>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<OctalYbxxxModel>> map) {
		this.map = map;
	}

	public Integer getOctNum() {
		return octNum;
	}

	public void setOctNum(Integer octNum) {
		this.octNum = octNum;
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

	public String getOcType() {
		return ocType;
	}

	public void setOcType(String ocType) {
		this.ocType = ocType;
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

	public String getCountOctal() {
		return countOctal;
	}

	public void setCountOctal(String countOctal) {
		this.countOctal = countOctal;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
