package com.matridx.igams.wechat.config;

import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "matridx",name="openMq2",havingValue = "true")
public class Amq2Config {

    @Autowired
    private RabbitTwoMq rabbitMq;
    private final Logger log = LoggerFactory.getLogger(Amq2Config.class);
    @Bean(name="connectionFactory2")
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory=new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitMq.getAddress());
        connectionFactory.setHost(rabbitMq.getHost());
        connectionFactory.setUsername(rabbitMq.getUsername());
        connectionFactory.setPassword(rabbitMq.getPassword());
        connectionFactory.setPort(Integer.parseInt(rabbitMq.getPort()));
        connectionFactory.setVirtualHost(StringUtil.isBlank(rabbitMq.getVirtualHost())?"/":rabbitMq.getVirtualHost());
        log.error("rabbitmq2 host:" + rabbitMq.getHost() + " Addresses:" + rabbitMq.getAddress() + " name:" + rabbitMq.getUsername() + " pass:" + rabbitMq.getPassword()+ " port:" + rabbitMq.getPort() + " virtualHost:" + rabbitMq.getVirtualHost());
        /*
          对于每一个RabbitTemplate只支持一个ReturnCallback。
          对于返回消息，模板的mandatory属性必须被设定为true，
          它同样要求CachingConnectionFactory的publisherReturns属性被设定为true。
          如果客户端通过调用setReturnCallback(ReturnCallback callback)注册了RabbitTemplate.ReturnCallback，那么返回将被发送到客户端。
          这个回调函数必须实现下列方法：
         void returnedMessage(Message message, intreplyCode, String replyText,String exchange, String routingKey);
         */
        // connectionFactory.setPublisherReturns(true);
        /*
          同样一个RabbitTemplate只支持一个ConfirmCallback。
          对于发布确认，template要求CachingConnectionFactory的publisherConfirms属性设置为true。
          如果客户端通过setConfirmCallback(ConfirmCallback callback)注册了RabbitTemplate.ConfirmCallback，那么确认消息将被发送到客户端。
          这个回调函数必须实现以下方法：
          void confirm(CorrelationData correlationData, booleanack);
         */
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        return connectionFactory;
    }

    /**
     * rabbitAdmin代理类
     */
    @Bean(name="rabbitAdmin2")
    public RabbitAdmin rabbitAdmin( @Qualifier("connectionFactory2")ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    @Bean(name="defaultFactory2")
    public SimpleRabbitListenerContainerFactory defaultFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("connectionFactory2") ConnectionFactory connectionFactory
    )
    {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
