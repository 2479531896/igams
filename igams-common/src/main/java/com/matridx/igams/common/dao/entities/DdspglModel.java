package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="DdspglModel")
public class DdspglModel extends BaseModel{
	//审批实例ID
	private String processinstanceid;
	//企业ID
	private String corpid;
	//事件类型
	private String eventtype;
	//类型 start\finish
	private String type;
	//审批模板的唯一码
	private String processcode;
	//实例标题
	private String title;
	//审批人ID
	private String staffid;
	//同意时result为agree，拒绝时result为refuse
	private String result;
	//操作时写的评论内容
	private String remark;
	//实例创建时间
	private String createtime;
	//审批结束时间
	private String finishtime;
	//钉钉审批管理ID
	private String ddspglid;
	//处理结果0:失败  1:成功  2:处理中
	private String cljg;
	//外部程序id
	private String wbcxid;
	
	
	public String getWbcxid() {
		return wbcxid;
	}
	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}
	public String getCljg() {
		return cljg;
	}
	public void setCljg(String cljg) {
		this.cljg = cljg;
	}
	public String getDdspglid() {
		return ddspglid;
	}
	public void setDdspglid(String ddspglid) {
		this.ddspglid = ddspglid;
	}
	//审批实例ID
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid){
		this.processinstanceid = processinstanceid;
	}
	//企业ID
	public String getCorpid() {
		return corpid;
	}
	public void setCorpid(String corpid){
		this.corpid = corpid;
	}
	//事件类型
	public String getEventtype() {
		return eventtype;
	}
	public void setEventtype(String eventtype){
		this.eventtype = eventtype;
	}
	//类型 start\finish
	public String getType() {
		return type;
	}
	public void setType(String type){
		this.type = type;
	}
	//审批模板的唯一码
	public String getProcesscode() {
		return processcode;
	}
	public void setProcesscode(String processcode){
		this.processcode = processcode;
	}
	//实例标题
	public String getTitle() {
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	//审批人ID
	public String getStaffid() {
		return staffid;
	}
	public void setStaffid(String staffid){
		this.staffid = staffid;
	}
	//同意时result为agree，拒绝时result为refuse
	public String getResult() {
		return result;
	}
	public void setResult(String result){
		this.result = result;
	}
	//操作时写的评论内容
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark){
		this.remark = remark;
	}
	//实例创建时间
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime){
		this.createtime = createtime;
	}
	//审批结束时间
	public String getFinishtime() {
		return finishtime;
	}
	public void setFinishtime(String finishtime){
		this.finishtime = finishtime;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
