package com.matridx.las.netty.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyMqConfig {
	@Autowired
	private RabbitAdmin rabbitAdmin;

	@Value("${matridx.rabbit.flg:}")
	private String rabbitFlg;
	
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;

	/**
	 * 注册合同新增
	 * 
	 * @return
	 */
	@Bean
	public Queue insertHtgl() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.htgl.insert" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册合同修改
	 * 
	 * @return
	 */
	@Bean
	public Queue updateHtgl() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.htgl.update" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册合同高级修改
	 * 
	 * @return
	 */
	@Bean
	public Queue advancedUpdateHtgl() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.htgl.advancedupdate" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
				 

	/**
	 * 注册合同删除
	 * 
	 * @return
	 */
	@Bean
	public Queue delHtgl() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.htgl.del" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册合同外发盖章修改
	 * 
	 * @return
	 */
	@Bean
	public Queue updateOutHtgl() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.htgl.updateOut" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册合同外明细修改
	 * 
	 * @return
	 */
	@Bean
	public Queue updateHtmx() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.htmx.update" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册请购增加
	 * 
	 * @return
	 */
	@Bean
	public Queue insertQggl() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.qggl.insert" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册请购高级修改
	 * 
	 * @return
	 */
	@Bean
	public Queue advancedUpdateQggl() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.qggl.advancedUpdate" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册请购修改
	 * 
	 * @return
	 */
	@Bean
	public Queue updateQggl() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.qggl.update" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册请购修改（审核时候的修改）
	 * 
	 * @return
	 */
	@Bean
	public Queue updateQgglxx() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.qggl.updateQgglxx" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册请购删除
	 * 
	 * @return
	 */
	@Bean
	public Queue delQggl() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.qggl.del" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册请购明细修改
	 * 
	 * @return
	 */
	@Bean
	public Queue updateQgmx() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.qgmx.update" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册审核信息新增
	 * 
	 * @return
	 */
	@Bean
	public Queue insertShxx() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.shxx.insert" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册请购明细修改
	 * 
	 * @return
	 */
	@Bean
	public Queue updatePurchase() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.qgmx.updatePurchase" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册合同明细数量修改
	 * 
	 * @return
	 */
	@Bean
	public Queue updateQuantityComp() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.htmx.updateQuantityComp" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册合同明细U8id修改
	 * 
	 * @return
	 */
	@Bean
	public Queue updateHtmxDtos() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.htmx.updateHtmxDtos" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册取消请购新增
	 * 
	 * @return
	 */
	@Bean
	public Queue insertQxqg() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.qgqxgl.insertQxqg" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册取消请购明细删除
	 * 
	 * @return
	 */
	@Bean
	public Queue delQgqxmx() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.qgqxmx.delQgqxmx" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册取消请购修改
	 * 
	 * @return
	 */
	@Bean
	public Queue updateQgqxgl() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.qgqxgl.updateQgqxgl" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册取消请购明细数量修改
	 * 
	 * @return
	 */
	@Bean
	public Queue updateQgmxByList() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.qgqxmx.updateQgmxByList" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册更新取消请购标记
	 * 
	 * @return
	 */
	@Bean
	public Queue updateQxqg() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.qgqxmx.updateQxqg" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册更新取消请购标记
	 * 
	 * @return
	 */
	@Bean
	public Queue updateWcbjs() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.qggl.updateWcbjs" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册取消请购修改
	 * 
	 * @return
	 */
	@Bean
	public Queue updateQgqxglxx() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.qgqxgl.updateQgqxglxx" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对合同添加消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeInsertHtgl(Queue insertHtgl, TopicExchange sys_exchange) {
		return BindingBuilder.bind(insertHtgl).to(sys_exchange).with(preRabbitFlg + "sys.igams.htgl.insert");
	}

	/**
	 * 交换机与消息队列进行绑定，对合同修改进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateHtgl(Queue updateHtgl, TopicExchange sys_exchange) {
		return BindingBuilder.bind(updateHtgl).to(sys_exchange).with(preRabbitFlg + "sys.igams.htgl.update");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对合同高级修改进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeAdvancedupdateHtgl(Queue advancedUpdateHtgl, TopicExchange sys_exchange) {
		return BindingBuilder.bind(advancedUpdateHtgl).to(sys_exchange).with(preRabbitFlg + "sys.igams.htgl.advancedupdate");
	}

	/**
	 * 交换机与消息队列进行绑定，对合同删除进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelHtgl(Queue delHtgl, TopicExchange sys_exchange) {
		return BindingBuilder.bind(delHtgl).to(sys_exchange).with(preRabbitFlg + "sys.igams.htgl.del");
	}

	/**
	 * 交换机与消息队列进行绑定，对合同外发盖章修改进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateOutHtgl(Queue updateOutHtgl, TopicExchange sys_exchange) {
		return BindingBuilder.bind(updateOutHtgl).to(sys_exchange).with(preRabbitFlg + "sys.igams.htgl.updateOut");
	}

	/**
	 * 交换机与消息队列进行绑定，对合同明细修改进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateHtmx(Queue updateHtmx, TopicExchange sys_exchange) {
		return BindingBuilder.bind(updateHtmx).to(sys_exchange).with(preRabbitFlg + "sys.igams.htmx.update");
	}

	/**
	 * 交换机与消息队列进行绑定，对请购增加进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeInsertQggl(Queue insertQggl, TopicExchange sys_exchange) {
		return BindingBuilder.bind(insertQggl).to(sys_exchange).with(preRabbitFlg + "sys.igams.qggl.insert");
	}

	/**
	 * 交换机与消息队列进行绑定，对请购高级修改进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeAdvancedUpdateQggl(Queue advancedUpdateQggl, TopicExchange sys_exchange) {
		return BindingBuilder.bind(advancedUpdateQggl).to(sys_exchange).with(preRabbitFlg + "sys.igams.qggl.advancedUpdate");
	}
				 

	/**
	 * 交换机与消息队列进行绑定，对请购修改进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateQggl(Queue updateQggl, TopicExchange sys_exchange) {
		return BindingBuilder.bind(updateQggl).to(sys_exchange).with(preRabbitFlg + "sys.igams.qggl.update");
	}

	/**
	 * 交换机与消息队列进行绑定，对请购删除进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelQggl(Queue delQggl, TopicExchange sys_exchange) {
		return BindingBuilder.bind(delQggl).to(sys_exchange).with(preRabbitFlg + "sys.igams.qggl.del");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对请购修改（审核时候的修改）进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateQgglxx(Queue updateQgglxx, TopicExchange sys_exchange) {
		return BindingBuilder.bind(updateQgglxx).to(sys_exchange).with(preRabbitFlg + "sys.igams.qggl.updateQgglxx");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对请购明细修改进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateQgmx(Queue updateQgmx, TopicExchange sys_exchange) {
		return BindingBuilder.bind(updateQgmx).to(sys_exchange).with(preRabbitFlg + "sys.igams.qgmx.update");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对审核信息新增进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeInsertShxx(Queue insertShxx, TopicExchange sys_exchange) {
		return BindingBuilder.bind(insertShxx).to(sys_exchange).with(preRabbitFlg + "sys.igams.shxx.insert");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对请购明细修改进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdatePurchase(Queue updatePurchase, TopicExchange sys_exchange) {
		return BindingBuilder.bind(updatePurchase).to(sys_exchange).with(preRabbitFlg + "sys.igams.qgmx.updatePurchase");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对合同明细数量修改进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateQuantityComp(Queue updateQuantityComp, TopicExchange sys_exchange) {
		return BindingBuilder.bind(updateQuantityComp).to(sys_exchange).with(preRabbitFlg + "sys.igams.htmx.updateQuantityComp");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对合请购完成标记修改进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateWcbjs(Queue updateWcbjs, TopicExchange sys_exchange) {
		return BindingBuilder.bind(updateWcbjs).to(sys_exchange).with(preRabbitFlg + "sys.igams.qggl.updateWcbjs");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对合同明细U8Id修改进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateHtmxDtos(Queue updateHtmxDtos, TopicExchange sys_exchange) {
		return BindingBuilder.bind(updateHtmxDtos).to(sys_exchange).with(preRabbitFlg + "sys.igams.htmx.updateHtmxDtos");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对取消请购新增进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeInsertQxqg(Queue insertQxqg, TopicExchange sys_exchange) {
		return BindingBuilder.bind(insertQxqg).to(sys_exchange).with(preRabbitFlg + "sys.igams.qgqxgl.insertQxqg");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对取消请购明细删除进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelQgqxmx(Queue delQgqxmx, TopicExchange sys_exchange) {
		return BindingBuilder.bind(delQgqxmx).to(sys_exchange).with(preRabbitFlg + "sys.igams.qgqxmx.delQgqxmx");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对取消请购修改进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateQgqxgl(Queue updateQgqxgl, TopicExchange sys_exchange) {
		return BindingBuilder.bind(updateQgqxgl).to(sys_exchange).with(preRabbitFlg + "sys.igams.qgqxgl.updateQgqxgl");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对取消请购明细数量修改进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateQgmxByList(Queue updateQgmxByList, TopicExchange sys_exchange) {
		return BindingBuilder.bind(updateQgmxByList).to(sys_exchange).with(preRabbitFlg + "sys.igams.qgqxmx.updateQgmxByList");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对更新请购取消标记进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateQxqg(Queue updateQxqg, TopicExchange sys_exchange) {
		return BindingBuilder.bind(updateQxqg).to(sys_exchange).with(preRabbitFlg + "sys.igams.qgqxmx.updateQxqg");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对请购取消修改进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateQgqxglxx(Queue updateQgqxglxx, TopicExchange sys_exchange) {
		return BindingBuilder.bind(updateQgqxglxx).to(sys_exchange).with(preRabbitFlg + "sys.igams.qgqxgl.updateQgqxglxx");
	}
}
