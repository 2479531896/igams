package com.matridxcomm.hrm.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class HrmRabbitMqConfig {

	@Value("${matridx.rabbit.flg:}")
	private String rabbitFlg;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;

	/**
	 * 考试提交
	 */
	@Bean
	public Queue saveTestResults() {
		return new Queue(preRabbitFlg+"sys.igams.grks.submit" +rabbitFlg);
	}

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

	/**
	 * 交换机与消息队列进行绑定,新增个人考试信息
	 */
	@Bean
	public Binding bindingExchangeInsertGrksgl(Queue saveTestResults, TopicExchange sys_exchange) {
		return BindingBuilder.bind(saveTestResults).to(sys_exchange).with(preRabbitFlg+"sys.igams.grks.submit" +rabbitFlg);
	}

}
