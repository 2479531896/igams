package com.matridx.igams.common.config.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CommonRabbitMqConfig {

	@Autowired  
    private RabbitAdmin rabbitAdmin;

	@Value("${matridx.rabbit.flg:}")
	private String rabbitFlg;

	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;

	@Value("${matridx.systemflg.remindflg:1}")
	private String remindflg;

	//创建队列
	@Bean
	public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
		return new RepublishMessageRecoverer(rabbitTemplate,"retry.exchange","retry.fail.key");
	}

	//创建队列
	@Bean
	public Queue retryFailQueue() {
		Queue queue = new Queue("retry.fail.queue");
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	@Bean
	public Queue sendInspectQueue() {
		Queue queue = new Queue("matridx.inspect.report.send");
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	//创建交互机
	@Bean
	public TopicExchange retryExchange(){
		TopicExchange retryExchange=new TopicExchange("retry.exchange");
		rabbitAdmin.declareExchange(retryExchange);
		return retryExchange;
	}
	//创建交互机
	@Bean
	public TopicExchange sendInspectExchange(){
		TopicExchange sendInspectExchange=new TopicExchange("reportSend.exchange");
		rabbitAdmin.declareExchange(sendInspectExchange);
		return sendInspectExchange;
	}
	@Bean
	public Binding bindingExchangeSendInspectQueue(Queue sendInspectQueue, TopicExchange sendInspectExchange) {
		Binding binding = BindingBuilder.bind(sendInspectQueue).to(sendInspectExchange).with("matridx.inspect.report.send");
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	//绑定
	@Bean
	public Binding bindingExchangeRetryFailQueue(Queue retryFailQueue, TopicExchange retryExchange) {
		Binding binding = BindingBuilder.bind(retryFailQueue).to(retryExchange).with("retry.fail.key");
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	//创建队列
	@Bean
	public Queue reportOutSideSend() {
		Queue queue = new Queue(preRabbitFlg+"wechar.report.outside.send.queue"+rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}


	//创建交互机
	@Bean
	public TopicExchange report_outside_send_exchange(){
		TopicExchange contractTopicExchange=new TopicExchange("wechar.report.outside.send.exchange");  
        rabbitAdmin.declareExchange(contractTopicExchange);  
        return contractTopicExchange;  
        //return new TopicExchange("wechar.report.outside.send.exchange");
	}

	//绑定
	@Bean
	public Binding bindingExchangeReportOutSideSend(Queue reportOutSideSend, TopicExchange report_outside_send_exchange) {
		Binding binding = BindingBuilder.bind(reportOutSideSend).to(report_outside_send_exchange).with(preRabbitFlg+"wechar.report.outside.send.key"+rabbitFlg);
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(reportOutSideSend).to(report_outside_send_exchange).with("wechar.report.outside.send.key");
	}

	//创建延时交互机
	@Bean
	public DirectExchange xx_delay_exchange(){
		DirectExchange dExchange=new DirectExchange("xx_delay_exchange");  
        rabbitAdmin.declareExchange(dExchange);  
        return dExchange; 
        
		//return new DirectExchange("xx_delay_exchange");
	}
	//创建延时队列
	@Bean
	public Queue xx_delay_queue(){
		Map<String ,Object> map = new HashMap<>();
		map.put("x-dead-letter-exchange","wechar.report.outside.send.exchange");
		map.put("x-dead-letter-routing-key",preRabbitFlg+"wechar.report.outside.send.key"+rabbitFlg);
		Queue xx_delay_queue = QueueBuilder.durable(preRabbitFlg+"xx_delay_queue"+rabbitFlg).withArguments(map).build();
		rabbitAdmin.declareQueue(xx_delay_queue);
		return xx_delay_queue;
	}
	//延时队列绑定
	@Bean
	public Binding xxBindingDelayExchangeAndQueue(){
		Binding binding = BindingBuilder.bind(xx_delay_queue()).to(xx_delay_exchange()).with(preRabbitFlg+"xx_delay_key"+rabbitFlg);
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(xx_delay_queue()).to(xx_delay_exchange()).with("xx_delay_key");
	}

	//创建队列
	@Bean
	public Queue ddMesageSend() {
		Queue queue = new Queue(preRabbitFlg+"wechar.dd.mesage.queue"+rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}


	//创建交互机
	@Bean
	public TopicExchange dd_mesage_exchange(){
		TopicExchange contractTopicExchange=new TopicExchange("wechar.dd.mesage.exchange");
		rabbitAdmin.declareExchange(contractTopicExchange);
		return contractTopicExchange;
		//return new TopicExchange("wechar.report.outside.send.exchange");
	}

	//绑定
	@Bean
	public Binding bindingExchangeddMesage(Queue ddMesageSend, TopicExchange dd_mesage_exchange) {
		Binding binding = BindingBuilder.bind(ddMesageSend).to(dd_mesage_exchange).with(preRabbitFlg+"wechar.dd.mesage.key"+rabbitFlg);
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(reportOutSideSend).to(report_outside_send_exchange).with("wechar.report.outside.send.key");
	}

	//创建延时交互机
	@Bean
	public DirectExchange dd_delay_exchange(){
		DirectExchange dExchange=new DirectExchange("dd_delay_exchange");
		rabbitAdmin.declareExchange(dExchange);
		return dExchange;

		//return new DirectExchange("xx_delay_exchange");
	}
	//创建延时队列
	@Bean
	public Queue dd_delay_queue(){
		Map<String ,Object> map = new HashMap<>();
		map.put("x-dead-letter-exchange","wechar.dd.mesage.exchange");
		map.put("x-dead-letter-routing-key",preRabbitFlg+"wechar.dd.mesage.key"+rabbitFlg);
		Queue xx_delay_queue = QueueBuilder.durable(preRabbitFlg+"dd_delay_queue"+rabbitFlg).withArguments(map).build();
		rabbitAdmin.declareQueue(xx_delay_queue);
		return xx_delay_queue;
	}
	//延时队列绑定

	@Bean
	public Binding ddBindingDelayExchangeAndQueue(){
		Binding binding = BindingBuilder.bind(dd_delay_queue()).to(dd_delay_exchange()).with(preRabbitFlg+"dd_delay_key"+rabbitFlg);
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(xx_delay_queue()).to(xx_delay_exchange()).with("xx_delay_key");
	}
	/**
	 * 注册用户表新增或修改消息
	 * 
	 * @return
	 */
	@Bean
	public Queue addOrModUserQueue() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.xtyh.update" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册用户表删除消息
	 * 
	 * @return
	 */
	@Bean
	public Queue delUserQueue() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.xtyh.del" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册用角色表新增或修改消息
	 * 
	 * @return
	 */
	@Bean
	public Queue addOrModRoleQueue() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.xtjs.update" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册角色表删除消息
	 * 
	 * @return
	 */
	@Bean
	public Queue delRole() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.xtjs.del" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册机构权限新增消息
	 * 
	 * @return
	 */
	@Bean
	public Queue insertYhjgqx() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.yhjgqx.insert" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册机构权限删除消息
	 * 
	 * @return
	 */
	@Bean
	public Queue delYhjgqx() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.yhjgqx.del" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册所属机构新增消息
	 * 
	 * @return
	 */
	@Bean
	public Queue insertYhssjg() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.yhssjg.insert" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册所属机构删除消息
	 * 
	 * @return
	 */
	@Bean
	public Queue delYhssjg() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.yhssjg.del" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册用户角色新增消息
	 * 
	 * @return
	 */
	@Bean
	public Queue insertYhjs() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.yhjs.insert" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册用户角色删除消息
	 * 
	 * @return
	 */
	@Bean
	public Queue delYhjs() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.yhjs.del" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册审批岗位新增消息
	 * 
	 * @return
	 */
	@Bean
	public Queue insertSpgwcy() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.spgwcy.insert" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册审批岗位删除消息
	 * 
	 * @return
	 */
	@Bean
	public Queue delSpgwcy() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.spgwcy.del" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册审批岗位成员批量删除消息
	 * 
	 * @return
	 */
	@Bean
	public Queue batchDelSpgwcy() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.spgwcy.batchdel" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册机构信息新增或修改消息
	 * 
	 * @return
	 */
	@Bean
	public Queue addOrModJgxx() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.jgxx.update" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 个人设置消息订阅
	 *
	 * @return
	 */
	@Bean
	public Queue modSaveMessage() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.grsz.save" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	/**
	 * 注册机构信息修改消息
	 *
	 * @return
	 */
	@Bean
	public Queue modJgxxList() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.jgxx.updateList" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	/**
	 * 注册机构信息删除消息
	 *
	 * @return
	 */
	@Bean
	public Queue delJgxxList() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.jgxx.dels" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册机构信息删除消息
	 * 
	 * @return
	 */
	@Bean
	public Queue delJgxx() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.jgxx.del" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册审批岗位新增消息
	 * 
	 * @return
	 */
	@Bean
	public Queue insertJsdwqx() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.jsdwqx.insert" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册角色单位信息删除消息
	 * 
	 * @return
	 */
	@Bean
	public Queue delJsdwqx() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.jsdwqx.del" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册系统审核表新增或修改
	 * @return
	 */
	@Bean
	public Queue addOrModXtsh() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.xtsh.update" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册系统审核表删除
	 * @return
	 */
	@Bean
	public Queue delXtsh() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.xtsh.del" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册审批岗位表新增或修改
	 * @return
	 */
	@Bean
	public Queue addOrModSpgw() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.spgw.update" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册审批岗位表删除
	 * @return
	 */
	@Bean
	public Queue delSpgw() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.spgw.del" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册审核流程表新增
	 * @return
	 */
	@Bean
	public Queue addShlc() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.shlc.insert" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册审核流程表更新
	 * @return
	 */
	@Bean
	public Queue updateShlc() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.shlc.update" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册审核类别表新增
	 * @return
	 */
	@Bean
	public Queue addShlb() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.shlb.insert" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册审核类别表更新
	 * @return
	 */
	@Bean
	public Queue updateShlb() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.shlb.update" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册审核类别表删除
	 * @return
	 */
	@Bean
	public Queue delShlb() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.shlb.del" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册基础数据表新增或修改
	 * @return
	 */
	@Bean
	public Queue addOrModJcsj() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.jcsj.update" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册基础数据表删除
	 * @return
	 */
	@Bean
	public Queue delJcsj() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.jcsj.del" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册签名同步
	 * @return
	 */
	@Bean
	public Queue userSign() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.xtyh.sign" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册物料表新增或修改消息
	 * 
	 * @return
	 */
	@Bean
	public Queue addOrModWlglQueue() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.wlgl.update" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 物料同步
	 *
	 * @return
	 */
	@Bean
	public Queue insertWlglQueue() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.wlgl.insertWlgl" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	/**
	 * 物料同步修改删除
	 *
	 * @return
	 */
	@Bean
	public Queue syncUpOrDelWlglQueue() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.wlgl.updateWlgl(deleteWlgl,updateKcwh)" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	/**
	 * 注册物料表删除消息
	 * 
	 * @return
	 */
	@Bean
	public Queue delWlglQueue() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.wlgl.del" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册有关用户信息同步的消息
	 *
	 * @return
	 */
	@Bean
	public Queue userOperate() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.user.operate" + rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册有关用户信息同步的消息
	 *
	 * @return
	 */
	@Bean
	public Queue taskHandle() {
		Queue queue = new Queue(preRabbitFlg + "sys.igams.task.taskHandle" + rabbitFlg+remindflg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}


	/**
	 * 交换机
	 * 
	 * @return
	 */
	@Bean
	public TopicExchange sys_exchange() {
		TopicExchange contractTopicExchange=new TopicExchange("sys.igams");  
        rabbitAdmin.declareExchange(contractTopicExchange);  
        return contractTopicExchange;
        
		//return new TopicExchange("sys.igams");
	}

	/**
	 * 交换机与消息队列进行绑定，对用户新增或修改消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeAddOrModUser(Queue addOrModUserQueue, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(addOrModUserQueue).to(sys_exchange).with(preRabbitFlg + "sys.igams.xtyh.update");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(addOrModUserQueue).to(sys_exchange).with("sys.igams.xtyh.update");
	}

	/**
	 * 交换机与消息队列进行绑定，对用户删除消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelUser(Queue delUserQueue, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(delUserQueue).to(sys_exchange).with(preRabbitFlg + "sys.igams.xtyh.del");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(delUserQueue).to(sys_exchange).with("sys.igams.xtyh.del");
	}

	/**
	 * 交换机与消息队列进行绑定，对角色新增或修改消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeAddOrModRole(Queue addOrModRoleQueue, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(addOrModRoleQueue).to(sys_exchange).with(preRabbitFlg + "sys.igams.xtjs.update");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(addOrModRoleQueue).to(sys_exchange).with("sys.igams.xtjs.update");
	}

	/**
	 * 交换机与消息队列进行绑定，对角色删除消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelRole(Queue delRole, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(delRole).to(sys_exchange).with(preRabbitFlg + "sys.igams.xtjs.del");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(delRole).to(sys_exchange).with("sys.igams.xtjs.del");
	}

	/**
	 * 交换机与消息队列进行绑定，对机构权限新增消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeInsertYhjgqx(Queue insertYhjgqx, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(insertYhjgqx).to(sys_exchange).with(preRabbitFlg + "sys.igams.yhjgqx.insert");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(insertYhjgqx).to(sys_exchange).with("sys.igams.yhjgqx.insert");
	}

	/**
	 * 交换机与消息队列进行绑定，对机构权限删除消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelYhjgqx(Queue delYhjgqx, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(delYhjgqx).to(sys_exchange).with(preRabbitFlg + "sys.igams.yhjgqx.del");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(delYhjgqx).to(sys_exchange).with("sys.igams.yhjgqx.del");
	}

	/**
	 * 交换机与消息队列进行绑定，对所属机构新增消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeInsertYhssjg(Queue insertYhssjg, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(insertYhssjg).to(sys_exchange).with(preRabbitFlg + "sys.igams.yhssjg.insert");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(insertYhssjg).to(sys_exchange).with("sys.igams.yhssjg.insert");
	}

	/**
	 * 交换机与消息队列进行绑定，对所属机构删除消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelYhssjg(Queue delYhssjg, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(delYhssjg).to(sys_exchange).with(preRabbitFlg + "sys.igams.yhssjg.del");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(delYhssjg).to(sys_exchange).with("sys.igams.yhssjg.del");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对用户角色新增消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeInsertYhjs(Queue insertYhjs, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(insertYhjs).to(sys_exchange).with(preRabbitFlg + "sys.igams.yhjs.insert");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(insertYhjs).to(sys_exchange).with("sys.igams.yhjs.insert");
	}

	/**
	 * 交换机与消息队列进行绑定，对用户角色删除消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelYhjs(Queue delYhjs, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(delYhjs).to(sys_exchange).with(preRabbitFlg + "sys.igams.yhjs.del");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(delYhjs).to(sys_exchange).with("sys.igams.yhjs.del");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对审批岗位新增消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeInsertSpgwcy(Queue insertSpgwcy, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(insertSpgwcy).to(sys_exchange).with(preRabbitFlg + "sys.igams.spgwcy.insert");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(insertSpgwcy).to(sys_exchange).with("sys.igams.spgwcy.insert");
	}

	/**
	 * 交换机与消息队列进行绑定，对审批岗位删除消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelSpgwcy(Queue delSpgwcy, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(delSpgwcy).to(sys_exchange).with(preRabbitFlg + "sys.igams.spgwcy.del");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(delSpgwcy).to(sys_exchange).with("sys.igams.spgwcy.del");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对审批岗位成员批量删除消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeBatchDelSpgwcy(Queue batchDelSpgwcy, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(batchDelSpgwcy).to(sys_exchange).with(preRabbitFlg + "sys.igams.spgwcy.batchdel");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(batchDelSpgwcy).to(sys_exchange).with("sys.igams.spgwcy.batchdel");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对机构信息新增消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeAddOrModJgxx(Queue addOrModJgxx, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(addOrModJgxx).to(sys_exchange).with(preRabbitFlg + "sys.igams.jgxx.update");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(addOrModJgxx).to(sys_exchange).with("sys.igams.jgxx.update");
	}

	/**
	 * 交换机与消息队列进行绑定，对个人设置消息订阅进行绑定
	 *
	 * @param modSaveMessage
	 * @param sys_exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeModSaveMessage(Queue modSaveMessage, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(modSaveMessage).to(sys_exchange).with(preRabbitFlg + "sys.igams.grsz.save");
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	/**
	 * 交换机与消息队列进行绑定，对机构信息新增消息进行绑定
	 *
	 * @param modJgxxList
	 * @param sys_exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeModJgxxList(Queue modJgxxList, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(modJgxxList).to(sys_exchange).with(preRabbitFlg + "sys.igams.jgxx.updateList");
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	/**
	 * 交换机与消息队列进行绑定，对机构信删除消息进行绑定
	 *
	 * @param delJgxxList
	 * @param sys_exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelJgxxList(Queue delJgxxList, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(delJgxxList).to(sys_exchange).with(preRabbitFlg + "sys.igams.jgxx.dels");
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	/**
	 * 交换机与消息队列进行绑定，对机构信息删除消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelJgxx(Queue delJgxx, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(delJgxx).to(sys_exchange).with(preRabbitFlg + "sys.igams.jgxx.del");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(delJgxx).to(sys_exchange).with("sys.igams.jgxx.del");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对角色单位新增消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeInsertJsdwqx(Queue insertJsdwqx, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(insertJsdwqx).to(sys_exchange).with(preRabbitFlg + "sys.igams.jsdwqx.insert");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(insertJsdwqx).to(sys_exchange).with("sys.igams.jsdwqx.insert");
	}

	/**
	 * 交换机与消息队列进行绑定，对角色单位删除消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelJsdwqx(Queue delJsdwqx, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(delJsdwqx).to(sys_exchange).with(preRabbitFlg + "sys.igams.jsdwqx.del");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(delJsdwqx).to(sys_exchange).with("sys.igams.jsdwqx.del");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对系统审核新增或修改消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateXtsh(Queue addOrModXtsh, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(addOrModXtsh).to(sys_exchange).with(preRabbitFlg + "sys.igams.xtsh.update");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(addOrModXtsh).to(sys_exchange).with("sys.igams.xtsh.update");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对系统审核删除消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelXtsh(Queue delXtsh, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(delXtsh).to(sys_exchange).with(preRabbitFlg + "sys.igams.xtsh.del");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(delXtsh).to(sys_exchange).with("sys.igams.xtsh.del");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对审批岗位新增或修改消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateSpgw(Queue addOrModSpgw, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(addOrModSpgw).to(sys_exchange).with(preRabbitFlg + "sys.igams.spgw.update");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(addOrModSpgw).to(sys_exchange).with("sys.igams.spgw.update");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对审批岗位删除消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelSpgw(Queue delSpgw, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(delSpgw).to(sys_exchange).with(preRabbitFlg + "sys.igams.spgw.del");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(delSpgw).to(sys_exchange).with("sys.igams.spgw.del");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对审核流程添加消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeAddShlc(Queue addShlc, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(addShlc).to(sys_exchange).with(preRabbitFlg + "sys.igams.shlc.insert");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(addShlc).to(sys_exchange).with("sys.igams.shlc.insert");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对审核流程更新消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateShlc(Queue updateShlc, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(updateShlc).to(sys_exchange).with(preRabbitFlg + "sys.igams.shlc.update");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(updateShlc).to(sys_exchange).with("sys.igams.shlc.update");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对审核类别添加消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeAddShlb(Queue addShlb, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(addShlb).to(sys_exchange).with(preRabbitFlg + "sys.igams.shlb.insert");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(addShlb).to(sys_exchange).with("sys.igams.shlb.insert");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对审核类别更新消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUpdateShlb(Queue updateShlb, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(updateShlb).to(sys_exchange).with(preRabbitFlg + "sys.igams.shlb.update");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(updateShlb).to(sys_exchange).with("sys.igams.shlb.update");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对审核类别删除消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelShlb(Queue delShlb, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(delShlb).to(sys_exchange).with(preRabbitFlg + "sys.igams.shlb.del");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(delShlb).to(sys_exchange).with("sys.igams.shlb.del");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对基础数据添加消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeAddOrModJcsj(Queue addOrModJcsj, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(addOrModJcsj).to(sys_exchange).with(preRabbitFlg + "sys.igams.jcsj.update");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(addOrModJcsj).to(sys_exchange).with("sys.igams.jcsj.update");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对基础数据更新消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelJcsj(Queue delJcsj, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(delJcsj).to(sys_exchange).with(preRabbitFlg + "sys.igams.jcsj.del");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(delJcsj).to(sys_exchange).with("sys.igams.jcsj.del");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对用户签名更新消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUserSign(Queue userSign, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(userSign).to(sys_exchange).with(preRabbitFlg + "sys.igams.xtyh.sign");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(userSign).to(sys_exchange).with("sys.igams.xtyh.sign");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对物料信息更新消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeAddOrModWlgl(Queue addOrModWlglQueue, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(addOrModWlglQueue).to(sys_exchange).with(preRabbitFlg + "sys.igams.wlgl.update");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(addOrModWlglQueue).to(sys_exchange).with("sys.igams.wlgl.update");
	}

	/**
	 * 交换机与消息队列进行绑定，对同步物料信息更新消息进行绑定
	 *
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeinsertWlgl(Queue insertWlglQueue, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(insertWlglQueue).to(sys_exchange).with(preRabbitFlg + "sys.igams.wlgl.insertWlgl"+rabbitFlg);
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(addOrModWlglQueue).to(sys_exchange).with("sys.igams.wlgl.update");
	}
	/**
	 * 交换机与消息队列进行绑定，对同步物料信息更新消息进行绑定
	 *
	 * @param syncUpOrDelWlglQueue
	 * @param sys_exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeSyncUpOrDelWlglQueue(Queue syncUpOrDelWlglQueue, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(syncUpOrDelWlglQueue).to(sys_exchange).with(preRabbitFlg + "sys.igams.wlgl.updateWlgl(deleteWlgl,updateKcwh)"+rabbitFlg);
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(addOrModWlglQueue).to(sys_exchange).with("sys.igams.wlgl.update");
	}
	/**
	 * 交换机与消息队列进行绑定，对物料信息删除消息进行绑定
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelWlgl(Queue delWlglQueue, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(delWlglQueue).to(sys_exchange).with(preRabbitFlg + "sys.igams.wlgl.del");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(delWlglQueue).to(sys_exchange).with("sys.igams.wlgl.del");
	}
	
	/**
	 * 交换机与消息队列进行绑定，对同步用户信息的消息进行绑定
	 * 
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUserOperate(Queue userOperate, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(userOperate).to(sys_exchange).with(preRabbitFlg + "sys.igams.user.operate");
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(userOperate).to(sys_exchange).with("sys.igams.user.operate");
	}
	/**
	 * 交换机与消息队列进行绑定，定时任务新增
	 *
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeTaskAdd(Queue taskHandle, TopicExchange sys_exchange) {
		Binding binding = BindingBuilder.bind(taskHandle).to(sys_exchange).with(preRabbitFlg + "sys.igams.task.taskHandle"+rabbitFlg+remindflg);
		rabbitAdmin.declareBinding(binding);
		return binding;
		//return BindingBuilder.bind(userOperate).to(sys_exchange).with("sys.igams.user.operate");
	}
	//创建队列
	@Bean
	public Queue sendGroupMessages() {
		Queue queue = new Queue(preRabbitFlg+"send.group.messages.queue"+rabbitFlg);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	//创建交互机
	@Bean
	public TopicExchange send_group_messages_exchange(){
		TopicExchange topicExchange = new TopicExchange("send.group.messages.exchange");
		rabbitAdmin.declareExchange(topicExchange);
		return topicExchange;
	}
	//绑定
	@Bean
	public Binding bindingExchangeSendGroupMessages(Queue sendGroupMessages, TopicExchange send_group_messages_exchange) {
		Binding binding = BindingBuilder.bind(sendGroupMessages).to(send_group_messages_exchange).with(preRabbitFlg+"send.group.messages.key"+rabbitFlg);
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	//创建延时交互机 发送钉钉群消息
	@Bean
	public DirectExchange send_group_messages_delay_exchange(){
		DirectExchange dExchange=new DirectExchange("send_group_messages_delay_exchange");
		rabbitAdmin.declareExchange(dExchange);
		return dExchange;
	}
	//创建延时队列 发送钉钉群消息
	@Bean
	public Queue send_group_messages_delay_queue(){
		Map<String ,Object> map = new HashMap<>();
		map.put("x-dead-letter-exchange","send.group.messages.exchange");
		map.put("x-dead-letter-routing-key",preRabbitFlg+"send.group.messages.key"+rabbitFlg);
		Queue send_group_messages_delay_queue = QueueBuilder.durable(preRabbitFlg+"send_group_messages_delay_queue"+rabbitFlg).withArguments(map).build();
		rabbitAdmin.declareQueue(send_group_messages_delay_queue);
		return send_group_messages_delay_queue;
	}
	//延时队列绑定 发送钉钉群消息
	@Bean
	public Binding sendGroupMessagesBindingDelayExchangeAndQueue(){
		Binding binding = BindingBuilder.bind(send_group_messages_delay_queue()).to(send_group_messages_delay_exchange()).with(preRabbitFlg+"send_group_messages_delay_key"+rabbitFlg);
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

}
