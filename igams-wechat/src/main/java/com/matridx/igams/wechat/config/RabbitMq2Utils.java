package com.matridx.igams.wechat.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.springboot.util.base.StringUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Component
@ConditionalOnProperty(prefix = "matridx",name="openMq2",havingValue = "true")
public class RabbitMq2Utils {


    @Autowired
    private RabbitTwoMq rabbitTwoMq;



    private final Logger log = LoggerFactory.getLogger(RabbitMq2Utils.class);

    public boolean sendDownloadReportMsg(Map<String,String> map){
        log.error("sendDownloadReportMsg内容："+ JSONObject.toJSONString(map));
        map.put("QUEUE_NAME","matridx2.inspect.report.send");
        map.put("EXCHANGE_NAME","reportSend2.exchange");
        return sendMsg(map);
    }
    public boolean sendMsg(Map<String,String> map){

        //队列
        String QUEUE_NAME = map.get("QUEUE_NAME");
        //交换机
        String EXCHANGE_NAME = map.get("EXCHANGE_NAME");
        String routingKey = QUEUE_NAME;

        log.error("sendDownloadReport 发送对象： host:"+rabbitTwoMq.getHost() + " name:"+rabbitTwoMq.getUsername()
                + " pw:"+rabbitTwoMq.getPassword() + " port:"+rabbitTwoMq.getPort() );
        String message = JSONObject.toJSONString(map);
        if(StringUtil.isAnyBlank(rabbitTwoMq.getHost(),rabbitTwoMq.getUsername(),
                rabbitTwoMq.getPassword(),rabbitTwoMq.getPort())){
            return false;
        }
        boolean isSuccess = false;
        Connection connection = null;
        Channel channelReport = null;
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(rabbitTwoMq.getHost());
            connectionFactory.setUsername(rabbitTwoMq.getUsername());
            connectionFactory.setPassword(rabbitTwoMq.getPassword());
            connectionFactory.setPort(Integer.parseInt(rabbitTwoMq.getPort()));
            // 设置连接超时时间为5秒
            connectionFactory.setConnectionTimeout(5000); // 单位是毫秒
            connection = connectionFactory.newConnection();
            channelReport = connection.createChannel();
            // 声明一个队列，如果队列不存在会被创建
            channelReport.queueDeclare(QUEUE_NAME, true, false, false, null);

            // 发布消息到队列中
            channelReport.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            isSuccess = true;
        } catch (IOException e) {
            log.error("reportRabbit2 获取连接IO异常或发送消息异常:" + e.getMessage() + " host:" + rabbitTwoMq.getHost()
                    + " name:" + rabbitTwoMq.getUsername() + " pw:" + rabbitTwoMq.getPassword() + " port:" + rabbitTwoMq.getPort()
                    );
        } catch (TimeoutException e) {
            log.error("reportRabbit2 获取连接超时:" + e.getMessage());
        }finally {
            if (channelReport!=null){
                try {
                    channelReport.close();
                } catch (IOException e) {
                    log.error("reportRabbit2 关闭channel失败:" + e.getMessage());
                } catch (TimeoutException e) {
                    log.error("reportRabbit2 关闭channel超时:" + e.getMessage());
                }
            }
            if (connection!=null){
                try {
                    connection.close();
                } catch (IOException e) {
                    log.error("reportRabbit2 关闭连接失败:" + e.getMessage());
                }
            }
        }
        return isSuccess;
    }
}
