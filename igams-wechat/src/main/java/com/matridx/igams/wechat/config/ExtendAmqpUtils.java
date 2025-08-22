package com.matridx.igams.wechat.config;

import com.alibaba.fastjson.JSON;
import com.matridx.springboot.util.base.StringUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Component
public class ExtendAmqpUtils {
    @Autowired
    private ReportRabbitMq reportRabbitMq;

    private final Logger log = LoggerFactory.getLogger(ExtendAmqpUtils.class);

    public boolean sendDownloadReportMsg(Map<String,Object> map){
        log.error("sendDownloadReportMsg内容："+JSON.toJSONString(map));
        map.put("QUEUE_NAME","matridx.inspect.report.notify");
        map.put("EXCHANGE_NAME","report.exchange");
        return sendMsg(map);
    }
    public boolean sendMsg(Map<String,Object> map){
        if(map.get("VirtualHost")==null){
            log.error("sendMsg VirtualHost为空:" + JSON.toJSONString(map));
            return false;
        }
        if(map.get("QUEUE_NAME")==null){
            log.error("sendMsg QUEUE_NAME为空:" + JSON.toJSONString(map));
            return false;
        }
        if(map.get("EXCHANGE_NAME")==null){
            log.error("sendMsg EXCHANGE_NAME为空:" + JSON.toJSONString(map));
            return false;
        }
        String VirtualHost = map.get("VirtualHost").toString();
        if (StringUtil.isBlank(VirtualHost)){
            log.error("sendMsg VirtualHost为空:" + JSON.toJSONString(map));
            return false;
        }
        //队列
        String QUEUE_NAME = map.get("QUEUE_NAME").toString();
        //交换机
        String EXCHANGE_NAME = map.get("EXCHANGE_NAME").toString();
        String routingKey = QUEUE_NAME;
        
        log.error("sendDownloadReport 发送对象： host:"+reportRabbitMq.getHost() + " name:"+reportRabbitMq.getUsername()
        + " pw:"+reportRabbitMq.getPassword() + " port:"+reportRabbitMq.getPort() + " VirtualHost:"+VirtualHost);
        String message = JSON.toJSONString(map);
        if(StringUtil.isAnyBlank(reportRabbitMq.getHost(),reportRabbitMq.getUsername(),
                reportRabbitMq.getPassword(),reportRabbitMq.getPort())){
            return false;
        }
        boolean isSuccess = false;
        Connection connection = null;
        Channel channelReport = null;
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(reportRabbitMq.getHost());
            connectionFactory.setUsername(reportRabbitMq.getUsername());
            connectionFactory.setPassword(reportRabbitMq.getPassword());
            connectionFactory.setPort(Integer.parseInt(reportRabbitMq.getPort()));
            connectionFactory.setVirtualHost(VirtualHost);
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
            log.error("reportRabbit 获取连接IO异常或发送消息异常:" + e.getMessage() + " host:" + reportRabbitMq.getHost() 
            	+ " name:" + reportRabbitMq.getUsername() + " pw:" + reportRabbitMq.getPassword() + " port:" + reportRabbitMq.getPort()
            	 + " VirtualHost:" + VirtualHost);
        } catch (TimeoutException e) {
            log.error("reportRabbit 获取连接超时:" + e.getMessage());
        }finally {
            if (channelReport!=null){
                try {
                    channelReport.close();
                } catch (IOException e) {
                    log.error("reportRabbit 关闭channel失败:" + e.getMessage());
                } catch (TimeoutException e) {
                    log.error("reportRabbit 关闭channel超时:" + e.getMessage());
                }
            }
            if (connection!=null){
                try {
                    connection.close();
                } catch (IOException e) {
                    log.error("reportRabbit 关闭连接失败:" + e.getMessage());
                }
            }
        }
        return isSuccess;
    }
}
