package com.matridx.igams.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
/**
 * MQ工具类
 * @author Administrator
 * 注意密码和mq管理平台的访问端口
 */
@Component
public class MQUtils {
    private static final Logger logger = LoggerFactory.getLogger(MQUtils.class);
    @Autowired
	private RabbitTemplate rabbitTemplate;
    @Value("${spring.rabbitmq.password:}")
	private String password;

    /**
     * 队列任务总数
     *
     * @param queueName
     * @return
     */
    public int getMessageCount(String queueName) throws IOException {
        String apiMessage = getApiMessage(queueName);
        if (Objects.equals(apiMessage, "")) {
            logger.error("请求RabbitMQ API时出错！！");
            return 0;
        }
        JSONObject jsonObject = JSON.parseObject(apiMessage);
        return Integer.parseInt(jsonObject.get("messages").toString());
    }

    /**
     * 队列ready任务数
     *
     * @param queueName
     * @return
     */
    public int getMessageReadyCount(String queueName) throws IOException {
        String apiMessage = getApiMessage(queueName);
        if (Objects.equals(apiMessage, "")) {
            logger.error("请求RabbitMQ API时出错！！");
            return 0;
        }
        JSONObject jsonObject = JSON.parseObject(apiMessage);
        return Integer.parseInt(jsonObject.get("messages_ready").toString());
    }

    /**
     * 队列unack数MQ
     *
     * @param queueName
     * @return
     */
    public int getMessagesUnacknowledgedCount(String queueName) throws IOException {
        String apiMessage = getApiMessage(queueName);
        if (Objects.equals(apiMessage, "")) {
            logger.error("请求RabbitMQ API时出错！！");
            return 0;
        }
        JSONObject jsonObject = JSON.parseObject(apiMessage);
        return Integer.parseInt(jsonObject.get("messages_unacknowledged").toString());
    }

    /**
     * 获取队列消息总数、ready消息数、unack消息数
     *
     * @param queueName
     * @return Map<String,Integer>
     */
    public Map<String, Integer> getMQCountMap(String queueName) throws IOException {
        String apiMessage = getApiMessage(queueName);
        JSONObject jsonObject = JSON.parseObject(apiMessage);
        Map<String, Integer> map = new HashMap<>();
        map.put("messages", Integer.parseInt(jsonObject.get("messages").toString()));
        map.put("messages_ready", Integer.parseInt(jsonObject.get("messages_ready").toString()));
        map.put("messages_unacknowledged", Integer.parseInt(jsonObject.get("messages_unacknowledged").toString()));
        return map;
    }

    public String getApiMessage(String queueName) throws IOException {
    	ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        //发送一个GET请求
        HttpURLConnection httpConn;
        BufferedReader in;
        String urlString = "http://" + connectionFactory.getHost() + ":" + "15672" + "/api/queues/%2F/"+ queueName;
        URL url = new URL(urlString);
        httpConn = (HttpURLConnection) url.openConnection();
        //设置用户名密码
        String auth = connectionFactory.getUsername() + ":" + password ;
        String encoding = Base64.encodeBase64String(auth.getBytes());
        httpConn.setDoOutput(true);
        httpConn.setRequestProperty("Authorization", "Basic " + encoding);
        // 建立实际的连接
        httpConn.connect();
        //读取响应
        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            StringBuilder content = new StringBuilder();
            String tempStr;
            in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            while ((tempStr = in.readLine()) != null) {
                content.append(tempStr);
            }
            in.close();
            httpConn.disconnect();
            return content.toString();
        } else {
            httpConn.disconnect();
            return "";
        }
    }
}