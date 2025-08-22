package com.matridx.server.wechat.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.PayTypeEnum;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.server.wechat.dao.entities.PayinfoDto;
import com.matridx.server.wechat.service.svcinterface.IBankService;
import com.matridx.server.wechat.service.svcinterface.IPayinfoService;

public class SelectPayResultThread extends Thread{
	
	private Logger log = LoggerFactory.getLogger(SelectPayResultThread.class);
	
	private IPayinfoService payinfoService;
	
	private IBankService bankService;
	
	private IXtszService xtszService;

	private RestTemplate restTemplate;

	public SelectPayResultThread(IPayinfoService payinfoService, IBankService bankService, IXtszService xtszService, RestTemplate restTemplate) {
		this.payinfoService = payinfoService;
		this.bankService = bankService;
		this.xtszService = xtszService;
		this.restTemplate = restTemplate;
	}

	@Override
	public void run() {
		try {
			while(true) {
				TimeUnit.SECONDS.sleep(60);
				// 查询订单列表
				PayinfoDto payinfoDto = new PayinfoDto();
				payinfoDto.setDylx(PayTypeEnum.PAY.getCode());
				List<PayinfoDto> payinfoList = payinfoService.getIncompOrders(payinfoDto);
				if(payinfoList != null && payinfoList.size() > 0) {
					log.info("查询未支付订单:"+ payinfoList.size());
					for (int i = 0; i < payinfoList.size(); i++) {
						// 调用订单查询方法
						bankService.payResultInquire(restTemplate, payinfoList.get(i));
					}
				}else {
					log.info("更改系统配置:"+ payinfoList.size());
					// 更改系统配置 0
					XtszDto xtszDto = new XtszDto();
					xtszDto.setSzlb(GlobalString.PAY_RESULT_FLAG);
					xtszDto.setSzz("0");
					xtszDto.setOldszlb(GlobalString.PAY_RESULT_FLAG);
					xtszService.update(xtszDto);
					return;
				}
			}
		} catch (Exception e) {
			// 更改系统配置 0
			XtszDto xtszDto = new XtszDto();
			xtszDto.setSzlb(GlobalString.PAY_RESULT_FLAG);
			xtszDto.setSzz("0");
			xtszDto.setOldszlb(GlobalString.PAY_RESULT_FLAG);
			xtszService.update(xtszDto);
			e.printStackTrace();
		}
	}
}
