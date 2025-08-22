package com.matridx.localization.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.rabbitmq.report.notopen", havingValue = "false")
public class RabbitMqLocalConfig {

    @Autowired
    @Qualifier("rabbitLocalAdmin")
    private RabbitAdmin rabbitAdmin;
    @Bean
    public Queue receiveLocalInspectQueue() {
        Queue queue = new Queue("matridx.local.inspect.enter");
        rabbitAdmin.declareQueue(queue);
        return queue;
    }
    @Bean
    public Queue receiveLocalSendMessageQueue() {
        Queue queue = new Queue("matridx.inspect.report.sendMessage");
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

    //创建交互机
    @Bean
    public TopicExchange receiveLocalInspectExchange(){
        TopicExchange receiveLocalInspectExchange=new TopicExchange("local.exchange");
        rabbitAdmin.declareExchange(receiveLocalInspectExchange);
        return receiveLocalInspectExchange;
    }

    @Bean
    public Binding bindingExchangeReceiveLocalInspectQueue(Queue receiveLocalInspectQueue, TopicExchange receiveLocalInspectExchange) {
        Binding binding = BindingBuilder.bind(receiveLocalInspectQueue).to(receiveLocalInspectExchange).with("matridx.local.inspect.enter");
        rabbitAdmin.declareBinding(binding);
        return binding;
    }
    @Bean
    public Binding bindingExchangeSendMessageQueue(Queue receiveLocalSendMessageQueue, TopicExchange receiveLocalInspectExchange) {
        Binding binding = BindingBuilder.bind(receiveLocalSendMessageQueue).to(receiveLocalInspectExchange).with("matridx.inspect.report.sendMessage");
        rabbitAdmin.declareBinding(binding);
        return binding;
    }
}
