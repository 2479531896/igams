package com.matridx.igams.wechat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

//@Component
public class ReportRabbitMqReceive {
	
	private Logger log = LoggerFactory.getLogger(ReportRabbitMqReceive.class);
	
	@Resource(name="reportRabbitTemplate")
	private AmqpTemplate reportRabbitTemplate;
	
	/*@RabbitListener(queues = "wechar.menu.click", containerFactory="defaultFactory")
	public void menuClick(String str) {
		
		log.error("Receive Menu Click:"+str);
    }*/
	
}
