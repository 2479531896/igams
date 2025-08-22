package com.matridx.igams.common.dao.entities;

import java.io.Serializable;

public class ProcessModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7126788062875010559L;
	
	//当前执行条数
	private int currentCount;
	//是否结束
	private boolean isFinish;
	//文件路径加名称
	private String filePath;
	//错误信息
	private String errorMsg;
	//文件ID
	private String wjid;
	
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public int getCurrentCount() {
		return currentCount;
	}
	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}
	public boolean isFinish() {
		return isFinish;
	}
	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFilePath() {
		return filePath;
	}
	public String getWjid() {
		return wjid;
	}
	public void setWjid(String wjid) {
		this.wjid = wjid;
	}
}
