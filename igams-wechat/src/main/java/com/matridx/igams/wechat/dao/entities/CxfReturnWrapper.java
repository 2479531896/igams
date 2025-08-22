package com.matridx.igams.wechat.dao.entities;

import java.util.List;

public class CxfReturnWrapper {
	//文件返回类
	List<CxfFileWrapper> cxfFileWrapper;
    // 状态
    private String status;
    // 错误码
    private String errorCode;
    
    public List<CxfFileWrapper> getCxfFileWrapper() {
		return cxfFileWrapper;
	}

	public void setCxfFileWrapper(List<CxfFileWrapper> cxfFileWrapper) {
		this.cxfFileWrapper = cxfFileWrapper;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
