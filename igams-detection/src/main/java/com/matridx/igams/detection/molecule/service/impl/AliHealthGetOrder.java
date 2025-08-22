package com.matridx.igams.detection.molecule.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.enums.DetMQTypeEnum;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxxService;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAlihealthMedicalOrderRealtimeGetRequest;
import com.taobao.api.response.AlibabaAlihealthMedicalOrderRealtimeGetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;


public class AliHealthGetOrder {

private final Logger log = LoggerFactory.getLogger(AliHealthGetOrder.class);


	private String fzjcid;
	private IFzjcxxService fzjcxxService;
	private AmqpTemplate amqpTemplate;

	public void init(String fzjcid,IFzjcxxService fzjcxxService, AmqpTemplate amqpTemplate){
		this.fzjcid = fzjcid;
		this.fzjcxxService=fzjcxxService;
		this.amqpTemplate=amqpTemplate;
	}

	/**
	 * 服务商回传报告
	 */
	public void getOrderDetails(){
		// TODO Auto-generated method stub
		try {
			Thread.sleep(120000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.error("--阿里健康--获取支付详情线程开始");
		String serverUrl = "https://eco.taobao.com/router/rest";
		String appKey = "33460000";
		String appSecret = "17cc7a528439a07a63b8c662c3df8f5a";
		TaobaoClient client = new DefaultTaobaoClient(serverUrl, appKey, appSecret);
		AlibabaAlihealthMedicalOrderRealtimeGetRequest req = new AlibabaAlihealthMedicalOrderRealtimeGetRequest();
		AlibabaAlihealthMedicalOrderRealtimeGetRequest.GetOrderRequest obj1 = new AlibabaAlihealthMedicalOrderRealtimeGetRequest.GetOrderRequest();
		obj1.setIsvReservationRecordId(fzjcid);
		req.setParam0(obj1);
		AlibabaAlihealthMedicalOrderRealtimeGetResponse rsp = null;
		try {
			rsp = client.execute(req);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		if (rsp != null) {
			log.error("--阿里健康--获取支付详情返回数据："+rsp.getBody());
			if(!"FAIL".equals(rsp.getResultStatus())){
				FzjcxxDto dto=new FzjcxxDto();
				AlibabaAlihealthMedicalOrderRealtimeGetResponse.OrderInfo data = rsp.getData();
				dto.setFkje(data.getTotalSalesPrice());
				dto.setSfje(data.getActualPaymentAmount());
				dto.setFkbj("1");
				dto.setFkrq(data.getPaymentSuccessTime());
				dto.setPtorderno(data.getOrderId());
				dto.setFzjcid(fzjcid);
				boolean b = fzjcxxService.updateAliOrder(dto);
				if(b){
					amqpTemplate.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.ALDD_UPD.getCode() + JSONObject.toJSONString(dto));
				}
			}
		}
	}
}
