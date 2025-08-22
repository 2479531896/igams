package com.matridx.igams.wechat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class ReturnCallback implements RabbitTemplate.ReturnCallback{

    private Logger log = LoggerFactory.getLogger(ReturnCallback.class);
    @Override
    public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("returnedMessage进来了");
        log.error("return exchange:" + exchange + ",routingKey:" + routingKey
                + ",replyCode:" + replyCode + ",replyText:" + replyText);
    }
}
