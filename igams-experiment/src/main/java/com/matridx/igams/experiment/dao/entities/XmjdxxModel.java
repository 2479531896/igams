package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XmjdxxModel")
public class XmjdxxModel extends BaseModel{
	//项目阶段ID
	private String xmjdid;
	//项目ID
	private String xmid;
	//序号
	private String xh;
	//阶段ID
	private String jdid;
	//阶段名称
	private String jdmc;
	//计划开始日期
	private String jhksrq;
	//实际开始日期
	private String sjksrq;
	//计划结束日期
	private String jhjsrq;
	//实际结束日期
	private String sjjsrq;
	//分数比例
	private String fsbl;

	public String getFsbl() {
		return fsbl;
	}

	public void setFsbl(String fsbl) {
		this.fsbl = fsbl;
	}

	//项目阶段ID
	public String getXmjdid() {
		return xmjdid;
	}
	public void setXmjdid(String xmjdid){
		this.xmjdid = xmjdid;
	}
	//项目ID
	public String getXmid() {
		return xmid;
	}
	public void setXmid(String xmid){
		this.xmid = xmid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//阶段ID
	public String getJdid() {
		return jdid;
	}
	public void setJdid(String jdid){
		this.jdid = jdid;
	}
	//阶段名称
	public String getJdmc() {
		return jdmc;
	}
	public void setJdmc(String jdmc){
		this.jdmc = jdmc;
	}
	public String getJhksrq() {
		return jhksrq;
	}
	public void setJhksrq(String jhksrq) {
		this.jhksrq = jhksrq;
	}
	public String getSjksrq() {
		return sjksrq;
	}
	public void setSjksrq(String sjksrq) {
		this.sjksrq = sjksrq;
	}
	public String getJhjsrq() {
		return jhjsrq;
	}
	public void setJhjsrq(String jhjsrq) {
		this.jhjsrq = jhjsrq;
	}
	public String getSjjsrq() {
		return sjjsrq;
	}
	public void setSjjsrq(String sjjsrq) {
		this.sjjsrq = sjjsrq;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
