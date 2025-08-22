package com.matridx.igams.common.dao.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.matridx.igams.common.GlobalString;
/**
 * 审核处理结果对象(用于审核后，返回相关信息）
 */
public class AuditResult implements Serializable {
	
	public final static int SUCCESS = 2;
	public final static int WARN = 0;
	public final static int ERROR = -2;
	
	private static final long serialVersionUID = 1L;
	
	private String msg;	//返回的提示信息
	private int type;		//结果类型：success、error、warn
	private int cnt;		//完成个数
	private boolean isFinished;		//是否完成
	private Map<String, String> backMap;//审核返回信息

	public AuditResult() {
	}
	
	public AuditResult setResultCount(int success, int warn, int error){
		return this;
	}
	
	public static AuditResult getInstance(int dataSize, List<String> sucMsg, List<String> warnMsg, List<String> errMsg,Map<String, String> b_Map) {
		StringBuilder msg = new StringBuilder();
		int type = SUCCESS;
		int sucSize = 0, warnSize = 0, errSize = 0;
		if(errMsg!=null&&!errMsg.isEmpty()){
			errSize = errMsg.size();
			msg.append(StringUtils.join(errMsg.toArray()));
		}
		if(sucMsg!=null&&!sucMsg.isEmpty()){
			sucSize = sucMsg.size();
			msg.append(StringUtils.join(sucMsg.toArray()));
		}
		if(warnMsg!=null&&!warnMsg.isEmpty()){
			warnSize = warnMsg.size();
			msg.append(StringUtils.join(warnMsg.toArray()));
		}
		if(sucSize==0){//未放成功信息的情况
			sucSize = Math.max(dataSize - warnSize - errSize, 0);
		}
		if(errSize>0) type=ERROR;
		if(sucSize>0) type=errSize>0?WARN:SUCCESS; //有成功，有错误
		if(warnSize>0) type=WARN;
		return new AuditResult(type, msg.toString(),b_Map);
	}
	
	public static AuditResult success(String msg){
		return new AuditResult(SUCCESS,msg);
	}
	
	public static AuditResult warn(String msg){
		return new AuditResult(WARN,msg);
	}
	
	public static AuditResult error(String msg){
		return new AuditResult(ERROR,msg);
	}
	
	private AuditResult(int type, String msg){
		this.msg = msg;
		this.type = type;
	}
	
	private AuditResult(int type, String msg,Map<String, String> b_map){
		this.msg = msg;
		this.type = type;
		this.backMap = b_map;
	}

	public String getReturnMsg(String success, String fail) {
		if(type==SUCCESS){
			return success+GlobalString.NEW_LINE+msg;
		}else if(type==WARN){
			return msg;
		}else{// if(type==ERROR){
			return fail+GlobalString.NEW_LINE+msg;
		}
	}
	
	public Boolean getReturnResult() {
		if(type==SUCCESS){
			return true;
		}else if(type==WARN){
			return null;
		}else{// if(type==ERROR){
			return false;
		}
	}
	
	public String getMsg() {
		return msg;
	}

	public int getType() {
		return type;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public Map<String, String> getBackMap() {
		return backMap;
	}

	public void setBackMap(Map<String, String> backMap) {
		this.backMap = backMap;
	}
	
}
