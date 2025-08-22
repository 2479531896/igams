package com.matridx.igams.wechat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomerConfirmCallback  implements RabbitTemplate.ConfirmCallback {

    private Logger log = LoggerFactory.getLogger(CustomerConfirmCallback.class);

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//        if (ack) {
//            log.error("消息发送成功：" + correlationData);
//        } else {
//            log.error("消息发送失败：" + cause);
//        }
    }

}
