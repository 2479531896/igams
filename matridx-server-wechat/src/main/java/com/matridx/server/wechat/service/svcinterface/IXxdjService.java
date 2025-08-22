package com.matridx.server.wechat.service.svcinterface;

import org.springframework.web.client.RestTemplate;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.XxdjDto;
import com.matridx.server.wechat.dao.entities.XxdjModel;

public interface IXxdjService extends BaseBasicService<XxdjDto, XxdjModel>{

	/**
	 * 保存消息登记表并发送消息队列到本地保存
	 * @param xxdjDto
	 * @return
	 */
    boolean saveRegister(RestTemplate restTemplate, XxdjDto xxdjDto);
}
