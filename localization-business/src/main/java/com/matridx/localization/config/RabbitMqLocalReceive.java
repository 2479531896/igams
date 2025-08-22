package com.matridx.localization.config;

import com.matridx.localization.service.svcinterface.ILocalizationHomePageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "spring.rabbitmq.report.notopen", havingValue = "false")
public class RabbitMqLocalReceive {

    @Autowired
    private ILocalizationHomePageService localizationHomePageService;


    private Logger log = LoggerFactory.getLogger(RabbitMqLocalReceive.class);

    /**
     * 接收本地消息
     * @param str
     */
    @RabbitListener(queues = ("matridx.local.inspect.enter"), containerFactory="localFactory")
    public void receive(String str) {
        log.error("LOCAL_MESSAGE：" + str);
        try {
            localizationHomePageService.dealLocalHospitalInspect(str);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * @Description: 发送rabbit消息
     * @param str
     * @return void
     * @Author: 郭祥杰
     * @Date: 2025/4/27 14:29
     */
    @RabbitListener(queues = ("matridx.inspect.report.sendMessage"), containerFactory="localFactory")
    public void sendMessage(String str) {
        try {
            localizationHomePageService.sendMessage(str);
        }catch (Exception e){
            log.error("sendMessage：" + e);
        }
    }
}
