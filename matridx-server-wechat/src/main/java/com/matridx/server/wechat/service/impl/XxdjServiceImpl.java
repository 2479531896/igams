package com.matridx.server.wechat.service.impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.server.wechat.dao.entities.XxdjDto;
import com.matridx.server.wechat.dao.entities.XxdjModel;
import com.matridx.server.wechat.dao.post.IXxdjDao;
import com.matridx.server.wechat.enums.MQWechatTypeEnum;
import com.matridx.server.wechat.service.svcinterface.IXxdjService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class XxdjServiceImpl extends BaseBasicServiceImpl<XxdjDto, XxdjModel, IXxdjDao> implements IXxdjService{
	@Autowired(required=false)
	private AmqpTemplate amqpTempl; 
	@Autowired
	IXtszService xtszService;
	
	/**
	 * 保存消息登记表并发送消息队列到本地保存
	 * @param xxdjDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean saveRegister(RestTemplate restTemplate, XxdjDto xxdjDto){
		// TODO Auto-generated method stub
		String xxid = xxdjDto.getXxid();
		boolean result;
		if(StringUtil.isNotBlank(xxid)){
			result =update(xxdjDto);
		}else{
			xxdjDto.setXxid(StringUtil.generateUUID());
			result =insert(xxdjDto);
		}
		if(result) {
			amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.REGISTER_SEND.getCode(), JSONObject.toJSONString(xxdjDto));
		}
		return result;
	}

}
