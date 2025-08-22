package com.matridx.igams.detection.molecule.service.impl;


import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAlihealthMedicalReportCustomerReportSyncRequest;
import com.taobao.api.response.AlibabaAlihealthMedicalReportCustomerReportSyncResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AliHealthReturnReport {

private final Logger log = LoggerFactory.getLogger(AliHealthReturnReport.class);


	private String fzjcid;
	private String fjid;
	private String wjm;
    private String zjh;
    private String sj;
    private String url;
	public void init(String fzjcid,String fjid,String wjm,String zjh,String sj,String url){
		this.fzjcid = fzjcid;
		this.fjid = fjid;
		this.wjm = wjm;
        this.zjh = zjh;
        this.sj=sj;
		this.url=url;
	}

	/**
	 * 服务商回传报告
	 */
	public void returnReport(){
		// TODO Auto-generated method stub
		log.info("-------阿里健康---回传报告线程开启------");
		String serverUrl = "https://eco.taobao.com/router/rest";
		String appKey = "33460000";
		String appSecret = "17cc7a528439a07a63b8c662c3df8f5a";
		TaobaoClient client = new DefaultTaobaoClient(serverUrl, appKey, appSecret);
		AlibabaAlihealthMedicalReportCustomerReportSyncRequest req = new AlibabaAlihealthMedicalReportCustomerReportSyncRequest();
		AlibabaAlihealthMedicalReportCustomerReportSyncRequest.ConsumerReportSyncRequest obj1 = new AlibabaAlihealthMedicalReportCustomerReportSyncRequest.ConsumerReportSyncRequest();
		obj1.setIsvReportId(fjid);
		obj1.setReportName(wjm);
		obj1.setIsvReportUrl(url);
		log.info(obj1.getIsvReportUrl());
		obj1.setSourceIsvReservationRecordId(fzjcid);
		obj1.setConsumerIdCardNo(zjh);
//		obj1.setConsumerPassportNo("E0450000");
		obj1.setConsumerPhoneNumber(sj);
		req.setParam0(obj1);
		AlibabaAlihealthMedicalReportCustomerReportSyncResponse rsp = null;
		try {
			rsp = client.execute(req);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		if (rsp != null) {
			log.info(rsp.getBody());
			String resultStatus = rsp.getResultStatus();
			if("SUCCESS".equals(resultStatus)){
				log.info("-------阿里健康---回传报告成功------");
			}else{
				log.info("-------阿里健康---回传报告失败------");
			}
		}
	}
}
