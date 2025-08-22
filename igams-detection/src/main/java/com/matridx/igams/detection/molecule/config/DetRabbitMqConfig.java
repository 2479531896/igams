package com.matridx.igams.detection.molecule.config;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.matridx.igams.detection.molecule.enums.DetMQTypeEnum;

@Configuration
public class DetRabbitMqConfig {

	@Autowired
	private RabbitAdmin rabbitAdmin;
	/**
	 * 新冠预约检测同步
	 */
	@Bean
	public Queue detectionAppointmentQueue() {
		Queue queue = new Queue(DetMQTypeEnum.APPOINTMENT_DETECTION.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 新冠预约检测同步
	 */
	@Bean
	public Queue detectAppointmentQueue() {
		Queue queue = new Queue(DetMQTypeEnum.APPOINTMENT_DETECT.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 交换机
	 */
	@Bean
    public TopicExchange detExchange() {
		TopicExchange dExchange = new TopicExchange("detection.exchange");
		rabbitAdmin.declareExchange(dExchange);
		return dExchange;
    }

	/**
	 * 普检同步至微信
	 */
	@Bean
	public Queue normalInspectToWechatQueue() {
		Queue queue = new Queue(DetMQTypeEnum.NORMAL_INSPECT_TOWECHAT.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	/**
	 * 普检操作同步至OA
	 * @return
	 */
	@Bean
	public Queue normalInspectToOAQueue() {
		Queue queue = new Queue(DetMQTypeEnum.NORMAL_INSPECT_TOOA.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	/**
	 * 交换机与消息队列进行绑定，对新冠预约检测操作进行绑定
	 */
	@Bean
	public Binding bindingExchangeDetectionAppointment(Queue detectionAppointmentQueue, TopicExchange detExchange) {
		Binding binding = BindingBuilder.bind(detectionAppointmentQueue).to(detExchange).with(DetMQTypeEnum.APPOINTMENT_DETECTION.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对新冠预约检测操作进行绑定
	 */
	@Bean
	public Binding bindingExchangeDetectAppointment(Queue detectAppointmentQueue, TopicExchange detExchange) {
		Binding binding = BindingBuilder.bind(detectAppointmentQueue).to(detExchange).with(DetMQTypeEnum.APPOINTMENT_DETECT.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	/**
	 * 交换机与消息队列进行绑定，对普检操作进行绑定
	 * @param normalInspectToWechatQueue
	 * @param detExchange
	 */
	@Bean
	public Binding bindingExchangeNormalInspectToWechat(Queue normalInspectToWechatQueue, TopicExchange detExchange) {
		Binding binding = BindingBuilder.bind(normalInspectToWechatQueue).to(detExchange).with(DetMQTypeEnum.NORMAL_INSPECT_TOWECHAT.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	/**
	 * 交换机与消息队列进行绑定，对普检操作进行绑定
	 * @param normalInspectToOAQueue
	 * @param detExchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeNormalInspectToOA(Queue normalInspectToOAQueue, TopicExchange detExchange) {
		Binding binding = BindingBuilder.bind(normalInspectToOAQueue).to(detExchange).with(DetMQTypeEnum.NORMAL_INSPECT_TOOA.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
}
