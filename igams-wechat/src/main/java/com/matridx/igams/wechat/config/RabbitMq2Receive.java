package com.matridx.igams.wechat.config;

import com.alibaba.druid.support.logging.Log;
import com.matridx.igams.common.enums.MQTypeEnum_pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Component
@ConditionalOnProperty(prefix = "matridx",name="openMq2",havingValue = "true")
public class RabbitMq2Receive {
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;

    @Value("${spring.mq2.sleeptime:10000}")
    private String rabbittime;

    private Logger log = LoggerFactory.getLogger(RabbitMq2Receive.class);

    @RabbitListener(queues = ("matridx2.inspect.report.send"), containerFactory="defaultFactory2")
    public void sendReportMq2(String str) throws Exception {
        try {
            amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),str);
            log.error("matridx2 重发正常！");
        }catch (Exception e){
            Thread.sleep(Integer.valueOf(rabbittime));
            log.error("matridx2 重发失败！延迟时间为：" + rabbittime);
            throw new Exception("网络故障");
        }

    }
}
