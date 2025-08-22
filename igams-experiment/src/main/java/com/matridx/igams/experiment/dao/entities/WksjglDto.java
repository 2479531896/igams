package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WksjglDto")
public class WksjglDto extends WksjglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//芯片ID
	private String id;
	//芯片名
	private String name;
	//上机时间
	private String start_time;
	//下机时间
	private String finish_time;
	//测序仪名
	private String machine;
	//数据存放的服务器
	private String zone;
	//芯片归属的实验室
	private String organization;
	//预计下机时间
	private String expect_finish_time;
	//文库上机明细JSON
	private String wksj_Json;
	//保存标记
	private String bcbj;
	//测序仪ID
	private String cxyid;
	private String spike;

	private String cxycskz3;

	public String getCxycskz3() {
		return cxycskz3;
	}

	public void setCxycskz3(String cxycskz3) {
		this.cxycskz3 = cxycskz3;
	}

	public String getSpike() {
		return spike;
	}

	public void setSpike(String spike) {
		this.spike = spike;
	}

	public String getCxyid() {
		return cxyid;
	}

	public void setCxyid(String cxyid) {
		this.cxyid = cxyid;
	}

	public String getBcbj() {
		return bcbj;
	}

	public void setBcbj(String bcbj) {
		this.bcbj = bcbj;
	}

	public String getWksj_Json() {
		return wksj_Json;
	}

	public void setWksj_Json(String wksj_Json) {
		this.wksj_Json = wksj_Json;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getFinish_time() {
		return finish_time;
	}

	public void setFinish_time(String finish_time) {
		this.finish_time = finish_time;
	}

	public String getMachine() {
		return machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getExpect_finish_time() {
		return expect_finish_time;
	}

	public void setExpect_finish_time(String expect_finish_time) {
		this.expect_finish_time = expect_finish_time;
	}
}
