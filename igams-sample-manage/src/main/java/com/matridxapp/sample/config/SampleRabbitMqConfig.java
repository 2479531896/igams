package com.matridxapp.sample.config;


import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SampleRabbitMqConfig {
	
	
	/**
	 * 交换机
	 */
	@Bean
	public TopicExchange exchange() {
       return new TopicExchange("wechat.exchange");
	}

	@Bean
	public TopicExchange doc2pdf_exchange() {
       return new TopicExchange("doc2pdf_exchange");
	}
	

	
}
