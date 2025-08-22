package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="RwrqModel")
public class RwrqModel extends BaseModel{
	//日期ID
	private String rqid;
	//任务ID
	private String rwid;
	//项目阶段ID
	private String xmjdid;
	//计划开始日期
	private String jhksrq;
	//实际开始日期
	private String sjksrq;
	//计划结束日期
	private String jhjsrq;
	//实际结束日期
	private String sjjsrq;
	//截至日期
	private String jzrq;
	//处理人员
	private String clry;
	//阶段分数
	private String jdfs;

	public String getJdfs() {
		return jdfs;
	}

	public void setJdfs(String jdfs) {
		this.jdfs = jdfs;
	}

	public String getClry() {
		return clry;
	}

	public void setClry(String clry) {
		this.clry = clry;
	}

	public String getJzrq() {
		return jzrq;
	}

	public void setJzrq(String jzrq) {
		this.jzrq = jzrq;
	}

	//日期ID
	public String getRqid() {
		return rqid;
	}
	public void setRqid(String rqid){
		this.rqid = rqid;
	}
	//任务ID
	public String getRwid() {
		return rwid;
	}
	public void setRwid(String rwid){
		this.rwid = rwid;
	}
	//项目阶段ID
	public String getXmjdid() {
		return xmjdid;
	}
	public void setXmjdid(String xmjdid){
		this.xmjdid = xmjdid;
	}
	//计划开始日期
	public String getJhksrq() {
		return jhksrq;
	}
	public void setJhksrq(String jhksrq){
		this.jhksrq = jhksrq;
	}
	//实际开始日期
	public String getSjksrq() {
		return sjksrq;
	}
	public void setSjksrq(String sjksrq){
		this.sjksrq = sjksrq;
	}
	//计划结束日期
	public String getJhjsrq() {
		return jhjsrq;
	}
	public void setJhjsrq(String jhjsrq){
		this.jhjsrq = jhjsrq;
	}
	//实际结束日期
	public String getSjjsrq() {
		return sjjsrq;
	}
	public void setSjjsrq(String sjjsrq){
		this.sjjsrq = sjjsrq;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
