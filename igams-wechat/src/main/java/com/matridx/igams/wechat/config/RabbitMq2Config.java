package com.matridx.igams.wechat.config;


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
@ConditionalOnProperty(prefix = "matridx",name="openMq2",havingValue = "true")
public class RabbitMq2Config {

    @Autowired
    @Qualifier("rabbitAdmin2")
    private RabbitAdmin rabbitAdmin;
    @Bean
    public Queue sendInspectQueue() {
        Queue queue = new Queue("matridx2.inspect.report.send");
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

    //创建交互机
    @Bean
    public TopicExchange sendInspectExchange(){
        TopicExchange sendInspectExchange=new TopicExchange("reportSend2.exchange");
        rabbitAdmin.declareExchange(sendInspectExchange);
        return sendInspectExchange;
    }

    @Bean
    public Binding bindingExchangeSendInspectQueue(Queue sendInspectQueue, TopicExchange sendInspectExchange) {
        Binding binding = BindingBuilder.bind(sendInspectQueue).to(sendInspectExchange).with("matridx2.inspect.report.send");
        rabbitAdmin.declareBinding(binding);
        return binding;
    }
}
