package com.matridxapp.las.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class NettyRabbitMqConfig {
	
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	
	/**
	 * 文档转换完成OK
	 */
	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private String DOC_OK = null;
	
	/**
	 * 文档转换完成OK
	 * @return
	 */
	@Bean
	public Queue docTranOkQueue() {
		return new Queue(DOC_OK);
	}
	
	/**
	 * 交换机
	 * @return
	 */
	@Bean
	public TopicExchange exchange() {
       return new TopicExchange("wechat.exchange");
	}

	@Bean
	public TopicExchange doc2pdf_exchange() {
       return new TopicExchange("doc2pdf_exchange");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对文档 转换进行绑定
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
    public Binding bindingExchangeclouddocTranOk(Queue docTranOkQueue, TopicExchange doc2pdf_exchange) {
        return BindingBuilder.bind(docTranOkQueue).to(doc2pdf_exchange).with(DOC_OK);
    }
	
}
