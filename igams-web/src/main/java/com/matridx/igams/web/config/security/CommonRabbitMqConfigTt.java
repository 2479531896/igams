package com.matridx.igams.web.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class CommonRabbitMqConfigTt {
	
	@Autowired  
    private RabbitAdmin rabbitAdmin;
    @Value("${matridx.rabbit.preflg:}")
    private String preRabbitFlg;
	
    private Logger log = LoggerFactory.getLogger(CommonRabbitMqConfigTt.class);
    /**
     * 延迟消息交换机
     */
    public final static String DELAY_EXCHANGE = "delay_exchange";
    /**
     * 延迟消息队列
     */
    public final static String DELAY_QUEUE = "delay_queue";
    /**
     * 延迟消息路由
     */
    public final static String DELAY_ROUTING_KEY = "routingKey";

    /**
     * 正常路由Key
     */
    public final static String ZC_ROUTING_KEY = "zc_routingKey";
    /**
     * 正常交换机
     */
    public final static String ZC_DELAY_EXCHANGE = "zc_delay_exchange";
    /**
     * 延迟消息队列
     */
    public final static String ZC_QUEUE = "zc_queue";
    /**
     * 创建延迟交换机
     * @return
     */

    //创建交互机
    @Bean
    public DirectExchange delay_exchange(){
    	DirectExchange dExchange=new DirectExchange(DELAY_EXCHANGE);  
        rabbitAdmin.declareExchange(dExchange);  
        return dExchange;
        //return new DirectExchange(DELAY_EXCHANGE);
    }
    //创建队列
    @Bean
    public Queue delay_queue(){
        Queue build = QueueBuilder.durable(DELAY_QUEUE).build();
        rabbitAdmin.declareQueue(build);
        return build;
    }
    //绑定
    @Bean
    public Binding bindingDelayExchangeAndQueue(){
    	Binding binding =  BindingBuilder.bind(delay_queue()).to(delay_exchange()).with(DELAY_ROUTING_KEY);
        rabbitAdmin.declareBinding(binding);
		return binding;
    }

    /**
     * 创建正常交换机
     */

    //创建交互机
    @Bean
    public DirectExchange zc_delay_exchange(){

    	DirectExchange dExchange=new DirectExchange(ZC_DELAY_EXCHANGE);  
        rabbitAdmin.declareExchange(dExchange);  
        return dExchange;
        //return new DirectExchange(ZC_DELAY_EXCHANGE);
    }

    //创建队列
    @Bean
    public Queue zc_queue(){
        Map<String ,Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange",DELAY_EXCHANGE);
        map.put("x-dead-letter-routing-key",DELAY_ROUTING_KEY);
        Queue build = QueueBuilder.durable(ZC_QUEUE).withArguments(map).build();
        rabbitAdmin.declareQueue(build);
        return build;
    }
    //绑定
    @Bean
    public Binding zc_bindingDelayExchangeAndQueue(){
    	Binding binding =  BindingBuilder.bind(zc_queue()).to(zc_delay_exchange()).with(ZC_ROUTING_KEY);
        rabbitAdmin.declareBinding(binding);
		return binding;
    }

//    @Bean //connectionFactory 也是要和最上面方法名保持一致
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        return rabbitTemplate;
//    }

    /**
     * 发送钉钉小程序，创建队列
     * @return
     */
    @Bean
    public Queue sendMessageDispose() {
        Queue queue = new Queue(preRabbitFlg+"Send.message.dispose");
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

    /**
     * 发送钉钉小程序，创建交互机
     * @return
     */
    @Bean
    public TopicExchange sendMessageDispose_exchange(){
        TopicExchange sendMessageDisposeExchange=new TopicExchange("send.message.dispose.exchange");
        rabbitAdmin.declareExchange(sendMessageDisposeExchange);
        return sendMessageDisposeExchange;
    }

    /**
     * 交换机与消息队列进行绑定，发送钉钉小程序
     * @param
     * @param
     * @return
     */
    @Bean
    public Binding bindingExchangeSendMessageDispose(Queue sendMessageDispose, TopicExchange sendMessageDispose_exchange) {
        Binding binding = BindingBuilder.bind(sendMessageDispose).to(sendMessageDispose_exchange).with(preRabbitFlg+"Send.message.dispose");
        rabbitAdmin.declareBinding(binding);
        return binding;
    }
}
