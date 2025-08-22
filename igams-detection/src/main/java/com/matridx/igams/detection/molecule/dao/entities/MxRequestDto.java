package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Alias(value="MxRequestDto")
public class MxRequestDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String requestType;  //接口名称
    private String requestId;
    private String requestTime;  //时间
    private String companyId;   //合作伙伴id
    private String requestData; // 请求的业务数据集合
    private String responseTime; //时间
    private String resultCode;  //返回结果代码
    private String resultMsg;  //返回结果描述
    private String responseData;  //相应业务集合

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }



    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }
}
