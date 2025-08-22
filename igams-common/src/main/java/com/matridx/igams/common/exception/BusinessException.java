package com.matridx.igams.common.exception;

/**
 * 
 * 类名称： 自定义异常
 */
@SuppressWarnings("serial")
public class BusinessException extends Exception {
	
	private String msgId;
	private String msg;
	private String lb;

	public BusinessException(String msgId) {
		super(msgId);
		this.msgId = msgId;
	}
	
	public BusinessException(String msgId,String msg) {
		super(msg);
		this.msgId = msgId;
		this.msg = msg;
	}
	public BusinessException(String msgId,String msg,String lb) {
		super(msg);
		this.msgId = msgId;
		this.msg = msg;
		this.lb = lb;
	}
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgId() {
		return msgId;
	}

	public String getLb() {
		return lb;
	}

	public void setLb(String lb) {
		this.lb = lb;
	}
}
