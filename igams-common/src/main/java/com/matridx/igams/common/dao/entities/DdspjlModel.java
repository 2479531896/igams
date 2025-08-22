package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="DdspjlModel")
public class DdspjlModel extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String processinstanceid;//实例id
    private String processcode;//流程编码
    private String attachedprocessinstanceids;//审批附属实例
    private String bizaction;//业务动作
    private String bizdata;//用户自定义业务参数透出
    private String businessid;//审批实例业务编号
    private String ccuserids;//抄送人userIds列表
    private String createtime;//创建时间
    private String finishtime;//结束时间
    private String formcomponentvalues;//表单组件值
    private String operationrecords;//操作记录列表
    private String originatordeptid;//发起人的部门
    private String originatordeptname;//发起人的部门名称
    private String originatoruserid;//发起人的userId
    private String result;//审批结果
    private String status;//审批状态
    private String tasks;//任务列表
    private String title;//审批实例标题

    public String getProcessinstanceid() {
        return processinstanceid;
    }

    public void setProcessinstanceid(String processinstanceid) {
        this.processinstanceid = processinstanceid;
    }

    public String getProcesscode() {
        return processcode;
    }

    public void setProcesscode(String processcode) {
        this.processcode = processcode;
    }

    public String getAttachedprocessinstanceids() {
        return attachedprocessinstanceids;
    }

    public void setAttachedprocessinstanceids(String attachedprocessinstanceids) {
        this.attachedprocessinstanceids = attachedprocessinstanceids;
    }

    public String getBizaction() {
        return bizaction;
    }

    public void setBizaction(String bizaction) {
        this.bizaction = bizaction;
    }

    public String getBizdata() {
        return bizdata;
    }

    public void setBizdata(String bizdata) {
        this.bizdata = bizdata;
    }

    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public String getCcuserids() {
        return ccuserids;
    }

    public void setCcuserids(String ccuserids) {
        this.ccuserids = ccuserids;
    }

    public String getFormcomponentvalues() {
        return formcomponentvalues;
    }

    public void setFormcomponentvalues(String formcomponentvalues) {
        this.formcomponentvalues = formcomponentvalues;
    }

    public String getOperationrecords() {
        return operationrecords;
    }

    public void setOperationrecords(String operationrecords) {
        this.operationrecords = operationrecords;
    }

    public String getOriginatordeptid() {
        return originatordeptid;
    }

    public void setOriginatordeptid(String originatordeptid) {
        this.originatordeptid = originatordeptid;
    }

    public String getOriginatordeptname() {
        return originatordeptname;
    }

    public void setOriginatordeptname(String originatordeptname) {
        this.originatordeptname = originatordeptname;
    }

    public String getOriginatoruserid() {
        return originatoruserid;
    }

    public void setOriginatoruserid(String originatoruserid) {
        this.originatoruserid = originatoruserid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
