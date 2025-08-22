package com.matridx.igams.common.config.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.matridx.springboot.util.base.StringUtil;

@Configuration
public class AmqpConfig {
	@Autowired
	private RabbitMq rabbitMq;
	
	private final Logger log = LoggerFactory.getLogger(AmqpConfig.class);
	/** 
	 * 连接rabbitmq
     */  
    @Bean
    @Primary
    public ConnectionFactory connectionFactory(){  
    	CachingConnectionFactory connectionFactory=new CachingConnectionFactory();  
        connectionFactory.setAddresses(rabbitMq.getAddress());
        connectionFactory.setHost(rabbitMq.getHost());
        connectionFactory.setUsername(rabbitMq.getUsername());
        connectionFactory.setPassword(rabbitMq.getPassword());
        connectionFactory.setPort(Integer.parseInt(rabbitMq.getPort()));
        connectionFactory.setVirtualHost(StringUtil.isBlank(rabbitMq.getVirtualHost())?"/":rabbitMq.getVirtualHost());
        log.error("rabbitmq host:" + rabbitMq.getHost() + " Addresses:" + rabbitMq.getAddress() + " name:" + rabbitMq.getUsername() + " pass:" + rabbitMq.getPassword()+ " port:" + rabbitMq.getPort() + " virtualHost:" + rabbitMq.getVirtualHost());
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
        connectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);  
        return connectionFactory;  
    }
    
    /** 
     * rabbitAdmin代理类
     */  
    @Bean  
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){  
        return new RabbitAdmin(connectionFactory);  
    }
    
    @Bean(name="defaultFactory")
	public SimpleRabbitListenerContainerFactory defaultFactory(
			SimpleRabbitListenerContainerFactoryConfigurer configurer,
			@Qualifier("connectionFactory") ConnectionFactory connectionFactory
			) 
    {
    	SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    	configurer.configure(factory, connectionFactory);
    	return factory;
	}
    @Bean(name="specialFactory")
    public SimpleRabbitListenerContainerFactory specialFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("connectionFactory") ConnectionFactory connectionFactory
    )
    {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setPrefetchCount(1);
        configurer.configure(factory, connectionFactory);
        return factory;
    }
    @Bean(name="rabbitTemplate")
    @Primary
	public RabbitTemplate rabbitTemplate(@Qualifier("connectionFactory") ConnectionFactory connectionFactory){
        return new RabbitTemplate(connectionFactory);
    }
    
}
