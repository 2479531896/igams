package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="FzjybgModel")
public class FzjybgModel extends BaseModel{

	private static final long serialVersionUID = 1L;

	// 任务号
	private String taskid;
	//资源码，门(急)诊检验报告：REQ.C0101.0303.02 ；门(急)诊检验常规报告：REQ.C0101.0303.0201 ；门(急)诊检验申请单 REQ.C0101.0303.01
	private String resourcecode;
	//资源名称
	private String resourcename;
	//分子检验报告id
	private String fzjybgid;
	private String uploadtime;
	//结果编码，-1 失败 ；1 成功
	private String resultcode;
	//结果描述
	private String resultdesc;
	//文件名
	private String wjm;
	//文件路径
	private String wjlj;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getResourcecode() {
		return resourcecode;
	}

	public void setResourcecode(String resourcecode) {
		this.resourcecode = resourcecode;
	}

	public String getResourcename() {
		return resourcename;
	}

	public void setResourcename(String resourcename) {
		this.resourcename = resourcename;
	}

	public String getFzjybgid() {
		return fzjybgid;
	}

	public void setFzjybgid(String fzjybgid) {
		this.fzjybgid = fzjybgid;
	}

	public String getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}

	public String getResultcode() {
		return resultcode;
	}

	public void setResultcode(String resultcode) {
		this.resultcode = resultcode;
	}

	public String getResultdesc() {
		return resultdesc;
	}

	public void setResultdesc(String resultdesc) {
		this.resultdesc = resultdesc;
	}

	public String getWjm() {
		return wjm;
	}

	public void setWjm(String wjm) {
		this.wjm = wjm;
	}

	public String getWjlj() {
		return wjlj;
	}

	public void setWjlj(String wjlj) {
		this.wjlj = wjlj;
	}
}
